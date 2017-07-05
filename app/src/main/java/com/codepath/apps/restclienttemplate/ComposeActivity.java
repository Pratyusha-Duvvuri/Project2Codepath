package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    //adding stuff for step 6

    TwitterClient client;
    EditText Message;
    Tweet tweet;
    private final int RESULT_OK = 10;
    private EditText mEditText;
    private TextView characterCount ;
    public int state;
    public long num;

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            int val = 140-s.length();

            characterCount.setText(String.valueOf(val));
            if (val <135)
            characterCount.setHighlightColor(getResources().getColor(R.color.red));
            else
                characterCount.setHighlightColor(getResources().getColor(R.color.blue));
        }

        public void afterTextChanged(Editable s) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client =  TwitterApp.getRestClient();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        mEditText = (EditText) findViewById(R.id.tweetHere);
        mEditText.addTextChangedListener(mTextEditorWatcher);
        characterCount = (TextView)findViewById(R.id.characterCount);
        Tweet tweet = getIntent().getParcelableExtra("tweet");
        state = getIntent().getIntExtra("iamhere",10);
       // if(!(tweet.user==null)){
        if(state ==10){
            num= tweet.uid;
        EditText editText = (EditText) findViewById(R.id.tweetHere);
        editText.setText("@"+tweet.user.screenName, TextView.BufferType.EDITABLE);}
    }


    public void returnTweet(View view) {

        // resolve the text field from the layout
        Message = (EditText) findViewById(R.id.tweetHere);
        //if it is a compose thing
        client.sendTweet(Message.getText().toString(),new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                //construct a new tweet here
                try {
                    tweet =  Tweet.fromJSON(response);
                    Intent data = new Intent();
                    // Pass relevant data back as a result
                    data.putExtra("tweet", tweet);
                    //data.putExtra("code", 200); // ints work too
                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish(); // closes the activity, pass data to parent

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        },num);


    }


}
