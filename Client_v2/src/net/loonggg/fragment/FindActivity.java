package net.loonggg.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class FindActivity extends Activity {
	 //final private DrawView image = (DrawView)findViewById(R.id.drawView1);
	 //final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_find);
		
		final DrawView image = (DrawView)findViewById(R.id.drawView1);
		final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		 image.startAnimation(rotate);
		 
		 
		 //image.startAnimation(rotate);
		 
		// try{
		// Thread.sleep(10000);
		 //Intent intent = getIntent();
		 
		// intent.setClass(FindActivity.this,MainActivity.class);
		// startActivity(intent);
		
		 //}catch(InterruptedException e){
			 
	//	 }
	}
	
	public void start(){
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.find, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
}
