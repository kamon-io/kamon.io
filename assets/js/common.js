const GAEvents = {
  onboarding_start: "onboarding_start",
  onboarding_start_signup: "onboarding_start_signup",
  onboarding_choose_integration: "onboarding_choose_integration",
  onboarding_choose_project_type: "onboarding_choose_project_type",
  onboarding_signup: "onboarding_signup",
}

function getBaseAPMUrl() {
  return window.location.hostname === "0.0.0.0" || window.location.hostname === "localhost"
    ? "http://localhost:9999"
    : "https://apm.kamon.io"
}

function sendGoogleAnalyticsEvent(eventName, eventLabel) {
  const dataLayer = window.dataLayer
  if (dataLayer != null) {
    dataLayer.push({
      "event": eventName,
      "eventLabel": eventLabel
    })
  }
}

function initScrollMainHeader() {
  const HEADER_BACKGROUND_TRIGGER_SCROLL = 40;
  var mainHeader = document.getElementById("main-header")
  var isMainHeaderTransparent = true
  
  var mainHeaderScrollListener = function() {
    if (isMainHeaderTransparent && window.pageYOffset >= HEADER_BACKGROUND_TRIGGER_SCROLL) {
      mainHeader.classList.add("bg-white")
      isMainHeaderTransparent = false
    } else if (!isMainHeaderTransparent && window.pageYOffset <= HEADER_BACKGROUND_TRIGGER_SCROLL) {
      mainHeader.classList.remove("bg-white")
      isMainHeaderTransparent = true
    }
  }

  if(window.location.pathname.startsWith("/docs/")) {
    mainHeader.classList.add("bg-white")
  } else {

    document.addEventListener("scroll", mainHeaderScrollListener, { passive: true })

    // Must be called during setup in case the current scroll location requires the background
    mainHeaderScrollListener()
  }
}

/**
 * 
 * @param {string} externalUrl External path instead of /onboarding (no leading slash)
 * @param {boolean} isSmall Should a smaller version of the modal with no extra graphics be shown
 * @param {string} solution The name of the solution/APM for which instructions should be given
 * @param {string} plan Which plan name to use (starter, teams, or developer), defaults to starter
 */
function showOnboardingModal(externalUrl, isSmall, solution, plan) {
  const width = Math.min(window.innerWidth, isSmall ? 600 : 1200)
  const height = Math.max(window.innerHeight, 800)
  const baseAPMUrl = getBaseAPMUrl()
  const extension = externalUrl != null ? externalUrl : "onboarding"
  const queryParams = new URLSearchParams()
  queryParams.set("external", "yes")
  
  if (solution != null) {
    queryParams.set("solution", solution)
  }

  if (plan != null) {
    const planID = (() => {
      switch (plan) {
        case "developer": return "v2-free"
        case "teams": return "v3-teams"
        case "starter":
        default: return "v3-starter"
      } 
    })()
    queryParams.set("plan", planID)
  }

  if (isSmall) {
    queryParams.set("small", "true")
  }

  const url = `${baseAPMUrl}/${extension}?${queryParams.toString()}`
  $("#onboarding-iframe").attr("width", width).attr("height", height)
  $("#onboarding-iframe").attr("src", url)
  
  if (isSmall) {
    $(".onboarding-modal-dialog").addClass("small-dialog")
  } else {
    $(".onboarding-modal-dialog").removeClass("small-dialog")
  }

  $("#onboarding-modal").modal("show")
}

function bootOnboarding() {
  $(".onboarding-start-button").on("click", function() {
    const isSignup = $(this).data("url") == "signup"
    if(isSignup)
      sendGoogleAnalyticsEvent(GAEvents.onboarding_start_signup, "Via CTA")
    else
      sendGoogleAnalyticsEvent(GAEvents.onboarding_start, "Via CTA")

    const url = $(this).data("url")
    const isSmall = $(this).data("small") == null || $(this).data("small")
    const solution = $(this).data("solution")
    const plan = $(this).data("plan")
    showOnboardingModal(url, isSmall, solution, plan)
  })

  window.addEventListener("message", function (tag) {
    const baseAPMUrl = getBaseAPMUrl()
    if (tag.origin.includes(baseAPMUrl)) {
      if (tag.data.type === "ga-event") {
        if (GAEvents[tag.data.eventCategory] == null) {
          console.error(`Cannot submit GA event with category [${tag.data.eventCategory}]. Allowed categories: [${Object.keys(GAEvents).join(", ")}]`)
        } else {
          sendGoogleAnalyticsEvent(GAEvents[tag.data.eventCategory], tag.data.eventAction)
        }
      }
      if (tag.data === "complete") {
        window.open(baseAPMUrl, "_blank")
        $("#onboarding-modal").modal("hide")
        $("#onboarding-iframe").attr("src", undefined)
      }
      if (tag.data === "close") {
        $("#onboarding-modal").modal("hide")
        $("#onboarding-iframe").attr("src", undefined)
      }
    }
  })

  if (window.location.hash === "#get-started") {
    sendGoogleAnalyticsEvent(GAEvents.onboarding_start, "Via URL")
    showOnboardingModal()
  }
}

