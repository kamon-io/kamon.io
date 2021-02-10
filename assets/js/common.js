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

  const solution = $(this).data("solution")
  const url = solution != null
    ? `https://apm.kamon.io/onboarding?external=yes&solution=${solution}`
    : "https://apm.kamon.io/onboarding?external=yes"

  $("#onboarding-iframe").attr("width", width).attr("height", height)
  $("#onboarding-iframe").attr("src", url)
  $("#onboarding-modal").modal("show")
}

function bootOnboarding() {
  $(".onboarding-start-button").on("click", showOnboardingModal)

  window.addEventListener("message", function (tag) {
    if (tag.origin.includes("apm.kamon.io")) {
      if (tag.data === "complete") {
        window.open("https://apm.kamon.io", "_blank")
      }
      $("#onboarding-modal").modal("hide")
    }
  })

  if (window.location.hash === "#get-started") {
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
