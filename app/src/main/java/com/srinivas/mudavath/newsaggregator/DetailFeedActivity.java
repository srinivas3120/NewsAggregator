package com.srinivas.mudavath.newsaggregator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import com.srinivas.mudavath.network.network.HinduEditorials;
import com.srinivas.mudavath.network.network.MyWebViewClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Mudavath Srinivas on 09-02-2016.
 */
public class DetailFeedActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private ActionBar ab;
  private ProgressBar progressBar;
  private ImageView popupMenu;

  WebView wv_detail;
  private String mailUrl;
  private String title = "";

  @SuppressLint("JavascriptInterface") @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    if (!getIntent().hasExtra("channel")) {
      Util.showBottomToast(this, "Sorry! News is not available...");
      super.onBackPressed();
    }
    mailUrl = getIntent().getStringExtra("channel");
    title = getIntent().getStringExtra("title");

    setUpActionBar();
    wv_detail = (WebView) findViewById(R.id.wv_detail);
    wv_detail.canGoBack();
    wv_detail.canGoForward();
    wv_detail.goBack();
    wv_detail.goForward();

    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    popupMenu = (ImageView) findViewById(R.id.popupMenu);
    popupMenu.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        showPopupMenu(view);
      }
    });

    WebSettings webSettings = wv_detail.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setAllowContentAccess(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setAppCacheEnabled(true);
    wv_detail.addJavascriptInterface(new LoadListener() {
      @JavascriptInterface public void processHTML(String html) {
        Document doc = Jsoup.parse(html);
        HinduEditorials hinduEditorials = new HinduEditorials();
        if (doc != null) {
          Elements link = doc.select("meta[rel=link]");
          if (link != null && link.size() > 0) {
            if (!link.get(0).attr("id").contains("http://www.thehindu.com/opinion/editorial")) {
              return;
            }
            hinduEditorials.setUrl(link.get(0).attr("id"));
          }
        }

        Elements title = doc.select("title");
        if (title != null && title.size() > 0) {
          hinduEditorials.setTitle(title.get(0).text());
        }

        Elements contentBody = doc.select("div[id^=content-body-]");
        if (contentBody != null && contentBody.size() > 0) {
          hinduEditorials.setContent(contentBody.outerHtml());
        }

        Elements publishedTime = doc.select("meta[property=article:published_time]");
        if (publishedTime != null && publishedTime.size() > 0) {
          hinduEditorials.setPublishedTime(publishedTime.get(0).attr("content"));
        }

        Elements modifiedTime = doc.select("meta[property=article:modified_time]");
        if (modifiedTime != null && modifiedTime.size() > 0) {
          hinduEditorials.setModifiedTime(modifiedTime.get(0).attr("content"));
        }

        Elements prevStory = doc.select("a[href]:contains(Previous Story)");
        if (prevStory != null && prevStory.size() > 0) {
          hinduEditorials.setPrevStoryUrl(prevStory.get(0).attr("href"));
        }
        Elements nextStory = doc.select("a[href]:contains(Next Story)");
        if (nextStory != null && nextStory.size() > 0) {
          hinduEditorials.setNextStoryUrl(nextStory.get(0).attr("href"));
        }
        Log.e("HTMLOUT", hinduEditorials.toString());
      }
    }, "HTMLOUT");
    wv_detail.setWebChromeClient(new WebChromeClient() {
      public void onProgressChanged(WebView view, int progress) {
        progressBar.setProgress(progress);
        if (progress == 100) {
          progressBar.setVisibility(View.GONE);
        } else {
          progressBar.setVisibility(View.VISIBLE);
        }
      }
    });

    wv_detail.loadUrl(mailUrl);
    wv_detail.setWebViewClient(new MyWebViewClient(this));
  }

  private void setUpActionBar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ab = getSupportActionBar();
    ab.setTitle(title);
    ab.setDisplayHomeAsUpEnabled(true);
  }

  @Override public boolean onKeyDown(final int keyCode, final KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_detail.canGoBack()) {
      wv_detail.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override public void onBackPressed() {
    wv_detail.destroy();
    super.onBackPressed();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void showPopupMenu(View view) {
    LayoutInflater layoutInflater =
        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View popupView = layoutInflater.inflate(R.layout.layout_back_forward_menu, null);

    final ImageView back = (ImageView) popupView.findViewById(R.id.iv_back);
    final ImageView forward = (ImageView) popupView.findViewById(R.id.iv_forward);

    updateMenu(back, forward);
    back.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (wv_detail.canGoBack()) {
          wv_detail.goBack();
        }
        updateMenu(back, forward);
      }
    });
    forward.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (wv_detail.canGoForward()) {
          wv_detail.goForward();
        }
        updateMenu(back, forward);
      }
    });
    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);

    popupWindow.setOutsideTouchable(true);
    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
      @Override public void onDismiss() {
        //TODO do sth here on dismiss
      }
    });
    popupWindow.showAsDropDown(view);
  }

  private void updateMenu(ImageView goBack, ImageView goForward) {
    if (wv_detail.canGoBack()) {
      goBack.setImageResource(R.drawable.back_active);
    } else {
      goBack.setImageResource(R.drawable.back_inactive);
    }

    if (wv_detail.canGoForward()) {
      goForward.setImageResource(R.drawable.forward_active);
    } else {
      goForward.setImageResource(R.drawable.forward_inactive);
    }
  }

  class LoadListener {
  }
}
