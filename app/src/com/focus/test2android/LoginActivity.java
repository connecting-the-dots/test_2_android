package com.focus.test2android;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity {

  private Dialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    // Check if there is a currently logged in user
    // and it's linked to a Facebook account.
    ParseUser currentUser = ParseUser.getCurrentUser();
    if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
      // Go to the user info activity
      showUserDetailsActivity();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
  }

  public void onLoginClick(View v) {
    progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

    List<String> permissions = Arrays.asList("public_profile");
    Log.d(Test2Android.TAG, Arrays.toString(permissions.toArray()));

    // NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
    // (https://developers.facebook.com/docs/facebook-login/permissions/)
      ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {

        @Override
        public void done(ParseUser user, ParseException err) {
          progressDialog.dismiss();
          if (user == null) {
            Log.d(Test2Android.TAG, "Uh oh. The user cancelled the Facebook login.");
          } else if (user.isNew()) {
            Log.d(Test2Android.TAG, "User signed up and logged in through Facebook!");
            showUserDetailsActivity();
          } else {
            Log.d(Test2Android.TAG, "User logged in through Facebook!");
            Log.d(Test2Android.TAG,
                    AccessToken.getCurrentAccessToken().getPermissions().toString());
            showUserDetailsActivity();
          }
        }
      });
  }

  private void showUserDetailsActivity() {
    Intent intent = new Intent(this, UserDetailsActivity.class);
    startActivity(intent);
  }
}
