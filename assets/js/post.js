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

$(document).ready(() => {
  generateTableOfContents()

  // I have no idea why, but Jekyll variables don't work here with blog posts, so hack it
  $('#main-header').attr('data-transparent', 'true')
  $('.footer-box-background').addClass('bg-lightest')
  $(document).trigger('reloadHeader')
})
