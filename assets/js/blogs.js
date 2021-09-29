const perPage = 6
let currentPage = 1

function adjustVisibility() {
  const posts = $('.js-blog-card')
  const currentlyVisible = currentPage * perPage

  posts.each((i, el) => {
    const $e = $(el)
    if (i < currentlyVisible) {
      $e.removeClass('invisible')
    }
  })

  const loadMoreButton = $('.js-load-more')

  if (currentlyVisible > posts.length) {
    loadMoreButton.addClass('invisible')
  }
}

function loadMore() {
  currentPage++
  adjustVisibility()
}

$(document).ready(() => {
  $('.js-blog-card').each((i, el) => $(el).addClass('invisible'))
  adjustVisibility()
  $('.js-load-more').on('click', loadMore)
})
