package com.srinivas.mudavath.network.network;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mudavath Srinivas on 08/02/17.
 */

public class MyWebViewClient extends WebViewClient {
  private AlertDialog alertDialog;
  private Map<String, Boolean> loadedUrls = new HashMap<>();

  public MyWebViewClient(AppCompatActivity activity) {
    alertDialog = new AlertDialog.Builder(activity).create();
  }

  @Override public void onPageFinished(WebView view, String url) {
    /* This call inject JavaScript into the page which just finished loading. */
    view.loadUrl(
        "javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    super.onPageFinished(view, url);
  }

  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    view.loadUrl(url);
    return true;
  }

  @Override public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
    boolean ad;
    if (!loadedUrls.containsKey(url)) {
      ad = AdBlocker.isAd(url);
      loadedUrls.put(url, ad);
    } else {
      ad = loadedUrls.get(url);
    }
    return ad ? AdBlocker.createEmptyResource() : super.shouldInterceptRequest(view, url);
  }

  public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    alertDialog.setTitle("Error");
    alertDialog.setMessage(description);
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        return;
      }
    });
    alertDialog.show();
  }

}
