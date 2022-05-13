// Moment when the special offer becomes null and void
function setupPriceCurrencyToggling() {
  $(".show-price-in-usd").on('click', () => {
    $(".show-price-in-usd").addClass("active")
    $(".show-price-in-eur").removeClass("active")
    $("#teams-plan-price").text("$100")
    $("#pro-plan-price").text("$700")
    $("#additional-10k-time-series-price").text("$90")
    $("#additional-100m-spans-price").text("$100")

  })

  $(".show-price-in-eur").on('click', () => {
    $(".show-price-in-eur").addClass("active")
    $(".show-price-in-usd").removeClass("active")
    $("#teams-plan-price").text("€90")
    $("#pro-plan-price").text("€650")
    $("#additional-10k-time-series-price").text("€80")
    $("#additional-100m-spans-price").text("€90")
  })
}

$(document).ready(function() {
  setupPriceCurrencyToggling()
})
