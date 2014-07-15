package net.loonggg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UcenterFragment extends Fragment {

	private static final String TAG = "Ucenter Activity";

	@Override
	public void onResume() {
		super.onResume();

		// get UI element reference
		TextView username = (TextView) getActivity().findViewById(
				R.id.ucenter_username);
		TextView balance = (TextView) getActivity().findViewById(
				R.id.ucenter_balance_count);
		TextView recordCount = (TextView) getActivity().findViewById(
				R.id.ucenter_record_count);
		TextView historyCount = (TextView) getActivity().findViewById(
				R.id.ucenter_history_count);
		EditText emailText = (EditText) getActivity().findViewById(
				R.id.ucenter_email);
		EditText addressText = (EditText) getActivity().findViewById(
				R.id.ucenter_address);
		Button requestButton = (Button) getActivity().findViewById(R.id.ucenter_confirm_button);
		
		// UI element initialization
		
		CGCApp appState = ((CGCApp)getActivity().getApplicationContext());  
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

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_ucenter, null);
		
		ImageView left=(ImageView)view.findViewById(R.id.iv_home_left);
		left.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).showLeft();
			}
		});
		
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
