# === إعداد المسارات ===
$proj = Get-Location
$app  = Join-Path $proj "app"
$res  = Join-Path $app "src\main\res"
$xml  = Join-Path $res "xml"
$layout = Join-Path $res "layout"
$assets = Join-Path $app "src\main\assets"
$javaPkg = Join-Path $app "src\main\java\com\ayman\waterbills"

New-Item -ItemType Directory -Force -Path $xml,$layout,$assets,$javaPkg | Out-Null

# === 1) build.gradle.kts ===
@'
plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.ayman.waterbills"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ayman.waterbills"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    packaging {
        resources.excludes += setOf(
            "META-INF/DEPENDENCIES",
            "META-INF/NOTICE",
            "META-INF/LICENSE",
            "META-INF/LICENSE.txt",
            "META-INF/NOTICE.txt"
        )
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.webkit:webkit:1.10.0")
}
'@ | Set-Content -Encoding UTF8 (Join-Path $app "build.gradle.kts")

# === 2) AndroidManifest.xml ===
@'
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.Material3.DayNight.NoActionBar">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>

    </application>
</manifest>
'@ | Set-Content -Encoding UTF8 (Join-Path $app "src\main\AndroidManifest.xml")

# === 3) res/xml/file_paths.xml ===
@'
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <cache-path name="cache" path="share/" />
</paths>
'@ | Set-Content -Encoding UTF8 (Join-Path $xml "file_paths.xml")

# === 4) res/layout/activity_main.xml ===
@'
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <WebView
        android:id="@+id/webView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:overScrollMode="never" />
</FrameLayout>
'@ | Set-Content -Encoding UTF8 (Join-Path $layout "activity_main.xml")

