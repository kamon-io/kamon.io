// function displaySearchResults(results) {
//   var appendString = '<li>No results found</li>'
//   if (results.length > 0) {
//     appendString = '';
//     for (var i = 0; i < results.length; i++) {
//       var item = window.store[results[i].ref];
//       var showDescCharactes = 30
//       appendString += '<li>'
//         appendString += '<a href="' + item.url + '">'
//           appendString += '<h3>' + item.title + '<span>'+item.product+'</span></h3>'
//           appendString += '<p>' + item.description.substring(0, showDescCharactes) + (item.description.length > showDescCharactes ? + '...' : '') + '</p>'
//         appendString += '</a>'
//       appendString += '</li>'
//     }
//     $("#search-results").html(appendString)
//   }
//   $("#search-results").html(appendString)
// }
//
// var activeSearchResult = undefined
// function setActiveSearchResult(movePositions) {
//   var searchResults = $("#search-results li")
//   $(searchResults).removeClass("active")
//   var resultCount = $(searchResults).length
//
//   if(movePositions != undefined && resultCount > 0) {
//     if(activeSearchResult == undefined){
//       if(movePositions >= 0){
//         activeSearchResult = 0
//       } else {
//         activeSearchResult = resultCount - 1
//       }
//     }else {
//       activeSearchResult = activeSearchResult + movePositions
//       if(activeSearchResult < 0) {
//         activeSearchResult = resultCount - 1
//       } else if(activeSearchResult >= resultCount) {
//         activeSearchResult = 0
//       }
//     }
//     $($(searchResults[activeSearchResult])).addClass("active")
//   }
// }
//
// function searchInit() {
//   var idx = lunr(function () {
//     this.ref('id')
//     this.field('title', { boost: 10 })
//     this.field('category', { boost: 5 })
//     this.field('description', { boost: 2 })
//     this.field('content')
//     this.field('product')
//     this.field('version')
//
//
//     for (var key in window.store) {
//       this.add({
//         'id': key,
//         'title': window.store[key].title,
//         'category': window.store[key].category,
//         'content': window.store[key].content,
//         'product': window.store[key].product,
//         'version': window.store[key].version
//       })
//     }
//   })
//   $("#search-box").keyup(function(event){
//     if(event.keyCode == 27) {
//       $(this).val("")
//       $("#search-results").html("")
//     } else if(event.keyCode == 13) {
//       window.open($($("#search-results li a")[activeSearchResult]).attr("href"), "_self")
//     }else if(event.keyCode == 40) {
//       setActiveSearchResult(1)
//     }else if(event.keyCode == 38) {
//       setActiveSearchResult(-1)
//     } else {
//       setActiveSearchResult(undefined)
//       var value = $(this).val();
//       var urlParts = window.location.pathname.split('/')
//       var currentProductResults = idx.query(function(q) {
//         q.term(value, { fields: [ 'title' ], boost: 10 })
//         q.term(value, { fields: [ 'category' ], boost: 5 })
//         q.term(value, { fields: [ 'description' ], boost: 2 })
//         q.term(value, { fields: [ 'content' ] })
//         if(urlParts.length > 2){
//           q.term(urlParts[1], { fields: [ 'product' ], presence: lunr.Query.presence.REQUIRED })
//           q.term(urlParts[2], { fields: [ 'version' ], presence: lunr.Query.presence.REQUIRED })
//         }
//       })
//       var otherProductLatest = idx.query(function(q) {
//         q.term(value, { fields: [ 'title' ], boost: 10 })
//         q.term(value, { fields: [ 'category' ], boost: 5 })
//         q.term(value, { fields: [ 'description' ], boost: 2 })
//         q.term(value, { fields: [ 'content' ] })
//         if(urlParts.length > 2){
//           q.term(urlParts[1], { fields: [ 'product' ], presence: lunr.Query.presence.PROHIBITED })
//           q.term("latest", { fields: [ 'version' ], presence: lunr.Query.presence.REQUIRED })
//         }
//       })
//       displaySearchResults(currentProductResults.concat(otherProductLatest))
//     }
//   })
// }

