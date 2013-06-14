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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterScreen extends Activity {
	public SharedPreferences preferences;
	private EditText uNameOld;
	private EditText uPassOld;
	private EditText uNameNew;
	private EditText uPassNew;
	private Button regBtn;
	private Button cancelBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_page);
		preferences = PreferenceManager.getDefaultSharedPreferences(RegisterScreen.this);
		setUpViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.register_screen, menu);
		return true;
	}

	private void setUpViews() {
//		private Button regBtn;
//		private Button cancelBtn;
		uNameOld=(EditText)findViewById(R.id.oldNameTxt);
		uPassOld=(EditText)findViewById(R.id.oldPassTxt);
		uNameNew=(EditText)findViewById(R.id.regNameTxt);
		uPassNew=(EditText)findViewById(R.id.regPassTxt);
		uNameOld.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(uNameOld.getText().toString().equalsIgnoreCase("Old Username (Empty if New)")){
					if(hasFocus){
						uNameOld.setTextColor(Color.parseColor("#000000"));
						uNameOld.setText("");
					}
				}
				else if(uNameOld.getText().toString().equalsIgnoreCase("")){
					uNameOld.setTextColor(Color.parseColor("#888888"));
					uNameOld.setText("Old Username (Empty if New)");
				}
			}
		});
		uNameNew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(uNameNew.getText().toString().equalsIgnoreCase("Confirm New Username")){
					if(hasFocus){
						uNameNew.setTextColor(Color.parseColor("#000000"));
						uNameNew.setText("");
					}
				}
				else if(uNameNew.getText().toString().equalsIgnoreCase("")){
					uNameNew.setTextColor(Color.parseColor("#888888"));
					uNameNew.setText("Confirm New Username");
				}
			}
		});
        uPassOld.setOnFocusChangeListener(new View.OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(uPassOld.getText().toString().equalsIgnoreCase("Old Pass (Empty it if New)")){
					if(hasFocus){
						uPassOld.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						uPassOld.setTextColor(Color.parseColor("#000000"));
						uPassOld.setText("");
					}
				}
				else if(uPassOld.getText().toString().equalsIgnoreCase("")){
						uPassOld.setInputType(InputType.TYPE_CLASS_TEXT);
						uPassOld.setTextColor(Color.parseColor("#888888"));
						uPassOld.setText("Old Pass (Empty it if New)");
				}
			}
		});
        uPassNew.setOnFocusChangeListener(new View.OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(uPassNew.getText().toString().equalsIgnoreCase("Confirm New Pass")){
					if(hasFocus){
						uPassNew.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						uPassNew.setTextColor(Color.parseColor("#000000"));
						uPassNew.setText("");
					}
				}
				else if(uPassNew.getText().toString().equalsIgnoreCase("")){
						uPassNew.setInputType(InputType.TYPE_CLASS_TEXT);
						uPassNew.setTextColor(Color.parseColor("#888888"));
						uPassNew.setText("Confirm New Pass");
				}
			}
		});
		regBtn=(Button)findViewById(R.id.registerBtn);
		//regBtn.setFocusableInTouchMode(true);
		//regBtn.requestFocus();
		regBtn.setOnClickListener(new OnClickListener() {
			String username = preferences.getString("username", "n/a");
			String password = preferences.getString("password", "n/a");
			private String newpass;
			private String newname;
			private String oldpass;
			private String oldname;

			@Override
			public void onClick(View v) {
				Log.i("sf", "PASS 1");
				newname=uNameNew.getText().toString();
				newpass=uPassNew.getText().toString();
				oldname=uNameOld.getText().toString();
				oldpass=uPassOld.getText().toString();
				if(oldname.equals("Old Username (Empty if New)") && oldpass.equals("Old Pass (Empty it if New)")){
					oldname=("n/a");
					oldpass=("n/a");
				}
				if(oldname.equals(username) && oldpass.equals(password)){
				    Editor edit = preferences.edit();
			        edit.putString("username",newname);
			        edit.putString("password",newpass);
			        edit.commit();
			        Toast.makeText(RegisterScreen.this,
					        "Successfully changed the credentials, Please Re-Login with New Identity", Toast.LENGTH_LONG).show();
			        Intent myIntent=new Intent(RegisterScreen.this,CredentialMain.class);
					startActivity(myIntent);
			    }
				else{
					Toast.makeText(RegisterScreen.this,
					        "WRONG OLD USERNAME/PASS", Toast.LENGTH_LONG).show();
					Toast.makeText(RegisterScreen.this,
					        "DEBUG MODE \nold usrname: "+username+", Old pass: "+password, Toast.LENGTH_LONG).show();
				}
			}
		});
		cancelBtn=(Button)findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(RegisterScreen.this,
				        "Please Re-Login with Old Identity", Toast.LENGTH_LONG).show();
		        Intent myIntent=new Intent(RegisterScreen.this,CredentialMain.class);
				startActivity(myIntent);	
			}
		});
		
	}
}
