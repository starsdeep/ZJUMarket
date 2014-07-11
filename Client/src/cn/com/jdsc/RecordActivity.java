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
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RecordActivity extends Activity {
	private List<Product> RecordList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_record);
		RecordList=new Vector<Product>();
		new recTask().execute();
	}

	private class recTask extends AsyncTask<Void, Void, String> {
		private final String TAG = "recGetTask";
		private final String ip = "192.168.137.1:8000";
		private String URL = "http://" + ip + "/buy/";

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String data = "";
			HttpURLConnection httpUrlConnection = null;
			try {
				String username="324159";
				URL = URL + "?type=android&username=" + username;
				httpUrlConnection = (HttpURLConnection) new URL(URL)
						.openConnection();
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
		
		protected void onPostExecute(String result) {
			ListView reListView=(ListView)findViewById(R.id.recordlist);
			try{
				JSONTokener reTokener=new JSONTokener(result);
				JSONArray buylist=(JSONArray)reTokener.nextValue();
				for (int i=0;i<buylist.length();i++){
					JSONObject tmpJSON=(JSONObject)buylist.get(i);
					JSONObject field=tmpJSON.getJSONObject("fields");
					RecordList.add(new Product(field.getInt("PID"), field.getBoolean("isBuy")));
				}
				for(int i=0;i<RecordList.size();i++){
					JSONArray JSONlist = (JSONArray)reTokener.nextValue();
					JSONObject tmpJSON=(JSONObject)JSONlist.get(0);
					JSONObject field=tmpJSON.getJSONObject("fields");
					RecordList.get(i).setInfo(field.getString("Keyword"), field.getString("Picture"), field.getString("Title"), field.getDouble("Price"), field.getString("Category"), field.getString("href"));
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reListView.setAdapter(new RecordListAdapter(RecordList, getLayoutInflater()));
			reListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent productDetailsIntent = new Intent(getBaseContext(),
							DetailActivity.class);
					productDetailsIntent.putExtra("index", position);
					productDetailsIntent.putExtra("list", (Serializable)RecordList);
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
