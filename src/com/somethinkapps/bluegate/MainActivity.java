package com.somethinkapps.bluegate;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import com.somethinkapps.thiefdetector.credentials.CredentialMain;
import com.somethinkapps.thiefdetector.credentials.RegisterScreen;
import com.somethinkapps.bluegate.R;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.DigitalOutput.Spec.Mode;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * This is the main activity of the HelloIOIO example application.
 * 
 * It displays a toggle button on the screen, which enables control of the
 * on-board LED. This example shows a very simple usage of the IOIO, by using
 * the {@link IOIOActivity} class. For a more advanced use case, see the
 * HelloIOIOPower example.
 */
public class MainActivity extends IOIOActivity implements SensorEventListener, SurfaceHolder.Callback {
	private ToggleButton button_;
	private TextView ioioStat;
	private ToggleButton gateToggle;
	//Servo Needs
	private final int PAN_PIN = 3;
	private final int TILT_PIN = 6;

	private final int PWM_FREQ = 100;

	private RatingBar gateStatus;
	private Button registerButton;
	private Button openButton;
	private Button enterButton;
	private Button exitButton;
	private Button sendButton;
	private SeekBar mPanSeekBar;
	private SeekBar mTiltSeekBar;
	private TextView panValue;
	private TextView tiltValue;
	private TextView title;
	private TextView status;
	private TextView distance;
	int panVal,tiltVal;
	float panFloat,tiltFloat;
	String parameterCmd;
	float reading=0;
	
	//Credential Needs
	public SharedPreferences preferences;
	
	//Bluetooth Needs
	//BluetoothChat bc=new BluetoothChat();
	
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    
    // Local Bluetooth adapter
    //private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    //private BluetoothChatService mChatService = null;
	
    //Sensor Needs
    SensorManager sm;
    Sensor proxSensor;
    TextView sensorStat;
    float proxVal=1;
    
    //Gate Logic Needs
    int parameter=0;
	int count=0;
	int lewat=0;
	
	//Bluetooth extras needs
	   // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;
    
	private TextView mTitle;
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;
    
