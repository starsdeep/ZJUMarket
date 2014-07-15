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

//import com.google.android.gms.internal.e;

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
import android.widget.Toast;

public class PayActivity extends Activity {

	private PayTask mAuthTask = null;
	private String ID;
	private String username;
	private String type;
	private String res;
	private String balance;
	private String msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.e("test", "on create");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		final Button loginButton = (Button) findViewById(R.id.pay);

		ID = getIntent().getExtras().getString("ID");
		username = getIntent().getExtras().getString("username");
		type = getIntent().getExtras().getString("type");
		loginButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("test", "on nfc");
				Money();
			}
		});

	}

	public void Money() {

		mAuthTask = new PayTask(username, ID, type);
		mAuthTask.execute((Void) null);
	}

	public class PayTask extends AsyncTask<Void, Void, Boolean> {

		private final String username;
		private final String ID;
		private final String type;
		private static final String TAG = "UserLoginTask";

		PayTask(String username, String ID, String type) {
			this.username = username;
			this.ID = ID;
			this.type = type;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			String contentToPost = /* "isAndoid=True" + & */"type=" + type
					+ "&username=" + username + "&ID=" + ID;
			final String url = "http://192.168.137.1:8000/usertest/";

			// String loginResult = "";
			HttpURLConnection httpUrlConnection = null;

			try {
				// network access.

				Log.e(TAG, "socket about to up,username:" + username + "ID:"
						+ ID);

				Log.e(TAG, "about to connect");
				httpUrlConnection = (HttpURLConnection) new URL(url)
						.openConnection();
				Log.e(TAG, "connnected");

				httpUrlConnection.setDoInput(true);
				httpUrlConnection.setDoOutput(true);
				httpUrlConnection.setRequestMethod("POST");
				httpUrlConnection.setUseCaches(false);
				httpUrlConnection.setInstanceFollowRedirects(false);
				httpUrlConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
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
				res = readStream(in);
				int responseCode = httpUrlConnection.getResponseCode();
				Log.e(TAG, "responseCode:" + responseCode);
				try {
					String JSONResponse = res.toString();
					JSONObject responseObject = (JSONObject) new JSONTokener(
							JSONResponse).nextValue();
					// Log.e("res",responseObject.getString("state"));
					if (responseObject.getString("state").equals("True")) {
						balance = responseObject.getString("money");
						msg = "支付成功";
						return true;
					}

					else if (responseObject.getString("state").equals("False")) {
						Log.e("res", "False");
						msg = "余额不足";
						return false;
					} else if (responseObject.getString("state").equals(
							"Invalid")) {
						Log.e("res", "Invalid");
						msg = "订单过期";
						return false;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				Thread.sleep(200);
			} catch (InterruptedException e) {
				return false;
			} catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			if (success) {

				Log.e("pay", "success");
				Toast.makeText(PayActivity.this, "支付成功，余额为：" + balance,
						Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					;
				}

				Intent NFCIntent = new Intent(PayActivity.this,
						HomeActivity.class);

				startActivity(NFCIntent);

			} else {
				Toast.makeText(PayActivity.this, msg, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			// showProgress(false);
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
	}

}
