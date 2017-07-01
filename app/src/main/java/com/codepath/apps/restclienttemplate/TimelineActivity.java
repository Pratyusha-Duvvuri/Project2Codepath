package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class TimelineActivity extends AppCompatActivity {

//    //this is for the intermediate progress bar
    MenuItem miActionProgressItem;
    ProgressBar v;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
         v = (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        populateTimeline();

        return super.onPrepareOptionsMenu(menu);
    }
    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    //first we put stuff in twitter client
    //now we are going to populate our timeline here

    //create references....
    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

   LinearLayoutManager llayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient();

        //find the recycler view and swipe containerview
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        tweets = new ArrayList<>();
        // init the array list (data source)
        //construct the adapter form this datasource
        tweetAdapter = new TweetAdapter(tweets);
        //RecyclerView setup ( layout manager, use adapter)
        llayout= new LinearLayoutManager(this) ;
        rvTweets.setLayoutManager(llayout);

        //set the adapter

        rvTweets.setAdapter(tweetAdapter);

//        //Stuff that i copied for the refresh action
//        //###############################
        // Lookup the swipe container view
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });


        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    //This is for infinite pagination
        scrollListener = new EndlessRecyclerViewScrollListener(llayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);



    }
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Toast.makeText(TimelineActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                // Remember to CLEAR OUT old items before appending in the new ones
                tweetAdapter.clear();
                // ...the data has come back, add new items to your adapter...


                for(int i =0;i< response.length();i++) {

                    //convert eachobject to a Tweet model
                    //add that Tweet model to our data source
                    //notify the adapter that we've added an item
                    Tweet tweet = null;

                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tweets.add(tweet);
                    tweetAdapter.notifyItemInserted(tweets.size() - 1);
                }
                //tweetAdapter.addAll(tweets);
                //tweetAdapter.notifyDataSetChanged();

                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });

    }



    private void populateTimeline(){
        showProgressBar();

        client.getHomeTimeline( new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("TwitterClient", response.toString() )    ;        }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //iterate through the JSON array
                // for each entry, deserialize the JSON onject

                for(int i =0;i< response.length();i++){

                //convert eachobject to a Tweet model
                //add that Tweet model to our data source
                //notify the adapter that we've added an item
                    Tweet tweet = null;

                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
             }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString )    ;
                throwable.printStackTrace();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString() )    ;
                throwable.printStackTrace();            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString() )    ;
                throwable.printStackTrace();             }
        });
        hideProgressBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // ActivityOne.java
// REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;
    private final int RESULT_OK = 10;

    // FirstActivity, launching an activity for a result
    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        //i.putExtra("mode", 2); // pass arbitrary data to launched activity
        Tweet tweet = new Tweet();
        tweet.user= null;
        ;
       // Log.d("Please", ""+tweet.user.uid);
        i.putExtra("iamhere",0);//Parcels.wrap(t)

        i.putExtra("tweet",tweet);//Parcels.wrap(t)

        startActivityForResult(i, REQUEST_CODE);
    }


    // ActivityOne.java, time to handle the result of the sub-activity aka Compose Activity

    //Have to use this code coz complex object http://guides.codepath.com/android/Using-Intents-to-Create-Flows#passing-complex-data-in-a-bundle

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet =  data.getParcelableExtra("tweet");
            // Toast the name to display temporarily on screen
            //Toast.makeText(this, (CharSequence) name, Toast.LENGTH_SHORT).show();
        //abive make sure that tweet is processed, button is assigned right fucntion signature
            //and we getClass properly


            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);


        }
    }


}