# === 5) MainActivity.kt ===
@'
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
                    toast("تم الحفظ ✅")
                } catch (e: Exception) {
                    toast("تعذّر الحفظ: ${e.message}")
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
                    toast("لا يوجد تطبيق لفتح الرابط"); true
                } catch (e: Exception) {
                    toast("تعذّر فتح الرابط: ${e.message}"); true
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
                                    startActivity(Intent.createChooser(send, "مشاركة الفاتورة"))
                                }
                            } catch (e: Exception) {
                                toast("تعذّرت مشاركة الصورة: ${e.message}")
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
                catch (e: Exception) { toast("تعذّر فتح الرابط: ${e.message}") }
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
'@ | Set-Content -Encoding UTF8 (Join-Path $javaPkg "MainActivity.kt")

# === 6) assets/index.html ===
@'
<!doctype html>
<html lang="ar" dir="rtl">
<head>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1"/>
<title>إيجارات أيمن</title>
<style>
  :root{--bg:#0e2740;--card:#132f4d;--chip:#1e4f86;--chip-fg:#eaf3ff;--ok:#19c37d;--txt:#e8f0ff;--muted:#a9c0df;--border:#295986}
  *{box-sizing:border-box}
  html,body{height:100%;margin:0;background:var(--bg);color:var(--txt);font-family:system-ui,Arial}
  .page{max-width:940px;margin:14px auto;padding:12px}
  .toolbar{display:grid;grid-template-columns:repeat(5,minmax(0,1fr));gap:10px;margin-bottom:12px}
  .chip{display:flex;align-items:center;justify-content:center;background:var(--chip);color:var(--chip-fg);border:1px solid var(--border);border-radius:14px;min-height:44px;padding:10px 8px;font-weight:700;cursor:pointer}
  .chip.ok{background:#0d8f60}
  .msg{display:none;margin-bottom:10px;padding:10px 12px;border-radius:10px;font-weight:700}
  .msg.ok{background:#144d35}.msg.err{background:#5a1416}
  .stats{background:var(--card);border:1px solid var(--border);border-radius:18px;padding:10px;margin-bottom:12px}
  .stats-grid{display:grid;gap:10px;grid-template-columns:repeat(3,minmax(0,1fr))}
  .stat{background:#0f2a47;border:1px solid var(--border);border-radius:14px;padding:12px;text-align:center}
  .stat .label{color:var(--muted);font-size:12px;margin-bottom:6px}
  .stat .value{font-size:22px;font-weight:800}
  .filters{background:var(--card);border:1px solid var(--border);border-radius:18px;padding:12px;margin-bottom:12px;display:grid;gap:10px;grid-template-columns:repeat(4,minmax(0,1fr))}
  .select,.input{background:#0f2a47;color:var(--txt);border:1px solid var(--border);border-radius:12px;padding:10px;font-size:14px;width:100%}
  .hint{grid-column:1/-1;color:var(--muted);font-size:12px}
  .apt{background:var(--card);border:1px solid var(--border);border-radius:18px;padding:12px;margin-bottom:12px;transition:background-color .2s}
  .apt.st-full{background:#155c3a22}.apt.st-part{background:#6b4f0022}.apt.st-zero{background:#7a1d1d22}
  .apt-head{display:flex;gap:8px;flex-wrap:wrap;align-items:center;margin-bottom:10px}
  .pill{background:#0f2a47;border:1px solid var(--border);border-radius:999px;padding:8px 12px;font-weight:800}
  .pill input{background:transparent;border:none;color:var(--txt);outline:none;font:inherit;width:160px}
  .row{display:grid;gap:10px;grid-template-columns:repeat(2,minmax(0,1fr));margin-top:8px}
  .apt-grid{display:grid;gap:10px;grid-template-columns:repeat(3,minmax(0,1fr))}
  .box{background:#0f2a47;border:1px solid var(--border);border-radius:12px;padding:10px}
  .box .label{color:var(--muted);font-size:12px;margin-bottom:6px}
  .box .value{font-size:18px;font-weight:800}
  .btn-row{display:flex;gap:10px;margin-top:10px;flex-wrap:wrap}
  button.btn{background:#0f2a47;color:var(--txt);border:1px solid var(--border);border-radius:12px;padding:10px 14px;font-weight:800;cursor:pointer}
  .btn.ok{background:#0d8f60}.btn.warn{background:#a23b3b}
  @media print{.toolbar,.filters,.stats,.btn-row,.hint{display:none!important}.apt{break-inside:avoid}}
</style>
</head>
<body>
<div class="page">
  <div id="msg" class="msg"></div>

  <div class="toolbar">
    <div class="chip" id="btnPrintAll">طباعة الكل 🖨️</div>
    <div class="chip" id="btnPdfYear">PDF سنة كاملة 🗓️</div>
    <div class="chip" id="btnExport">تصدير ⤴️</div>
    <div class="chip" id="btnImport">استيراد ⤵️</div>
    <div class="chip ok" id="btnSaveTop">حفظ 💾</div>
  </div>

  <div class="stats">
    <div class="stats-grid">
      <div class="stat"><div class="label">إجمالي المستحق</div><div id="totDue" class="value">0</div></div>
      <div class="stat"><div class="label">إجمالي المدفوع</div><div id="totPaid" class="value">0</div></div>
      <div class="stat"><div class="label">إجمالي المتبقي</div><div id="totRest" class="value">0</div></div>
    </div>
  </div>

  <div class="filters">
    <select id="selBuilding" class="select"></select>
    <select id="selMonth" class="select"></select>
    <select id="selYear" class="select"></select>
    <input id="qName" class="input" placeholder="بحث بالاسم ..."/>
    <div class="hint">* الشهر المختار يظهر على كل بطاقة.</div>
  </div>

  <div id="list"></div>
</div>

<script>
const months=['','يناير','فبراير','مارس','أبريل','مايو','يونيو','يوليو','أغسطس','سبتمبر','أكتوبر','نوفمبر','ديسمبر'];
const $=s=>document.querySelector(s);
const fmt=n=>Number(n||0).toLocaleString('en-US');
const escapeHtml=s=>String(s||'').replace(/[&<>"']/g,m=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[m]));

const defaultDB={buildings:[
  {id:'airport',name:'عمارة المطار',apts:7,rents:[35000,70000,65000,70000,60000,45000,45000]},
  {id:'wadi',name:'عمارة وادي أحمد',apts:9,defaultRent:44000},
  {id:'zayed',name:'عمارة زايد',apts:12,defaultRent:50000},
  {id:'popular',name:'البيت الشعبي',apts:1,defaultRent:35000}
],tenants:{},history:{}};
let DB=null; try{DB=JSON.parse(localStorage.getItem('ejaratDB'))||null;}catch(_){}
if(!DB){DB=JSON.parse(JSON.stringify(defaultDB));localStorage.setItem('ejaratDB',JSON.stringify(DB));}
const state={building:(DB.buildings[0]||{}).id||'airport',month:(new Date().getMonth()+1),year:(new Date().getFullYear()),q:''};
const ym=(y,m)=>`${y}-${String(m).padStart(2,'0')}`;
const getBuilding=id=>DB.buildings.find(b=>b.id===(id||state.building))||{};
const ensureTenant=(bid,apt)=>(DB.tenants[bid]=DB.tenants[bid]||{},DB.tenants[bid][apt]=DB.tenants[bid][apt]||{name:`مستأجر ${apt}`,phone:''});
function ensureMonth(bid,apt,y,m){const k=ym(y,m);DB.history[bid]=DB.history[bid]||{};DB.history[bid][apt]=DB.history[bid][apt]||{};DB.history[bid][apt][k]=DB.history[bid][apt][k]||{};return DB.history[bid][apt][k];}
function readMonth(bid,apt,y,m){const b=getBuilding(bid);const H=(((DB.history[bid]||{})[apt]||{})[ym(y,m)])||{};const rent=Array.isArray(b.rents)?(H.rent??b.rents[apt-1]??b.defaultRent??0):(H.rent??b.defaultRent??0);const paid=Number(H.paid||0),rest=Math.max(rent-paid,0);const t=(DB.tenants[bid]||{})[apt]||{};return {rent:Number(rent),paid,rest,full:!!H.full,toDad:!!H.toDad,dadDate:H.dadDate||'',date:H.date||'',note:H.note||'',t};}

function setAptStatus(card,R){card.classList.remove('st-full','st-part','st-zero');if(R.full||R.rest===0)card.classList.add('st-full');else if(R.paid===0)card.classList.add('st-zero');else card.classList.add('st-part');}
function renderSelectors(){
  $('#selBuilding').innerHTML=DB.buildings.map(b=>`<option value="${b.id}" ${b.id===state.building?'selected':''}>${b.name}</option>`).join('');
  $('#selMonth').innerHTML=Array.from({length:12},(_,i)=>`<option value="${i+1}" ${i+1===state.month?'selected':''}>${months[i+1]}</option>`).join('');
  const Y=new Date().getFullYear();
  $('#selYear').innerHTML=Array.from({length:7},(_,i)=>Y-3+i).map(v=>`<option value="${v}" ${v===state.year?'selected':''}>${v}</option>`).join('');
}
function renderList(){
  const b=getBuilding();const list=$('#list');list.innerHTML='';let totD=0,totP=0,totR=0;
  for(let i=1;i<=b.apts;i++){
    const R=readMonth(b.id,i,state.year,state.month),T=ensureTenant(b.id,i);
    if(state.q && !(T.name||'').includes(state.q)) continue;
    totD+=R.rent;totP+=R.paid;totR+=R.rest;
    const card=document.createElement('div');card.className='apt';
    card.innerHTML=`
      <div class="apt-head">
        <span class="pill">شقة ${i}</span>
        <span class="pill">الاسم: <input class="name" data-apt="${i}" value="${T.name||''}"></span>
      </div>
      <div class="row">
        <div class="box"><div class="label">واتساب</div><input class="input phone" data-apt="${i}" value="${T.phone||''}" placeholder="7xxxxxxxx"/></div>
        <div class="box"><div class="label">الشهر</div><div class="value">${months[state.month]} — ${state.year}</div></div>
      </div>
      <div class="apt-grid" style="margin-top:8px">
        <div class="box"><div class="label">الإيجار</div><input class="input rent" data-apt="${i}" value="${fmt(R.rent)}" inputmode="numeric"/></div>
        <div class="box"><div class="label">المدفوع</div><input class="input paid" data-apt="${i}" value="${fmt(R.paid)}" inputmode="numeric"/></div>
        <div class="box"><div class="label">المتبقي</div><div class="value" id="rest-${i}">${fmt(R.rest)}</div></div>
      </div>
      <div class="row">
        <div class="box"><div class="label">ملاحظات</div><input class="input note" data-apt="${i}" value="${R.note||''}" placeholder="..."/></div>
        <div class="box"><div class="label">تاريخ السداد</div><input class="input date" type="date" data-apt="${i}" value="${R.date||''}"/></div>
      </div>
      <div class="row">
        <div class="box">
          <label><input type="checkbox" class="full" data-apt="${i}" ${R.full?'checked':''}
                 onchange="toggleFull('${b.id}', ${i}, this.checked)"/> إذا مدفوع كامل</label>
        </div>
        <div class="box">
          <label><input type="checkbox" class="todad" data-apt="${i}" ${R.toDad?'checked':''}/> حوّلت لأبي (اليوم)</label>
          <div class="label" style="margin-top:6px">تاريخ التحويل: <b id="dadDate-${i}">${R.dadDate||'—'}</b></div>
        </div>
      </div>
      <div class="btn-row">
        <button class="btn ok" onclick="saveApt('${b.id}',${i})">حفظ</button>
        <button class="btn" onclick="whatsappInvoice('${b.id}',${i})">واتساب (صورة فاتورة)</button>
        <button class="btn" onclick="printInvoice('${b.id}',${i})">طباعة فاتورة</button>
        <button class="btn warn" onclick="clearPaid('${b.id}',${i})">مسح المدفوع</button>
      </div>`;
    setAptStatus(card,R);list.appendChild(card);
  }
  $('#totDue').textContent=fmt(totD);$('#totPaid').textContent=fmt(totP);$('#totRest').textContent=fmt(totR);
  bindAptEvents();
}
function recalcRest(apt){
  const rent=Number(String(document.querySelector(`.rent[data-apt="${apt}"]`).value).replace(/[^\d.-]/g,''))||0;
  const paid=Number(String(document.querySelector(`.paid[data-apt="${apt}"]`).value).replace(/[^\d.-]/g,''))||0;
  const rest=Math.max(rent-paid,0);
  document.querySelector(`#rest-${apt}`).textContent=fmt(rest);
}
function formatOnBlur(el){el.value=fmt(Number(String(el.value).replace(/[^\d.-]/g,''))||0);}
function updateAptUI(bid,apt){
  const R=readMonth(bid,apt,state.year,state.month);
  const rentInp=document.querySelector(`.rent[data-apt="${apt}"]`);
  const paidInp=document.querySelector(`.paid[data-apt="${apt}"]`);
  if(rentInp)rentInp.value=fmt(R.rent);
  if(paidInp)paidInp.value=fmt(R.paid);
  const restBox=document.querySelector(`#rest-${apt}`);if(restBox)restBox.textContent=fmt(Math.max(R.rent-Number(R.paid||0),0));
  const dadDateEl=document.querySelector(`#dadDate-${apt}`);if(dadDateEl)dadDateEl.textContent=R.dadDate||'—';
  const card=document.querySelectorAll('.apt')[apt-1];setAptStatus(card,R);
  const b=getBuilding();let d=0,p=0;for(let i=1;i<=b.apts;i++){const x=readMonth(b.id,i,state.year,state.month);d+=x.rent;p+=x.paid;}
  $('#totDue').textContent=fmt(d);$('#totPaid').textContent=fmt(p);$('#totRest').textContent=fmt(Math.max(d-p,0));
}
function saveApt(bid,apt,show=true){
  const H=ensureMonth(bid,apt,state.year,state.month);
  H.rent=Number(String(document.querySelector(`.rent[data-apt="${apt}"]`).value).replace(/[^\d.-]/g,''))||0;
  H.paid=Number(String(document.querySelector(`.paid[data-apt="${apt}"]`).value).replace(/[^\d.-]/g,''))||0;
  H.note=document.querySelector(`.note[data-apt="${apt}"]`).value;
  H.date=document.querySelector(`.date[data-apt="${apt}"]`).value;
  const dadChk=document.querySelector(`.todad[data-apt="${apt}"]`);
  if(dadChk && dadChk.checked){H.toDad=true;if(!H.dadDate)H.dadDate=new Date().toISOString().slice(0,10);}else{H.toDad=false;H.dadDate='';}
  persist(show);updateAptUI(bid,apt);
}
function clearPaid(bid,apt){const H=ensureMonth(bid,apt,state.year,state.month);H.paid=0;H.full=false;persist(true);updateAptUI(bid,apt);}
function styledDoc(title,body){
  return `<!doctype html><html dir="rtl" lang="ar"><meta charset="utf-8">
  <style>
    :root{--c1:#0e2740;--c2:#1d3b63;--c3:#e8f0ff}
    body{font-family:Arial,system-ui;margin:24px;color:#122}
    .hdr{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px}
    .title{font-size:22px;font-weight:800}
    .tag{background:#e8f0ff;border:1px solid #b9c6e6;padding:6px 10px;border-radius:10px;font-weight:700}
    table{width:100%;border-collapse:collapse}
    th{background:#1d3b63;color:#fff}
    th,td{border:1px solid #9bb0d7;padding:8px;text-align:center}
    tbody tr:nth-child(even){background:#f4f7ff}
    tfoot td{font-weight:800;background:#eef3ff}
    @page{margin:18mm}
  </style>
  <body>
    <div class="hdr"><div class="title">${escapeHtml(title)}</div><div class="tag">${months[state.month]} ${state.year}</div></div>
    ${body}
  </body></html>`;
}
function printInvoice(bid,apt){
  const R=readMonth(bid,apt,state.year,state.month),T=ensureTenant(bid,apt);
  const body=`<div style="margin:6px 0 12px;">المستأجر: <b>${escapeHtml(T.name||'')}</b> — شقة ${apt}</div>
    <table><thead><tr><th>الإيجار</th><th>المدفوع</th><th>المتبقي</th><th>حوّلت لأبي</th><th>تاريخ التحويل</th></tr></thead>
      <tbody><tr><td>${fmt(R.rent)}</td><td>${fmt(R.paid)}</td><td>${fmt(Math.max(R.rent-R.paid,0))}</td><td>${R.toDad?'✔︎':'—'}</td><td>${R.dadDate||'—'}</td></tr></tbody></table>`;
  const html=styledDoc(`فاتورة إيجار`,body);
  try{Android.printHtml(html,`فاتورة شقة ${apt}`);}catch(_){window.print();}
}
function whatsappInvoice(bid,apt){
  const R=readMonth(bid,apt,state.year,state.month),T=ensureTenant(bid,apt);
  const body=`<div style="margin:6px 0 12px;">المستأجر: <b>${escapeHtml(T.name||'')}</b> — شقة ${apt}</div>
    <table><thead><tr><th>الإيجار</th><th>المدفوع</th><th>المتبقي</th><th>حوّلت لأبي</th><th>تاريخ التحويل</th></tr></thead>
      <tbody><tr><td>${fmt(R.rent)}</td><td>${fmt(R.paid)}</td><td>${fmt(Math.max(R.rent-R.paid,0))}</td><td>${R.toDad?'✔︎':'—'}</td><td>${R.dadDate||'—'}</td></tr></tbody></table>`;
  const html=styledDoc(`فاتورة إيجار — ${months[state.month]} ${state.year}`,body);
  const caption=`فاتورة الإيجار — ${months[state.month]} ${state.year}\n${T.name||''} (شقة ${apt})`;
  try{Android.shareHtmlAsImage(html,`invoice_${apt}.png`,caption);}catch(_){alert("مشاركة الصورة تحتاج التطبيق الأصلي.");}
}
function printAllMonth(){
  const b=getBuilding();let rows='',totD=0,totP=0,totR=0;
  for(let i=1;i<=b.apts;i++){const R=readMonth(b.id,i,state.year,state.month),T=ensureTenant(b.id,i);
    rows+=`<tr><td>${i}</td><td>${escapeHtml(T.name||'')}</td><td>${fmt(R.rent)}</td><td>${fmt(R.paid)}</td><td>${fmt(Math.max(R.rent-R.paid,0))}</td><td>${R.toDad?'✔︎':'—'}</td><td>${R.dadDate||'—'}</td></tr>`;
    totD+=R.rent;totP+=R.paid;totR+=Math.max(R.rent-R.paid,0);}
  const body=`<table><thead><tr><th>الوحدة</th><th>الاسم</th><th>الإيجار</th><th>المدفوع</th><th>المتبقي</th><th>حوّلت لأبي</th><th>تاريخ التحويل</th></tr></thead><tbody>${rows}</tbody><tfoot><tr><td colspan="2">الإجمالي</td><td>${fmt(totD)}</td><td>${fmt(totP)}</td><td>${fmt(totR)}</td><td colspan="2"></td></tr></tfoot></table>`;
  const html=styledDoc(`${b.name} — تقرير شهر`,body);
  try{Android.printHtml(html,`تقرير ${months[state.month]} ${state.year}`);}catch(_){window.print();}
}
function printYearPDF(){
  const b=getBuilding();let sections='';
  for(let m=1;m<=12;m++){
    let rows='',totD=0,totP=0,totR=0;
    for(let i=1;i<=b.apts;i++){const R=readMonth(b.id,i,state.year,m),T=ensureTenant(b.id,i);
      rows+=`<tr><td>${i}</td><td>${escapeHtml(T.name||'')}</td><td>${fmt(R.rent)}</td><td>${fmt(R.paid)}</td><td>${fmt(Math.max(R.rent-R.paid,0))}</td><td>${R.toDad?'✔︎':'—'}</td><td>${R.dadDate||'—'}</td></tr>`;
      totD+=R.rent;totP+=R.paid;totR+=Math.max(R.rent-R.paid,0);}
    sections+=`<div style="margin:18px 0 8px;font-weight:800">${state.year} ${months[m]}</div>
      <table><thead><tr><th>الوحدة</th><th>الاسم</th><th>الإيجار</th><th>المدفوع</th><th>المتبقي</th><th>حوّلت لأبي</th><th>تاريخ التحويل</th></tr></thead><tbody>${rows}</tbody><tfoot><tr><td colspan="2">الإجمالي</td><td>${fmt(totD)}</td><td>${fmt(totP)}</td><td>${fmt(totR)}</td><td colspan="2"></td></tr></tfoot></table>`;
  }
  const html=styledDoc(`تقرير سنة كاملة — ${b.name}`,sections);
  try{Android.printHtml(html,`تقرير سنة ${state.year}`);}catch(_){window.print();}
}
function persist(showToast=true){localStorage.setItem('ejaratDB',JSON.stringify(DB));if(showToast)toast('تم الحفظ ✅');}
function exportBackup(){const text=JSON.stringify(DB,null,2);try{Android.exportJson(text,'backup.json');}catch(_){const a=document.createElement('a');a.href=URL.createObjectURL(new Blob([text],{type:'application/json'}));a.download='backup.json';a.click();}}
function importBackup(){try{Android.importJson();}catch(_){const inp=document.createElement('input');inp.type='file';inp.accept='application/json';inp.onchange=()=>{const f=inp.files[0];if(!f)return;const r=new FileReader();r.onload=()=>{try{DB=JSON.parse(r.result);persist(false);toast('تم الاستيراد ✅');render();}catch(e){toast('ملف غير صالح',true);}};r.readAsText(f);};inp.click();}}
window.onBackupImported=function(text){try{DB=JSON.parse(text);persist(false);toast('تم الاستيراد ✅');render();}catch(e){toast('ملف غير صالح',true);}};
let toastTimer=null;function toast(t,isErr=false){const b=$('#msg');b.textContent=t;b.className='msg '+(isErr?'err':'ok');b.style.display='block';clearTimeout(toastTimer);toastTimer=setTimeout(()=>b.style.display='none',1600);}
function bindAptEvents(){document.querySelectorAll('.name').forEach(e=>{e.onchange=()=>{ensureTenant(state.building,+e.dataset.apt).name=e.value.trim();persist(false);};});document.querySelectorAll('.phone').forEach(e=>{e.onchange=()=>{ensureTenant(state.building,+e.dataset.apt).phone=e.value.trim();persist(false);};});document.querySelectorAll('.rent').forEach(e=>{e.oninput=()=>recalcRest(+e.dataset.apt);e.onblur=()=>formatOnBlur(e);e.onchange=()=>saveApt(state.building,+e.dataset.apt,false);});document.querySelectorAll('.paid').forEach(e=>{e.oninput=()=>recalcRest(+e.dataset.apt);e.onblur=()=>formatOnBlur(e);e.onchange=()=>saveApt(state.building,+e.dataset.apt,false);});['note','date'].forEach(cls=>{document.querySelectorAll('.'+cls).forEach(e=>{e.onchange=()=>saveApt(state.building,+e.dataset.apt,false);});});document.querySelectorAll('.todad').forEach(e=>{e.onchange=()=>saveApt(state.building,+e.dataset.apt,true);});}
$('#btnPrintAll').onclick=printAllMonth;$('#btnPdfYear').onclick=printYearPDF;$('#btnExport').onclick=exportBackup;$('#btnImport').onclick=importBackup;$('#btnSaveTop').onclick=()=>persist(true);
$('#selBuilding').onchange=e=>{state.building=e.target.value;render();};$('#selMonth').onchange=e=>{state.month=+e.target.value;render();};$('#selYear').onchange=e=>{state.year=+e.target.value;render();};$('#qName').oninput=e=>{state.q=e.target.value.trim();renderList();};
function render(){renderSelectors();renderList();}render();
</script>
</body>
</html>
'@ | Set-Content -Encoding UTF8 (Join-Path $assets "index.html")

# === 7) gradle.properties (اختياري لإخفاء تحذير compileSdk 35) ===
$gp = Join-Path $proj "gradle.properties"
if (Test-Path $gp) {
    if (-not (Select-String -Path $gp -Pattern "android.suppressUnsupportedCompileSdk=35" -Quiet)) {
        Add-Content -Path $gp -Value "android.suppressUnsupportedCompileSdk=35"
    }
}

Write-Host "`nتم تجهيز كل الملفات ✔️"
Write-Host "افتح Android Studio → Sync → Rebuild → Run"