    public static SurfaceView mSurfaceView;
    public static SurfaceHolder mSurfaceHolder;
    public static Camera mCamera ;
    public static boolean mPreviewRunning;
    //Database Initialization
    BlueDatabase db;
    String tes="90:C1:15:3A:40:F3";
    int i=0;
    int no=1;
    int second;
    int minute;
    int hourofday;
    int dayofyear;
    int year;
    int month;
    int dayofmonth;
    /**
	 * Called when the activity is first created. Here we normally initialize
	 * our GUI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Set up the custom title
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);
        mTitle = (TextView) findViewById(R.id.title_right_text);
        distance = (TextView) findViewById(R.id.textView_distanceSensor);
        
		button_ = (ToggleButton) findViewById(R.id.button);
		gateToggle = (ToggleButton) findViewById(R.id.toggleButton_gate);
		ioioStat = (TextView) findViewById(R.id.textView_connStat);
		ioioStat.setText("Not Ready");
	
		gateStatus=(RatingBar)findViewById(R.id.ratingBar_status);
		gateStatus.setRating(Float.parseFloat("2.0")); //Default indicator
		sendButton=(Button)findViewById(R.id.button_send);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				//bc.setupChat();
			}
        });
		
		registerButton=(Button)findViewById(R.id.button_register);
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent=new Intent(MainActivity.this,RegisterScreen.class);
				startActivity(myIntent);
			}
        });
		
		enterButton=(Button)findViewById(R.id.button_enter);
		exitButton=(Button)findViewById(R.id.button_exit);
		exitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			updt("1C:66:AA:C4:6C:72");
			}
        });
		enterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//exit
				insert("90:C1:15:3A:40:F3");
			}
        });
		
		status= (TextView) findViewById(R.id.textView_status);
		preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		String username = preferences.getString("username", "n/a");
		status.setText("Hello "+username+", Enjoy Your stay");
		title = (TextView) findViewById(R.id.title);
		panValue = (TextView) findViewById(R.id.textView_panValue);
		tiltValue = (TextView) findViewById(R.id.textView_tiltValue);
		mPanSeekBar = (SeekBar) findViewById(R.id.seekBar_pan);
		mPanSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {	//Sets the seekbar to be clickable, the below is the code that 'listens' for changes

			@Override
			public void onStopTrackingTouch(SeekBar PWMSeekBar) {
				//What to do when the seekabar stops moving (nothing in this case)
			}

			@Override
			public void onStartTrackingTouch(SeekBar PWMSeekBar) {
				//What to do when the seekabar detects it being touched
			}

			@Override
			public void onProgressChanged(SeekBar PWMSeekBar, int progress, boolean fromUser) {
				panVal = progress;				//progress ^^^^ is the variable that stores the position of the seekbar
				panFloat = ((float) progress)/1000;		//converted progress to a float so we can use it for PWM Output (value must be between 0 and 1)
				//panValue.setText(Integer.toString(panVal));
				panValue.setText(Integer.toString(400 + (mPanSeekBar.getProgress() * 12)));
			}
		});
		
		mTiltSeekBar = (SeekBar) findViewById(R.id.seekBar_tilt);
		mTiltSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {	//Sets the seekbar to be clickable, the below is the code that 'listens' for changes

			@Override
			public void onStopTrackingTouch(SeekBar PWMSeekBar) {
				//What to do when the seekabar stops moving (nothing in this case)
			}

			@Override
			public void onStartTrackingTouch(SeekBar PWMSeekBar) {
				//What to do when the seekabar detects it being touched
			}

			@Override
			public void onProgressChanged(SeekBar PWMSeekBar, int progress, boolean fromUser) {
				tiltVal = progress;				//progress ^^^^ is the variable that stores the position of the seekbar
				tiltFloat = ((float) progress)/1000;		//converted progress to a float so we can use it for PWM Output (value must be between 0 and 1)
				//tiltValue.setText(Integer.toString(panVal));
				tiltValue.setText(Integer.toString(500 + mTiltSeekBar.getProgress() * 2));
			}
		});
		openButton=(Button)findViewById(R.id.button_open);
		openButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			     Intent intent = new Intent(MainActivity.this, RecorderService.class);
			      //  System.out.println(VideoRecorder.class);
			         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			         startService(intent);
			         System.out.println("AAAAA");
				// TODO Auto-generated method stub
			parameter=2;
			}
        });
		
		bluetoothSetup();
		sensorSetup();
		gateStatus.setRating(Float.parseFloat("0.0"));
		databaseSetup();
		ensureDiscoverable();
	}
	
	public void databaseSetup(){
        db = new BlueDatabase(this);
        db.deleteAll(); 
        db.insertTitle(1,"90:C1:15:3A:40:F3", "Gredie", "001");
        db.insertTitle(2,"1C:66:AA:C4:6C:72","Chris","002");
        db.insertTitle(3,"D0:51:62:94:DE:B7", "Nuge","003");
        db.insertTitle(4,"D4:87:D8:3A:B6:C9", "Anto","004");
        
	}
	
	private void insert(String macAddress){ 
		List<String> IDdataList = this.db.selectID();
    	for (String IDmain : IDdataList) {
        	if(macAddress.equalsIgnoreCase(IDmain))
        	{		
    	int jml=1;
    Calendar cal = Calendar.getInstance(); 
      second = cal.get(Calendar.SECOND);
      minute = cal.get(Calendar.MINUTE);
      hourofday = cal.get(Calendar.HOUR_OF_DAY);
      dayofyear = cal.get(Calendar.DAY_OF_YEAR);//untuk perhitungan
      year = cal.get(Calendar.YEAR);
      month = cal.get(Calendar.MONTH);
      dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        String time=String.valueOf(hourofday)+":"+String.valueOf(minute)+":"+String.valueOf(second);
        String date=String.valueOf(dayofmonth)+"-"+String.valueOf(month+1)+"-"+String.valueOf(year);
        Toast.makeText(MainActivity.this,"Entry time: "+hourofday+"hour "+minute+" minute "+second+" second", Toast.LENGTH_SHORT).show();
        List<String> IDlist = this.db.selectID1();
    	for (String id : IDlist) {
    	if(macAddress.equalsIgnoreCase(id))
    		jml++;
    	}
        db.insertTitle1(no,macAddress,jml,time,date,hourofday,minute,second,dayofyear);
        no++;
        break;
        	}
    	}
    }
	 private void updt(String mac)
	    {
	    	i=0;
	    	List<String> IDlist = this.db.selectID1();
	    	for (String id : IDlist) {
	    		i++;
	    	}
	    	List<String> EntryHlist = this.db.selectEntryH();
	    	List<String> EntryMlist = this.db.selectEntryM();
	    	List<String> EntrySlist = this.db.selectEntryS();
	    	List<String> Elapsedlist = this.db.selectElapsed();
	    	List<String> EntryDaylist = this.db.selectEntryDayofyear();
	    	int entrysec[]=new int[i];
	    	String elap[]=new String[i];
	    	int entrymin[]=new int[i];
	    	int entryhour[]=new int[i];
	    	int entryday[]=new int[i];
	    	i=0;
	   	 for (String exits : Elapsedlist) {
	          elap[i]=exits;
	   		 i++;
	         }
	    	i=0;
	    	 for (String ens : EntrySlist) {
	           entrysec[i]=Integer.parseInt(ens);
	    		 i++;
	          }
	    	 i=0;
	    	 for (String min : EntryMlist) {
	             entrymin[i]=Integer.parseInt(min);
	      		 i++;
	            }
	    	 i=0;
	    	 for (String hour : EntryHlist) {
	             entryhour[i]=Integer.parseInt(hour);
	      		 i++;
	            }
	    	 i=0;
	    	 for (String day : EntryDaylist) {
	             entryday[i]=Integer.parseInt(day);
	      		 i++;
	            }
	    	Calendar cal1 = Calendar.getInstance(); 
	    	int second1 = cal1.get(Calendar.SECOND);
	    	int minute1 = cal1.get(Calendar.MINUTE);
	    	int hourofday1 = cal1.get(Calendar.HOUR_OF_DAY);
	    	int dayofyear1 = cal1.get(Calendar.DAY_OF_YEAR);//untuk perhitungan
	    	int year1 = cal1.get(Calendar.YEAR);
	    	int month1 = cal1.get(Calendar.MONTH);
	      	int dayofmonth1 = cal1.get(Calendar.DAY_OF_MONTH);
	         i=0;
	         int day1;
	         int exm1=0;
	         int exs1=0;
	         int eh=0;
	         String dum=" ";

	         for (String id : IDlist) {
	        	 if(id.equalsIgnoreCase(mac) && elap[i].equalsIgnoreCase(dum))
	        	 { 
	        		 int ssec=second1-entrysec[i];
	        		day1=dayofyear1-entryday[i];
	        		if(ssec<0)
	        		{	minute1--;
	        			exs1=60+ssec;    			
	        		}
	        		else
	        			exs1=ssec;
	        		int smin=minute1-entrymin[i];
	        		if(smin<0)
	        		{	eh--;
	        			exm1=60+smin;        			
	        		}
	        		else
	        			exm1=smin;
	        		if(day1<0)
	        			day1=day1+365;
	        		for(day1=day1;day1>0;day1--)       		
	        			eh=eh+24;
	        		eh=eh+hourofday1-entryhour[i];
	        		Toast.makeText(MainActivity.this,eh+"jam "+exm1+"menit "+exs1+"detik elapsed", Toast.LENGTH_SHORT).show();
	        		String time=String.valueOf(hourofday1)+":"+String.valueOf(minute1)+":"+String.valueOf(second1);
	                String date=String.valueOf(dayofmonth1)+"-"+String.valueOf(month1+1)+"-"+String.valueOf(year1);
	                String elapsed=String.valueOf(eh)+" jam "+String.valueOf(exm1)+"menit "+String.valueOf(exs1)+"detik";
	                db.update(i+1, time, date, hourofday1, minute1, second1, dayofyear1, elapsed);
	        		i=0;
	        		break;
	        	 }
	        	 else
	        		 i++;
	         }
	   
	     }
	public void sensorSetup(){
		sm=(SensorManager)getSystemService(SENSOR_SERVICE);
		proxSensor=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		sensorStat=(TextView)findViewById(R.id.textView_sensorStat);
		sm.registerListener(this, proxSensor, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		proxVal=event.values[0];
		sensorStat.setText(String.valueOf(proxVal));
	}
	
	public void bluetoothSetup(){
		// Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
	}
        
        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	stopService(new Intent(MainActivity.this, RecorderService.class));
                // Send a message using content of the edit text widget

            }
        });
        
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
	}
	
    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            //if (mChatService == null) setupChat();
        }
        
        //Handler handler = null;
		//BluetoothSocket socket = null;
		//TextView messageText = null;
		//BluetoothSocketListener bsl = new BluetoothSocketListener(socket, handler, messageText);
        //Thread messageListener = new Thread(bsl);
        //messageListener.start();
    }


    
    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
    	stopService(new Intent(MainActivity.this, RecorderService.class));
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);
                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    mTitle.setText(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                //Toast.makeText(getApplicationContext(), readMessage, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,readMessage+"read message", Toast.LENGTH_SHORT).show();
                if(readMessage.equals("enter")||readMessage.equals("exit")){
            		command(readMessage);
            		Toast.makeText(MainActivity.this,"Command", Toast.LENGTH_SHORT).show(); 
            	}
            	else {
                    checkCredentials(readMessage);
                    Toast.makeText(MainActivity.this,"Cek Credensial", Toast.LENGTH_SHORT).show();    				   				
            	}
                //checkCredentials(readMessage);
                //System.out.println(readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
    
    public void checkCredentials(String username){
//    	String parameterID=null;

    	List<String> IDlist = this.db.selectID();
    	 for (String ID2 : IDlist) {
    	if(ID2.equalsIgnoreCase(username))
    	{
    		parameterCmd=username;
    	break;    		
    	}
    	else{
    		parameterCmd="ERROR ID";
 			gateStatus.setRating(Float.parseFloat("3.0"));
     	}
		
     	}
 }
    
    public void command(String cmd){
		if(cmd.equals("enter")){
    		gateStatus.setRating(Float.parseFloat("1.5"));
			insert(parameterCmd);
    		Toast.makeText(MainActivity.this,parameterCmd+" memasuki area", Toast.LENGTH_SHORT).show();
			parameter=3;
			}
			else if(cmd.equals("exit")){
				updt(parameterCmd);
	    		Toast.makeText(MainActivity.this,parameterCmd+" keluar area", Toast.LENGTH_SHORT).show();
				parameter=3;
			}
			else{
	    		Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
	    		parameter=0;
			}
		Toast.makeText(MainActivity.this,parameter+"asf", Toast.LENGTH_SHORT).show();
//		virtualcmd(parameter);
    }
    
    public void virtualcmd(int code){
    	lewat=0;
    	System.out.println("Step one");
		if (code==3){
			System.out.println("Step two");
		openVirtualGate();
		Toast.makeText(MainActivity.this,"Masuk IF", Toast.LENGTH_SHORT).show();
		
		while(proxVal==0){
			System.out.println("step 3");
//			count++;
//			if(count==50){
//				while(true){
//				closeGate();
//				}
//			}
			openVirtualGate();
			lewat=1;
		}
		if(proxVal==1&&lewat==1){
			System.out.println("step 4");
			closeVirtualGate();
			lewat=0;
			parameter=0;
    		Toast.makeText(MainActivity.this,"Gate closing", Toast.LENGTH_SHORT).show();
		}
		System.out.println("Step 5");
//		else if(proxVal==1){
//			count++;
//		}
		}
    }
    

	public void openVirtualGate(){
			gateStatus.setRating(Float.parseFloat("5.0"));
	}
	public void closeVirtualGate(){
			gateStatus.setRating(Float.parseFloat("1.0"));
	}
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
        case REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, false);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                bluetoothSetup();
            } else {
                // User did not enable Bluetooth or an error occured
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
            //    finish();
            }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BLuetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.secure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        case R.id.insecure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }
    
	/**
	 * This is the thread on which all the IOIO activity happens. It will be run
	 * every time the application is resumed and aborted when it is paused. The
	 * method setup() will be called right after a connection with the IOIO has
	 * been established (which might happen several times!). Then, loop() will
	 * be called repetitively until the IOIO gets disconnected.
	 */
	class Looper extends BaseIOIOLooper {
		/** The on-board LED. */
		private DigitalOutput led_;
		private PwmOutput panPwmOutput;
		private PwmOutput tiltPwmOutput;
		private AnalogInput input_;
		/**
		 * Called every time a connection with IOIO has been established.
		 * Typically used to open pins.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
		 */
		@Override
		protected void setup() throws ConnectionLostException {
			
			led_ = ioio_.openDigitalOutput(0, true);
			//ioioStat.setText("Ready");
			panPwmOutput = ioio_.openPwmOutput(new DigitalOutput.Spec(
					PAN_PIN, Mode.OPEN_DRAIN), PWM_FREQ);
			tiltPwmOutput = ioio_.openPwmOutput(new DigitalOutput.Spec(
					TILT_PIN, Mode.OPEN_DRAIN), PWM_FREQ);
			input_ = ioio_.openAnalogInput(40);
			//Untuk bisa setText dalam IOIO
			enableUi(true);
			closeGate();
		}

