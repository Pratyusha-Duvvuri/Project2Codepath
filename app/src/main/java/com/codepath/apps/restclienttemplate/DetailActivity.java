package com.codepath.apps.restclienttemplate;

import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

/**
 * Created by pratyusha98 on 6/29/17.
 */

public class DetailActivity {

    TwitterClient client;
    EditText Message;
    Tweet tweet;
    private final int RESULT_OK = 10;
    private EditText mEditText;
    private TextView characterCount ;
    public int state;
    public long num;

//    protected void onCreate(Bundle savedInstanceState) {
//        client =  TwitterApp.getRestClient();
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_compose);
//        mEditText = (EditText) findViewById(R.id.tweetHere);
//        mEditText.addTextChangedListener(mTextEditorWatcher);
//        characterCount = (TextView)findViewById(R.id.characterCount);
//        Tweet tweet = getIntent().getParcelableExtra("tweet");
//        state = getIntent().getIntExtra("iamhere",10);
//        Log.d("kayfam", "ayy");
//
//        // if(!(tweet.user==null)){
//        if(state ==10){
//            num= tweet.uid;
//            EditText editText = (EditText) findViewById(R.id.tweetHere);
//            editText.setText("@"+tweet.user.screenName, TextView.BufferType.EDITABLE);}
//    }





}
