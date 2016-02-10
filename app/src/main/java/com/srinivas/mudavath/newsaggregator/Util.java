package com.srinivas.mudavath.newsaggregator;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mudavath Srinivas on 08-02-2016.
 */

public class Util {
    private static ProgressDialog progressDialog = null;
    private static Toast toast=null;
    public static String HINDU_FEED_URL ="http://www.thehindu.com/?service=rss";
    public static String HINDU_INTERNATIONAL_URL ="http://www.thehindu.com/news/international/?service=rss";
    public static String HINDU_NATIONAL_URL ="http://www.thehindu.com/news/national/?service=rss";
    public static String HINDU_SPORT_URL ="http://www.thehindu.com/sport/?service=rss";
    public static String TOI_FEED_URL ="http://timesofindia.feedsportal.com/c/33039/f/533917/index.rss";
    public static String TOI_INDIA_URL ="http://timesofindia.feedsportal.com/c/33039/f/533917/index.rss";
    public static String TOI_SPORT_URL ="http://timesofindia.indiatimes.com/rssfeeds/671208.cms";
    public static String TOI_INTERNATIONAL_URL ="http://timesofindia.feedsportal.com/c/33039/f/533991/index.rss";
    //"http://timesofindia.feedsportal.com/c/33039/f/533917/index.rss"
    //public static String CNN_FEED_URL ="http://rss.cnn.com/rss/edition.rss";
    public static String INDIAN_EXPRESS_FEED_URL ="http://syndication.indianexpress.com/rss/latest-news.xml";
    public static String INDIAN_EXPRESS_WORLD_URL ="http://indianexpress.com/section/world/feed/";
    public static String INDIAN_EXPRESS_INDIA_URL ="http://indianexpress.com/section/india/feed/";
    public static String INDIAN_EXPRESS_SPORTS_URL ="http://indianexpress.com/section/sports/feed/";
    public static String BBC_FEED_URL ="http://feeds.bbci.co.uk/news/rss.xml";
    public static String BBC_ASIA_URL ="http://feeds.bbci.co.uk/news/world/asia/rss.xml";
    public static String BBC_WORLD_URL ="http://feeds.bbci.co.uk/news/world/rss.xml";
    public static String BBC_SPORT_URL ="http://feeds.bbci.co.uk/sport/0/rss.xml?edition=uk";


    public static void showProgressDialog(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void showProgressDialog(Context context, String message,boolean cancelable) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        try {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            progressDialog = null;
        }
    }

    public static void showCenteredToast(Context ctx, String msg) {
        if(toast==null){
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        }else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showBottomToast(Context ctx, String msg) {
        if(toast==null){
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connManager.getActiveNetworkInfo() != null
                && connManager.getActiveNetworkInfo().isConnected();
    }

    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    public static final List<String> timesString = Arrays.asList("year","month","day","hour","minute","second");

    public static String getFeedDuration(long duration) {
        duration=System.currentTimeMillis()-duration;
        StringBuffer res = new StringBuffer();
        for(int i=0;i< times.size(); i++) {
            Long current = times.get(i);
            long temp = duration/current;
            if(temp>0) {
                res.append(temp).append(" ").append( timesString.get(i) ).append(temp > 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if("".equals(res.toString()))
            return "0 second ago";
        else
            return res.toString();
    }
}