		/**
		 * Called repetitively while the IOIO is connected.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
		 */
		@Override
		public void loop() throws ConnectionLostException{
			led_.write(!button_.isChecked());
			checkSensor();
			displaychange();
			if (parameter==3){
			openGate();
			while(proxVal==0){
				openGate();
				lewat=1;
			}
			if(proxVal==1&&lewat==1){
				closeGate();
				lewat=0;
				parameter=0;
			}
			}
			
			if (parameter==2){
			closeGate();	
			}
			
			if (parameter==1){
				try {
					closeGate();
					while(count==0){
					Thread.sleep(500);
					openGate();
					if(proxVal==0){
						closeGate();
						count++;
						parameter=0;
					}
					else if(proxVal==1){
						count++;
					}
					}

				} 
				catch (InterruptedException e) {
					
				}
			}
			
/*			else{
			if (gateToggle.isChecked()) {
				try {
					panPwmOutput.setPulseWidth(600);
					Thread.sleep(2000);
					panPwmOutput.setPulseWidth(1600);
					Thread.sleep(2000);
				} catch (InterruptedException e) {

				}
			} else {
				try {
					panPwmOutput
							.setPulseWidth(400 + mPanSeekBar.getProgress() * 12);
					tiltPwmOutput.setPulseWidth(500 + mTiltSeekBar
							.getProgress() * 2);
					Thread.sleep(100);
				} catch (InterruptedException e) {

				}
			}
			}*/
		}
		
