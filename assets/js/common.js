const GAEvents = {
  onboarding_start: "onboarding_start",
  onboarding_choose_integration: "onboarding_choose_integration",
  onboarding_signup: "onboarding_signup",
}

function getPageViewForGAEvent(eventCategory, eventAction) {
  switch (eventCategory) {
    case GAEvents.onboarding_start:
      return ({ title: "Onboarding Solution Selection", path: "onboarding/solution-selection/" })
    case GAEvents.onboarding_choose_integration:
      return { title: `Onboarding Started with ${eventAction}`, path: `onboarding/started-with/${eventAction.toLowerCase()}/`}
    case GAEvents.onboarding_signup:
      return { title: "Onboarding Signed up with APM", path: "onboarding/signed-up/" }
  }
}

function getBaseAPMUrl() {
  return window.location.hostname === "0.0.0.0"
    ? "http://localhost:9999"
    : "https://apm.kamon.io"
}

// NOTE: we're deliberately submitting both custom events and manual page view events.
// Why? Because you cannot visualise a sequence of events in GA, but you can do so with page views.
function sendGoogleAnalyticsEvent(eventCategory, eventAction) {
  const dataLayer = window.dataLayer
  if (dataLayer != null) {
    dataLayer.push({
      "event": eventCategory,
      "customEventAction": eventAction.toLowerCase()
    })
    let pageView = getPageViewForGAEvent(eventCategory, eventAction)
    if (pageView != null) {
      dataLayer.push({
        "event": "pageView",
        "customPageViewTitle": pageView.title,
        "customPageViewPath": pageView.path,
      })
    }
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

  document.addEventListener("scroll", mainHeaderScrollListener, { passive: true })

  // Must be called during setup in case the current scroll location requires the background
  mainHeaderScrollListener()
}

function showOnboardingModal() {
  const width = Math.min(window.innerWidth, 1200)
  const height = Math.max(window.innerHeight, 800)

  const baseAPMUrl = getBaseAPMUrl()
  const solution = $(this).data("solution")
  const url = solution != null
    ? `${baseAPMUrl}/onboarding?external=yes&solution=${solution}`
    : `${baseAPMUrl}/onboarding?external=yes`

  $("#onboarding-iframe").attr("width", width).attr("height", height)
  $("#onboarding-iframe").attr("src", url)
  $("#onboarding-modal").modal("show")
}

function bootOnboarding() {
  $(".onboarding-start-button").on("click", () => {
    sendGoogleAnalyticsEvent(GAEvents.onboarding_start, "cta_click")
    showOnboardingModal()
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
      }
      if (tag.data === "close") {
        $("#onboarding-modal").modal("hide")
      }
    }
  })

  if (window.location.hash === "#get-started") {
    sendGoogleAnalyticsEvent(GAEvents.onboarding_start, "get_started_url")
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

$(document).ready(function() {
  initNotificationBar()
  initScrollMainHeader()
  initMobileNavBackground()
  bootOnboarding()
})
