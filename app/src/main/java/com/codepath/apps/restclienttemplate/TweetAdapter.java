package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
    public String relTime;
    //public RecyclerView rvTweets;

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
        client = TwitterApp.getRestClient();

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
        holder.tvScreenName.setText("@"+tweet.user.screenName);
        relTime= getRelativeTimeAgo(tweet.createdAt);
        holder.tvTimeStamp.setText(relTime);

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
        if(tweet.favorite_status)
        holder.favorite.setImageResource(R.drawable.ic_vector_heart);
        else holder.favorite.setImageResource(R.drawable.ic_vector_heart_stroke);
        if(tweet.retweet_status)
            holder.retweet.setImageResource(R.drawable.ic_vector_retweet);
        else holder.retweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        holder.retweetCount.setText(""+tweet.retweet_count);
        holder.favoriteCount.setText(""+tweet.favorite_count);


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
        public TextView retweetCount;
        public TextView favoriteCount;
        private final int REQUEST_CODE = 20;
        private final int RESULT_OK = 10;

        //for time stamp

       // @SuppressLint("WrongViewCast")
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
            retweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);
            favoriteCount = (TextView) itemView.findViewById(R.id.tvFavoriteCount);
           // rvTweets = (RecyclerView) itemView.findViewById(R.id.rvTweet);


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


                if (v.getId() == R.id.ivReply) {
                    Intent intent = new Intent(context, ComposeActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra("tweet", tweet);//Parcels.wrap(t)
                    // show the activity
                    //context.startActivityforResult(intent, REQUEST_CODE);
                    ((TimelineActivity)context).startActivityForResult(intent,20);
                    // REQUEST_CODE is defined above
//                    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//                        // Extract name value from result extras
//                        Tweet tweet = (Tweet) data.getParcelableExtra("tweet");
                    //Todo ask about the thing abopve like what is going on with startActivity
                }
                else if (v.getId() == R.id.ivRetweet) {
                    Log.d("Cont", "retweet");


                    if(tweet.retweet_status){
                        tweet.retweet_count-=1;
                        //TweetAdapter.this.notify();



                        client.unretweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Toast.makeText(context, "Unretweeted", Toast.LENGTH_SHORT).show();
                                retweet.setImageResource(R.drawable.ic_vector_retweet_stroke);

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });

                    }

                    else{
                        tweet.retweet_count+=1;
                       // TweetAdapter.this.notify();

                        client.retweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(context, "Retweeted", Toast.LENGTH_SHORT).show();
                            retweet.setImageResource(R.drawable.ic_vector_retweet);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });}

                } else if (v.getId() == R.id.ivFavorite) {
                    Log.d("Cont", "favorite");
                    if(tweet.favorite_status)
                    {
                        //Toast.makeText(context, "INHERE", Toast.LENGTH_SHORT).show();

                        client.unfavoriteTweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Toast.makeText(context, "Unfavorited", Toast.LENGTH_SHORT).show();
                                favorite.setImageResource(R.drawable.ic_vector_heart_stroke);

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(context, "NOTWORKING", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                    else{
                    client.favoriteTweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Toast.makeText(context, "Favorited", Toast.LENGTH_SHORT).show();
                            favorite.setImageResource(R.drawable.ic_vector_heart);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });}
                }
             else {
                    //Toast.makeText(context, "Going to Details", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DetailActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra("tweet", tweet);//Parcels.wrap(t)
                    intent.putExtra("timeStamp", relTime);
                    // show the activity
                    //context.startActivityforResult(intent, REQUEST_CODE);
                    context.startActivity(intent);

             }
                Toast.makeText(context, "Going to Details", Toast.LENGTH_SHORT).show();


                //mTweets.add(0, tweet);
                //tweetAdapter.notifyItemInserted(0);
                //TweetAdapter.this.notifyItemInserted(0);
                //rvTweets.scrollToPosition(0);

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

