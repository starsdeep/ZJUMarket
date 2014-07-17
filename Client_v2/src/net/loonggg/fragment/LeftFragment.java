package net.loonggg.fragment;

import com.zxing.activity.CaptureActivity;

import net.loonggg.fragment.CGCApp.State;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LeftFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_fragment, null);
		ImageView userimg = (ImageView)view.findViewById(R.id.siderbar_userimg);
		TextView username = (TextView)view.findViewById(R.id.siderbar_username);
		RelativeLayout ucenterLayout = (RelativeLayout) view
				.findViewById(R.id.ucenter_Layout);
		LinearLayout homeLayout = (LinearLayout) view
				.findViewById(R.id.homeLayout);
		LinearLayout searchLayout = (LinearLayout) view
				.findViewById(R.id.searchLayout);
		LinearLayout accountLayout = (LinearLayout) view
				.findViewById(R.id.accountLayout);
		LinearLayout recordLayout = (LinearLayout) view
				.findViewById(R.id.recordLayout);
		LinearLayout nfcLayout = (LinearLayout) view
				.findViewById(R.id.nfcLayout);
		LinearLayout picLayout = (LinearLayout) view
				.findViewById(R.id.picLayout);
		// LinearLayout settingLayout = (LinearLayout)
		// view.findViewById(R.id.settingLayout);
		
		CGCApp appState = ((CGCApp) getActivity().getApplicationContext());
		if(appState.getLoginState() == false){
			userimg.setImageResource(R.drawable.not_login);
			username.setText("未登录");
			
			ucenterLayout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
					ft.replace(R.id.center_frame, new LoginFragment());
					ft.commit();
					((MainActivity) getActivity()).showLeft();
				}
			});
		}
		else{
			userimg.setImageResource(R.drawable.user);
			username.setText(appState.getUsername());
			
			ucenterLayout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
					ft.replace(R.id.center_frame, new UcenterFragment());
					ft.commit();
					((MainActivity) getActivity()).showLeft();
				}
			});
		}
		
//		ucenterLayout.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				FragmentTransaction ft = getActivity().getFragmentManager()
//						.beginTransaction();
//				ft.replace(R.id.center_frame, new UcenterFragment());
//				ft.commit();
//				((MainActivity) getActivity()).showLeft();
//			}
//		});

		homeLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.center_frame, new HomeFragment());
				ft.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});
		//
		//
		searchLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.center_frame, new SearchFragment());
				ft.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});

		accountLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.center_frame, new AccountFragment());
				ft.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});
		//
		// accountLayout.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// FragmentTransaction ft =
		// getActivity().getFragmentManager().beginTransaction();
		// ft.replace(R.id.center_frame, new accountFragment());
		// ft.commit();
		// ((MainActivity) getActivity()).showLeft();
		// }
		// });
		//
		recordLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.center_frame, new RecordFragment());
				ft.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});
		//
		nfcLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CGCApp appState = ((CGCApp) getActivity()
						.getApplicationContext());
				Boolean isin = appState.getLoginState();
				((MainActivity) getActivity()).showLeft();
				if (!isin) {
					AlertDialog.Builder builder = new Builder(getActivity());
					builder.setTitle("请登录");
					builder.setMessage("您还未登录，请先登录！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Fragment newFragment = new LoginFragment();
									FragmentTransaction transaction = getFragmentManager()
											.beginTransaction();
									transaction.replace(R.id.center_frame,
											newFragment);
									transaction.commit();
								}
							});
					AlertDialog a = builder.create();
					a.setCanceledOnTouchOutside(false);
					a.show();
				} else {
					Intent intent = new Intent(getActivity(), NFCActivity.class);
					startActivity(intent);
				}
			}
		});

		picLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CGCApp appState = ((CGCApp) getActivity()
						.getApplicationContext());
				Boolean isin = appState.getLoginState();
				((MainActivity) getActivity()).showLeft();
				if (!isin) {
					AlertDialog.Builder builder = new Builder(getActivity());
					builder.setTitle("请登录");
					builder.setMessage("您还未登录，请先登录！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Fragment newFragment = new LoginFragment();
									FragmentTransaction transaction = getFragmentManager()
											.beginTransaction();
									transaction.replace(R.id.center_frame,
											newFragment);
									transaction.commit();
								}
							});
					AlertDialog a = builder.create();
					a.setCanceledOnTouchOutside(false);
					a.show();
				} else {
					Intent intent = new Intent(getActivity(),
							CaptureActivity.class);
					startActivity(intent);
				}
			}
		});
		// nfc activity
		// pic activtiy
		// setting

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
