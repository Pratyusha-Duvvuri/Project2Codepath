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
    public int retweet_count;
    public int favorite_count;
    public boolean retweet_status;
    public boolean favorite_status;





    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{

        Tweet tweet = new Tweet();

        // extract the values from JSON


        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON( jsonObject.getJSONObject("user"));
        tweet.reply = jsonObject.getString("in_reply_to_status_id");
        tweet.retweet_count = jsonObject.getInt("retweet_count");
        tweet.favorite_count = jsonObject.getInt("favorite_count");
        tweet.retweet_status  = jsonObject.getBoolean("retweeted");
        tweet.favorite_status = jsonObject.getBoolean("favorited");

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

        retweet_count = in.readInt();
        favorite_count = in.readInt();
        retweet_status  = in.readByte()==1;
        favorite_status = in.readByte()==1;


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
        out.writeInt(retweet_count);
        out.writeInt(favorite_count);
        if(retweet_status) out.writeByte((byte)1); else out.writeByte((byte)0);
        if(retweet_status) out.writeByte((byte)1); else out.writeByte((byte)0);

    }
}
