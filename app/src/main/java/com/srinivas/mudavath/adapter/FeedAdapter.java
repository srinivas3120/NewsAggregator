package com.srinivas.mudavath.adapter;

import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.srinivas.mudavath.network.network.MyApplication;
import com.srinivas.mudavath.network.network.VolleySingleton;
import com.srinivas.mudavath.newsaggregator.R;
import com.srinivas.mudavath.newsaggregator.Util;
import com.srinivas.mudavath.pojo.FeedElement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mudavath Srinivas on 09-02-2016.
 */
public class FeedAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private View.OnClickListener clickListener;
    private ArrayList<FeedElement> feeds=new ArrayList<FeedElement>();
    VolleySingleton volleySingleton=null;
    ImageLoader imageLoader=null;


    int defaultImage;
    private String searchString=null;

    public FeedAdapter(ArrayList<FeedElement> feeds, View.OnClickListener clickListener) {
        this.feeds = feeds;
        this.clickListener = clickListener;

        volleySingleton=VolleySingleton.getInstance();
        imageLoader=volleySingleton.getImageLoader();

        defaultImage= R.drawable.profile_pic_default_profile_pic;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_element,parent, false);
        return new ViewHolderForFeedElement(itemView);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        FeedElement feedElement = feeds.get(position);
        final ViewHolderForFeedElement viewHolderForFeedElement = (ViewHolderForFeedElement) holder;
        Resources res = MyApplication.getAppContext().getResources();

        String feed_by = String.format(res.getString(R.string.feed_by), feedElement.getAuthor());

        if(!TextUtils.isEmpty(feedElement.getPubDate())){
            DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss");
            Date startDate=new Date(System.currentTimeMillis());
            try {
                startDate = df.parse(feedElement.getPubDate().substring(5,25));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String feed_pubDate = String.format(res.getString(R.string.feed_pubDate), Util.getFeedDuration(startDate.getTime()));
            viewHolderForFeedElement.tv_feed_pubDate.setVisibility(View.VISIBLE);
            viewHolderForFeedElement.tv_feed_pubDate.setText(feed_pubDate);
        }else {
            viewHolderForFeedElement.tv_feed_pubDate.setVisibility(View.GONE);
        }

        if(feedElement.getAuthor()!=null && !TextUtils.isEmpty(feedElement.getAuthor().trim())){
            viewHolderForFeedElement.tv_feed_by.setVisibility(View.VISIBLE);
            viewHolderForFeedElement.tv_feed_by.setText(feed_by);
        }else {
            viewHolderForFeedElement.tv_feed_by.setVisibility(View.GONE);
        }
        viewHolderForFeedElement.tv_feed_title.setText(feedElement.getTitle().trim());
        viewHolderForFeedElement.tv_feed_desc.setText(feedElement.getDescription().trim());

        if(!TextUtils.isEmpty(feedElement.getThumbnail())){
            viewHolderForFeedElement.iv_feed_thumbnail.setVisibility(View.VISIBLE);
            imageLoader.get(feedElement.getThumbnail(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        viewHolderForFeedElement.iv_feed_thumbnail.setVisibility(View.VISIBLE);
                        viewHolderForFeedElement.iv_feed_thumbnail.setImageBitmap(response.getBitmap());
                    } else {
                        viewHolderForFeedElement.iv_feed_thumbnail.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (defaultImage != 0) {
                        viewHolderForFeedElement.iv_feed_thumbnail.setVisibility(View.VISIBLE);
                        viewHolderForFeedElement.iv_feed_thumbnail.setImageResource(R.drawable.profile_pic_default_profile_pic);
                    }
                }
            });
        }else {
            viewHolderForFeedElement.iv_feed_thumbnail.setVisibility(View.GONE);
        }
        viewHolderForFeedElement.cv_feed_element.setTag(position);
        viewHolderForFeedElement.iv_feed_thumbnail.setTag(position);
        viewHolderForFeedElement.tv_feed_desc.setTag(position);
        viewHolderForFeedElement.tv_feed_title.setTag(position);
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }


    public class ViewHolderForFeedElement extends RecyclerView.ViewHolder {

        CardView cv_feed_element;
        ImageView iv_feed_thumbnail;
        TextView tv_feed_title;
        TextView tv_feed_desc;
        TextView tv_feed_by;
        TextView tv_feed_pubDate;


        public ViewHolderForFeedElement(View itemView) {
            super(itemView);
            cv_feed_element= (CardView) itemView.findViewById(R.id.cv_feed_element);
            iv_feed_thumbnail= (ImageView) itemView.findViewById(R.id.iv_feed_thumbnail);
            tv_feed_title= (TextView) itemView.findViewById(R.id.tv_feed_title);
            tv_feed_desc= (TextView) itemView.findViewById(R.id.tv_feed_desc);
            tv_feed_by = (TextView) itemView.findViewById(R.id.tv_feed_by);
            tv_feed_pubDate= (TextView) itemView.findViewById(R.id.tv_feed_pubDate);

            iv_feed_thumbnail.setVisibility(View.GONE);
            tv_feed_by.setVisibility(View.GONE);
            tv_feed_pubDate.setVisibility(View.GONE);
            cv_feed_element.setOnClickListener(clickListener);
            iv_feed_thumbnail.setOnClickListener(clickListener);
            tv_feed_title.setOnClickListener(clickListener);
            tv_feed_desc.setOnClickListener(clickListener);
        }
    }


}
