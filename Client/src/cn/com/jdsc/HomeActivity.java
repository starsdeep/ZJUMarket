package cn.com.jdsc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private List<Product> mProductList;
	private String key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home);
		Toast.makeText(getApplicationContext(), "陈国成帮您网购，陈国成神马的萌萌哒！", Toast.LENGTH_LONG).show();
		/* mProductList=HomeCat.getCatalog(getResources()); */
		key = "";
		Button searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mProductList = new Vector<Product>();
				ProgressBar mainbar = (ProgressBar) findViewById(R.id.HomeprogressBar);
				mainbar.setVisibility(View.VISIBLE);
				EditText searchtext = (EditText) findViewById(R.id.searchcontent);
				if (getCurrentFocus() != null) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
				}
				TextView hid = (TextView) findViewById(R.id.hidwarn);
				key = searchtext.getText().toString();
				if (key.equals("")) {
					hid.setVisibility(View.VISIBLE);
				} else {
					hid.setVisibility(View.INVISIBLE);
					new cata1GetTask().execute();
				}
			}
		});
	}

	private class cata1GetTask extends AsyncTask<Void, Void, String> {
		private final String TAG = "cataGetTask";
		private final String ip = "192.168.137.1:8000";
		private String URL = "http://" + ip + "/search/";

		@Override
		protected String doInBackground(Void... params) {
			String data = "";
			HttpURLConnection httpUrlConnection = null;
			try {

				key = URLEncoder.encode(key, "utf-8");
				URL = URL + "?type=android&key=" + key;
				httpUrlConnection = (HttpURLConnection) new URL(URL)
						.openConnection();
				InputStream inStream = httpUrlConnection.getInputStream();
				data = readStream(inStream);
			} catch (MalformedURLException e) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Toast.makeText(getBaseContext(), "网络无法连通，请查看网络设置",
						Toast.LENGTH_LONG).show();
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			ProgressBar mainbar = (ProgressBar) findViewById(R.id.HomeprogressBar);
			mainbar.setVisibility(View.GONE);
			ListView listViewCatalog = (ListView) findViewById(R.id.mainlist);
			listViewCatalog.setVisibility(View.VISIBLE);
			try {
				JSONArray JSONlist = (JSONArray) new JSONTokener(result)
						.nextValue();
				for (int i = 0; i < JSONlist.length() && i < 30; i++) {
					JSONObject tmpJSON = (JSONObject) JSONlist.get(i);
					JSONObject field = tmpJSON.getJSONObject("fields");
					mProductList.add(new Product(field.getString("Keyword"),
							field.getString("Picture"), field
									.getString("Title"), field
									.getDouble("Price"), field
									.getString("Category"), field
									.getString("href"), tmpJSON.getInt("pk")));
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
					productDetailsIntent.putExtra("list",
							(Serializable) mProductList);
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

	@Override
	public void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}
}
