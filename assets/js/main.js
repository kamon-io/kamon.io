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

  const normalizePath = function(path) {
    if(path == undefined) return ""; else {
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

function scrollElevation() {
  $(window).scroll(function() {
    var scroll = $(window).scrollTop();

    if (scroll >= 50) {
        $(".header-bar").addClass("elevation-header");
        $("body").addClass("has-scroll");
        $("#docs-title").hide()
      } else {
        $(".header-bar").removeClass("elevation-header");
        $("body").removeClass("has-scroll");
        $("#docs-title").show()
    }
  });
}

function moveDocumentationTocToSidebar() {
  var tocContainer = $("#docs-sidebar #toc-container")
  var markdownToc = $("#markdown-toc")

  console.log("evaluating")
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
    const footerHeight = $("#layout-footer").height()
    const currentScroll = $(window).scrollTop()
    var visibleFooterArea = (currentScroll + viewportHeight) - (contentHeight - footerHeight)
    if(visibleFooterArea < 0)
      visibleFooterArea = 0

    const docsSidebarSelector = "#docs-sidebar:not(.mobile-docs-sidebar)"
    const topMargin = currentScroll > 50 ? 200 : 265
    const availableSidebarHeight = viewportHeight - visibleFooterArea - topMargin
    const sidebarHeight = $("#docs-sidebar").children().height()


    if(availableSidebarHeight < sidebarHeight) {
      $("#docs-sidebar").css({ maxHeight: availableSidebarHeight })
    } else {
      $("#docs-sidebar").css({ maxHeight: "" })
    }
  }

  if($("#docs-sidebar").length && $("#layout-footer").length) {
    adjustSidebarMaxHeight()

    $(window).scroll(function() {
      adjustSidebarMaxHeight()
    })
  }
}

function toggleMobileSidebar() {
  $('#mobile-sidebar-button').click(function(event) {
    $('#mobile-sidebar').toggleClass('open')
    event.preventDefault()
  })

  $(window).click(function(event) {
    if($("#mobile-sidebar.open").length) {
      const clickTarget = $(event.target)

      if(event.target.id != "mobile-sidebar-button"&& !clickTarget.parents("#mobile-sidebar-button").length) {
        if(event.target.id != "mobile-sidebar" && !clickTarget.parents("#mobile-sidebar").length) {
          console.log("I should close: ", event.target.id)
          $('#mobile-sidebar').removeClass('open')
        }
      }

    } else {
      console.log("The sidebar was not open, nothing matters")
    }
  })
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

$(document).ready(function() {
  moveDocumentationTocToSidebar()
  smoothScrollToAnchor()
  toggleActiveAnchor()
  searchInit()
  scrollElevation()
  scrollOnDocsSidebar()
  toggleMobileSidebar()
  copySidebarContentForMobile()
})