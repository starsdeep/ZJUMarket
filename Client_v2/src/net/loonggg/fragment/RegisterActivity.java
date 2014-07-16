package net.loonggg.fragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;












import android.R.string;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserRegisterTask mAuthTask = null;
	
	
	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	private EditText usernameText;
	private EditText emailText;
	private EditText passwordText1;
	private EditText passwordText2;
	private TextView RegisterErrorTextView;
	private View mLoginFormView;
	private View mProgressView;
	private static final String TAG = "RegisterActivity";
	private ProgressBar progressbar;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		
		usernameText = (EditText) findViewById(R.id.login_input_name);
		emailText = (EditText) findViewById(R.id.login_email);	
		passwordText1 = (EditText) findViewById(R.id.login_input_password);	
		passwordText2 = (EditText) findViewById(R.id.login_input_confirm_password);
	
		progressbar=(ProgressBar)findViewById(R.id.RegisterprogressBar);
		progressbar.setVisibility(View.GONE);
		final Button loginButton = (Button) findViewById(R.id.login_button);
		final Button registerButton = (Button) findViewById(R.id.register_button);
		RegisterErrorTextView = (TextView) findViewById(R.id.register_error);
		Log.e(TAG, "in onCreate7");
			
				
		
		
		//login
		loginButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("test", "on click login button ");
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.center_frame, new LoginFragment());
				ft.commit();
				//Intent intObj = new Intent(getBaseContext(),LoginActivity.class);
				//startActivity(intObj);
			}
		});
		Log.e(TAG, "in onCreate6");
		//register
		registerButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				progressbar.setVisibility(View.VISIBLE);
				Log.e("test", "on click register button ");
				attemptRegister();
			}
		});
		
	}

	public void attemptRegister() {

		usernameText.setError(null);
		passwordText1.setError(null);
		passwordText2.setError(null);

		String username = usernameText.getText().toString();
		String email = emailText.getText().toString();
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
			focusView = emailText;
			cancel = true;
		} else if (!isEmailValid(email)) {
			usernameText.setError(getString(R.string.error_invalid_email));
			focusView = emailText;
			cancel = true;
		}
		//password1 and 2 must be the same
		if(! password1.equals(password2) ){
			passwordText2.setError(getString(R.string.error_password_does_not_match));
			focusView = passwordText2;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			//showProgress(true);
			mAuthTask = new UserRegisterTask(username,email, password1);
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
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

		private  String username;
		private  String email;
		private  String password;
		private static final String TAG = "UserRegisterTask";
		private String registerResult = "";

		UserRegisterTask(String username, String email, String password) {
			this.username = username;
			this.email = email;
			this.password = password;
			
			try {
				this.username = URLEncoder.encode(username,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			String contentToPost = /*"isAndoid=True" + &*/"type=android"+"&username=" + username + "&password=" + password +"&email=" + email ;
			final String url = "http://192.168.137.1:8000/register/";
			
			
			HttpURLConnection httpUrlConnection = null;
			
			try {
				//network access.
				
				Log.e(TAG, "contentToPost:" + contentToPost);
				
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
				registerResult = readStream(in);	
				int responseCode = httpUrlConnection.getResponseCode();
				Log.e(TAG, "responseCode:" + responseCode);
				
			}catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			Log.e(TAG, "the register result is:" + registerResult);
			Log.e(TAG, "Bolean:" + registerResult.equals("True"));
			return registerResult.equals("True");
			// TODO: register the new account here.		
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			Log.e(TAG, "success?" + success);
					
			progressbar.setVisibility(View.GONE);	
			
			if (success) {
				//FragmentTransaction ft = getFragmentManager().beginTransaction();
				//ft.replace(R.id.center_frame, new HomeFragment());
				//ft.commit();
				//Intent intObj = new Intent(getBaseContext(),HomeActivity.class);
				//startActivity(intObj);
				CGCApp appState = (CGCApp)getApplicationContext();
				appState.setLoginState(true);
				appState.setUsername(username);
				appState.setPassword(password);
				appState.setEmail("email");
				
				
				finish();
				
			} else {
				RegisterErrorTextView.setText(registerResult);
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
		
	
}
