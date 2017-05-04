package sk.com.findme.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by sanchirkartiev on 22/03/15.
 */
public class FriendsObject implements Serializable, Parcelable{

    private String name;
    private double lat;
    private double longs;
    private String email;

    public FriendsObject(JSONObject object)
    {
        this.parseFromJson(object);
    }
    public FriendsObject(String name)
    {
        this.name = name;
        this.lat = 0;
        this.longs = 0;
        this.email = "";
    }
    public FriendsObject(Parcel in)
    {
        this.name = in.readString();
        this.lat = in.readDouble();
        this.longs = in.readDouble();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<FriendsObject> CREATOR = new Parcelable.Creator<FriendsObject>() {
        public FriendsObject createFromParcel(Parcel in) {
            return new FriendsObject(in);
        }
        public FriendsObject[] newArray(int size) {
            return new FriendsObject[size];
        }
    };
    private void parseFromJson(JSONObject jsonObject) {

        this.name = jsonObject.optString("name");
        this.lat = jsonObject.optDouble("lat");
        this.longs	= jsonObject.optDouble("long");
        this.email	= jsonObject.optString("email");
    }
    public String getName()
    {
        return name;
    }
    public double getLat()
    {
        return lat;
    }
    public double getLongs()
    {
        return longs;
    }
    public String getEmail()
    {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(longs);
        dest.writeString(email);

    }
    public String toString() {
        return "Person [name=" + name + ", lat=" + lat + ",long="+ longs +"]";
    }
}
