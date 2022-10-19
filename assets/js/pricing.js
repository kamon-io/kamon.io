// Moment when the special offer becomes null and void
function setupPriceCurrencyToggling() {
  $(".show-price-in-usd").on('click', () => {
    $(".show-price-in-usd").addClass("active")
    $(".show-price-in-eur").removeClass("active")
    $("#teams-plan-price").text("$89")
    $("#teams-plan-price-in-details").text("$89")
    $("#business-plan-price").text("$299")
    $("#business-plan-price-in-details").text("$299")
    $("#additional-10k-metrics-price-teams").text("$90")
    $("#additional-10k-metrics-price-business").text("$90")
    $("#additional-100m-spans-price-teams").text("$60")
    $("#additional-100m-spans-price-business").text("$60")
    $("#enterprise-starting-at").text("$1499")

  })

  $(".show-price-in-eur").on('click', () => {
    $(".show-price-in-eur").addClass("active")
    $(".show-price-in-usd").removeClass("active")
    $("#teams-plan-price").text("€89")
    $("#teams-plan-price-in-details").text("€89")
    $("#business-plan-price").text("€299")
    $("#business-plan-price-in-details").text("€299")
    $("#additional-10k-metrics-price-teams").text("€90")
    $("#additional-10k-metrics-price-business").text("€90")
    $("#additional-100m-spans-price-teams").text("€60")
    $("#additional-100m-spans-price-business").text("€60")
    $("#enterprise-starting-at").text("€1499")
  })
}

$(document).ready(function() {
  setupPriceCurrencyToggling()
})
