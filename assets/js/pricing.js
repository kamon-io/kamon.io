function initAnnualBillingToggle() {
  var annualBillingToggle = document.getElementById("billed-annually-switch")
  if (annualBillingToggle == null) {
    return
  }
  var billedMonthlyLabel = document.getElementById("billed-monthly-label")
  var billedAnnualLabel = document.getElementById("billed-annually-label")
  var growPlanPriceValue = document.getElementById("grow-plan-monthly-price")
  var createBillingTypeToggleHandler = function(manualToggleValue) {
    return function() {
      if (manualToggleValue != null) {
        if (manualToggleValue == annualBillingToggle.checked) {
          return
        }
        annualBillingToggle.checked = manualToggleValue
      }
      growPlanPriceValue.textContent = annualBillingToggle.checked ? 24 : 30
    }
  }
  annualBillingToggle.addEventListener("click", createBillingTypeToggleHandler())
  billedMonthlyLabel.addEventListener("click", createBillingTypeToggleHandler(false))
  billedAnnualLabel.addEventListener("click", createBillingTypeToggleHandler(true))
}

$(document).ready(function() {
  initAnnualBillingToggle()
})
