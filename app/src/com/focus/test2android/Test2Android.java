package com.focus.test2android;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class Test2Android extends Application {

  static final String TAG = "MyApp";

  @Override
  public void onCreate() {
    super.onCreate();

      FacebookSdk.sdkInitialize(getApplicationContext());

      Parse.enableLocalDatastore(this);
      Parse.initialize(this,
              "mpU3AQ5oMjIfgdZYMlrW9SaiVM08uERULL5pEYTZ",
              "Q4o6Q53XVOu9NNo6gd0KQiC9FyLikl8Ebp1b01a2"
      );

      ParseFacebookUtils.initialize(this);
  }
}
