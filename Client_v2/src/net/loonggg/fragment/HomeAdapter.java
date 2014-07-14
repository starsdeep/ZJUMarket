package net.loonggg.fragment;

import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {
	private Context context;
	private String[] key;
	private List<List> homelist;
	private List<List> photolist;
	private LayoutInflater mInflater;

	public HomeAdapter(List<List> homelist, List<List> photolist,
			LayoutInflater mInflater, String[] key, Context context) {
		this.key = new String[6];
		this.key[0]="手机数码";
		this.key[1]="男女服装";
		this.key[2]="图书影音";
		this.key[3]="鞋类箱包";
		this.key[4]="家用电器";
		this.key[5]="零食特产";
		this.homelist = homelist;
		this.photolist = photolist;
		this.mInflater = mInflater;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return homelist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return homelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewItem item;
		convertView = mInflater.inflate(R.layout.home_item, null);
		item = new ViewItem();
		item.listtitle = (TextView) convertView.findViewById(R.id.listtitle);
		item.itemtext[0] = (TextView) convertView.findViewById(R.id.item1text);
		item.itemtext[1] = (TextView) convertView.findViewById(R.id.item2text);
		item.itemtext[2] = (TextView) convertView.findViewById(R.id.item3text);
		item.itemimg[0] = (ImageView) convertView.findViewById(R.id.item1img);
		item.itemimg[1] = (ImageView) convertView.findViewById(R.id.item2img);
		item.itemimg[2] = (ImageView) convertView.findViewById(R.id.item3img);
		item.itemprice[0] = (TextView) convertView
				.findViewById(R.id.item1price);
		item.itemprice[1] = (TextView) convertView
				.findViewById(R.id.item2price);
		item.itemprice[2] = (TextView) convertView
				.findViewById(R.id.item3price);
		item.item[0] = (RelativeLayout) convertView.findViewById(R.id.item1);
		item.item[1] = (RelativeLayout) convertView.findViewById(R.id.item2);
		item.item[2] = (RelativeLayout) convertView.findViewById(R.id.item3);
		convertView.setTag(item);
		final List<Product> curlist = homelist.get(position);
		final List<Bitmap> curphotolist = photolist.get(position);
		item.listtitle.setText(key[position]);
		for (int i = 0; i < 3; i++) {
			item.itemtext[i].setText(curlist.get(i).description);
			item.itemimg[i].setImageBitmap(curphotolist.get(i));
			item.itemprice[i].setText("￥" + curlist.get(i).price);
			
		}
		
		
		//以下内容因为intent需要final所以重复3次。。。
		item.item[0].setOnClickListener(new View.OnClickListener() {
			final Bitmap tmpBitmap = curphotolist.get(0);
			final 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent productDetailsIntent = new Intent(context,
						DetailActivity.class);
				productDetailsIntent.putExtra("index", 0);
				productDetailsIntent.putExtra("list",
						(Serializable) curlist);
				productDetailsIntent.putExtra("bitmap", tmpBitmap);
				context.startActivity(productDetailsIntent);
			}
		});
		item.item[1].setOnClickListener(new View.OnClickListener() {
			final Bitmap tmpBitmap = curphotolist.get(1);
			final 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent productDetailsIntent = new Intent(context,
						DetailActivity.class);
				productDetailsIntent.putExtra("index", 1);
				productDetailsIntent.putExtra("list",
						(Serializable) curlist);
				productDetailsIntent.putExtra("bitmap", tmpBitmap);
				context.startActivity(productDetailsIntent);
			}
		});
		
		item.item[2].setOnClickListener(new View.OnClickListener() {
			final Bitmap tmpBitmap = curphotolist.get(2);
			final 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent productDetailsIntent = new Intent(context,
						DetailActivity.class);
				productDetailsIntent.putExtra("index", 2);
				productDetailsIntent.putExtra("list",
						(Serializable) curlist);
				productDetailsIntent.putExtra("bitmap", tmpBitmap);
				context.startActivity(productDetailsIntent);
			}
		});
		
		return convertView;
	}

	private class ViewItem {
		TextView listtitle;
		TextView[] itemtext;
		ImageView[] itemimg;
		TextView[] itemprice;
		RelativeLayout[] item;

		public ViewItem(){
			  itemtext=new TextView[3];
			  itemimg=new ImageView[3];
			  itemprice=new TextView[3];
			  item=new RelativeLayout[3];
		  }
	}
}
