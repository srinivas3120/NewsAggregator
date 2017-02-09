package com.srinivas.mudavath.newsaggregator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.srinivas.mudavath.pojo.FeedElement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mudavath Srinivas on 08/02/17.
 */

public class IEDetailFeedActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private ActionBar ab;

  ImageView iv_feed_thumbnail;
  TextView tv_feed_title;
  TextView tv_feed_desc;
  TextView tv_feed_by;
  TextView tv_feed_pubDate;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ie_detail);
    if (!getIntent().hasExtra("feedElement")) {
      Util.showBottomToast(this, "Sorry! News is not available...");
      super.onBackPressed();
    }
    initializeViews();
    setUpActionBar();
    setData();
  }

  private void initializeViews() {
    setUpActionBar();
    iv_feed_thumbnail = (ImageView) findViewById(R.id.iv_feed_thumbnail);
    tv_feed_title = (TextView) findViewById(R.id.tv_feed_title);
    tv_feed_desc = (TextView) findViewById(R.id.tv_feed_desc);
    tv_feed_by = (TextView) findViewById(R.id.tv_feed_by);
    tv_feed_pubDate = (TextView) findViewById(R.id.tv_feed_pubDate);
    iv_feed_thumbnail.setVisibility(View.GONE);
    tv_feed_title.setTextSize(20);
  }

  private void setUpActionBar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ab = getSupportActionBar();
    ab.setTitle("Details");
    ab.setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void setData() {
    final FeedElement feedElement = (FeedElement) getIntent().getSerializableExtra("feedElement");

    tv_feed_title.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(IEDetailFeedActivity.this, DetailFeedActivity.class);
        intent.putExtra("channel", feedElement.getLink());
        intent.putExtra("title", feedElement.getTitle());
        startActivity(intent);
      }
    });

    String feed_by = String.format(getString(R.string.feed_by), feedElement.getAuthor());
    if (!TextUtils.isEmpty(feedElement.getPubDate())) {
      DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss");
      Date startDate = new Date(System.currentTimeMillis());
      try {
        startDate = df.parse(feedElement.getPubDate().substring(5, 25));
      } catch (ParseException e) {
        df = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm a z");
        try {
          startDate = df.parse(feedElement.getPubDate());
        } catch (Exception ex) {
        }
      }
      String feed_pubDate = String.format(getString(R.string.feed_pubDate),
          Util.getFeedDuration(startDate.getTime()));
      tv_feed_pubDate.setVisibility(View.VISIBLE);
      tv_feed_pubDate.setText(feed_pubDate);
    } else {
      tv_feed_pubDate.setVisibility(View.GONE);
    }

    if (feedElement.getAuthor() != null && !TextUtils.isEmpty(feedElement.getAuthor().trim())) {
      tv_feed_by.setVisibility(View.VISIBLE);
      tv_feed_by.setText(feed_by);
    } else {
      tv_feed_by.setVisibility(View.GONE);
    }
    tv_feed_title.setText(Html.fromHtml(feedElement.getTitle().trim()));
    tv_feed_desc.setText(Html.fromHtml(feedElement.getStory().trim()));
  }
}