package net.loonggg.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

@SuppressLint("NewApi")
public class RecordFragment extends Fragment {
	private List<Product> RecordList;
	private List<Product> RecordList1;
	private List<Product> RecordList2;
	private List<Bitmap> photolist;
	private List<Bitmap> photolist1;
	private List<Bitmap> photolist2;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view=inflater.inflate(R.layout.main_record, container, false);
		ImageView left=(ImageView)view.findViewById(R.id.iv_record_left);
		CGCApp appState= ((CGCApp)getActivity().getApplicationContext());
		Boolean isin=appState.getLoginState();
		if(!isin){
			AlertDialog.Builder builder = new Builder(getActivity());
			builder.setTitle("请登录");
			builder.setMessage("您还未登录，请先登录！");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Fragment newFragment = new LoginFragment();
					FragmentTransaction transaction =getFragmentManager().beginTransaction();
					transaction.replace(R.id.center_frame, newFragment);
					transaction.commit();
				}
			});
			AlertDialog a=builder.create();
			a.setCanceledOnTouchOutside(false);
			a.show();
		}
		left.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).showLeft();
			}
		});
        return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		RecordList = new Vector<Product>();
		RecordList1 = new Vector<Product>();
		RecordList2 = new Vector<Product>();
		photolist=new Vector<Bitmap>();
		photolist1=new Vector<Bitmap>();
		photolist2=new Vector<Bitmap>();
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
				CGCApp appState= ((CGCApp)getActivity().getApplicationContext());
				String username=appState.getUsername();
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
			try {
				JSONTokener reTokener = new JSONTokener(data);
				JSONArray buylist = (JSONArray) reTokener.nextValue();
				for (int i = 0; i < buylist.length(); i++) {
					JSONObject tmpJSON = (JSONObject) buylist.get(i);
					JSONObject field = tmpJSON.getJSONObject("fields");
					RecordList.add(new Product(field.getInt("PID"), field
							.getBoolean("isBuy")));
				}
				for (int i = 0; i < RecordList.size(); i++) {
					JSONArray JSONlist = (JSONArray) reTokener.nextValue();
					JSONObject tmpJSON = (JSONObject) JSONlist.get(0);
					JSONObject field = tmpJSON.getJSONObject("fields");
					RecordList.get(i).setInfo(field.getString("Keyword"),
							field.getString("Picture"),
							field.getString("Title"), field.getDouble("Price"),
							field.getString("Category"),
							field.getString("href"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0;i<RecordList.size();i++){
				photolist.add(getBitmap(RecordList.get(i).productImage));
			}
			Iterator<Product> tmpit = RecordList.iterator();
			int i=0;
			while (tmpit.hasNext()) {
				Product tmppro = tmpit.next();
				if (tmppro.issold.equals(false)) {
					RecordList1.add(tmppro);
					photolist1.add(photolist.get(i));
				} else {
					RecordList2.add(tmppro);
					photolist2.add(photolist.get(i));
				}
				i++;
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			final ListView reListView = (ListView) getActivity().findViewById(R.id.recordlist);
			
			Spinner respin = (Spinner) getActivity().findViewById(R.id.respin);

			reListView.setAdapter(new RecordListAdapter(RecordList,photolist,
					getActivity().getLayoutInflater()));
			reListView.setVisibility(View.VISIBLE);
			ProgressBar recordbar = (ProgressBar) getActivity().findViewById(R.id.recordbar);
			recordbar.setVisibility(View.GONE);
			reListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent productDetailsIntent = new Intent(getActivity().getBaseContext(),
							DetailActivity.class);
					productDetailsIntent.putExtra("index", position);
					productDetailsIntent.putExtra("list",
							(Serializable) RecordList);
					productDetailsIntent.putExtra("bitmap",photolist.get(position));
					startActivity(productDetailsIntent);
				}
			});
			respin.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if (position == 0) {
						reListView.setAdapter(new RecordListAdapter(RecordList,photolist,
								getActivity().getLayoutInflater()));
						reListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								Intent productDetailsIntent = new Intent(getActivity().getBaseContext(),
										DetailActivity.class);
								productDetailsIntent.putExtra("index", position);
								productDetailsIntent.putExtra("list",
										(Serializable) RecordList);
								productDetailsIntent.putExtra("bitmap",photolist.get(position));
								startActivity(productDetailsIntent);
							}
						});
					}
					else if(position==1){
						reListView.setAdapter(new RecordListAdapter(RecordList1,photolist1,getActivity().getLayoutInflater()));
						reListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								Intent productDetailsIntent = new Intent(getActivity().getBaseContext(),
										DetailActivity.class);
								productDetailsIntent.putExtra("index", position);
								productDetailsIntent.putExtra("list",
										(Serializable) RecordList1);
								productDetailsIntent.putExtra("bitmap",photolist1.get(position));
								startActivity(productDetailsIntent);
							}
						});
					}
					else{
						reListView.setAdapter(new RecordListAdapter(RecordList2,photolist2,
								getActivity().getLayoutInflater()));
						reListView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								// TODO Auto-generated method stub
								Intent productDetailsIntent = new Intent(getActivity().getBaseContext(),
										DetailActivity.class);
								productDetailsIntent.putExtra("index", position);
								productDetailsIntent.putExtra("list",
										(Serializable) RecordList2);
								productDetailsIntent.putExtra("bitmap",photolist2.get(position));
								startActivity(productDetailsIntent);
							}
						});
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					reListView.setAdapter(new RecordListAdapter(RecordList,photolist,
							getActivity().getLayoutInflater()));
					reListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							Intent productDetailsIntent = new Intent(getActivity().getBaseContext(),
									DetailActivity.class);
							productDetailsIntent.putExtra("index", position);
							productDetailsIntent.putExtra("list",
									(Serializable) RecordList);
							productDetailsIntent.putExtra("bitmap",photolist.get(position));
							startActivity(productDetailsIntent);
						}
					});
				}
			});
		}
		public Bitmap getBitmap(String s)  
	    {  
	        Bitmap bitmap = null;  
	        try  
	        {
	        	URL url = new URL(s);
	            bitmap = BitmapFactory.decodeStream(url.openStream());  
	        } catch (Exception e)  
	        {  
	            // TODO Auto-generated catch block  
	        	Log.e("img",e.toString());
	            e.printStackTrace();  
	        }  
	          
	        return bitmap;  
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
}
