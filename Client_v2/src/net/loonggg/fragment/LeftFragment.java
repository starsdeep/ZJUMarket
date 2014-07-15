package net.loonggg.fragment;

import com.zxing.activity.CaptureActivity;
import net.loonggg.fragment.CGCApp.State;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LeftFragment extends Fragment {
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 111) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			Log.e("res","ssss");
			Toast.makeText(getActivity(), "111", Toast.LENGTH_LONG).show();
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_fragment, null);

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

		ucenterLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.center_frame, new UcenterFragment());
				ft.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});

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
				Intent intent = new Intent(getActivity(), NFCActivity.class);
				startActivity(intent);
			}
		});

		picLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CaptureActivity.class);
				startActivity(intent);
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
