package net.loonggg.fragment;

import java.util.List;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordListAdapter extends BaseAdapter {
	private List<Product> RecordList;
	private LayoutInflater mInflater;
	private List<Bitmap> mPhotoList;
	public RecordListAdapter(List<Product> list,List<Bitmap> PhotoList, LayoutInflater inflater) {
		RecordList = list;
		mInflater = inflater;
		mPhotoList=PhotoList;
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

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewItem item;
		convertView = mInflater.inflate(R.layout.record_item, null);
		item = new ViewItem();
		item.cancel = (Button) convertView.findViewById(R.id.reCancel);
		item.recordImageView = (ImageView) convertView
				.findViewById(R.id.reImage);
		item.recordPrice = (TextView) convertView.findViewById(R.id.rePrice);
		item.recordState = (TextView) convertView.findViewById(R.id.reState);
		item.recordTitle = (TextView) convertView.findViewById(R.id.reTitle);
		convertView.setTag(item);
		Product curProduct = RecordList.get(position);
		Bitmap img = mPhotoList.get(position);
		item.recordImageView.setImageBitmap(img);
		item.recordPrice.setText("￥" + curProduct.price);
		String state = "";
		if (curProduct.issold) {
			state = "交易完成";
			item.cancel.setVisibility(View.GONE);
		} else {
			state = "正在派送";
		}
		item.recordState.setText(state);
		item.recordTitle.setText(curProduct.description);
		return convertView;
	}


	private class ViewItem {
		ImageView recordImageView;
		TextView recordTitle;
		TextView recordState;
		TextView recordPrice;
		Button cancel;
	}
}
