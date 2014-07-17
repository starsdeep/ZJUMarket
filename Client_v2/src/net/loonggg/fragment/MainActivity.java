package net.loonggg.fragment;

import net.loonggg.view.SlidingMenu;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {
	private SlidingMenu mSlidingMenu;// 侧边栏的view
	private LeftFragment leftFragment; // 左侧边栏的碎片化view
	/*private RightFragment rightFragment; // 右侧边栏的碎片化view
*/	private LoadingFragment centerFragment;// 中间内容碎片化的view
	private FragmentTransaction ft; // 碎片化管理的事务
	private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		ft = this.getFragmentManager().beginTransaction();
		leftFragment = new LeftFragment();
/*		rightFragment = new RightFragment();*/
		ft.replace(R.id.left_frame, leftFragment);
		/*ft.replace(R.id.right_frame, rightFragment);*/

		centerFragment = new LoadingFragment();
		ft.replace(R.id.center_frame, centerFragment);
		ft.commit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}

	public void llronclick(View v) {
		switch (v.getId()) {
		case R.id.llr_energy_management:

			Intent intent = new Intent(this, DetailsActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	@Override
	public void onResume(){
    	if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
    		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	}
    	super.onResume();
    }
	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

}
