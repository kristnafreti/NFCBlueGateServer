package com.somethinkapps.thiefdetector.credentials;

import com.somethinkapps.bluegate.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class CredentialMain extends Activity {
	private EditText uName;
	private EditText uPass;
	private Button loginBtn;
	private Button regBtn;
	public SharedPreferences preferences;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        setUpViews();
        
        // Initialize preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(CredentialMain.this);
		regBtn=(Button)findViewById(R.id.registerBtn);
		regBtn.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
        	  Intent myIntent=new Intent(CredentialMain.this,RegisterScreen.class);
        	  startActivity(myIntent);
//            String username = preferences.getString("username", "n/a");
//            String password = preferences.getString("password", "n/a");
//            //showPrefs(username, password);
//			    Editor edit = preferences.edit();
//		        edit.putString("username","Tes");
//		        edit.putString("password","Tes");
//		        edit.commit();
//    		uName.setText(username);
//    		uName.setTextColor(Color.parseColor("#888888"));
//    		uPass.setText(password);
//    		uPass.setInputType(InputType.TYPE_CLASS_TEXT);
//    		uPass.setTextColor(Color.parseColor("#888888"));
          }
        });

 //       Button buttonChangePreferences = (Button) findViewById(R.id.Button02);
 //       buttonChangePreferences.setOnClickListener(new OnClickListener() {
 //         public void onClick(View v) {
 //           
 //           updatePreferenceValue();
 //         }
 //       });
    }
	@Override
	protected void onResume() {
		super.onResume();
		uName.setText("Enter User Name");
		uName.setTextColor(Color.parseColor("#888888"));
		uPass.setText("Enter Password");
		uPass.setInputType(InputType.TYPE_CLASS_TEXT);
		uPass.setTextColor(Color.parseColor("#888888"));
	}
	private void setUpViews() {
		uName=(EditText)findViewById(R.id.usrNameTxt);
		uPass=(EditText)findViewById(R.id.usrPassTxt);
		uName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(uName.getText().toString().equalsIgnoreCase("Enter User Name")){
					if(hasFocus){
						uName.setTextColor(Color.parseColor("#000000"));
						uName.setText("");
					}
				}
				else if(uName.getText().toString().equalsIgnoreCase("")){
					uName.setTextColor(Color.parseColor("#888888"));
					uName.setText("Enter User Name");
				}
			}
		});
        uPass.setOnFocusChangeListener(new View.OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(uPass.getText().toString().equalsIgnoreCase("Enter Password")){
					if(hasFocus){
						uPass.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						uPass.setTextColor(Color.parseColor("#000000"));
						uPass.setText("");
					}
				}
				else if(uPass.getText().toString().equalsIgnoreCase("")){
						uPass.setInputType(InputType.TYPE_CLASS_TEXT);
						uPass.setTextColor(Color.parseColor("#888888"));
						uPass.setText("Enter Password");
				}
			}
		});
		loginBtn=(Button)findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new OnClickListener() {
			private String pass;
			private String name;
			Intent myIntent;
			@Override
			public void onClick(View v) {
				name=uName.getText().toString();
				pass=uPass.getText().toString();
				String username = preferences.getString("username", "n/a");
				String password = preferences.getString("password", "n/a");
				if(name.equalsIgnoreCase(username) && pass.equalsIgnoreCase(password)){
					myIntent=new Intent(CredentialMain.this,WelcomeScreen.class);
					System.out.println("---IF---");
			    }
				else if(name.equalsIgnoreCase("vimaltuts")){
			    	myIntent=new Intent(CredentialMain.this,ErrorScreen.class);
			    	myIntent.putExtra("user", "ok");
			    	System.out.println("---ELSE IF---");
			    }
			    else{
			    	myIntent=new Intent(CredentialMain.this,ErrorScreen.class);
			    	System.out.println("---ELSE---");
			    }
				myIntent.putExtra("value", name);
				startActivity(myIntent);
			}
		});

		
	}
}