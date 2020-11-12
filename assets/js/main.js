function getTopOffet(element) {
  return parseInt($(element).offset().top) - 200
}
function smoothScrollToAnchor(){
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault()
        var href = $.attr(this, 'href')
        $('html, body').animate({
          scrollTop: getTopOffet(href)
        }, 500, function () {
          window.location.hash = href;
        })
        stickyMenuClass()
    })
  })
}
function setActiveAnchor(tag) {
  $("#markdown-toc a").removeClass("active")
  if(tag && tag.id) {
    $("#markdown-toc a[href$='#"+tag.id+"']").addClass("active")
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
      if(getTopOffet($('#'+hTags[i + 1].id)) > scrollTop) {
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
    if($("#markdown-toc a[href$='#"+id+"']").length > 0) {
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

  var normalizePath = function(path) {
    if(path == undefined) return ""; else {
      var newPath = path
      if(!path.startsWith("/"))
        newPath = "/" + newPath
      if(!path.endsWith("/"))
        newPath = newPath + "/"

      return newPath
    }
  }

  var currentPath = normalizePath(window.location.pathname)
  $("#docs-sidebar a").each(function(i, anchor) {
    var anchorPath = normalizePath(anchor.pathname)

    if(anchorPath === currentPath) {
      if(anchor.hash == undefined || anchor.hash.trim().length == 0) {
        $(this).addClass("active")
      }
    } else if(currentPath.startsWith(anchorPath)) {
      $(this).addClass("active-parent")
    }
  })
}


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

function moveDocumentationTocToSidebar() {
  var tocContainer = $("#docs-sidebar #toc-container")
  var markdownToc = $("#markdown-toc")

  if(tocContainer.length) {
    console.log("Trying to move the thing")
    if(markdownToc.length) {
      markdownToc.removeClass("d-none")
      markdownToc.appendTo(tocContainer)
    } else {
      tocContainer.remove()
    }
  }
}

function scrollOnDocsSidebar() {
  const adjustSidebarMaxHeight = function() {
    const viewportHeight = window.innerHeight
    const contentHeight = $(document).height()
    const footerHeight = $("#layout-footer").height() + 280
    const currentScroll = $(window).scrollTop()
    var visibleFooterArea = (currentScroll + viewportHeight) - (contentHeight - footerHeight)
    if(visibleFooterArea < 0)
      visibleFooterArea = 0

    const docsSidebarSelector = "#docs-sidebar:not(.mobile-docs-sidebar)"
    const topMargin = currentScroll > 50 ? 200 : 265
    const availableSidebarHeight = viewportHeight - visibleFooterArea - topMargin
    const sidebarHeight = $("#docs-sidebar").children().height()


    if(availableSidebarHeight < sidebarHeight) {
      if(availableSidebarHeight > 0) {
        $("#docs-sidebar").css({ maxHeight: availableSidebarHeight })
        $("#docs-sidebar").addClass('d-lg-block')
        $("#docs-sidebar").removeClass('d-lg-none')
      } else {
        $("#docs-sidebar").removeClass('d-lg-block')
        $("#docs-sidebar").addClass('d-lg-none')
      }
    } else {
      $("#docs-sidebar").css({ maxHeight: "" })
      $("#docs-sidebar").addClass('d-lg-block')
      $("#docs-sidebar").removeClass('d-lg-none')
    }
  }

  if($("#docs-sidebar").length && $("#layout-footer").length) {
    adjustSidebarMaxHeight()

    $(window).scroll(function() {
      adjustSidebarMaxHeight()
    })
  }
}

function copySidebarContentForMobile() {
  const docsNavigationLinks = $("#docs-navigation-links > ul")
  const mobileDocsNavigationContainer = $("#docs-navigation-container")
  const mobileDocsSidebarContainer = $("#docs-sidebar-container")

  if(docsNavigationLinks.length && mobileDocsNavigationContainer.length) {
    const copyOfLinks = docsNavigationLinks.clone()
    const sectionName = window.location.pathname.startsWith("/blog") ? "Blog" : "Documentation"
    mobileDocsNavigationContainer.append("<div class=\"section-title\">" + sectionName + "</div>")
    mobileDocsNavigationContainer.append(copyOfLinks)

    const docsSidebar = $("#docs-sidebar")
    if(docsSidebar.length && mobileDocsSidebarContainer.length) {
      const copyOfDocsSidebar = docsSidebar.clone()
      copyOfDocsSidebar.attr("id", "mobile-docs-sidebar")
      copyOfDocsSidebar.addClass("mobile-docs-sidebar")
      copyOfDocsSidebar.removeClass("col")
      copyOfDocsSidebar.removeClass("d-none")
      copyOfDocsSidebar.removeClass("d-lg-block")
      mobileDocsSidebarContainer.append(copyOfDocsSidebar)
    } else {
      mobileDocsSidebarContainer.remove()
    }
  } else {
    mobileDocsNavigationContainer.remove()
    mobileDocsSidebarContainer.remove()
  }
}

function startInstrumentationSlideshow() {
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

function installToggleOptions(rootElementID, suffix, options) {
  if($(`#${rootElementID}`).length > 0) {

    // hides all options and ensures only the provided option is shown
    const showOption = function(optionName) {
      options.forEach(o => {
        if(o !== optionName) {
          $(`#${o}-${suffix}-toggle`).removeClass("active")
          $(`#${o}-${suffix}`).removeClass("d-block")
          $(`#${o}-${suffix}`).addClass("d-none")
        }
      })

      $(`#${optionName}-${suffix}-toggle`).addClass("active")
      $(`#${optionName}-${suffix}`).addClass("d-block")
      $(`#${optionName}-${suffix}`).removeClass("d-none")
    }

    // Register manual toggles
    options.forEach(o => $(`#${o}-${suffix}-toggle`).click(function() { showOption(o) }))
  }
}

function scrollHeaders() {
  const MAIN_HEADER_HEIGHT = 52;

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
  moveDocumentationTocToSidebar()
  smoothScrollToAnchor()
  toggleActiveAnchor()
  searchInit()
  scrollOnDocsSidebar()
  copySidebarContentForMobile()
  startInstrumentationSlideshow()
  installToggleOptions("get-started-options", "setup-steps", ['plain', 'play', 'manual'])
  installToggleOptions("reporter-options", "setup-steps", ['apm', 'prometheus', 'zipkin', 'influxdb', 'datadog'])
  scrollHeaders()
})
