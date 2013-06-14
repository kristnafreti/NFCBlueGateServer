package com.somethinkapps.thiefdetector.credentials;
import com.somethinkapps.bluegate.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ErrorScreen extends Activity {
	private Bundle myValues;
	private TextView errorMsg;
	private Button backButton;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_page);
        myValues=getIntent().getExtras();
        setUpViews();
    }
	private void setUpViews() {
		errorMsg=(TextView)findViewById(R.id.errmsg);
		if(myValues.getString("user")!=null){
			if(myValues.getString("user").equals("ok")){
				errorMsg.setText("User Found : "+myValues.getString("value")+"\n\nBut Password is Wrong \nContact Admin to Reset");
			}
		}
		else{
			errorMsg.setText("Invalid Entry : "+myValues.getString("value"));
		}
		backButton=(Button)findViewById(R.id.backBtn);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}