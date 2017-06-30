package com.codepath.apps.restclienttemplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pratyusha98 on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

//    tweetAdapter = new TweetAdapter(tweets);
//    //RecyclerView setup ( layout manager, use adapter)
//                rvTweets.setLayoutManager(new LinearLayoutManager(this));
//
//    //set the adapter
//                rvTweets.setAdapter(tweetAdapter);

    // pass in the Tweets array in the constructor
    public static List<Tweet> mTweets;
    Context context;
    TwitterClient client;

    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }




    //final descriptions : inflates the layout
    // for each row, inflate the layout and cache references
    //only invoked when a new row has to be created
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //inflate the tweet row
        View tweetView =inflater.inflate(R.layout.item_tweet, parent, false);

        //create a viewholder object
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // bind the values based on the position of the element (repopuate data based on previously cached viewholders)

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        Tweet tweet = mTweets.get(position);
        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvScreenName.setText("       @"+tweet.user.screenName);

        holder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.createdAt));

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);


    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    // create ViewHolder class

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimeStamp;
        public TextView tvScreenName;
        public Button replyButton;
        public ImageButton retweet;
        public ImageButton favorite;
        private final int REQUEST_CODE = 20;
        private final int RESULT_OK = 10;

        //for time stamp

        @SuppressLint("WrongViewCast")
        public ViewHolder(View itemView){
                super(itemView);
            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            replyButton  = (Button) itemView.findViewById(R.id.ivReply);
            replyButton.setOnClickListener(this);
            retweet  = (ImageButton) itemView.findViewById(R.id.ivRetweet);
            retweet.setOnClickListener(this);
            favorite  = (ImageButton) itemView.findViewById(R.id.ivFavorite);
            favorite.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
                // gets item position
                int position = getAdapterPosition();
                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION) {
                    // get the movie at the position, this won't work if the class is static
                    Tweet tweet = mTweets.get(position);
                    // create intent for the new activity


                    if (v.getId() == R.id.ivReply){
                        Intent intent = new Intent(context, ComposeActivity.class);
                        // serialize the movie using parceler, use its short name as a key
                        intent.putExtra("tweet", tweet);//Parcels.wrap(t)
                        // show the activity
                        //context.startActivityforResult(intent, REQUEST_CODE);
                        context.startActivity(intent);
                    //Todo ask about the thing abopve like what is going on with startActivity
                    }

                    else if(v.getId() == R.id.ivRetweet){
                        client = TwitterApp.getRestClient();
                        client.retweet(Long.toString(tweet.uid),new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                    });

                    }


                    }
                    else if (v.getId() == R.id.ivFavorite){}
                    else
                    {
//                        Intent intent = new Intent(context, DetailActivity.class);
//                        // seria\lize the movie using parceler, use its short name as a key
//                        //intent.putExtra("tweet", tweet);
//                        // show the activity
//                        //context.startActivityforResult(intent, REQUEST_CODE);
//                       // ((ComposeActivity) context).startActivityForResult(intent, REQUEST_CODE);
//
//                        context.startActivity(intent);
                    }


                }
        }


    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}

