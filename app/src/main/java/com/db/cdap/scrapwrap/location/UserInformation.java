package com.db.cdap.scrapwrap.location;

/**
 * Created by panalk on 9/27/2018.
 */

public class UserInformation extends UserID{

    String name, image;

    public UserInformation()
    {

    }

    public UserInformation(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
