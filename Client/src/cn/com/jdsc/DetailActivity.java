package cn.com.jdsc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {
	private Product selectedProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_detail);
		@SuppressWarnings("unchecked")
		List<Product> catalog = (List<Product>) getIntent().getExtras()
				.getSerializable("list");
		int index = getIntent().getExtras().getInt("index");
		selectedProduct = catalog.get(index);
		TextView titleText = (TextView) findViewById(R.id.titleText);
		ImageView imageview = (ImageView) findViewById(R.id.productbigimage);
		TextView producttitle = (TextView) findViewById(R.id.producttitle);
		TextView productdes = (TextView) findViewById(R.id.productdes);
		TextView productprice = (TextView) findViewById(R.id.productprice);
		TextView sellername = (TextView) findViewById(R.id.sellername);
		TextView sellerphone = (TextView) findViewById(R.id.sellerphone);

		titleText.setText(selectedProduct.title);
		Bitmap img = getBitmap(selectedProduct.productImage);
		imageview.setImageBitmap(img);
		producttitle.setText(selectedProduct.title);
		productdes.setText(selectedProduct.description);
		productprice.setText("价格：\n            ￥" + selectedProduct.price);
		sellername.setText("商品来源： " + selectedProduct.seller);
		sellerphone.setText("商品网址： " + selectedProduct.phone);

		Button reservebutton = (Button) findViewById(R.id.reserve);
		reservebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ReserveTask().execute();
			}
		});
	}

	private class ReserveTask extends AsyncTask<Void, Void, String> {
		private String username = "324159";
		private final String ip = "192.168.137.1:8000";
		private String URL = "http://" + ip + "/reserve/";
		private final String TAG = "ReserveTask";
		@Override
		protected String doInBackground(Void... params) {
			HttpURLConnection httpUrlConnection = null;
			String data="";
			try{
				URL=URL+"?type=android&username="+username+"&pid="+selectedProduct.ID;
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
		
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("True")){
				Toast.makeText(getApplicationContext(), "预订成功", Toast.LENGTH_LONG).show();
				Button reservebutton = (Button) findViewById(R.id.reserve);
				reservebutton.setText("已预订");
				reservebutton.setClickable(false);
			}
			else{
				Toast.makeText(getApplicationContext(), "预订失败", Toast.LENGTH_LONG).show();
			}
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

	public Bitmap getBitmap(String s) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(s);
			bitmap = BitmapFactory.decodeStream(url.openStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}

	public void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

}
