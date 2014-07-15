package net.loonggg.fragment;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AccountLineGraph extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_line_graph);
		
		//set image view
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.graph_frame);
		

		//set line graph view
		CGCApp appState = ((CGCApp) getApplicationContext());
		ArrayList<Double> balanceList = appState.getBalanceList();
		final int num = balanceList.size();
		GraphViewData[] data = new GraphViewData[num];
		double v=0;
		//set data
		for (int i=0; i<num; i++) {
		  v += 0.2;
		  data[i] = new GraphViewData(i, balanceList.get(i));
		}
		GraphView graphView = new LineGraphView(
		    this
		    , "个人账户统计图"
		);
		// add data
		graphView.addSeries(new GraphViewSeries(data));
		// set view port, start=2, size=40
		graphView.setViewPort(0, 8);
		graphView.setScrollable(true);
		// optional - activate scaling / zooming
		graphView.setScalable(true);
		final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		
		Date d = new Date();  
        final Calendar now = Calendar.getInstance();       
        now.setTime(d); 
        final int today = now.get(Calendar.DAY_OF_YEAR);
		graphView.setCustomLabelFormatter(new CustomLabelFormatter() 
	    {
	        @Override
	        public String formatLabel(double value, boolean isValueX) 
	        {
	            if (isValueX)
	            {
	            	
	    	        Log.e("format", "today:" + today + "value:" + (long)value + "num:"+num);
	            	now.set(Calendar.DAY_OF_YEAR, today + (int)value - num + 1);
	            	//Log.e("format", "day:" + (long)value);
	            	Date date = new Date((long) now.getTimeInMillis());
	            	Log.e("format", "day:" + (int)value);
	            	return sdf.format(date).toString();
	               
	            }
	            return null; // let graphview generate Y-axis label for us
	        }
	    });
		relativeLayout.addView(graphView);				
	}
	
}
