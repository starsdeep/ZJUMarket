package net.loonggg.fragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountFragment extends Fragment {

	private static final String TAG = "Ucenter Activity";
	private TextView totalText;
	private TextView weeklyInText;
	private TextView weeklyOutText;
	private EditText inNum;
	private Button inButton;
	private Button graphButton;
	PostAccountTask mPostTask = null;

	@Override
	public void onResume() {
		super.onResume();

		// get UI element reference
		totalText = (TextView) getActivity().findViewById(R.id.total_number);
		weeklyInText = (TextView) getActivity().findViewById(R.id.weekly_in);
		weeklyOutText = (TextView) getActivity().findViewById(R.id.weekly_out);
		inNum = (EditText) getActivity().findViewById(R.id.in_num);
		inButton = (Button) getActivity().findViewById(R.id.in_button);

		// UI element initialization
		CGCApp appState = ((CGCApp) getActivity().getApplicationContext());
		if (appState.getLoginState() == false) {
			FragmentTransaction ft = getActivity().getFragmentManager()
					.beginTransaction();
			ft.replace(R.id.center_frame, new LoginFragment());
			ft.commit();
		} 
		else {
			Log.e(TAG, "Already logined");
			totalText.setText(appState.getTotalMoney());
			weeklyInText.setText(appState.getWeekIn());
			weeklyOutText.setText(appState.getWeekOut());
			Log.e(TAG, "Already 1");
			mPostTask = new PostAccountTask(appState.getUsername(),
					appState.getPassword());
			Log.e(TAG, "Already 2");
			mPostTask.execute((Void) null);
			Log.e(TAG, "Already 3");
			inButton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e(TAG, "on button click ");
					// new HttpGetTask().execute();
					// attemptLogin();
				}
			});
		}

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_acount, null);
		ImageView left = (ImageView) view.findViewById(R.id.iv_home_left);
		LinearLayout total_layout = (LinearLayout) view.findViewById(R.id.totalLayout);
		LinearLayout consume_linear = (LinearLayout) view.findViewById(R.id.consume_linear);
		LinearLayout income_linear = (LinearLayout) view.findViewById(R.id.income_linear);
		
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).showLeft();
			}
		});

		total_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intObj = new Intent(getActivity(),AccountLineGraph.class);
				startActivity(intObj);
			}
		});
		
		consume_linear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intObj = new Intent(getActivity(),AccountLineGraph.class);
				startActivity(intObj);
			}
		});
		
		income_linear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intObj = new Intent(getActivity(),AccountLineGraph.class);
				startActivity(intObj);
			}
		});
		
		
		
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public class PostAccountTask extends AsyncTask<Void, Void, String> {
		private final String username;
		private final String password;
		private static final String TAG = "GetAccountTask";

		PostAccountTask(String username, String password) {
			this.username = username;
			this.password = password;
		}

		@Override
		protected String doInBackground(Void... params) {
			final String url = "http://192.168.137.1:8000/balance/";
			String contentToPost = "type=android&username=" + username
					+ "&password=" + password;
			String result = "";

			HttpURLConnection httpUrlConnection = null;
			try {
				// network access.

				Log.e(TAG, "socket about to up,username:" + username
						+ "password:" + password);
				Log.e(TAG, "about to connect");
				httpUrlConnection = (HttpURLConnection) new URL(url)
						.openConnection();
				Log.e(TAG, "connnected");
				httpUrlConnection.setDoInput(true);
				httpUrlConnection.setDoOutput(true);
				httpUrlConnection.setRequestMethod("GET");
				httpUrlConnection.setUseCaches(false);
				httpUrlConnection.setInstanceFollowRedirects(false);
				httpUrlConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// Log.e(TAG, "about to post connect");
				// httpUrlConnection.connect();
				// Log.e(TAG, "post connected");
				Log.e(TAG, "get output stream");
				DataOutputStream out = new DataOutputStream(
						httpUrlConnection.getOutputStream());
				Log.e(TAG, "get output stream successed");
				out.writeBytes(contentToPost);
				out.flush();
				out.close();
				Log.e(TAG, "get input stream");
				InputStream in = new BufferedInputStream(
						httpUrlConnection.getInputStream());
				Log.e(TAG, "get input stream successed");
				result = readStream(in);
				int responseCode = httpUrlConnection.getResponseCode();
				Log.e(TAG, "responseCode:" + responseCode);
				CGCApp appState = ((CGCApp) getActivity()
						.getApplicationContext());
				// Using JSON to initialize Text view
				try {
					Log.e(TAG, result);
					JSONArray JSONlist = (JSONArray) new JSONTokener(result).nextValue();
					int num = JSONlist.length();
					ArrayList<Double> balanceList = appState.getBalanceList();
					for (int i = 0; i < num && i < 365; i++) {
						//JSONObject field = tmpJSON.getJSONObject("fields");
						Log.e("json array", "" + JSONlist.getDouble(i));
						balanceList.add(JSONlist.getDouble(i));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				appState.printBalanceList();

			} catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			Log.e(TAG, "the login result is:" + result);

			return result;
		}

		private String readStream(InputStream in) {
			BufferedReader reader = null;
			StringBuffer data = new StringBuffer();
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					Log.e(TAG, "in readStrem" + line);
					data.append(line);
				}

			} catch (IOException e) {
				Log.e("test", "IOException");
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						Log.e("test", "IOException");
					}
				}
			}
			return data.toString();
		}

		@Override
		protected void onPostExecute(final String result) {

			Log.e(TAG, "result" + result);

			if (!result.equals("")) {
				Log.e(TAG, "successed !!!");
			} else {
				Log.e(TAG, "result is empty" + result);
			}

		}

	}

}
