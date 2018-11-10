package com.db.cdap.scrapwrap.location;

import android.support.annotation.NonNull;

/**
 * Created by panalk on 9/27/2018.
 */

public class UserID {

    public String userId;

    public <T extends UserID> T withId(@NonNull final String id)
    {
        this.userId = id;
        return (T)this;
    }
}
