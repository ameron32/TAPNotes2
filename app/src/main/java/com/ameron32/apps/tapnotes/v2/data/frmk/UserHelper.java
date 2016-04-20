package com.ameron32.apps.tapnotes.v2.data.frmk;

import com.ameron32.apps.tapnotes.v2.data.model.IUser;

import rx.Observable;

/**
 * Created by klemeilleur on 4/20/2016.
 */
public interface UserHelper extends Helper {

  Observable<IUser> login(String username, String password);
  Observable<IUser> logout();
  Observable<IUser> getClientUser();
}
