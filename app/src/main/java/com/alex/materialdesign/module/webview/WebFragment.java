package com.alex.materialdesign.module.webview;

import android.os.Bundle;

import com.alex.materialdesign.R;
import com.alex.materialdesign.library.MDFragment;

import org.alex.callback.SimpleWebViewClient;
import org.alex.helper.WebViewHelper;
import org.alex.view.NestedWebView;

/**
 * 作者：Alex
 * 时间：2017/2/26 07:48
 * 简述：
 */
public class WebFragment extends MDFragment {
    protected NestedWebView webView;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_web_view;
    }

    @Override
    public void onCreateData(Bundle bundle) {
        webView = (NestedWebView) rootView.findViewById(R.id.wv);
        WebViewHelper.Builder.getInstance().build().attachToWebView(webView);
        webView.loadUrl("http://www.jianshu.com/users/c3c4ea133871/latest_articles");
        webView.setWebViewClient(new SimpleWebViewClient());
    }
}
