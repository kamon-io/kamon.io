function generateTableOfContents() {
  const $container = $('.js-markdown-content')
  const $toc = $('.js-toc')
  const $headingsLevel1 = $container.find('h1')
  const $headingsLevel2 = $container.find('h2')
  const $headings = $headingsLevel1.length ? $headingsLevel1 : $headingsLevel2

  $headings.each((_index, h) => {
    const $h = $(h)
    const text = $h.text()
    const link = $h.attr('id')
    const $linkEl = $(`<li class="toc-item"><a href="#${link}">${text}</a></li>`)
    $toc.append($linkEl)
  })
}

// Note: Liquid seems to have a problem of auto-closing tags
// when trying to group by modulo of index. I cannot figure out why,
// except for the fact that Liquid sucks. Hence JS grouping
function createCarouselFromItems() {
  const perPage = window.innerWidth >= 992 ? 3 : 1
  const $templateContainer = $('.js-related-posts-templates')
  const $templates = $templateContainer.children()
  const $carousel = $('.js-carousel-items')
  const $indicators = $('.js-carousel-indicators')

  let elementBuffer = []

  const appendBuffer = () => {
    if (elementBuffer.length > 0) {
      const $container = $('<div class="row d-flex justify-content-center"></div>')
      elementBuffer.forEach(($el) => {
        $container.append($el)
      })
      const $item = $('<div class="carousel-item"></div>')
      $item.append($container)
      $carousel.append($item)
    }

    elementBuffer = []
  }

  let page = -1

  for (let i = 0; i < $templates.length; i++) {
    if (i % perPage === 0) {
      page++
      appendBuffer()
    }

    // Only go with the first several pages
    if (page >= 5) {
      break
    }

    elementBuffer.push($($templates[i]))
    
    if (perPage === 1 && $templates.length > 1) {
      const $indicator = $(`<li class="js-indicator" data-target="#blogPostsCarousel" data-slide-to="${i}"></li>`)
      $indicators.append($indicator)
    }
  }

  appendBuffer()

  // Clean up the initial templates
  $templateContainer.remove()
}

function initCarouselItems() {
  createCarouselFromItems()
  const $items = $('.carousel-item')
  const $indicators = $('.js-indicator')
  const first = $items.get(0)

  if (first) {
    $(first).addClass('active')
    if ($indicators[0]) {
      $indicators[0].classList.add('active')
    }
  } else {
    // We don't have any items then. Let's remove the whole thing
    $('.js-related-items-carousel-container').remove()
  }

  if ($items.length === 1) {
    $('.js-carousel-btn').remove()
  }
}

$(document).ready(() => {
  generateTableOfContents()
  initCarouselItems()
  
  // I have no idea why, but Jekyll variables don't work here with blog posts, so hack it
  $('#main-header').attr('data-transparent', 'true')
  $('.footer-box-background').addClass('bg-lightest')
  $(document).trigger('reloadHeader')
})
