import { getAssetFromKV } from '@cloudflare/kv-asset-handler'

/**
 * The DEBUG flag will do two things that help during development:
 * 1. we will skip caching on the edge, which makes it easier to
 *    debug.
 * 2. we will return an error message on exception in your Response rather
 *    than the default 404.html page.
 */
const DEBUG = false

addEventListener('fetch', event => {
  try {
    event.respondWith(handleEvent(event))
  } catch (e) {
    if (DEBUG) {
      return event.respondWith(
        new Response(e.message || e.toString(), {
          status: 500,
        }),
      )
    }
    event.respondWith(new Response('Internal Error', { status: 500 }))
  }
})

async function handleEvent(event) {
  const url = new URL(event.request.url)
  let options = {}

  /**
   * You can add custom logic to how we fetch your assets
   * by configuring the function `mapRequestToAsset`
   */
  options.mapRequestToAsset = handleJekyllOutput()

  try {
    if (DEBUG) {
      // customize caching
      options.cacheControl = {
        bypassCache: true,
      }
    }
    return await getAssetFromKV(event, options)
  } catch (e) {
    // if an error is thrown try to serve the asset at 404.html
    if (!DEBUG) {
      try {
        let notFoundResponse = await getAssetFromKV(event, {
          mapRequestToAsset: req => new Request(`${new URL(req.url).origin}/404.html`, req),
        })

        return new Response(notFoundResponse.body, { ...notFoundResponse, status: 404 })
      } catch (e) {}
    }

    return new Response(e.message || e.toString(), { status: 500 })
  }
}

function handleJekyllOutput() {
  return request => {
    const parsedUrl = new URL(request.url)
    let pathname = parsedUrl.pathname

    if(pathname.endsWith('/') || pathname === '/blog') {
      let newPath;

      if (pathname === '/') {
        // Fixes trying to access directly to https://kamon.io, without any path.
        newPath = "/index.html"
      }
      else if (pathname === '/blog' || pathname.startsWith('/blog/')) {
        newPath = pathname + "index.html"
      }
      else {
        // Replaces paths like /instrumentation/ -> /instrumentation.html
        newPath = pathname.substring(0, pathname.length - 1) + '.html'
      }

      parsedUrl.pathname = newPath
      return new Request(parsedUrl, request)

    }

    return request
  }
}
