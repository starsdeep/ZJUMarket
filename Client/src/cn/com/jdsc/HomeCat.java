package cn.com.jdsc;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Vector;
import java.net.URL;
import org.json.*;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
public class HomeCat {
	private static List<Product> catalog;
	private String key;
	public List<Product> getCatalog(Resources res,String key1){
		catalog=new Vector<Product>();
		key=key1;
		new cataGetTask().execute();
		
		return catalog;
	}
	private class cataGetTask extends AsyncTask<Void,Void,String>{
		private final String TAG = "cataGetTask";
		private final String ip="192.168.137.208";
		private final String URL="http://"+ip+"/search";
		@Override
		protected String doInBackground(Void... params) {
			String data="";
			HttpURLConnection httpUrlConnection=null;
			
			try{
				httpUrlConnection=(HttpURLConnection)new URL(URL).openConnection();
				httpUrlConnection.setRequestMethod("POST");
				httpUrlConnection.setDoOutput(true);
				String postcon="key="+key;
				byte[] bypes = postcon.getBytes();
				httpUrlConnection.getOutputStream().write(bypes);
				InputStream inStream=httpUrlConnection.getInputStream();
				data=readStream(inStream);
			}catch(MalformedURLException e){
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
		protected void onPostExecute(String result){
			
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
			return data.toString();
		}
	}
}
