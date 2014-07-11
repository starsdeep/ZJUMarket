package cn.com.jdsc;

import java.net.URL;
import java.util.Currency;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordListAdapter extends BaseAdapter {
	private List<Product> RecordList;
	private LayoutInflater mInflater;

	public RecordListAdapter(List<Product> list, LayoutInflater inflater) {
		RecordList = list;
		mInflater = inflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return RecordList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return RecordList.get(position);
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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.record_item, null);
			item = new ViewItem();
			item.cancel = (Button) convertView.findViewById(R.id.reCancel);
			item.recordImageView = (ImageView) convertView
					.findViewById(R.id.reImage);
			item.recordPrice = (TextView) convertView
					.findViewById(R.id.rePrice);
			item.recordState = (TextView) convertView
					.findViewById(R.id.reState);
			item.recordTitle = (TextView) convertView
					.findViewById(R.id.reTitle);
			convertView.setTag(item);
		} else {
			item = (ViewItem) convertView.getTag();
		}
		Product curProduct = RecordList.get(position);
		Bitmap img=getBitmap(curProduct.productImage);
		item.recordImageView.setImageBitmap(img);
		item.recordPrice.setText("￥"+curProduct.price);
		String state="";
		if(curProduct.issold){
			state="交易完成";
			item.cancel.setVisibility(View.GONE);
		}
		else{
			state="正在派送";
			}
		item.recordState.setText(state);
		item.recordTitle.setText(curProduct.description);
		return convertView;
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

	private class ViewItem {
		ImageView recordImageView;
		TextView recordTitle;
		TextView recordState;
		TextView recordPrice;
		Button cancel;
	}
}
