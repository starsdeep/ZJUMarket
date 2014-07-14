package net.loonggg.fragment;


import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ProductAdapter extends BaseAdapter{
	private List<Product> mProductList;
	private List<Bitmap> mphotoList;
	private LayoutInflater mInflater;
	public ProductAdapter(List<Product> list,List<Bitmap> photoList, LayoutInflater inflater) {
		  mProductList = list;
		  mInflater = inflater;
		  mphotoList=photoList;
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
		item.productImageView.setImageBitmap(mphotoList.get(position));
		item.productTitle.setText(curProduct.description);
		item.productPrice.setText("￥"+curProduct.price);
		return convertView;
	}
	
	private class ViewItem {
		  ImageView productImageView;
		  TextView productTitle;
		  TextView productPrice;
		 }
	
}