		public void checkSensor() throws ConnectionLostException{
			try {
				reading = input_.getVoltage();
				System.out.println("Cek Sensor");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		public void openGate() throws ConnectionLostException{
			try {
				panPwmOutput.setPulseWidth(1600);
				gateStatus.setRating(Float.parseFloat("5.0"));
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}
		public void closeGate() throws ConnectionLostException{
			try {
				panPwmOutput.setPulseWidth(600);
				gateStatus.setRating(Float.parseFloat("1.0"));
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}
		
		public void disconnected(){
			enableUi(false);
		}
	}

	/**
	 * A method to create our IOIO thread.
	 * 
	 * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
	 */
	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
	
	private void displaychange() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {				
//			panValue.setText((CharSequence) (mPanSeekBar));
			//editText_usartoutput.
			distance.setText("Appr:"+reading);
//			tiltValue.setText((CharSequence) mTiltSeekBar);//Overwrites UART Input String with the incoming data
//			NotificationManager notifMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
//			  Notification notif = new Notification();
//			  notif.ledARGB = Color.argb(255, 255, 255, 255);
//			  notif.flags |= Notification.FLAG_SHOW_LIGHTS;
//			  notif.ledOnMS = 1;
			  //notif.ledOffMS = 300;
			
			//notifMgr.notify(1234, notif);
			}
		});

	}
	private void enableUi(final boolean enable) { //Take a note that enable here is a boolean variable contains true or false
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//For disable effect if not connected to IOIO
				if (enable == true) {
					ioioStat.setText("IOIO Ready!");

				} else {
					ioioStat.setText("Not Ready");
				}
			}
		});
	}

	//bluetooth needs
	private class BluetoothSocketListener implements Runnable {

		  private BluetoothSocket socket;
		  private TextView textView;
		  private Handler handler;

		  public BluetoothSocketListener(BluetoothSocket socket, 
		                                 Handler handler, TextView textView) {
		    this.socket = socket;
		    this.textView = textView;
		    this.handler = handler;
		  }

		public void run() {
		  int bufferSize = 1024;
		  byte[] buffer = new byte[bufferSize];      
		  try {
		    InputStream instream = socket.getInputStream();
		    int bytesRead = -1;
		    String message = "";
		    while (true) {
		      message = "";
		      bytesRead = instream.read(buffer);
		      if (bytesRead != -1) {
		        while ((bytesRead==bufferSize)&&(buffer[bufferSize-1] != 0)) {
		          message = message + new String(buffer, 0, bytesRead);
		          bytesRead = instream.read(buffer);
		        }
		        message = message + new String(buffer, 0, bytesRead - 1); 

		        handler.post(new MessagePoster(textView, message));              
		        socket.getInputStream();
		      }
		    }
		  } catch (IOException e) {
		    Log.d("BLUETOOTH_COMMS", e.getMessage());
		  } 
		}
		}
	
	private class MessagePoster implements Runnable {
	    private TextView textView;
	    private String message;

	    public MessagePoster(TextView textView, String message) {
	      this.textView = textView;
	      this.message = message;
	    }

	    public void run() {
	      textView.setText(message);
	    }     
	  }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}