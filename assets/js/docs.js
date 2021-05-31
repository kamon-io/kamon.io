function copyToClipboard(text) {
  if (window.clipboardData && window.clipboardData.setData) {
    // Internet Explorer-specific code path to prevent textarea being shown while dialog is visible.
    return clipboardData.setData("Text", text);

  }
  else if (document.queryCommandSupported && document.queryCommandSupported("copy")) {
    var textarea = document.createElement("textarea");
    textarea.textContent = text;
    textarea.style.position = "fixed";  // Prevent scrolling to bottom of page in Microsoft Edge.
    document.body.appendChild(textarea);
    textarea.select();
    try {
      return document.execCommand("copy");  // Security exception may be thrown by some browsers.
    }
    catch (ex) {
      console.warn("Copy to clipboard failed.", ex);
      return false;
    }
    finally {
      document.body.removeChild(textarea);
    }
  }
}

function initCodeExampleCopy() {
  var codeExampleContainers = document.getElementsByClassName("code-example-container")
  if (codeExampleContainers.length === 0) {
    return
  }
  Array.from(codeExampleContainers).forEach(function(codeExample) {
    $(codeExample).on("click", "div.fa-copy", function(e) {
      e.preventDefault()
      var codeTabs = $(codeExample).find(".tab-pane")
      if (codeTabs != null && codeTabs.length !== 0) {
        var copyText = $(codeExample).find(".tab-pane.active").text().trim()
        copyToClipboard(copyText)
      } else {
        var copyText = $(codeExample).find("code").text().trim()
        copyToClipboard(copyText)
      }
    })
  })
}

function initVideoLightbox() {
  const $videos = $('[data-video-src]')
  $videos.each(function() {
    const $video = $(this)
    const src = $video.data('video-src')
    const caption = $video.data('caption')
    const videoHtml = `<div class="w-100 my-4 text-center kamon-lightbox">
    <a href="${src}" data-caption="${caption}" data-fancybox="lightbox">
      <video loop muted autoplay width="100%" class="my-2">
        <source src="${src}" type="video/mp4">
      </video>
      <br>
      ${caption}
    </a>
  </div>`
    $video.replaceWith(videoHtml)
  })
}

$(document).ready(function() {
  initCodeExampleCopy()
  initVideoLightbox()
})
