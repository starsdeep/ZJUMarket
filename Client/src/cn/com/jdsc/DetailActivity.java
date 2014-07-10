package cn.com.jdsc;

import java.net.URL;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_detail);
		Log.e("DeStart", "go");
		@SuppressWarnings("unchecked")
		List<Product> catalog=(List<Product>)getIntent().getExtras().getSerializable("list");
		int index=getIntent().getExtras().getInt("index");
		final Product selectedProduct=catalog.get(index);
		Log.e("DeGetproduct", "go");
		TextView titleText=(TextView)findViewById(R.id.titleText);
		ImageView imageview=(ImageView)findViewById(R.id.productbigimage);
		TextView producttitle=(TextView)findViewById(R.id.producttitle);
		TextView productdes=(TextView)findViewById(R.id.productdes);
		TextView productprice=(TextView)findViewById(R.id.productprice);
		TextView sellername=(TextView)findViewById(R.id.sellername);
		TextView sellerphone=(TextView)findViewById(R.id.sellerphone);
		
		titleText.setText(selectedProduct.title);
		Bitmap img=getBitmap(selectedProduct.productImage);
		imageview.setImageBitmap(img);
		producttitle.setText(selectedProduct.title);
		productdes.setText(selectedProduct.description);
		productprice.setText("价格：\n            ￥"+selectedProduct.price);
		sellername.setText("商家姓名： "+selectedProduct.seller);
		sellerphone.setText("联系电话： "+selectedProduct.phone);
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
            e.printStackTrace();  
        }  
          
        return bitmap;  
    }
	public void onResume(){
   		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
   			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
   			}
   		super.onResume();
   		}

}
