package org.alex.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

import static android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK;
import static android.webkit.WebSettings.LOAD_DEFAULT;


/**
 * 作者：Alex
 * 时间：2016/12/8 17 44
 * 简述：
 */
@SuppressWarnings("all")
public class WebViewHelper {
    private Builder builder;
    private WebView webView;
    private WebSettings webSettings;
    private String webViewCacheDir;

    private WebViewHelper(Builder builder) {
        this.builder = builder;
    }

    public WebViewHelper attachToWebView(WebView view) {
        this.webView = view;
        this.webSettings = webView.getSettings();
        initDefaultWebViewSettings();
        scrollBarEnabled(builder.isScrollBarEnabled);
        setAppCachePath(builder.cachePath);
        setCacheMode(builder.mode);
        javaScriptEnabled(builder.isJavaScriptEnabled);
        setAppCachePath(builder.cachePath);
        return this;
    }

    private void initDefaultWebViewSettings() {
        /*提高渲染的优先级*/
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        /*设置自适应屏幕，两者合用*/
        /*1. 将图片调整到适合webview的大小*/
        webSettings.setUseWideViewPort(true);
        /*2. 缩放至屏幕的大小*/
        webSettings.setLoadWithOverviewMode(true);
        /*支持缩放，默认为true。是下面那个的前提。
        * 是false，则该WebView不可缩放，这个不管设置什么都不能缩放。*/
        webSettings.setSupportZoom(true);
        /*设置内置的缩放控件。*/
        webSettings.setBuiltInZoomControls(true);
        /*隐藏原生的缩放控件*/
        webSettings.setDisplayZoomControls(false);
        /*支持内容重新布局*/
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        /*多窗口*/
        webSettings.supportMultipleWindows();
        /*设置可以访问文件*/
        webSettings.setAllowFileAccess(true);
        /*当webview调用requestFocus时为webview设置节点*/
        webSettings.setNeedInitialFocus(true);
        /*支持自动加载图片*/
        webSettings.setLoadsImagesAutomatically(true);
        /*设置编码格式*/
        webSettings.setDefaultTextEncodingName("utf-8");
        /*开启 DOM storage API 功能*/
        webSettings.setDomStorageEnabled(true);
        /*开启 database storage API 功能*/
        webSettings.setDatabaseEnabled(true);
        /*开启 Application Caches 功能*/
        webSettings.setAppCacheEnabled(true);
        javaScriptEnabled(true);
    }

    /**
     * 支持 js；
     * 支持通过JS打开新窗口；
     *
     * @param enabled
     * @return
     */
    private WebViewHelper javaScriptEnabled(boolean enabled) {
        webSettings.setJavaScriptEnabled(enabled);
        /*支持通过JS打开新窗口*/
        webSettings.setJavaScriptCanOpenWindowsAutomatically(enabled);
        return this;
    }

    /**
     * 设置 显示 滚动条
     *
     * @param enabled
     * @return
     */
    private void scrollBarEnabled(boolean enabled) {
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        /*水平不显示*/
        webView.setHorizontalScrollBarEnabled(enabled);
        /*垂直不显示*/
        webView.setVerticalScrollBarEnabled(enabled);
    }

    /**
     * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
     * LOAD_DEFAULT: （默认） 加载一张网页会检查是否有cache，如果有并且没有过期则使用本地cache，否则   从网络上获取。
     * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
     * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使
     *
     * @return
     */
    private void setCacheMode(int mode) {
        /*关闭WebView中缓存*/
        webSettings.setCacheMode(mode);
    }

    /**
     * 如果联网，从 网络获取；
     * 如果没网，从 本地获取；
     *
     * @return
     */
    private WebViewHelper applyCacheModeByNetWork() {
        /*根据cache-control决定是否从网络上取数据。 */
        if (isNetworkConnected()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            /*没网，则从本地获取，即离线加载*/
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        return this;
    }
    public void clearCache() {
        if(webView!=null){
            clearCache(webView);
        }
    }
    public void clearCache(WebView webView) {
        Context context = webView.getContext();
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        context.deleteDatabase("webview.db");
        context.deleteDatabase("webviewCache.db");
        context.deleteDatabase("webviewCookiesChromium.db");
        context.deleteDatabase("webviewCookiesChromiumPrivate.db");
        /*WebView 缓存文件*/
        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath() + "/webviewCacheChromium");
        /*删除webview 缓存目录*/
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        /*删除webview 缓存目录*/
        File fileWebViewCacheDir = new File(webViewCacheDir);
        if (fileWebViewCacheDir.exists()) {
            deleteFile(fileWebViewCacheDir);
        }
    }

    /**
     * 不需要 包含 sd卡 根目录
     *
     * @param cacheDir
     * @return
     */
    private void setAppCachePath(String cacheDir) {
        webViewCacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + (TextUtils.isEmpty(cacheDir) ? "android" : cacheDir);
        createSDCardDir(webViewCacheDir);
        /*设置  Application Caches 缓存目录*/
        webSettings.setAppCachePath(webViewCacheDir);
    }

    private boolean isNetworkConnected() {
        Context context = webView.getContext();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private boolean createSDCardDir(String newPath) {
        if (Environment.getExternalStorageDirectory().getAbsolutePath() == null) {
            return false;
        } else {
            File path1 = new File(newPath);
            if (!path1.exists()) {
                boolean mkdirs = path1.mkdirs();
                return mkdirs;
            } else {
                return true;
            }
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    private void deleteFile(File file) {
        if (file == null) {
            return;
        }
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {

        }
    }

    public static class Builder {
        private boolean isScrollBarEnabled = false;
        private boolean isJavaScriptEnabled = true;
        private String cachePath;
        private int mode = LOAD_DEFAULT;

        private Builder() {
        }

        public static Builder getInstance() {
            return new Builder();
        }

        public WebViewHelper build() {
            return new WebViewHelper(this);
        }

        public Builder scrollBarEnabled(boolean enabled) {
            isScrollBarEnabled = enabled;
            return this;
        }

        public Builder javaScriptEnabled(boolean enabled) {
            isJavaScriptEnabled = enabled;
            return this;
        }

        /**
         * 不需要携带 SD卡 根目录
         *
         * @param path
         * @return
         */
        public Builder appCachePath(String path) {
            cachePath = path;
            return this;
        }

        public Builder cacheMode(int mode) {
            this.mode = mode;
            return this;
        }
    }
}
