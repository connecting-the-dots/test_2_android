package com.focus.test2android;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailsActivity extends Activity {

  private ProfilePictureView userProfilePictureView;
  private TextView userNameView;
  //private TextView userGenderView;
  //private TextView userEmailView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.userdetails);

    userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
    userNameView = (TextView) findViewById(R.id.userName);
    //userGenderView = (TextView) findViewById(R.id.userGender);
    //userEmailView = (TextView) findViewById(R.id.userEmail);


    //Fetch Facebook user info if it is logged
    ParseUser currentUser = ParseUser.getCurrentUser();
    if ((currentUser != null) && currentUser.isAuthenticated()) {
      makeMeRequest();
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    ParseUser currentUser = ParseUser.getCurrentUser();
    if (currentUser != null) {
      // Check if the user is currently logged
      // and show any cached content
      updateViewsWithProfileInfo();
    } else {
      // If the user is not logged in, go to the
      // activity showing the login view.
      startLoginActivity();
    }
  }

  private void makeMeRequest() {
    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
              @Override
              public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                if (jsonObject != null) {
                  JSONObject userProfile = new JSONObject();

                  try {
                    userProfile.put("facebookId", jsonObject.getLong("id"));
                    userProfile.put("name", jsonObject.getString("name"));

                    //if (jsonObject.getString("gender") != null)
                    //userProfile.put("gender", jsonObject.getString("gender"));

                    //if (jsonObject.getString("email") != null)
                    //userProfile.put("email", jsonObject.getString("email"));

                    // Save the user profile info in a user property
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    currentUser.put("profile", userProfile);
                    currentUser.saveInBackground();

                    // Show the user info
                    updateViewsWithProfileInfo();
                  } catch (JSONException e) {
                    Log.d(Test2Android.TAG,
                            "Error parsing returned user data. " + e);
                  }
                } else if (graphResponse.getError() != null) {
                  switch (graphResponse.getError().getCategory()) {
                    case LOGIN_RECOVERABLE:
                      Log.d(Test2Android.TAG,
                              "Authentication error: " + graphResponse.getError());
                      break;

                    case TRANSIENT:
                      Log.d(Test2Android.TAG,
                              "Transient error. Try again. " + graphResponse.getError());
                      break;

                    case OTHER:
                      Log.d(Test2Android.TAG,
                              "Some other error: " + graphResponse.getError());
                      break;
                  }
                }
              }
            });

    request.executeAsync();
  }

  private void updateViewsWithProfileInfo() {
    ParseUser currentUser = ParseUser.getCurrentUser();
    if (currentUser.has("profile")) {
      JSONObject userProfile = currentUser.getJSONObject("profile");
      try {

        if (userProfile.has("facebookId")) {
          userProfilePictureView.setProfileId(userProfile.getString("facebookId"));
        } else {
          // Show the default, blank user profile picture
          userProfilePictureView.setProfileId(null);
        }

        if (userProfile.has("name")) {
          userNameView.setText(userProfile.getString("name"));
        } else {
          userNameView.setText("");
        }

        /*if (userProfile.has("gender")) {
          userGenderView.setText(userProfile.getString("gender"));
        } else {
          userGenderView.setText("");
        }*/

        /*if (userProfile.has("email")) {
          userEmailView.setText(userProfile.getString("email"));
        } else {
          userEmailView.setText("");
        }*/

      } catch (JSONException e) {
        Log.d(Test2Android.TAG, "Error parsing saved user data.");
      }
    }
  }

  public void onLogoutClick(View v) {
    logout();
  }

  private void logout() {
    // Log the user out
    ParseUser.logOut();

    // Go to the login view
    startLoginActivity();
  }

  private void startLoginActivity() {
    Intent intent = new Intent(this, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  public void onTrackClick(View v) {
    showAppTrackActivity();
    Log.d(Test2Android.TAG, "click!");
  }

  private void showAppTrackActivity() {
    Intent intent = new Intent(this, AppTrackActivity.class);
    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }
}
