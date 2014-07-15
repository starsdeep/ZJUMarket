package net.loonggg.fragment;

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

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HomeFragment extends Fragment {
	private List<List> Homelist;
	private List<List> photolist;
	private Boolean isopen = true;
	private String[] key;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_home, container, false);
		ImageView left = (ImageView) view.findViewById(R.id.iv_home_left);
		left.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).showLeft();
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		Homelist = new Vector<List>();
		photolist = new Vector<List>();
		new homeTask().execute();
	}

	private class homeTask extends AsyncTask<Void, Void, String> {
		private final String TAG = "cataGetTask";
		private final String ip = "192.168.137.1:8000";
		private String URL = "http://" + ip + "/search/";

		private String getresult(String key) {
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
				Log.e(TAG, "IOException");
				isopen = false;
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			return data;
		}

		private List<Product> token(String data) {
			List<Product> newlist = new Vector<Product>();
			try {
				JSONArray JSONlist = (JSONArray) new JSONTokener(data)
						.nextValue();
				for (int i = 0; i < 3; i++) {
					JSONObject tmpJSON = (JSONObject) JSONlist.get(i);
					JSONObject field = tmpJSON.getJSONObject("fields");
					newlist.add(new Product(field.getString("Keyword"), field
							.getString("Picture"), field.getString("Title"),
							field.getDouble("Price"), field
									.getString("Category"), field
									.getString("href"), tmpJSON.getInt("pk")));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return newlist;
		}

		private List<Bitmap> getbmplist(List<Product> mProductList) {
			List<Bitmap> newlist = new Vector<Bitmap>();
			for (int i = 0; i < mProductList.size(); i++) {
				newlist.add(getBitmap(mProductList.get(i).productImage));
			}
			return newlist;
		}

		@Override
		protected String doInBackground(Void... params) {
			key = new String[6];
			key[0] = "手机";
			key[1] = "衣服";
			key[2] = "图书";
			key[3] = "鞋";
			key[4] = "家电";
			key[5] = "零食";
			for (int i = 0; i < 6; i++) {
				Homelist.add(token(getresult(key[i])));
				photolist.add(getbmplist(Homelist.get(i)));
			}
			String data = "";
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			/*
			 * ProgressBar mainbar = (ProgressBar)
			 * getActivity().findViewById(R.id.HomeprogressBar);
			 * mainbar.setVisibility(View.GONE);
			 */
			ProgressBar homebar=(ProgressBar)getActivity().findViewById(R.id.homeprogressBar);
			homebar.setVisibility(View.GONE);
			
			if (!isopen) {
				Toast.makeText(getActivity().getApplicationContext(),
						"无法连接，请检查网络设置", Toast.LENGTH_LONG).show();
			}
			ListView listViewCatalog = (ListView) getActivity().findViewById(
					R.id.homelist);
			listViewCatalog.setVisibility(View.VISIBLE);
			listViewCatalog.setAdapter(new HomeAdapter(Homelist, photolist,
					getActivity().getLayoutInflater(), key, getActivity()));
			RelativeLayout[] iconlist = new RelativeLayout[6];
			iconlist[0] = (RelativeLayout) getActivity().findViewById(
					R.id.icon1);
			iconlist[1] = (RelativeLayout) getActivity().findViewById(
					R.id.icon2);
			iconlist[2] = (RelativeLayout) getActivity().findViewById(
					R.id.icon3);
			iconlist[3] = (RelativeLayout) getActivity().findViewById(
					R.id.icon4);
			iconlist[4] = (RelativeLayout) getActivity().findViewById(
					R.id.icon5);
			iconlist[5] = (RelativeLayout) getActivity().findViewById(
					R.id.icon6);

			iconlist[0].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment newFragment = new SearchFragment();
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.center_frame, newFragment);
					Bundle bundle = new Bundle();
					bundle.putString("key", key[0]);
					newFragment.setArguments(bundle);
					transaction.commit();
				}
			});

			iconlist[1].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment newFragment = new SearchFragment();
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.center_frame, newFragment);
					Bundle bundle = new Bundle();
					bundle.putString("key", key[1]);
					newFragment.setArguments(bundle);
					transaction.commit();
				}
			});
			
			iconlist[2].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment newFragment = new SearchFragment();
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.center_frame, newFragment);
					Bundle bundle = new Bundle();
					bundle.putString("key", key[2]);
					newFragment.setArguments(bundle);
					transaction.commit();
				}
			});
			
			iconlist[3].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment newFragment = new SearchFragment();
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.center_frame, newFragment);
					Bundle bundle = new Bundle();
					bundle.putString("key", key[3]);
					newFragment.setArguments(bundle);
					transaction.commit();
				}
			});
			
			iconlist[4].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment newFragment = new SearchFragment();
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.center_frame, newFragment);
					Bundle bundle = new Bundle();
					bundle.putString("key", key[4]);
					newFragment.setArguments(bundle);
					transaction.commit();
				}
			});
			
			iconlist[5].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Fragment newFragment = new SearchFragment();
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.center_frame, newFragment);
					Bundle bundle = new Bundle();
					bundle.putString("key", key[5]);
					newFragment.setArguments(bundle);
					transaction.commit();
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

}
