const GAEvents = {
  onboarding_start: "onboarding_start",
  onboarding_start_signup: "onboarding_start_signup",
  onboarding_start_booking: "onboarding_start_booking",
  onboarding_choose_integration: "onboarding_choose_integration",
  onboarding_choose_project_type: "onboarding_choose_project_type",
  onboarding_signup: "onboarding_signup",
}

function getBaseAPMUrl() {
  return window.location.hostname === "0.0.0.0" || window.location.hostname === "localhost"
    ? "http://localhost:9999"
    : "https://apm.kamon.io"
}

function sendAnalyticsEvent(eventName, eventLabel) {
  const dataLayer = window.dataLayer
  if (dataLayer != null) {
    dataLayer.push({
      "event": eventName,
      "eventLabel": eventLabel
    })
  }

  if(eventName == GAEvents.onboarding_start) {
    plausibleEvent('Launch Onboarding')
  }

  if(eventName == GAEvents.onboarding_start_signup) {
    plausibleEvent('Launch Sign Up')
  }

  if(eventName == GAEvents.onboarding_start_booking) {
    plausibleEvent('Launch Booking')
  }

  if(eventName == GAEvents.onboarding_signup) {
    plausibleEvent('Sign Up')
  }
}

function plausibleEvent(eventName) {
  const plausible = window.plausible
  if (plausible != null) {
    
    plausible(eventName)
  }
}

function initScrollMainHeader() {
  const HEADER_BACKGROUND_TRIGGER_SCROLL = 36;
  var mainHeader = document.getElementById("main-header")
  var shouldBeTransparentHeader = $(mainHeader).data("transparent") === true
  var isMainHeaderTransparent = false
  
  
  var mainHeaderScrollListener = function() {
    if(window.pageYOffset >= HEADER_BACKGROUND_TRIGGER_SCROLL) {
      mainHeader.classList.add('page-scrolled')
    } else {
      mainHeader.classList.remove('page-scrolled')
    }

    if(shouldBeTransparentHeader) {
      if (isMainHeaderTransparent && window.pageYOffset >= HEADER_BACKGROUND_TRIGGER_SCROLL) {
        mainHeader.classList.remove("bg-transparent")
        isMainHeaderTransparent = false
      } else if (!isMainHeaderTransparent && window.pageYOffset <= HEADER_BACKGROUND_TRIGGER_SCROLL) {
        mainHeader.classList.add("bg-transparent")
        isMainHeaderTransparent = true
      }
    }
  }

  document.addEventListener("scroll", mainHeaderScrollListener, { passive: true })

  // Must be called during setup in case the current scroll location requires the background
  mainHeaderScrollListener()
}

function initOnboardingEvents() {
  $('[data-target="#apmOnboardingModal"]').on("click", function() {
    console.log("Clicked on Signup")
    var onboardingFrame = $('iframe#apmOnboardingVideoFrame');
    onboardingFrame.attr('src', getBaseAPMUrl() + '/signup?small=true&external=true')

    $('#apmOnboardingModal .modal-header').hide()
    $('#apmOnboardingModal .modal-body').hide()
    $('#apmOnboardingModal .modal-footer').hide()
    $('#videoFrameWrapper').addClass('show-onboarding')

    sendAnalyticsEvent(GAEvents.onboarding_start_signup, "Via Modal")
  })

  $('#launchBookingModal').on("click", function() {
    sendAnalyticsEvent(GAEvents.onboarding_start_booking, "Via Modal")
  })

  $('#launchBookingPricing').on("click", function() {
    sendAnalyticsEvent(GAEvents.onboarding_start, "Via Pricing")
  })

  window.addEventListener("message", function (tag) {
    const baseAPMUrl = getBaseAPMUrl()
    if (tag.origin.includes(baseAPMUrl)) {
      if (tag.data.type === "ga-event") {
        if (GAEvents[tag.data.eventCategory] == null) {
          console.error(`Cannot submit GA event with category [${tag.data.eventCategory}]. Allowed categories: [${Object.keys(GAEvents).join(", ")}]`)
        } else {
          sendAnalyticsEvent(GAEvents[tag.data.eventCategory], tag.data.eventAction)
        }
      }

      if (tag.data === "complete") {
        window.open(baseAPMUrl, "_blank")
        $("#apmOnboardingModal").modal("hide")
        $("#apmOnboardingVideoFrame").attr("src", undefined)
      }
      if (tag.data === "close") {
        $("#apmOnboardingModal").modal("hide")
        $("#apmOnboardingVideoFrame").attr("src", undefined)
      }
    }
  })
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

function initApmAccordions() {
  
  $(window).resize(function() {
    var windowWidth = $(window).width();
    if(windowWidth < 992) {
      $("#alertsContent").removeClass("collapse")
      $("#dashboardsContent").removeClass("collapse")
      $("#serviceOverviewContent").removeClass("collapse")
      $("#serviceMapContent").removeClass("collapse")
      $("#highFidelityMetricsContent").removeClass("collapse")
      $("#builtInDashboardsContent").removeClass("collapse")
      $(".monitoring-features-accordion-1").attr("id", "disabledAccordion1")
      $(".monitoring-features-accordion-2").attr("id", "disabledAccordion2")

    } else {

      $("#alertsContent").addClass("collapse")
      $("#serviceMapContent").addClass("collapse")
      
      $("#dashboardsContent").addClass("collapse")
      $("#serviceOverviewContent").addClass("collapse")
      $("#highFidelityMetricsContent").addClass("collapse")
      $("#builtInDashboardsContent").addClass("collapse")
      $(".monitoring-features-accordion-1").attr("id", "monitoringFeaturesFirst")
      $(".monitoring-features-accordion-2").attr("id", "monitoringFeaturesSecond")
    }
  })


  $("#alertsContent").on("show.bs.collapse", function() {
    $("#firstAccordionImage").attr("src", "/assets/img/pages/apm/apm-alert-drawer.png")
  })

  $("#dashboardsContent").on("show.bs.collapse", function() {
    $("#firstAccordionImage").attr("src", "/assets/img/pages/apm/apm-dashboard.png")
  })

  $("#serviceOverviewContent").on("show.bs.collapse", function() {
    $("#firstAccordionImage").attr("src", "/assets/img/pages/apm/apm-service-overview.png")
  })

  $("#serviceMapContent").on("show.bs.collapse", function() {
    $("#secondAccordionImage").attr("src", "/assets/img/pages/apm/apm-service-map.png")
  })

  $("#highFidelityMetricsContent").on("show.bs.collapse", function() {
    $("#secondAccordionImage").attr("src", "/assets/img/pages/apm/apm-high-fidelity-metrics.png")
  })

  $("#builtInDashboardsContent").on("show.bs.collapse", function() {
    $("#secondAccordionImage").attr("src", "/assets/img/pages/apm/apm-built-in-dashboards.png")
  })

  
}

$(document).ready(function() {
  initScrollMainHeader()
  initMobileNavBackground()
  initHeaderDropdownOnHover()
  initApmAccordions()
  initOnboardingEvents()

  $(document).on('reloadHeader', initScrollMainHeader)
})
