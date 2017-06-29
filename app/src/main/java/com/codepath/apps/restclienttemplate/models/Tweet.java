package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pratyusha98 on 6/26/17.
 */

public class Tweet implements Parcelable {

    //list out the attributes

    public String body;

    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public String reply;




    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{

        Tweet tweet = new Tweet();

        // extract the values from JSON


        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON( jsonObject.getJSONObject("user"));
        tweet.reply = jsonObject.getString("in_reply_to_status_id");
        return tweet;
    }


    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {


        @Override
        public Tweet createFromParcel(Parcel source) {


            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    public Tweet(){}
    private Tweet(Parcel in) {
        uid = in.readLong();

        user = in.readParcelable(User.class.getClassLoader());
        createdAt = in.readString();

        body = in.readString();
        reply = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(uid);
        out.writeParcelable(user,flags);
        out.writeString(createdAt);
        out.writeString(body);
        out.writeString(reply);

    }
}