function initNotificationBar() {
  const CLOSED_NOTIFICATION_KEY = "ClosedNotification"
  const currentNotificationID = $(".notification-bar").first().attr('id')
  const closedNotification = localStorage.getItem(CLOSED_NOTIFICATION_KEY)

  if(currentNotificationID != closedNotification) {
    $("body").addClass("has-notification")

    $('.notification-bar .close').click(function(e) {
      e.preventDefault();
      localStorage.setItem(CLOSED_NOTIFICATION_KEY, currentNotificationID)
      $("body").removeClass("has-notification")
    });
  }
}

function initHeaderDropdownOnHover() {
  $('.dropdown').hover(
    function() { 
      $('.dropdown-toggle', this).trigger('click'); 
    },
    function() { 
      if($('.dropdown-toggle', this).attr("aria-expanded") == "true")
        $('.dropdown-toggle', this).trigger('click'); 
    });
}

function initMobileNavBackground() {
  $("#siteNavigation").on("show.bs.collapse", function () {
    $("#main-header").addClass("mobile-expanded")
  })

  $("#siteNavigation").on("hide.bs.collapse", function () {
    $("#main-header").removeClass("mobile-expanded")
  })
}

function debounce(func, delay = 300) {
  var inDebounce = void 0;
  return function () {
    var context = this;
    var args = arguments;
    clearTimeout(inDebounce);
    inDebounce = setTimeout(function () {
      return func.apply(context, args);
    }, delay);
  };
}

function initTeamsPlanPriceCalculation() {
  const servicesInput = $("#teams-plan-service-number")
  const serviceUpButton = $("#teams-plan-service-up-button")
  const serviceDownButton = $("#teams-plan-service-down-button")
  const teamsPlanPrice = $("#teams-plan-monthly-price")
  const teamsPlanPriceFrequency = $("#teams-plan-price-frequency")
  const teamsPlanPriceExplainer = $("#teams-plan-price-explainer")
  const teamsPlanSpans = $("#teams-plan-spans-count")

  function updatePlanInfo() {
    let newVal = servicesInput.val()
    if (newVal.indexOf("+") != null && newVal !== "50+") {
      newVal = newVal.replace(/\+/g, "")
    }
    if (newVal < 1) {
      servicesInput.val(1)
      teamsPlanPriceFrequency.show()
    } else if (newVal > 50) {
      servicesInput.val("50+")
      teamsPlanPrice.text("Let's talk!")
      teamsPlanPriceFrequency.hide()
      teamsPlanPriceExplainer.html("Reach us via <a class='text-primary' href='mailto:hello@kamon.io'>hello@kamon.io</a>")
      teamsPlanSpans.text("10M+")
    } else {
      servicesInput.val(newVal)
      teamsPlanPriceFrequency.show()
      teamsPlanPrice.html("&euro;" + newVal * 30)
      if (teamsPlanPriceExplainer.text() !== "Per Month") {
        teamsPlanPriceExplainer.text("")
      }
      const spansCount = newVal * 200_000
      if (spansCount < 1_000_000) {
        teamsPlanSpans.text(spansCount / 1_000 + "K")
      } else {
        teamsPlanSpans.text(spansCount / 1_000_000 + "M")
      }
    }
  }

  function serviceCountDecrement() {
    if (servicesInput.val() === "50+") {
      servicesInput.val(50)
      updatePlanInfo()
    } else {
      const currentCount = parseFloat(servicesInput.val())
      if (currentCount > 1) {
        servicesInput.val(currentCount - 1)
        updatePlanInfo()
      }
    }
  }

  function serviceCountIncrement() {
    if (servicesInput.val() !== "50+") {
      const currentCount = parseFloat(servicesInput.val())
      if (currentCount <= 50) {
        servicesInput.val(currentCount + 1)
        updatePlanInfo()
      }
    }
  }

  servicesInput.on("keydown", function(event) {
    // increment/decrement on arrow up/down
    if (event.keyCode === 38) {
      event.preventDefault()
      serviceCountIncrement()
    } else if (event.keyCode === 40) {
      event.preventDefault()
      serviceCountDecrement()
    } else {
      // allow: keyboard 0-9, numpad 0-9, backspace, tab, left arrow, right arrow, delete
      const isAllowedKey =
        (event.keyCode >= 48 && event.keyCode <= 57)
        || (event.keyCode >= 96 && event.keyCode <= 105)
        || event.keyCode == 8
        || event.keyCode == 9
        || event.keyCode == 37
        || event.keyCode == 39
        || event.keyCode == 46
      if (!isAllowedKey) {
        event.preventDefault()
      }
    }
  })

  serviceDownButton.on("click", serviceCountDecrement)
  serviceUpButton.on("click", serviceCountIncrement)
  servicesInput.on("change", updatePlanInfo)
}


$(document).ready(function() {
  initNotificationBar()
  initScrollMainHeader()
  initMobileNavBackground()
  bootOnboarding()
  initTeamsPlanPriceCalculation()
  initHeaderDropdownOnHover()
})
