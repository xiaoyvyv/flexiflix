package com.xiaoyv.comic.flexiflix.ui.component.web

import android.content.Context
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.xiaoyv.flexiflix.common.utils.debugLog
import com.xiaoyv.flexiflix.extension.MediaSourceFactory
import com.xiaoyv.flexiflix.extension.impl.java.network.interceptor.CommonInterceptor
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Request

/**
 * [WebViewer]
 *
 * @author why
 * @since 5/29/24
 */
@Composable
fun WebViewer(
    modifier: Modifier = Modifier,
    url: String,
    onWebViewCreated: (WebView) -> Unit = {},
    onProgressChange: (WebView, Float) -> Unit = { _, _ -> },
) {
    val currentUrl by rememberUpdatedState(url)
    var webView: WebView? by remember { mutableStateOf(null) }

    AndroidView(
        modifier = modifier.testTag("WebViewer"),
        factory = {
            createWebView(
                context = it,
                onProgressChange = onProgressChange
            ).apply {
                // 同步 Cookie
                syncHttpClientCookieToWeb(url)

                onWebViewCreated(this)
                webView = this
            }
        },
        update = {
            debugLog { "WebView loadUrl: $currentUrl" }

            it.loadUrl(currentUrl)
        },
        onRelease = {
            it.destroy()

            debugLog { "Release WebView" }
        }
    )
}

@Suppress("DEPRECATION", "SetJavaScriptEnabled")
private fun createWebView(
    context: Context,
    onProgressChange: (WebView, Float) -> Unit = { _, _ -> },
): WebView {
    val webView = WebView(context)

    webView.webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            onProgressChange(view, newProgress / 100f)
        }
    }

    webView.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest,
        ): Boolean {
            if (URLUtil.isNetworkUrl(request.url.toString())) {
                view.loadUrl(request.url.toString())
                return true
            }

            return true
        }

        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest,
        ): WebResourceResponse? {

            Request.Builder()
                .method(request.method, null)
                .build()


            return super.shouldInterceptRequest(view, request)
        }
    }

    webView.settings.apply {
        javaScriptEnabled = true
        userAgentString = CommonInterceptor.userAgent
        useWideViewPort = true
        loadWithOverviewMode = true
        builtInZoomControls = true
        displayZoomControls = false
        allowFileAccess = true
        allowContentAccess = true
        allowFileAccessFromFileURLs = true
        loadsImagesAutomatically = true
        defaultTextEncodingName = "UTF-8"
        cacheMode = WebSettings.LOAD_DEFAULT
        databaseEnabled = true
        domStorageEnabled = true
        blockNetworkImage = false
        textZoom = 100
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        setSupportZoom(true)
        setSupportMultipleWindows(false)
        setGeolocationEnabled(true)
    }

    webView.layoutParams = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    CookieManager.getInstance().removeAllCookie()

    return webView
}

/**
 * 将 HttpClient 的 Cookie 同步到 WebView
 */
fun syncHttpClientCookieToWeb(url: String) {
    val httpUrl = url.toHttpUrlOrNull() ?: return
    val cookies = MediaSourceFactory.cookieJar.loadForRequest(httpUrl)

    CookieManager.getInstance().apply {
        setAcceptCookie(true)
        cookies.forEach {
            setCookie(url, it.name + "=" + it.value)
        }
        flush()
    }
}

/**
 * 将 WebView 的 Cookie 同步到 HttpClient
 */
fun syncWebCookieToHttpClient(url: String) {
    val httpUrl = url.toHttpUrlOrNull() ?: return

    val cookies = CookieManager.getInstance().getCookie(url)
        .split(";")
        .mapNotNull {
            Cookie.parse(httpUrl, it)?.newBuilder()?.expiresAt(Long.MAX_VALUE)?.build()
        }

    debugLog { "同步：" + cookies.map { it.toString() } }

    MediaSourceFactory.cookieJar.saveFromResponse(httpUrl, cookies)
}
