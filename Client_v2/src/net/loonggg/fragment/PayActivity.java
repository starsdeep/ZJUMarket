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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

//import com.google.android.gms.internal.e;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PayActivity extends Activity implements Callback {

	private PayTask mAuthTask = null;
	private OrderTask orderTask = null;
	private String ID;
	private String username;
	private String type;
	private String res;
	private String balance;
	private String msg;
	private Button payreturn;
	private Product myProduct;
	private Bitmap pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.e("test", "on create");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		final Button loginButton = (Button) findViewById(R.id.paybutton);
		payreturn = (Button) findViewById(R.id.payreturn);

		ID = getIntent().getExtras().getString("ID");
		// username = getIntent().getExtras().getString("username");
		CGCApp appState = ((CGCApp) getApplicationContext());
		username = appState.getUsername();
		Log.e("username", username);
		type = getIntent().getExtras().getString("type");
		Order();
		loginButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("test", "on nfc");
				Money();
			}
		});

		payreturn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				PayActivity.this.finish();
			}
		});

	}

	public void Order() {

		orderTask = new OrderTask(username, ID, type);
		orderTask.execute((Void) null);
	}

	public class OrderTask extends AsyncTask<Void, Void, Boolean> {

		private final String username;
		private final String ID;
		private final String type;
		private static final String TAG = "OrderTask";

		OrderTask(String username, String ID, String type) {
			this.username = username;
			this.ID = ID;
			this.type = type;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			String contentToPost = /* "isAndoid=True" + & */"type=" + type
					+ "&username=" + username + "&ID=" + ID;
			final String url = "http://192.168.137.1:8000/order/";

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
					JSONArray JSONlist = (JSONArray) new JSONTokener(res)
							.nextValue();

//					if (((JSONObject) JSONlist.get(0)).getJSONObject("state")
//							.equals("False")) {
//						return false;
//					}
					for (int i = 0; i < JSONlist.length() && i < 15; i++) {
						JSONObject tmpJSON = (JSONObject) JSONlist.get(i);
						JSONObject field = tmpJSON.getJSONObject("fields");
						myProduct = new Product(field.getString("Keyword"),
								field.getString("Picture"),
								field.getString("Title"),
								field.getDouble("Price"),
								field.getString("Category"),
								field.getString("href"), tmpJSON.getInt("pk"));
					}
					pic = getBitmap(myProduct.productImage);

					Log.e("json", myProduct.title);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return false;
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
			orderTask = null;
			Log.e("returnstate", success.toString());
			if (!success)
				Toast.makeText(PayActivity.this, "获取订单详情失败", Toast.LENGTH_LONG)
						.show();
			else {
				TextView titleText = (TextView) findViewById(R.id.titleText);
				ImageView imageview = (ImageView) findViewById(R.id.productbigimage);
				TextView producttitle = (TextView) findViewById(R.id.producttitle);
				TextView productdes = (TextView) findViewById(R.id.productdes);
				TextView productprice = (TextView) findViewById(R.id.productprice);
				TextView sellername = (TextView) findViewById(R.id.sellername);
				TextView sellerphone = (TextView) findViewById(R.id.sellerphone);

				titleText.setText(myProduct.title);
				imageview.setImageBitmap(pic);
				producttitle.setText(myProduct.title);
				productdes.setText(myProduct.description);
				productprice.setText("价格：\n         ￥" + myProduct.price);
				sellername.setText("商品来源： " + myProduct.seller);
				sellerphone.setText("商品网址： " + myProduct.phone);
			}

		}

		@Override
		protected void onCancelled() {
			orderTask = null;
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

				PayActivity.this.finish();
			} else {
				Toast.makeText(PayActivity.this, msg, Toast.LENGTH_LONG).show();
			}
			// PayActivity.this.finish();
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

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	public Bitmap getBitmap(String s) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(s);
			bitmap = BitmapFactory.decodeStream(url.openStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("img", e.toString());
			e.printStackTrace();
		}

		return bitmap;
	}

}
