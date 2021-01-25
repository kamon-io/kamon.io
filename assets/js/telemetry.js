function instrumentationSlideshow() {
  const options = [
    'metrics',
    'tracing',
    'context',
  ]

  var currentOption = 0

  if($("#instrumentation-slideshow").length > 0) {
    // hides all options and ensures only the provided option is shown
    const showOption = function(optionName) {
      currentOption = options.indexOf(optionName)
      options.forEach(o => {
        if(o !== optionName) {
          $(`#${o}-code-example-toggle`).removeClass("active")
          $(`#${o}-code-example`).removeClass("d-block")
          $(`#${o}-code-example`).addClass("d-none")
        }
      })

      $(`#${optionName}-code-example-toggle`).addClass("active")
      $(`#${optionName}-code-example`).addClass("d-block")
      $(`#${optionName}-code-example`).removeClass("d-none")
    }

    // Register manual toggles
    $("#metrics-code-example-toggle").click(function() { showOption('metrics'); })
    $("#tracing-code-example-toggle").click(function() { showOption('tracing'); })
    $("#context-code-example-toggle").click(function() { showOption('context'); })
  }
}

$(document).ready(function() {
  instrumentationSlideshow()
})
