package cn.com.jdsc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class UcenterActivity extends Activity {

	private static final String TAG = "Ucenter Activity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ucenter);
		
		
		//get UI element reference	
		TextView username  = (TextView)findViewById(R.id.ucenter_username);
		TextView balance = (TextView)findViewById(R.id.ucenter_balance_count);
		TextView recordCount = (TextView)findViewById(R.id.ucenter_record_count);
		TextView historyCount = (TextView)findViewById(R.id.ucenter_history_count);
		
		EditText emailText  = (EditText)findViewById(R.id.ucenter_email);
		EditText addressText = (EditText)findViewById(R.id.ucenter_address);
		
		final Button requestButton = (Button) findViewById(R.id.ucenter_confirm_button);
		
		// UI element initialization
		
		CGCApp appState = ((CGCApp)getApplicationContext());  
		appState.printState();
		if(appState.getLoginState() == true){
			username.setText(appState.getUsername());
			balance.setText(appState.getBalance());
			recordCount.setText(appState.getRecordCount());
			historyCount.setText(appState.getHistoryCount());
			emailText.setHint(appState.getEmail());
			addressText.setHint(appState.getAddress());
			
			
		}
				
				
		requestButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(TAG, "on button click ");
				//new HttpGetTask().execute();
				//attemptLogin();
			}
		});
		
		
	
	}
	
}
