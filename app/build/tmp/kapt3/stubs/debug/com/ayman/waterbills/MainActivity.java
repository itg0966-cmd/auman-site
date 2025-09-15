package com.ayman.waterbills;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0019B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fH\u0002J\b\u0010\u0013\u001a\u00020\u0011H\u0016J\u0012\u0010\u0014\u001a\u00020\u00112\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J\u0010\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\fH\u0002R\u001c\u0010\u0003\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/ayman/waterbills/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "fileChooserCallback", "Landroid/webkit/ValueCallback;", "", "Landroid/net/Uri;", "fileChooserLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "openLauncher", "pendingText", "", "saveLauncher", "webView", "Landroid/webkit/WebView;", "configureWebView", "", "wv", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "toast", "msg", "Bridge", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private android.webkit.WebView webView;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String pendingText;
    @org.jetbrains.annotations.Nullable()
    private android.webkit.ValueCallback<android.net.Uri[]> fileChooserCallback;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> fileChooserLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> saveLauncher = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> openLauncher = null;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void configureWebView(android.webkit.WebView wv) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void toast(java.lang.String msg) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0007J\b\u0010\b\u001a\u00020\u0004H\u0007J\b\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0006H\u0007J\u0012\u0010\f\u001a\u00020\u00042\b\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0007J\u001a\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00062\b\u0010\r\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0006H\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/ayman/waterbills/MainActivity$Bridge;", "", "(Lcom/ayman/waterbills/MainActivity;)V", "exportJson", "", "json", "", "fileName", "goBack", "importJson", "openUrl", "url", "printCurrent", "title", "printHtml", "html", "toast", "msg", "app_debug"})
    public final class Bridge {
        
        public Bridge() {
            super();
        }
        
        @android.webkit.JavascriptInterface()
        public final void printCurrent(@org.jetbrains.annotations.Nullable()
        java.lang.String title) {
        }
        
        @android.webkit.JavascriptInterface()
        public final void printHtml(@org.jetbrains.annotations.NotNull()
        java.lang.String html, @org.jetbrains.annotations.Nullable()
        java.lang.String title) {
        }
        
        @android.webkit.JavascriptInterface()
        public final void exportJson(@org.jetbrains.annotations.NotNull()
        java.lang.String json, @org.jetbrains.annotations.NotNull()
        java.lang.String fileName) {
        }
        
        @android.webkit.JavascriptInterface()
        public final void importJson() {
        }
        
        @android.webkit.JavascriptInterface()
        public final void toast(@org.jetbrains.annotations.NotNull()
        java.lang.String msg) {
        }
        
        @android.webkit.JavascriptInterface()
        public final void goBack() {
        }
        
        @android.webkit.JavascriptInterface()
        public final void openUrl(@org.jetbrains.annotations.NotNull()
        java.lang.String url) {
        }
    }
}