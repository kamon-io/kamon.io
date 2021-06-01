// Moment when the special offer becomes null and void
var SPECIAL_OFFER_END = moment('2021-07-01').startOf("day")

function getTimeUntil(when) {
  var now = moment()
  if (now.isAfter(when)) {
    return
  }

  if (when.diff(now.startOf("day"), "days") >= 1) {
    return when.diff(now, "days") + " days"
  }

  return now.to(when)
}

function setDiscountExpirationTime() {
  var $discountEl = $('.js-discount-expires-in')
  var untilString = getTimeUntil(SPECIAL_OFFER_END)
  $discountEl.text(untilString)
}

$(document).ready(function() {
  setDiscountExpirationTime()
})
