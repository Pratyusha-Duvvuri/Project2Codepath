package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.R.id.ivDetailsProfile;
import static com.codepath.apps.restclienttemplate.R.id.ivLoadedImage;
import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by pratyusha98 on 6/29/17.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvDetailsBody)
    TextView tvBody;

    //public TextView tvBody;

    Tweet tweet;
    //    private final int RESULT_OK = 10;
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvTimeStamp;
    public TextView tvScreenName;
    public Button replyButton;
    public ImageButton retweet;
    public ImageButton favorite;
    public TextView retweetCount;
    public TextView favoriteCount;
    public String relTime;
    public ImageView loadedImage;
    public String rightUrl;
    TwitterClient client;


    // Context context = getApplicationContext();
//    CharSequence text = "Hello toast!";
//    int duration = Toast.LENGTH_SHORT;


//        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Toast.makeText(context, "Unretweeted", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tweet = getIntent().getParcelableExtra("tweet");
        relTime = getIntent().getStringExtra("timeStamp");
        client = TwitterApp.getRestClient();


        tvBody = (TextView) findViewById(R.id.tvDetailsBody);
        tvUsername = (TextView) findViewById(R.id.tvDetailsUser);
        tvTimeStamp = (TextView) findViewById(R.id.tvDetailsTimeStamp);
        tvScreenName = (TextView) findViewById(R.id.tvDetailScreenName);
        retweetCount = (TextView) findViewById(R.id.tvDetailsRetweets);
        favoriteCount = (TextView) findViewById(R.id.tvDetailsLike);
        ivProfileImage = (ImageView) findViewById(ivDetailsProfile);
        favorite = (ImageButton) findViewById(R.id.ivDetailLike);
        retweet = (ImageButton) findViewById(R.id.ivDetailReply);
        loadedImage = (ImageView) findViewById(ivLoadedImage);
        //setting their values
        String body = tweet.body;
        log.d("crashes", body);

        tvUsername.setText(tweet.user.name);
        tvBody.setText(body);
        tvScreenName.setText("       @" + tweet.user.screenName);

        tvTimeStamp.setText(relTime);
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        Glide.with(this).load(rightUrl).into(ivProfileImage);

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
                // Toast.makeText(this, "INHERE", Toast.LENGTH_SHORT).show();

                client.unfavoriteTweet(Long.toString(tweet.uid), new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //Toast.makeText(this, "Unfavorited", Toast.LENGTH_SHORT).show();
                        favorite.setImageResource(R.drawable.ic_vector_heart_stroke);
                        //Toast.makeText(this, "INHERE", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //Toast.makeText(this, "NOTWORKING", Toast.LENGTH_SHORT).show();

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
            Log.d("Cont", "retweet");


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












