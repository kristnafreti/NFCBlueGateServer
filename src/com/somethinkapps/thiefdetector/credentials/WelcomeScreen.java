package com.somethinkapps.thiefdetector.credentials;
import com.somethinkapps.bluegate.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeScreen extends Activity {
	Bundle myValues;
	private TextView welcomeMsg;
	private Button backButton;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        myValues=getIntent().getExtras();
        setUpViews();
    }
	private void setUpViews() {
		welcomeMsg=(TextView)findViewById(R.id.msg);
		welcomeMsg.setText("Welcome : "+myValues.getString("value"));
		backButton=(Button)findViewById(R.id.backBtn);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent;
				myIntent=new Intent(getBaseContext(),com.somethinkapps.bluegate.MainActivity.class);
				System.out.println("GO!");
				startActivity(myIntent);
			}
		});
	}
}