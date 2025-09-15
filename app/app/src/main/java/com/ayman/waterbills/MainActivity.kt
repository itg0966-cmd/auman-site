package com.ayman.waterbills

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
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

    // ✅ أضفت هذا: رابط موقعك على GitHub Pages
    private val HOME_URL = "https://itg0966-cmd.github.io/auman-site/"

    private lateinit var webView: WebView
    private var pendingText: String? = null

    // دعم <input type="file"> (لو احتجته من الصفحة)
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
                    // استدعِ callback من الجافاسكربت لإسقاط البيانات داخل localStorage
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

        // ✅ بدّلت التحميل ليكون من GitHub Pages
        webView.loadUrl(HOME_URL)
        // كان سابقًا: webView.loadUrl("file:///android_asset/index.html")
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

        // === دعم alert / confirm / prompt + اختيار ملف ===
        wv.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?, url: String?, message: String?, result: JsResult?
            ): Boolean {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("تنبيه")
                    .setMessage(message)
                    .setPositiveButton("موافق") { _, _ -> result?.confirm() }
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }

            override fun onJsConfirm(
                view: WebView?, url: String?, message: String?, result: JsResult?
            ): Boolean {
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

            // دعم اختيار الملفات إن استخدمت input[type=file]
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                fileChooserCallback?.onReceiveValue(null) // الغِ أي طلب سابق
                fileChooserCallback = filePathCallback
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                fileChooserLauncher.launch(intent)
                return true
            }
        }

        // التعامل مع روابط واتساب/خارجية
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
                    } else {
                        false
                    }
                } catch (e: ActivityNotFoundException) {
                    toast("لا يوجد تطبيق لفتح الرابط"); true
                } catch (e: Exception) {
                    toast("تعذّر فتح الرابط: ${e.message}"); true
                }
            }
        }

        wv.addJavascriptInterface(Bridge(), "Android")
    }

    inner class Bridge {
        // طباعة الصفحة الحالية كما هي
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
                    toast("تعذّرت الطباعة: ${e.message}")
                }
            }
        }

        // طباعة HTML مُولّد (نستخدمه لفاتورة/سنة الشقة مع نطاق التاريخ)
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

        // تصدير JSON عبر واجهة SAF (أفضل من التنزيل بالويب في WebView)
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

        // استيراد JSON عبر SAF
        @JavascriptInterface
        fun importJson() {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
            }
            openLauncher.launch(intent)
        }

        @JavascriptInterface
        fun toast(msg: String) = this@MainActivity.toast(msg)

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
        if (this::webView.isInitialized && webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    private fun toast(msg: String) =
        runOnUiThread { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }
}

private fun String.js(): String = "\"" + replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("\n", "\\n")
    .replace("\r", "\\r") + "\""
