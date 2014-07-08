package cn.com.jdsc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HomeActivity extends Activity {
	private List<Product> mProductList;
	private String key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home);
		mProductList = new Vector<Product>();
		key = "";
		new cata1GetTask().execute();

	}

	private class cata1GetTask extends AsyncTask<Void, Void, String> {
		private final String TAG = "cataGetTask";
		private final String ip = "192.168.137.208";
		private final String URL = "http://" + ip + "/search";

		@Override
		protected String doInBackground(Void... params) {
			String data = "";
			HttpURLConnection httpUrlConnection = null;
			try {
				httpUrlConnection = (HttpURLConnection) new URL(URL)
						.openConnection();
				httpUrlConnection.setRequestMethod("POST");
				httpUrlConnection.setDoOutput(true);
				String postcon = "key=" + key;
				byte[] bypes = postcon.getBytes();
				httpUrlConnection.getOutputStream().write(bypes);
				InputStream inStream = httpUrlConnection.getInputStream();
				data = readStream(inStream);
			} catch (MalformedURLException e) {
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
			ListView listViewCatalog = (ListView) findViewById(R.id.mainlist);
			try {
				JSONTokener JSONlist = new JSONTokener(result);
				while(JSONlist.more()){
					JSONObject tmpJSON=(JSONObject) JSONlist.nextValue();
					JSONObject field=tmpJSON.getJSONObject("field");
					String title=field.getString("title");
					String des=field.getString("description");
					double price=field.getDouble("price");
					String img=field.getString("img");
					String seller=field.getString("seller");
					String phone=field.getString("phone");
					mProductList.add(new Product(title, img, des, price, seller, phone));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listViewCatalog.setAdapter(new ProductAdapter(mProductList,
					getLayoutInflater()));
			listViewCatalog.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent productDetailsIntent = new Intent(getBaseContext(),
							DetailActivity.class);
					productDetailsIntent.putExtra("index", position);
					productDetailsIntent.putExtra("list", (Serializable)mProductList);
					startActivity(productDetailsIntent);
				}
			});
		}

		private String readStream(InputStream in) {
			BufferedReader reader = null;
			StringBuffer data = new StringBuffer("");
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					data.append(line);
				}
			} catch (IOException e) {
				Log.e("getcata", "IOException");
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return data.toString();
		}
	}

	public void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}
}
