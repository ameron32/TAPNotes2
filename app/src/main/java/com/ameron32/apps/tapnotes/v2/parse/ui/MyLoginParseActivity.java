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

  @Override
  protected void performLogin(final String email, final String password) {
    ParseUser.logInInBackground(email, password, new LogInCallback() {
      @Override
      public void done(final ParseUser parseUser, final ParseException e) {
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

  @Override
  protected void performSignUp(final String email, final String password) {
    final ParseUser user = new ParseUser();
    user.setEmail(email);
    user.setUsername(email);
    user.setPassword(password);
    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(final ParseException e) {

        if (e == null) {
          onLoginSuccess();
        } else {
          e.printStackTrace();
          dialog.showInterruptDialog("Account Creation Failed", "We couldn't create your account " +
                  "with the username and/or password you have chosen.\n" +
                  "--valid email address\n" +
                  "--password 6 characters or longer\n" +
                  "--email cannot already have an account\n" +
                  "Please try again."
                  ,
              new Runnable() {
            @Override
            public void run() {
              e.printStackTrace();
              Log.d(MyLoginParseActivity.class.getSimpleName(), "error: " + e.getCode() + " " + e.getLocalizedMessage());
              onLoginError();
            }
          });
        }

        // if (cancelled()) {
        //   onLoginCancelled();
        // }
      }
    });
  }

  @Override
  protected void performForgotPassword(final String email) {
    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
      @Override
      public void done(final ParseException e) {
        if (e == null) {
          onResetSuccess();
          dialog.showInterruptDialog("Password Reset", "Your password is ready to reset. " +
                  "Please check your email address for an e-mail from noreply@parseapps.com. " +
                  "Follow the link in the email and enter your new password. " +
                  "Your login will work after that.",
              new Runnable() {
                @Override
                public void run() {
                  Log.d(MyLoginParseActivity.class.getSimpleName(), "success!");
                  onResetSuccess();
                }
              });
        } else {
          e.printStackTrace();
          onResetError();
        }

        // if (cancelled()) {
        //   onLoginCancelled();
        // }
      }
    });
  }
}
