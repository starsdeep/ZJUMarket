package cn.com.jdsc;

import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter{
	private List<Product> mProductList;
	private LayoutInflater mInflater;
	public ProductAdapter(List<Product> list, LayoutInflater inflater) {
		  mProductList = list;
		  mInflater = inflater;
		 }
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mProductList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mProductList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewItem item;
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.main_item, null);
			item=new ViewItem();
			item.productImageView=(ImageView)convertView.findViewById(R.id.ImageViewItem);
			item.productTitle=(TextView) convertView.findViewById(R.id.TextViewItem);
			item.productPrice=(TextView) convertView.findViewById(R.id.textViewPrice);
			convertView.setTag(item);
		}
		else{
			item=(ViewItem) convertView.getTag();
		}
		Product curProduct=mProductList.get(position);
		Bitmap img=getBitmap(curProduct.productImage);
		item.productImageView.setImageBitmap(img);
		item.productTitle.setText(curProduct.title);
		item.productPrice.setText("гд"+curProduct.price);
		return convertView;
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
	private class ViewItem {
		  ImageView productImageView;
		  TextView productTitle;
		  TextView productPrice;
		 }
	
}
