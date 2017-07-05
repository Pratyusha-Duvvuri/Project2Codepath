package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by pratyusha98 on 6/29/17.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.ivDetailsProfile)ImageView ivProfileImage;
    @BindView(R.id.tvDetailsUser)TextView tvUsername;
    @BindView(R.id.tvDetailsTimeStamp)TextView tvTimeStamp;
    @BindView(R.id.tvDetailsBody)TextView tvBody;
    @BindView(R.id.tvDetailScreenName)TextView tvScreenName;
    @BindView(R.id.ivDetailReply)ImageButton retweet;
    @BindView(R.id.ivDetailLike)ImageButton favorite;
    @BindView(R.id.tvDetailsRetweets)TextView retweetCount;
    @BindView(R.id.ivDetailsLoadedImage)ImageView loadedImage;
    @BindView(R.id.tvDetailsLike)TextView favoriteCount;


    //public TextView tvBody;

    Tweet tweet;
//    //    private final int RESULT_OK = 10;

    //These values are seperate
    public String rightUrl;
    TwitterClient client;
    public String relTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Toast.makeText(context, "Unretweeted", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        tweet = getIntent().getParcelableExtra("tweet");
        relTime = getIntent().getStringExtra("timeStamp");
        client = TwitterApp.getRestClient();


        //setting their values
        String body = tweet.body;

        tvUsername.setText(tweet.user.name);
        tvBody.setText(body);
        tvScreenName.setText("@" + tweet.user.screenName);

        tvTimeStamp.setText(relTime);
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        if(tweet.imageURL!="urgh") {

            Toast.makeText(this, ""+tweet.imageURL, Toast.LENGTH_SHORT).show();

            Glide.with(this).load(tweet.imageURL).into(loadedImage);

        }

        if (tweet.favorite_status)
            favorite.setImageResource(R.drawable.ic_vector_heart);
        else favorite.setImageResource(R.drawable.ic_vector_heart_stroke);
        if (tweet.retweet_status)
            retweet.setImageResource(R.drawable.ic_vector_retweet);
        else retweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        retweetCount.setText("" + tweet.retweet_count);
        favoriteCount.setText("" + tweet.favorite_count);

        retweet.setOnClickListener(this);
        favorite.setOnClickListener(this);

    }


    public void onSubmit(View v) {
        // closes the activity and returns to first screen
        this.finish();
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.ivDetailLike) {
            Log.d("Cont", "favorite");
            if (tweet.favorite_status) {

                client.unfavoriteTweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        favorite.setImageResource(R.drawable.ic_vector_heart_stroke);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });


            } else {
                client.favoriteTweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        favorite.setImageResource(R.drawable.ic_vector_heart);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }

                });
            }
        } else if (v.getId() == R.id.ivDetailReply) {

            if (tweet.retweet_status) {
                tweet.retweet_count -= 1;


                client.unretweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        retweet.setImageResource(R.drawable.ic_vector_retweet_stroke);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            } else {
                tweet.retweet_count += 1;

                client.retweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        retweet.setImageResource(R.drawable.ic_vector_retweet);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }


        }

    }
}












