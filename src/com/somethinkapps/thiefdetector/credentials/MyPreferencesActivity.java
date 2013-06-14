package com.somethinkapps.thiefdetector.credentials;

import com.somethinkapps.bluegate.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MyPreferencesActivity extends PreferenceActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferences);
  }
} 