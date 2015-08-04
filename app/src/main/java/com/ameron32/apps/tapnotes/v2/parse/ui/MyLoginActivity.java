package com.ameron32.apps.tapnotes.v2.parse.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.TAPActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by klemeilleur on 6/18/2015.
 */
public abstract class MyLoginActivity
    extends TAPActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {

  // UI references.
  @InjectView(R.id.email)
  AutoCompleteTextView mEmailView;
  @InjectView(R.id.password)
  EditText mPasswordView;
  @InjectView(R.id.login_progress)
  View mProgressView;
  @InjectView(R.id.login_form)
  View mLoginFormView;
  @Optional
  @InjectView(R.id.login_form_column2)
  View mLoginFormView_Column2;
  @Optional
  @InjectView(R.id.login_form_column3)
  View mLoginFormView_Column3;

  protected abstract @LayoutRes
  int inflateActivityLayout();

  protected void onViewSet(View view) {};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final View v = LayoutInflater.from(this)
        .inflate(inflateActivityLayout(),
            (ViewGroup) this.getWindow().getDecorView().getRootView(), false);
    setContentView(v);
    onViewSet(v);
    ButterKnife.inject(this);

    // Set up the login form.
    populateAutoComplete();

    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
          softKeyboardAction(id);
          return true;
        }
        return false;
      }
    });
  }

  @OnClick(R.id.email_sign_in_button)
  void clickSignIn() {
    attemptLogin();
  }

  @OnClick(R.id.create_account_button)
  void createAccount() {
    attemptSignup();
  }

  @OnClick(R.id.forgot_password_button)
  void forgotPassword() {
    attemptForgotPassword();
  }

  void softKeyboardAction(int id) {
    attemptLogin();
  }

  private void populateAutoComplete() {
    getLoaderManager().initLoader(0, null, this);
  }


  /**
   * Attempts to sign in to the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  public void attemptLogin() {

    // Store values at the time of the login attempt.
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    if (!hasFormErrors(REQUEST_LOGIN)) {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.
      showProgress(true);

      performLogin(email, password);
    }
  }

  /**
   * Attempts to register the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual registration attempt is made.
   */
  public void attemptSignup() {
    // Store values at the time of the login attempt.
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    if (!hasFormErrors(REQUEST_CREATE)) {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.
      showProgress(true);

      performSignUp(email, password);
    }
  }

  /**
   * Attempts to report password lost for username specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual registration attempt is made.
   */
  public void attemptForgotPassword() {
    // Store values at the time of the login attempt.
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    if (!hasFormErrors(REQUEST_FORGOT)) {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.
      showProgress(true);

      performForgotPassword(email);
    }
  }

  private static final int REQUEST_LOGIN = 0;
  private static final int REQUEST_CREATE = 1;
  private static final int REQUEST_FORGOT = 2;

  private boolean hasFormErrors(final int requestType) {
    // Reset errors.
    mEmailView.setError(null);
    mPasswordView.setError(null);

    // Store values at the time of the login attempt.
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    boolean hasFormErrors = false;
    View focusView = null;


    if (requestType == REQUEST_LOGIN || requestType == REQUEST_CREATE) {
      // Check for a valid password, if the user entered one.
      if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
        mPasswordView.setError(getString(R.string.error_invalid_password));
        focusView = mPasswordView;
        hasFormErrors = true;
      }
    }

    // Check for a valid email address.
    if (TextUtils.isEmpty(email)) {
      mEmailView.setError(getString(R.string.error_field_required));
      focusView = mEmailView;
      hasFormErrors = true;
    } else if (!isEmailValid(email)) {
      mEmailView.setError(getString(R.string.error_invalid_email));
      focusView = mEmailView;
      hasFormErrors = true;
    }

    if (hasFormErrors) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      focusView.requestFocus();
    }

    return hasFormErrors;
  }

  protected abstract void performLogin(String email, String password);

  protected abstract void performSignUp(String email, String password);

  protected abstract void performForgotPassword(String email);

  private boolean isEmailValid(String email) {
    //TODO: Replace this with your own logic
    return email.contains("@");
  }

  private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return password.length() > 4;
  }

  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  public void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      mLoginFormView.animate().setDuration(shortAnimTime).alpha(
          show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
      });

      if (mLoginFormView_Column2 != null) {
        mLoginFormView_Column2.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView_Column2.animate().setDuration(shortAnimTime).alpha(
            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            mLoginFormView_Column2.setVisibility(show ? View.GONE : View.VISIBLE);
          }
        });
      }

      if (mLoginFormView_Column3 != null) {
        mLoginFormView_Column3.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView_Column3.animate().setDuration(shortAnimTime).alpha(
            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            mLoginFormView_Column3.setVisibility(show ? View.GONE : View.VISIBLE);
          }
        });
      }

      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mProgressView.animate().setDuration(shortAnimTime).alpha(
          show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      if (mLoginFormView_Column2 != null) {
        mLoginFormView_Column2.setVisibility(show ? View.GONE : View.VISIBLE);
      }
      if (mLoginFormView_Column3 != null) {
        mLoginFormView_Column3.setVisibility(show ? View.GONE : View.VISIBLE);
      }
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    return new CursorLoader(this,
        // Retrieve data rows for the device user's 'profile' contact.
        Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
            ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

        // Select only email addresses.
        ContactsContract.Contacts.Data.MIMETYPE +
            " = ?", new String[]{ContactsContract.CommonDataKinds.Email
        .CONTENT_ITEM_TYPE},

        // Show primary email addresses first. Note that there won't be
        // a primary email address if the user hasn't specified one.
        ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    List<String> emails = new ArrayList<String>();
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      emails.add(cursor.getString(ProfileQuery.ADDRESS));
      cursor.moveToNext();
    }

    addEmailsToAutoComplete(emails);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> cursorLoader) {

  }

  private interface ProfileQuery {
    String[] PROJECTION = {
        ContactsContract.CommonDataKinds.Email.ADDRESS,
        ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
    };

    int ADDRESS = 0;
    int IS_PRIMARY = 1;
  }


  private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
    //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(MyLoginActivity.this,
            android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

    mEmailView.setAdapter(adapter);
  }

  protected void onLoginCancelled() {
    showProgress(false);
    mPasswordView.setError(getString(R.string.error_incorrect_password));
    mPasswordView.requestFocus();
  }

  protected void onLoginError() {
    showProgress(false);
  }

  protected void onLoginSuccess() {
    setResult(RESULT_OK);
    finish();
  }

  protected void onResetError() {
    showProgress(false);
  }

  protected void onResetSuccess() {
    showProgress(false);
  }
}
