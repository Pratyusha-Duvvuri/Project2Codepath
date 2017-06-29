package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by pratyusha98 on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    // pass in the Tweets array in the constructor
    public static List<Tweet> mTweets;
    Context context;
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
        //for time stamp

        public ViewHolder(View itemView){
                super(itemView);
            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            replyButton  = (Button) itemView.findViewById(R.id.reply);
            replyButton.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
                // gets item position
                int position = getAdapterPosition();
                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION) {
                    // get the movie at the position, this won't work if the class is static
                    Tweet t = mTweets.get(position);
                    // create intent for the new activity
                    Intent intent = new Intent(context, ComposeActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra(Tweet.class.getSimpleName(), 0);//Parcels.wrap(t)

                    // show the activity
                    context.startActivity(intent);
                    //startActivityForResult(i, REQUEST_CODE);

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




//
//
//    @Override
//    public void onClick(View v) {
//        // gets item position
//        int position = getAdapterPosition();
//        // make sure the position is valid, i.e. actually exists in the view
//        if (position != RecyclerView.NO_POSITION) {
//            // get the movie at the position, this won't work if the class is static
//            Tweet t = mTweets.get(position);
//            // create intent for the new activity
//            Intent intent = new Intent(context, ComposeActivity.class);
//            // serialize the movie using parceler, use its short name as a key
//            intent.putExtra(Tweet.class.getSimpleName(), 0);//Parcels.wrap(t)
//
//            // show the activity
//            context.startActivity(intent);
//            //startActivityForResult(i, REQUEST_CODE);
//
//        }