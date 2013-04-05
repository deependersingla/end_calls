package com.einstein.vishal.voicecalltest;

import java.lang.reflect.Method;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class VoiceCallTestActivity extends Activity {

	Button btn_Test_Call;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_call_test);
		
		EndCallListener callListener = new EndCallListener();
    	TelephonyManager mTM = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
    	mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
    	
		btn_Test_Call = (Button) findViewById(R.id.btn_Test_Call);
	        
		btn_Test_Call.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	        
	            	Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:09781967258"));
					startActivity(callIntent);
	            }
	        });
	        
	}
	
	boolean call_end_start = false;
	
	private class EndCallListener extends PhoneStateListener {
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	        
	        if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
	            //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
	         	
	        	Log.i(TAG, "OffHook");
	        	
	        	try 
            	{
	    			Log.d(TAG,"wait for 10 sec");
					Thread.sleep(10000);
					
					fun_END_Call();
				} 
            	catch (Exception e) 
				{	
            		Log.d(TAG,"Error  ;"+e.getMessage());
					e.printStackTrace();
				}
	    		
            	   
	        }
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_voice_call_test, menu);
		return true;
	}

	String TAG = "testCall";
	public void fun_END_Call()
    {
	    	try {
	    		
	    		
	    		
	            //String serviceManagerName = "android.os.IServiceManager";
	            String serviceManagerName = "android.os.ServiceManager";
	            String serviceManagerNativeName = "android.os.ServiceManagerNative";
	            String telephonyName = "com.android.internal.telephony.ITelephony";
	
	            Class telephonyClass;
	            Class telephonyStubClass;
	            Class serviceManagerClass;
	            Class serviceManagerNativeClass;
	            
	            Method telephonyEndCall;
	           
	            Object telephonyObject;
	            Object serviceManagerObject;
	
	            telephonyClass = Class.forName(telephonyName);
	            telephonyStubClass = telephonyClass.getClasses()[0];
	            serviceManagerClass = Class.forName(serviceManagerName);
	            serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
	
	            Method getService = 
	                    serviceManagerClass.getMethod("getService", String.class);
	
	            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod(
	                    "asInterface", IBinder.class);
	
	            Binder tmpBinder = new Binder();
	            tmpBinder.attachInterface(null, "fake");
	
	            serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
	            IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
	            Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
	
	            telephonyObject = serviceMethod.invoke(null, retbinder);
	            telephonyEndCall = telephonyClass.getMethod("endCall");
	            telephonyEndCall.invoke(telephonyObject);
	            Log.v("VoiceCall", "Call End Complete.");
	            
	
	        } catch (Exception e) {
	            e.printStackTrace();
	            Log.e("VoiceCall", "FATAL ERROR: could not connect to telephony subsystem");
	            Log.e("VoiceCall", "Exception object: " + e);
	    }

    }
	
}
