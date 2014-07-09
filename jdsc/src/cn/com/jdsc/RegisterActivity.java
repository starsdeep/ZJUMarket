package cn.com.jdsc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;



import com.google.android.gms.internal.e;

import android.R.string;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	
	
	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	private EditText usernameText;
	private EditText passwordText1;
	private EditText passwordText2;
	private TextView mTextView;
	private View mLoginFormView;
	private View mProgressView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.e("test", "on create");
		// Restore any saved state
		super.onCreate(savedInstanceState);
		// Set content view
		setContentView(R.layout.main_buy);
		// Initialize UI elements
		usernameText = (EditText) findViewById(R.id.login_input_name);
		passwordText1 = (EditText) findViewById(R.id.login_input_password);
		passwordText2 = (EditText) findViewById(R.id.login_input_confirm_password);
		
		
		final Button loginButton = (Button) findViewById(R.id.login_comfirm_button);
		final Button registerButton = (Button) findViewById(R.id.register_link);
		mTextView = (TextView) findViewById(R.id.textView1);
		
		
		// Link UI elements to actions in code
		//login
		loginButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("test", "on click login button ");
				Intent intObj = new Intent(RegisterActivity.this,HomeActivity.class);
				startActivity(intObj);
			}
		});
		//register
		registerButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("test", "on click register button ");
				attemptRegister();
			}
		});
		
	}

	public void attemptRegister() {

		usernameText.setError(null);
		passwordText1.setError(null);
		passwordText2.setError(null);

		String email = usernameText.getText().toString();
		String password1 = passwordText1.getText().toString();
		String password2 = passwordText2.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password1, if the user entered one.
		if (!TextUtils.isEmpty(password1) && !isPasswordValid(password1)) {
			passwordText1.setError(getString(R.string.error_invalid_password));
			focusView = passwordText1;
			cancel = true;
		}
		// Check for a valid password2, if the user entered one.
		if (!TextUtils.isEmpty(password2) && !isPasswordValid(password2)) {
			passwordText2.setError(getString(R.string.error_invalid_password));
			focusView = passwordText2;
			cancel = true;
		}
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			usernameText.setError(getString(R.string.error_field_required));
			focusView = usernameText;
			cancel = true;
		} else if (!isEmailValid(email)) {
			usernameText.setError(getString(R.string.error_invalid_email));
			focusView = usernameText;
			cancel = true;
		}
		//to do password1 and 2 
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			//showProgress(true);
			mAuthTask = new UserLoginTask(email, password1);
			mAuthTask.execute((Void) null);
		}

	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	/*
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	*/
	
	
	
	 
	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}

	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String username;
		private final String password;
		private static final String TAG = "UserLoginTask";

		UserLoginTask(String email, String password) {
			this.username = email;
			this.password = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			String contentToPost = /*"isAndoid=True" + &*/"username=" + username + "&password=" + password;
			final String url = "http://192.168.137.1:8000/login/";
			
			String loginResult = "";
			HttpURLConnection httpUrlConnection = null;
			
			try {
				//network access.
				
				Log.e(TAG, "socket about to up,username:" + username + "password:" + password);
				
				Log.e(TAG, "about to connect");
				httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
				Log.e(TAG, "connnected");
				
				
				httpUrlConnection.setDoInput(true);
				httpUrlConnection.setDoOutput(true);
				httpUrlConnection.setRequestMethod("GET");
				httpUrlConnection.setUseCaches(false);
				httpUrlConnection.setInstanceFollowRedirects(false);
				httpUrlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				//Log.e(TAG, "about to post connect");
				//httpUrlConnection.connect();
				//Log.e(TAG, "post connected");
				Log.e(TAG, "get output stream");
				DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
				Log.e(TAG, "get output stream successed");
				out.writeBytes(contentToPost);				
				out.flush();
				out.close();
				Log.e(TAG, "get input stream");
				InputStream in = new BufferedInputStream(httpUrlConnection.getInputStream());
				Log.e(TAG, "get input stream successed");
				loginResult = readStream(in);	
				int responseCode = httpUrlConnection.getResponseCode();
				Log.e(TAG, "responseCode:" + responseCode);
				Thread.sleep(200);
			} catch (InterruptedException e) {
				return false;
			}catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			Log.e(TAG, "the login result is:" + loginResult);
			Log.e(TAG, "Bolean:" + loginResult.equals("True"));
			return loginResult.equals("True");
			// TODO: register the new account here.		
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			Log.e(TAG, "success?" + success);
			//showProgress(false);

			if (success) {
				Intent intObj = new Intent(RegisterActivity.this,HomeActivity.class);
				startActivity(intObj);
				//finish();
				
				
			} else {
				passwordText1.setError(getString(R.string.error_incorrect_password));
				passwordText1.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			//showProgress(false);
		}
				
		// readStream to a String
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
		
		
	}
	
	
	
	
	
	
	
	private class HttpGetTask extends AsyncTask<Void, Void, String> {

		private static final String TAG = "HttpGetTask";

		// Get your own user name at http://www.geonames.org/login
		// private static final String USER_NAME = "";
		private static final String URL = "http://192.168.137.1:8000/login";

		@Override
		protected String doInBackground(Void... params) {
			String data = "";
			HttpURLConnection httpUrlConnection = null;

			try {
				Log.e(TAG, "about to connect");
				httpUrlConnection = (HttpURLConnection) new URL(URL).openConnection();
				Log.e(TAG, "connected");
				InputStream in = new BufferedInputStream(httpUrlConnection.getInputStream());
				Log.e(TAG, "GET inputstread successfully,about to readStream");
				data = readStream(in);

			} catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}

			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			mTextView.setText(result);
		}

		private String readStream(InputStream in) {
			Log.e(TAG, "in readStream");
			BufferedReader reader = null;
			StringBuffer data = new StringBuffer("");

			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					Log.e(TAG, "Read Line:" + line);
					data.append(line);
				}

			} catch (IOException e) {
				Log.e(TAG, "IOException");
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			String JSONResponse = data.toString();
			String result = "";
			
//			// JSON
//			try {
//				// Get top-level JSON Object - a Map
//				JSONObject responseObject = (JSONObject) new JSONTokener(
//						JSONResponse).nextValue();
//				result += "id:" + responseObject.get("id") + "name"
//						+ responseObject.get("name");
//				// Extract value of "earthquakes" key -- a List
//				// JSONArray earthquakes =
//				// responseObject.getJSONArray(EARTHQUAKE_TAG);
//				// Iterate over earthquakes list
//
//				/*
//				 * for (int idx = 0; idx < earthquakes.length(); idx++) {
//				 * 
//				 * // Get single earthquake data - a Map JSONObject earthquake =
//				 * (JSONObject) earthquakes.get(idx);
//				 * 
//				 * // Summarize earthquake data as a string and add it to //
//				 * result result.add(MAGNITUDE_TAG + ":" +
//				 * earthquake.get(MAGNITUDE_TAG) + "," + LATITUDE_TAG + ":" +
//				 * earthquake.getString(LATITUDE_TAG) + "," + LONGITUDE_TAG +
//				 * ":" + earthquake.get(LONGITUDE_TAG)); }
//				 */
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			return data.toString();
			//return result;

		}
	}

	private class HttpPostTask extends AsyncTask<Void, Void, String> {

		private static final String TAG = "HttpPostTask";
		private static final String url = "10.180.32.23";
		String username = usernameText.getText().toString();
		String password = passwordText1.getText().toString();

		@Override
		protected String doInBackground(Void... params) {
			String data = "";
			HttpURLConnection httpUrlConnection = null;

			try {

				Log.e(TAG, "socket about to up,username:" + username
						+ "password:" + password);

				httpUrlConnection = (HttpURLConnection) new URL(url)
						.openConnection();
				httpUrlConnection.setDoInput(true);
				httpUrlConnection.setDoOutput(true);
				httpUrlConnection.setRequestMethod("POST");
				httpUrlConnection.setUseCaches(false);
				httpUrlConnection.setInstanceFollowRedirects(false);
				httpUrlConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				httpUrlConnection.connect();

				String contentToPost = "isAndoid=True" + "&username="
						+ username + "&password=" + password;

				DataOutputStream out = new DataOutputStream(
						httpUrlConnection.getOutputStream());
				InputStream in = new BufferedInputStream(
						httpUrlConnection.getInputStream());
				out.writeBytes(contentToPost);
				// 刷新、关闭
				out.flush();

				data = readStream(in);

			} catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			mTextView.setText(result);

		}

		// readStream to a String
		private String readStream(InputStream in) {
			BufferedReader reader = null;
			StringBuffer data = new StringBuffer();
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
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
		//

	}
}
