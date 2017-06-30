package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import butterknife.BindView;

import static com.codepath.apps.restclienttemplate.R.id.ivDetailsProfile;
import static com.codepath.apps.restclienttemplate.R.id.ivLoadedImage;
import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by pratyusha98 on 6/29/17.
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tvDetailsBody) TextView tvBody;
    
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
    public  ImageView loadedImage;
    public String rightUrl;


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


        tvBody = (TextView) findViewById(R.id.tvDetailsBody);
        tvUsername = (TextView) findViewById(R.id.tvDetailsUser);
        tvTimeStamp = (TextView) findViewById(R.id.tvDetailsTimeStamp);
        tvScreenName = (TextView) findViewById(R.id.tvDetailScreenName);
        retweetCount = (TextView) findViewById(R.id.tvDetailsRetweets);
        favoriteCount= (TextView) findViewById(R.id.tvDetailsLike);
        ivProfileImage = (ImageView) findViewById(ivDetailsProfile);
        favorite = (ImageButton) findViewById(R.id.ivDetailLike);
        retweet = (ImageButton) findViewById(R.id.ivDetailReply);
        loadedImage = (ImageView) findViewById(ivLoadedImage);
        //setting their values
        String body = tweet.body;
        log.d("crashes",body);

        tvUsername.setText(tweet.user.name);
        tvBody.setText(body);
        tvScreenName.setText("       @"+tweet.user.screenName);

        tvTimeStamp.setText(relTime);
        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileImage);
        Glide.with(this).load(rightUrl).into(ivProfileImage);

        if(tweet.favorite_status)
            favorite.setImageResource(R.drawable.ic_vector_heart);
        else favorite.setImageResource(R.drawable.ic_vector_heart_stroke);
        if(tweet.retweet_status)
            retweet.setImageResource(R.drawable.ic_vector_retweet);
        else retweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        retweetCount.setText(""+tweet.retweet_count);
        favoriteCount.setText(""+tweet.favorite_count);


    }

    public void onSubmit(View v) {
        // closes the activity and returns to first screen
        this.finish();
    }

}
