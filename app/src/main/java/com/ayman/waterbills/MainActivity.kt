package com.ayman.waterbills

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        // رابط GitHub Pages الخاص بك
        private const val HOME_URL = "https://itg0966-cmd.github.io/auman-site/"

        private fun bust(url: String) = url +
                (if (url.contains("?")) "&" else "?") + "v=" + System.currentTimeMillis()
    }

    private lateinit var webView: WebView
    private var pendingText: String? = null

    // دعم <input type="file">
    private var fileChooserCallback: ValueCallback<Array<Uri>>? = null
    private val fileChooserLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            val resultUris = if (res.resultCode == Activity.RESULT_OK) {
                val uri = res.data?.data
                if (uri != null) arrayOf(uri) else emptyArray()
            } else emptyArray()
            fileChooserCallback?.onReceiveValue(resultUris)
            fileChooserCallback = null
        }

    // حفظ JSON عبر SAF
    private val saveLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res: ActivityResult ->
            if (res.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = res.data?.data
                try {
                    contentResolver.openOutputStream(uri!!)?.use { os ->
                        os.write((pendingText ?: "").toByteArray())
                    }
                    toast("تم الحفظ ✅")
                } catch (e: Exception) {
                    toast("تعذّر الحفظ: ${e.message}")
                }
            }
            pendingText = null
        }

    // فتح JSON عبر SAF
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
                    toast("تم الاستيراد ✅")
                } catch (e: Exception) {
                    toast("تعذّر الاستيراد: ${e.message}")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        configureWebView(webView)

        // تنظيف كاش/كوكيز ليظهر آخر نسخة من الصفحة
        webView.clearCache(true)
        webView.clearHistory()
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()

        // جرّب الموقع أونلاين، ولو فشل/مافي نت -> افتح النسخة المحلية
        if (isOnline()) {
            webView.loadUrl(bust(HOME_URL))
        } else {
            webView.loadUrl("file:///android_asset/index.html")
        }
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
            cacheMode = WebSettings.LOAD_NO_CACHE // لا تعتمد على الكاش
        }

        // alert / confirm / prompt + اختيار ملف
        wv.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("تنبيه")
                    .setMessage(message)
                    .setPositiveButton("موافق") { _, _ -> result?.confirm() }
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }

            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("تأكيد")
                    .setMessage(message)
                    .setPositiveButton("نعم") { _, _ -> result?.confirm() }
                    .setNegativeButton("لا") { _, _ -> result?.cancel() }
                    .create()
                    .show()
                return true
            }

            override fun onJsPrompt(
                view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?
            ): Boolean {
                val input = EditText(this@MainActivity)
                input.setText(defaultValue ?: "")
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(message)
                    .setView(input)
                    .setPositiveButton("موافق") { _, _ -> result?.confirm(input.text.toString()) }
                    .setNegativeButton("إلغاء") { _, _ -> result?.cancel() }
                    .create()
                    .show()
                return true
            }

            override fun onShowFileChooser(
                webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?
            ): Boolean {
                fileChooserCallback?.onReceiveValue(null)
                fileChooserCallback = filePathCallback
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                fileChooserLauncher.launch(intent)
                return true
            }
        }

        wv.webViewClient = object : WebViewClient() {

            // لو حصل خطأ تحميل (انقطاع/404…) نرجع فورًا للنسخة المحلية
            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError?
            ) {
                if (request?.isForMainFrame == true) {
                    view?.loadUrl("file:///android_asset/index.html")
                }
            }

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
                    toast("لا يوجد تطبيق لفتح الرابط"); true
                } catch (e: Exception) {
                    toast("تعذّر فتح الرابط: ${e.message}"); true
                }
            }
        }

        wv.addJavascriptInterface(Bridge(), "Android")
    }

    // فحص الاتصال
    private fun isOnline(): Boolean {
        val cm = getSystemService(ConnectivityManager::class.java)
        val net = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(net) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    inner class Bridge {
        @JavascriptInterface
        fun printCurrent(title: String?) {
            runOnUiThread {
                try {
                    val pm = getSystemService(PRINT_SERVICE) as PrintManager
                    val adapter: PrintDocumentAdapter =
                        webView.createPrintDocumentAdapter(title ?: "Ejarat")
                    val attrs = PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .build()
                    pm.print(title ?: "Ejarat", adapter, attrs)
                } catch (e: Exception) {
                    toast("تعذّرت الطباعة: ${e.message}")
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
                            toast("تعذّرت الطباعة: ${e.message}")
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

        @JavascriptInterface fun toast(msg: String) = this@MainActivity.toast(msg)

        @JavascriptInterface
        fun goBack() {
            runOnUiThread { if (webView.canGoBack()) webView.goBack() else finish() }
        }

        @JavascriptInterface
        fun openUrl(url: String) {
            runOnUiThread {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        addCategory(Intent.CATEGORY_BROWSABLE)
                    })
                } catch (e: Exception) {
                    toast("تعذّر فتح الرابط: ${e.message}")
                }
            }
        }
    }

    override fun onBackPressed() {
        if (this::webView.isInitialized && webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    private fun toast(msg: String) =
        runOnUiThread { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }
}

private fun String.js(): String = "\"" + replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("\n", "\\n")
    .replace("\r", "\\r") + "\""
