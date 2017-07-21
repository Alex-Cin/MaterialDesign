package org.alex.callback;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 作者：Alex
 * 时间：2017/2/26 08:17
 * 简述：
 */
public class SimpleWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.toString());
        return true;
    }
}
