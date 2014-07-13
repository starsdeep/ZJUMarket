package net.loonggg.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LeftFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_fragment, null);
		
		RelativeLayout ucenterLayout = (RelativeLayout) view.findViewById(R.id.ucenter_Layout);
		LinearLayout homeLayout = (LinearLayout) view.findViewById(R.id.homeLayout);
		LinearLayout searchLayout = (LinearLayout) view.findViewById(R.id.searchLayout);
		LinearLayout accountLayout = (LinearLayout) view.findViewById(R.id.accountLayout);
		LinearLayout recordLayout = (LinearLayout) view.findViewById(R.id.recordLayout);
		//LinearLayout nfcLayout = (LinearLayout) view.findViewById(R.id.nfcLayout);
		//LinearLayout picLayout = (LinearLayout) view.findViewById(R.id.picLayout);
		//LinearLayout settingLayout = (LinearLayout) view.findViewById(R.id.settingLayout);
		
		
		
		ucenterLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.center_frame, new UcenterFragment());
				ft.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});

//		homeLayout.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
//				ft.replace(R.id.center_frame, new homeFragment());
//				ft.commit();
//				((MainActivity) getActivity()).showLeft();
//			}
//		});
//		
//		
//		searchLayout.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
//				ft.replace(R.id.center_frame, new searchFragment());
//				ft.commit();
//				((MainActivity) getActivity()).showLeft();
//			}
//		});
//		
//		accountLayout.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
//				ft.replace(R.id.center_frame, new searchFragment());
//				ft.commit();
//				((MainActivity) getActivity()).showLeft();
//			}
//		});
//		
//		accountLayout.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
//				ft.replace(R.id.center_frame, new accountFragment());
//				ft.commit();
//				((MainActivity) getActivity()).showLeft();
//			}
//		});
//		
//		recordLayout.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
//				ft.replace(R.id.center_frame, new recordFragment());
//				ft.commit();
//				((MainActivity) getActivity()).showLeft();
//			}
//		});
//		
//		nfcLayout.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
//				ft.replace(R.id.center_frame, new nfcFragment());
//				ft.commit();
//				((MainActivity) getActivity()).showLeft();
//			}
//		});
		
		//nfc activity
		//pic activtiy
		//setting
										
			
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
