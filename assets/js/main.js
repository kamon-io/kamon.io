function displaySearchResults(results) {
  var appendString = '<li>No results found</li>';
  if (results.length > 0) {
    appendString = '';
    for (var i = 0; i < results.length; i++) {
      var item = window.store[results[i].ref];
      var showDescCharactes = 30
      appendString += '<li>'
        appendString += '<a href="' + item.url + '">'
          appendString += '<h3>' + item.title + '<span>'+item.product+'</span></h3>'
          appendString += '<p>' + item.description.substring(0, showDescCharactes) + (item.description.length > showDescCharactes ? + '...' : '') + '</p>'
        appendString += '</a>'
      appendString += '</li>'
    }
    $("#search-results").html(appendString)
  }
  $("#search-results").html(appendString)
}

var activeSearchResult = undefined
function setActiveSearchResult(movePositions) {
  var searchResults = $("#search-results li")
  $(searchResults).removeClass("active")
  var resultCount = $(searchResults).length

  if(movePositions != undefined && resultCount > 0) {
    if(activeSearchResult == undefined){
      if(movePositions >= 0){
        activeSearchResult = 0
      } else {
        activeSearchResult = resultCount - 1
      }
    }else {
      activeSearchResult = activeSearchResult + movePositions
      if(activeSearchResult < 0) {
        activeSearchResult = resultCount - 1
      } else if(activeSearchResult >= resultCount) {
        activeSearchResult = 0
      }
    }
    $($(searchResults[activeSearchResult])).addClass("active")
  }
}

function searchInit() {
  var idx = lunr(function () {
    this.ref('id')
    this.field('title', { boost: 10 })
    this.field('category', { boost: 5 })
    this.field('description', { boost: 2 })
    this.field('content')
    this.field('product')
    this.field('version')


    for (var key in window.store) {
      this.add({
        'id': key,
        'title': window.store[key].title,
        'category': window.store[key].category,
        'content': window.store[key].content,
        'product': window.store[key].product,
        'version': window.store[key].version
      })
    }
  })
  $("#search-box").keyup(function(event){
    if(event.keyCode == 27) {
      $(this).val("")
      $("#search-results").html("")
    } else if(event.keyCode == 13) {
      window.open($($("#search-results li a")[activeSearchResult]).attr("href"), "_self")
    }else if(event.keyCode == 40) {
      setActiveSearchResult(1)
    }else if(event.keyCode == 38) {
      setActiveSearchResult(-1)
    } else {
      setActiveSearchResult(undefined)
      var value = $(this).val();
      var urlParts = window.location.pathname.split('/')
      var currentProductResults = idx.query(function(q) {
        q.term(value, { fields: [ 'title' ], boost: 10 })
        q.term(value, { fields: [ 'category' ], boost: 5 })
        q.term(value, { fields: [ 'description' ], boost: 2 })
        q.term(value, { fields: [ 'content' ] })
        if(urlParts.length > 2){
          q.term(urlParts[1], { fields: [ 'product' ], presence: lunr.Query.presence.REQUIRED })
          q.term(urlParts[2], { fields: [ 'version' ], presence: lunr.Query.presence.REQUIRED })
        }
      })
      var otherProductLatest = idx.query(function(q) {
        q.term(value, { fields: [ 'title' ], boost: 10 })
        q.term(value, { fields: [ 'category' ], boost: 5 })
        q.term(value, { fields: [ 'description' ], boost: 2 })
        q.term(value, { fields: [ 'content' ] })
        if(urlParts.length > 2){
          q.term(urlParts[1], { fields: [ 'product' ], presence: lunr.Query.presence.PROHIBITED })
          q.term("latest", { fields: [ 'version' ], presence: lunr.Query.presence.REQUIRED })
        }
      })
      displaySearchResults(currentProductResults.concat(otherProductLatest))
    }
  })
}

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

function scrollHeaders() {
  const MAIN_HEADER_HEIGHT = 57;

  // Switch main headers from transparent to white + border bottom depending on scroll position
  function scrollMainHeader() {
    var mainHeader = document.getElementById("main-header");
    if (mainHeader == null) {
      return;
    }
    var isMainHeaderColorFixed = mainHeader.classList.contains("header-background-fixed");
    if (isMainHeaderColorFixed) {
      return;
    }
    var isMainHeaderTransparent = true;
    document.addEventListener(
      "scroll",
      function() {
        if (isMainHeaderTransparent && window.pageYOffset > MAIN_HEADER_HEIGHT) {
          mainHeader.classList.add("header-background-colour");
          isMainHeaderTransparent = false;
        } else if (!isMainHeaderTransparent && window.pageYOffset < MAIN_HEADER_HEIGHT) {
          mainHeader.classList.remove("header-background-colour");
          isMainHeaderTransparent = true;
        }
      },
      { passive: true });
  }
  scrollMainHeader();

  // Switch docs header/sidebar navigation position from default to fixed/sticky depending on scroll position
  function scrollDocsHeader() {
    var isDocsHeaderFixed = false;
    var docsHeader = document.getElementById("docs-header");
    if (docsHeader == null) {
      return;
    }
    var offset = docsHeader.offsetTop;
    document.addEventListener(
      "scroll",
      function() {
        if (!isDocsHeaderFixed && window.pageYOffset + MAIN_HEADER_HEIGHT > offset) {
          docsHeader.classList.add("fixed-docs-header");
          isDocsHeaderFixed = true;
        } else if (isDocsHeaderFixed && window.pageYOffset + MAIN_HEADER_HEIGHT < offset) {
          docsHeader.classList.remove("fixed-docs-header");
          isDocsHeaderFixed = false;
        }
      },
      { passive: true });
  }
  scrollDocsHeader();
}

$(document).ready(function() {
  searchInit()
  instrumentationSlideshow()
  scrollHeaders()
})
