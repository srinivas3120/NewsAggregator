package com.srinivas.mudavath.newsaggregator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.srinivas.mudavath.adapter.FeedAdapter;
import com.srinivas.mudavath.network.network.CustomJsonObjectRequest;
import com.srinivas.mudavath.network.network.VolleySingleton;
import com.srinivas.mudavath.pojo.FeedElement;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mudavath Srinivas on 08-02-2016.
 */
public class FeedsFragment extends Fragment implements View.OnClickListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv_feeds;

    View.OnClickListener viewClickListener;
    Context mContext;

    LinearLayout ll_progress_status;
    TextView tv_error_status;
    ProgressBar progressBar;

    LinearLayout ll_search_container;
    EditText et_search_view;
    ImageView iv_clear_search;


    CustomJsonObjectRequest customJsonObjectRequest=null;
    VolleySingleton volleySingleton;

    ArrayList<FeedElement> feedsData =new ArrayList<FeedElement>();
    ArrayList<FeedElement> unFilteredFeedsData =new ArrayList<FeedElement>();
    FeedAdapter feedAdapter;
    private String url;
    private TextWatcher onChangeWatcher;
    private String searchString;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        View v=inflater.inflate(R.layout.fragment_feeds, container, false);

        url = getArguments().getString("url");
        if(TextUtils.isEmpty(url)){
            getActivity().getFragmentManager().popBackStack();
        }

        rv_feeds= (RecyclerView) v.findViewById(R.id.rv_feeds);
        ll_progress_status= (LinearLayout) v.findViewById(R.id.ll_progress_status);
        tv_error_status= (TextView) v.findViewById(R.id.tv_error_status);
        progressBar= (ProgressBar) v.findViewById(R.id.progressBar);

        ll_search_container= (LinearLayout) v.findViewById(R.id.ll_search_container);
        et_search_view= (EditText) v.findViewById(R.id.et_search_view);
        iv_clear_search= (ImageView) v.findViewById(R.id.iv_clear_search);
        iv_clear_search.setOnClickListener(this);
        onTextChangeListener();

        mSwipeRefreshLayout= (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                fetchFeeds();
            }
        });

        linearLayoutManager=new LinearLayoutManager(mContext);
        rv_feeds.setLayoutManager(linearLayoutManager);
        mClicklistner();
        volleySingleton= VolleySingleton.getInstance();
        feedAdapter=new FeedAdapter(feedsData,viewClickListener);
        rv_feeds.setAdapter(feedAdapter);
        fetchFeeds();
        return v;
    }

    private void onTextChangeListener() {
        if (onChangeWatcher==null)
            onChangeWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i,
                                              int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i,
                                          int i1, int i2) {

                    searchString=et_search_view.getText().toString().trim();
                    filterDataSet();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            };
        if (et_search_view != null) {
            et_search_view.addTextChangedListener(onChangeWatcher);
        }
    }

    private void filterDataSet() {

        feedsData.clear();
        for(FeedElement feedElement:unFilteredFeedsData){
            if(feedElement.getTitle().toLowerCase().contains(searchString.toLowerCase())){
                feedsData.add(feedElement);
            }
        }
        feedAdapter.notifyDataSetChanged();
    }


    private void mClicklistner() {

        viewClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position= (Integer) v.getTag();

                switch (v.getId()) {
                    case R.id.tv_feed_title:
                    case R.id.tv_feed_desc:
                    case R.id.iv_feed_thumbnail:
                    case R.id.cv_feed_element:
                        startDetailedActivity(feedsData.get(position).getLink());
                        break;
                    default:
                        break;

                }
            }
        };
    }

    private void startDetailedActivity(String link) {
        Intent intent = new Intent(getActivity(), DetailFeedActivity.class);
        intent.putExtra("channel", link);
        getActivity().startActivity(intent);
    }

    private void fetchFeeds() {
        if(Util.isNetworkAvailable(mContext)){
            new LoadFeed().execute(url);
        }else{
            ll_progress_status.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tv_error_status.setText("Check your internet connection...");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_clear_search:
                et_search_view.setText("");
                break;
            default:
                break;
        }
    }

    private class LoadFeed extends AsyncTask<String, Void, ArrayList<FeedElement>> {

        public LoadFeed() {
            if (feedsData.size() > 0) {
                ll_search_container.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(true);
            } else {
                ll_search_container.setVisibility(View.GONE);
                ll_progress_status.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                tv_error_status.setText("Loading...");
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<FeedElement> doInBackground(String... params) {
            ArrayList<FeedElement> feeds = new ArrayList<FeedElement>();
            XmlPullParserFactory factory = null;
            XmlPullParser parser = null;
            try {

                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                System.out.println("Response Code: " + conn.getResponseCode());
                InputStream is = new BufferedInputStream(conn.getInputStream());

                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is));

                int eventType = parser.getEventType();
                FeedElement feed = new FeedElement();
                String text = "";
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagname = parser.getName();
                    //Log.d(tagname, "tagname");
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (tagname.equalsIgnoreCase("item")) {
                                feed = new FeedElement();
                            }
                            break;
                        case XmlPullParser.TEXT:
                            text = parser.getText();
                            break;
                        case XmlPullParser.END_TAG:
                            if (tagname.equalsIgnoreCase("item")) {
                                feeds.add(feed);
                            } else if (tagname.equalsIgnoreCase("title")) {
                                feed.setTitle(text);
                            } else if (tagname.equalsIgnoreCase("description")) {
                                feed.setDescription(text.replaceAll("\\<[^>]*>",""));
                            } else if (tagname.equalsIgnoreCase("link")) {
                                feed.setLink(text);
                            }else if (tagname.equalsIgnoreCase("category")) {
                                feed.setCategory(text);
                            } else if (tagname.equalsIgnoreCase("author") || tagname.equalsIgnoreCase("creator")) {
                                feed.setAuthor(text);
                            } else if(tagname.equalsIgnoreCase("thumbnail")){
                                feed.setThumbnail(parser.getAttributeValue(null, "url"));
                            } else if (tagname.equalsIgnoreCase("pubDate")) {
                                feed.setPubDate(text);
                            }
                            break;
                        default:
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return feeds;
        }

        @Override
        protected void onPostExecute(ArrayList<FeedElement> feeds) {
            if(feeds.size()>0){
                feedsData.clear();
                feedsData.addAll(feeds);
                unFilteredFeedsData.clear();
                unFilteredFeedsData.addAll(feedsData);
                ll_search_container.setVisibility(View.VISIBLE);
                feedAdapter.notifyDataSetChanged();
            }
            feedAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
            ll_progress_status.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            //set data to rv
        }
    }

}