function convertRemToPixels(rem) {
  return rem * parseFloat(getComputedStyle(document.documentElement).fontSize);
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

function initScrollHeaders() {
  const MAIN_HEADER_HEIGHT = 57;

  // Switch main header from transparent to white + border bottom depending on scroll position
  function initScrollMainHeader() {
    var mainHeader = document.getElementById("main-header")
    if (mainHeader == null || mainHeader.classList.contains("header-background-fixed")) {
      return
    }
    var isMainHeaderTransparent = true
    var mainHeaderScrollListener = function() {
      if (isMainHeaderTransparent && window.pageYOffset >= MAIN_HEADER_HEIGHT) {
        mainHeader.classList.add("header-background-color")
        isMainHeaderTransparent = false
      } else if (!isMainHeaderTransparent && window.pageYOffset <= MAIN_HEADER_HEIGHT) {
        mainHeader.classList.remove("header-background-color")
        isMainHeaderTransparent = true
      }
    }
    document.addEventListener("scroll", mainHeaderScrollListener, { passive: true })
  }

  // Switch docs header/sidebar navigation position from default to fixed/sticky depending on scroll position
  function initScrollDocsHeader() {
    var docsHeader = document.getElementById("docs-header")
    if (docsHeader == null) {
      return
    }
    var isDocsHeaderFixed = false
    var maxDocsHeaderOffset = MAIN_HEADER_HEIGHT + convertRemToPixels(14) - 52
    var docsHeaderScrollListener = function() {
      if (!isDocsHeaderFixed && window.pageYOffset > maxDocsHeaderOffset) {
        docsHeader.classList.add("fixed-docs-header")
        isDocsHeaderFixed = true
      } else if (isDocsHeaderFixed && window.pageYOffset < maxDocsHeaderOffset) {
        docsHeader.classList.remove("fixed-docs-header")
        isDocsHeaderFixed = false
      }
    }
    document.addEventListener("scroll", docsHeaderScrollListener, { passive: true })
  }

  initScrollMainHeader()
  initScrollDocsHeader()
}

// mobile docs navigation covers the entire screen, hide body scroll when it's open
function initHideBodyScrollOnMobileDocsNavigation() {
  var mobileDocsToggler = document.getElementById("mobile-docs-navigation-toggle")
  if (mobileDocsToggler != null) {
    mobileDocsToggler.addEventListener("click", function() {
      document.getElementsByTagName("body")[0].classList.toggle("overflow-hidden")
    })
  }
}

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

function getTopOffset(element) {
  return parseInt($(element).offset().top) - 200
}

function smoothScrollToAnchor(){
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
      e.preventDefault()
      var href = $.attr(this, 'href')
      $('html, body').animate({
        scrollTop: getTopOffset(href)
      }, 500, function () {
        window.location.hash = href;
      })
    })
  })
}

function setActiveAnchor(tag) {
  $("#markdown-toc-container a").removeClass("active")
  if(tag && tag.id) {
    $("#markdown-toc-container a[href$='#"+tag.id+"']").addClass("active")
  }
}

function findCurrentAnchor(hTags) {
  if ((window.innerHeight + window.scrollY + 10) >= document.body.offsetHeight) {
    return hTags[hTags.length - 1]
  }

  var currentHtag = hTags[0]
  var scrollTop = $(window).scrollTop()
  for(var i=0; i< hTags.length; i++){
    currentHtag = hTags[i]
    if(hTags.length > (i + 1)) {
      if(getTopOffset($('#'+hTags[i + 1].id)) > scrollTop) {
        break
      }
    }
  }
  return currentHtag
}

function toggleActiveAnchor() {
  var hTags = []
  $(":header[id]").each(function(i, tag){
    var id = $(tag).attr('id')
    if($("#markdown-toc-container a[href$='#"+id+"']").length > 0) {
      hTags.push({
        id: id,
        top: $(tag).offset().top - parseInt($(tag).css('marginTop'))
      })
    }
  })

  setActiveAnchor(findCurrentAnchor(hTags))
  $(window).scroll(function(e){
    setActiveAnchor(findCurrentAnchor(hTags))
  })

  const normalizePath = function(path) {
    if(path == null) {
      return ""
    } else {
      var newPath = path
      if(!path.startsWith("/"))
        newPath = "/" + newPath
      if(!path.endsWith("/"))
        newPath = newPath + "/"

      return newPath
    }
  }

  const currentPath = normalizePath(window.location.pathname)
  $("#docs-sidebar a").each(function(i, anchor) {
    const anchorPath = normalizePath(anchor.pathname)

    if(anchorPath === currentPath) {
      if(anchor.hash == undefined || anchor.hash.trim().length == 0) {
        $(this).addClass("active")
      }
    } else if(currentPath.startsWith(anchorPath)) {
      $(this).addClass("active-parent")
    }
  })
}

function initDocsMarkdownToc() {
  var hasMarkdownToc = document.getElementById("docs-markdown-toc")
  if (!hasMarkdownToc) {
    return
  }
  var allMarkdownSubtitles = $("#docs-content h2").get()
  var ul = document.createElement("ul")
  allMarkdownSubtitles.forEach(function(h2) {
    var a = document.createElement("a")
    a.textContent = h2.innerText
    a.setAttribute("href", "#" + h2.id)
    a.classList.add("search-result-link")
    var li = document.createElement("li")
    li.append(a)
    li.classList.add("docs-section-link")
    ul.append(li)
  })
  var markdownTocContainer = document.getElementById("markdown-toc-container")
  // var mobileMarkdownTocContainer = document.getElementById("markdown-toc-container-mobile")
  markdownTocContainer.append(ul)
  // mobileMarkdownTocContainer.append(ul)
}

function copyCodeExample(snippetID) {
  console.log(`trying to copy code with id ${snippetID}`)
}

$(document).ready(function() {
  // searchInit()
  instrumentationSlideshow()
  initScrollHeaders()
  initHideBodyScrollOnMobileDocsNavigation()
  initAnnualBillingToggle()
  initDocsMarkdownToc()
  toggleActiveAnchor()
  smoothScrollToAnchor()
})
