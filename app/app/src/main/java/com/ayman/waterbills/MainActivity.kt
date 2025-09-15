package com.ayman.waterbills

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var pendingText: String? = null

    private val saveLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res: ActivityResult ->
            if (res.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = res.data?.data
                try {
                    contentResolver.openOutputStream(uri!!)?.use { os ->
                        os.write((pendingText ?: "").toByteArray())
                    }
                    toast("طھظ… ط§ظ„ط­ظپط¸ âœ…")
                } catch (e: Exception) {
                    toast("طھط¹ط°ظ‘ط± ط§ظ„ط­ظپط¸: ${e.message}")
                }
            }
            pendingText = null
        }

    private val openLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = res.data?.data
                try {
                    val txt = contentResolver.openInputStream(uri!!)?.reader()?.readText() ?: ""
                    webView.evaluateJavascript(
                        "window.onBackupImported && onBackupImported(${txt.js()})",
                        null
                    )
                    toast("طھظ… ط§ظ„ط§ط³طھظٹط±ط§ط¯ âœ…")
                } catch (e: Exception) {
                    toast("طھط¹ط°ظ‘ط± ط§ظ„ط§ط³طھظٹط±ط§ط¯: ${e.message}")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        configureWebView(webView)
        webView.loadUrl("file:///android_asset/index.html")
    }

    private fun configureWebView(wv: WebView) {
        with(wv.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowUniversalAccessFromFileURLs = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = false
            displayZoomControls = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        wv.webChromeClient = WebChromeClient()

        wv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                return handleExternalUrl(url)
            }

            @Suppress("DEPRECATION")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url.isNullOrEmpty()) return false
                return handleExternalUrl(url)
            }

            private fun handleExternalUrl(url: String): Boolean {
                return try {
                    if (url.startsWith("whatsapp://", true) ||
                        url.startsWith("intent://", true) ||
                        url.contains("api.whatsapp.com", true) ||
                        url.contains("wa.me/", true)
                    ) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                            addCategory(Intent.CATEGORY_BROWSABLE)
                        })
                        true
                    } else false
                } catch (e: ActivityNotFoundException) {
                    toast("ظ„ط§ ظٹظˆط¬ط¯ طھط·ط¨ظٹظ‚ ظ„ظپطھط­ ط§ظ„ط±ط§ط¨ط·"); true
                } catch (e: Exception) {
                    toast("طھط¹ط°ظ‘ط± ظپطھط­ ط§ظ„ط±ط§ط¨ط·: ${e.message}"); true
                }
            }
        }

        wv.addJavascriptInterface(Bridge(), "Android")
    }

    inner class Bridge {

        @JavascriptInterface
        fun printCurrent(title: String?) {
            runOnUiThread {
                try {
                    val pm = getSystemService(PRINT_SERVICE) as PrintManager
                    val adapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter(title ?: "Ejarat")
                    val attrs = PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .build()
                    pm.print(title ?: "Ejarat", adapter, attrs)
                } catch (e: Exception) {
                    toast("طھط¹ط°ظ‘ط±طھ ط§ظ„ط·ط¨ط§ط¹ط©: ${e.message}")
                }
            }
        }

        @JavascriptInterface
        fun printHtml(html: String, title: String?) {
            runOnUiThread {
                val temp = WebView(this@MainActivity)
                configureWebView(temp)
                temp.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        try {
                            val pm = getSystemService(PRINT_SERVICE) as PrintManager
                            val adapter = temp.createPrintDocumentAdapter(title ?: "Ejarat")
                            val attrs = PrintAttributes.Builder()
                                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                                .build()
                            pm.print(title ?: "Ejarat", adapter, attrs)
                        } catch (e: Exception) {
                            toast("طھط¹ط°ظ‘ط±طھ ط§ظ„ط·ط¨ط§ط¹ط©: ${e.message}")
                        }
                    }
                }
                temp.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null)
            }
        }

        @JavascriptInterface
        fun exportJson(json: String, fileName: String) {
            pendingText = json
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, if (fileName.isBlank()) "backup.json" else fileName)
            }
            saveLauncher.launch(intent)
        }

        @JavascriptInterface
        fun importJson() {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
            }
            openLauncher.launch(intent)
        }

        @JavascriptInterface
        fun shareHtmlAsImage(html: String, fileName: String?, caption: String?) {
            runOnUiThread {
                val offscreen = WebView(this@MainActivity)
                configureWebView(offscreen)

                offscreen.measure(
                    WebView.MeasureSpec.makeMeasureSpec(1080, WebView.MeasureSpec.EXACTLY),
                    WebView.MeasureSpec.makeMeasureSpec(0, WebView.MeasureSpec.UNSPECIFIED)
                )
                offscreen.layout(0, 0, 1080, 1)

                offscreen.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        offscreen.postDelayed({
                            try {
                                val contentHeight = (offscreen.contentHeight * offscreen.scale).toInt().coerceAtLeast(1)
                                val bmp = Bitmap.createBitmap(1080, contentHeight, Bitmap.Config.ARGB_8888)
                                val canvas = Canvas(bmp)
                                offscreen.draw(canvas)

                                val dir = File(cacheDir, "share").apply { mkdirs() }
                                val f = File(dir, (fileName?.ifBlank { null } ?: "invoice.png"))
                                f.outputStream().use { out -> bmp.compress(Bitmap.CompressFormat.PNG, 100, out) }

                                val uri = FileProvider.getUriForFile(
                                    this@MainActivity,
                                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                                    f
                                )

                                val send = Intent(Intent.ACTION_SEND).apply {
                                    type = "image/png"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    putExtra(Intent.EXTRA_TEXT, caption ?: "")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    setPackage("com.whatsapp")
                                }

                                try {
                                    startActivity(send)
                                } catch (_: Exception) {
                                    send.`package` = null
                                    startActivity(Intent.createChooser(send, "ظ…ط´ط§ط±ظƒط© ط§ظ„ظپط§طھظˆط±ط©"))
                                }
                            } catch (e: Exception) {
                                toast("طھط¹ط°ظ‘ط±طھ ظ…ط´ط§ط±ظƒط© ط§ظ„طµظˆط±ط©: ${e.message}")
                            }
                        }, 120)
                    }
                }

                offscreen.loadDataWithBaseURL(
                    "file:///android_asset/",
                    html, "text/html", "UTF-8", null
                )
            }
        }

        @JavascriptInterface fun toast(msg: String) = this@MainActivity.toast(msg)
        @JavascriptInterface fun goBack() { runOnUiThread { if (webView.canGoBack()) webView.goBack() else finish() } }
        @JavascriptInterface fun openUrl(url: String) {
            runOnUiThread {
                try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply { addCategory(Intent.CATEGORY_BROWSABLE) }) }
                catch (e: Exception) { toast("طھط¹ط°ظ‘ط± ظپطھط­ ط§ظ„ط±ط§ط¨ط·: ${e.message}") }
            }
        }
    }

    override fun onBackPressed() {
        if (this::webView.isInitialized && webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    private fun toast(msg: String) =
        runOnUiThread { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }
}

private fun String.js(): String = "\"" + replace("\\", "\\\\")
    .replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\""
