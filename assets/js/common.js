const GAEvents = {
  onboarding_start: "onboarding_start",
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

function showOnboardingModal(externalUrl) {
  const width = Math.min(window.innerWidth, 1200)
  const height = Math.max(window.innerHeight, 800)
  const baseAPMUrl = getBaseAPMUrl()
  const solution = $(this).data("solution")
  const extension = externalUrl != null
    ? externalUrl
    : solution != null
      ? `onboarding?external=yes&solution=${solution}`
      : `onboarding?external=yes`
  const url = `${baseAPMUrl}/${extension}`
  $("#onboarding-iframe").attr("width", width).attr("height", height)
  $("#onboarding-iframe").attr("src", url)
  $("#onboarding-modal").modal("show")
}

function bootOnboarding() {
  $(".onboarding-start-button").on("click", function() {
    sendGoogleAnalyticsEvent(GAEvents.onboarding_start, "Via CTA")
    const url = $(this).data("url")
    showOnboardingModal(url)
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
  $('.dropdown').hover(function(){ 
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
  const teamsPlanPriceExplainer = $("#teams-plan-price-explainer")
  const teamsPlanSpans = $("#teams-plan-spans-count")

  function updatePlanInfo() {
    let newVal = servicesInput.val()
    if (newVal.indexOf("+") != null && newVal !== "50+") {
      newVal = newVal.replace(/\+/g, "")
    }
    if (newVal < 1) {
      servicesInput.val(1)
    } else if (newVal > 50) {
      servicesInput.val("50+")
      teamsPlanPrice.text("Let's talk")
      teamsPlanPriceExplainer.html("Reach us via <a class='text-primary' href='mailto:hello@kamon.io'>hello@kamon.io</a>")
      teamsPlanSpans.text("10+M")
    } else {
      servicesInput.val(newVal)
      teamsPlanPrice.html("&euro;" + newVal * 30)
      if (teamsPlanPriceExplainer.text() !== "Per Month") {
        teamsPlanPriceExplainer.text("Per Month")
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
