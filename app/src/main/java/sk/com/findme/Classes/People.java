package sk.com.findme.Classes;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import java.net.URL;


/**
 * Created by sanchirkartiev on 21/03/15.
 */
public class People {
    private Bitmap picture;
    private String name;
    private String phoneNumber;

    public People()
    {
        picture = null;
        name = "";
        phoneNumber = "";
    }
    public People(String name)
    {
        this.name = name;
        this.picture = null;
        this.phoneNumber = "";
    }
    public People(String name, String phoneNumber)
    {
        this.picture = null;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    public People(Bitmap picture, String name, String phoneNumber)
    {
        this.picture = picture;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    public void setPicture(Bitmap picture)
    {
        this.picture = picture;
    }
    public void setName(String firstName)
    {
        this.name = firstName;
    }
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public Bitmap getPicture()
    {
        return picture;
    }
    public String getName() { return name; }
    public String getPhoneNumber() {return phoneNumber;}

}
