package com.ameron32.apps.tapnotes.v2.data.parse.model;

import com.ameron32.apps.tapnotes.v2.data.model.IUser;
import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by klemeilleur on 3/24/2016.
 */
@ParseClassName("_User")
public class User extends ParseUser implements IUser {

    public User() {
        // required empty constructor
    }
}
