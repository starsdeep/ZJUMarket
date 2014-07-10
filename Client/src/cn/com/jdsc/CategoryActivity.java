package cn.com.jdsc;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class CategoryActivity  extends Activity{
  @Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_category);
}
  
  
  public void onResume(){
  	if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
  		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  	}
  	super.onResume();
  }
}
