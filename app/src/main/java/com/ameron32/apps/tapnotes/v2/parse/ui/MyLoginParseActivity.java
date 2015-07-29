package com.ameron32.apps.tapnotes.v2.parse.ui;

import android.util.Log;
import android.view.View;


import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.controller.ActivityAlertDialogController;
import com.ameron32.apps.tapnotes.v2.di.module.ActivityModule;
import com.ameron32.apps.tapnotes.v2.di.module.DefaultAndroidActivityModule;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by klemeilleur on 3/16/2015.
 */
public class MyLoginParseActivity extends MyLoginActivity {

  @Override
  protected int inflateActivityLayout() {
    return R.layout.activity_login_parse;
  }

  @Override
  protected void onViewSet(View view) {
    super.onViewSet(view);
    ButterKnife.inject(this, view);
  }

  @Override
  protected Object[] getModules() {
    return new Object[] {
        new ActivityModule(this),
        new DefaultAndroidActivityModule(this)
    };
  }

  @Override
  protected int getLayoutResource() {
    return inflateActivityLayout();
  }

  @Inject
  ActivityAlertDialogController dialog;

  /**
   * TODO: CURRENTLY UNUSED, PLEASE INCLUDE IN MyLoginActivity PROCESS FLOW
   * @param email
   * @param password
   */
  @Override
  protected void performLogin(final String email, final String password) {
    ParseUser.logInInBackground(email, password, new LogInCallback() {
      @Override
      public void done(ParseUser parseUser, ParseException e) {
        if (e != null) {
          if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
            e.printStackTrace();
            dialog.showInterruptDialog("Validation Failed", "Your email address and password are invalid. " +
                    "If you do not yet have an account and would like to create one, " +
                    "click OK and then click CREATE AN ACCOUNT " +
                    "with your preferred email address and password in the boxes.",
                new Runnable() {
                  @Override
                  public void run() {
                    e.printStackTrace();
                    Log.d(MyLoginParseActivity.class.getSimpleName(), "error: " + e.getCode() + " " + e.getLocalizedMessage());
                    onLoginError();
                  }
                });
            return;
          }
        }

        if (e == null) {
          onLoginSuccess();
        } else {
          e.printStackTrace();
          Log.d(MyLoginParseActivity.class.getSimpleName(), "error: " + e.getCode() + " " + e.getLocalizedMessage());
          onLoginError();
        }

        // if (cancelled()) {
        //   onLoginCancelled();
        // }
      }
    });
  }

  /**
   * TODO: CURRENTLY UNUSED, PLEASE INCLUDE IN MyLoginActivity PROCESS FLOW
   * @param email
   * @param password
   */
  @Override
  protected void performSignUp(String email, String password) {
    ParseUser user = new ParseUser();
    user.setEmail(email);
    user.setUsername(email);
    user.setPassword(password);
    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {

        // TODO: Confirm if login is still needed

        if (e == null) {
          onLoginSuccess();
        } else {
          e.printStackTrace();
          onLoginError();
        }

        // if (cancelled()) {
        //   onLoginCancelled();
        // }
      }
    });
  }

  /**
   * TODO: CURRENTLY UNUSED, PLEASE INCLUDE IN MyLoginActivity PROCESS FLOW
   * @param email
   */
  @Override
  protected void performForgotPassword(String email) {
    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null) {
          onResetSuccess();
        } else {
          // e.printStackTrace();
          onResetError();
        }

        // if (cancelled()) {
        //   onLoginCancelled();
        // }
      }
    });
  }
}
