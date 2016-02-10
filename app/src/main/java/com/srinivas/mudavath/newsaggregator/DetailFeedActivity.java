package com.srinivas.mudavath.newsaggregator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by Mudavath Srinivas on 09-02-2016.
 */
public class DetailFeedActivity extends AppCompatActivity {

    WebView wv_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(!getIntent().hasExtra("channel")){
            Util.showBottomToast(this,"Sorry! News is not available...");
            super.onBackPressed();
        }

        wv_detail = (WebView)findViewById(R.id.wv_detail);
        wv_detail.getSettings().setJavaScriptEnabled(true);
        wv_detail.loadUrl(getIntent().getStringExtra("channel"));
    }
}
