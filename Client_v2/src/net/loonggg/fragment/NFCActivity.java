package cn.com.jdsc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NFCActivity extends Activity {
	private NfcAdapter nfcAdapter = null;
	private Intent nfcIntent = null;
	private PendingIntent pi = null;
	private IntentFilter tagDetected = null;
	private TextView promt = null;
	private boolean isNFC_support = false;
	private DrawView image;
	private Animation rotate;
	private String metaInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc);
		promt = (TextView) findViewById(R.id.promt);

		promt.setText("等待NFC设备");

		image = (DrawView) findViewById(R.id.drawView1);
		Animation rotate = AnimationUtils.loadAnimation(this,
				R.anim.anim_rotate);
		image.startAnimation(rotate);

		isNFC_support = true;
		nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		String metaInfo = "";
		if (nfcAdapter == null) {
			metaInfo = "设备不支持NFC！";
			Toast.makeText(this, metaInfo, Toast.LENGTH_SHORT).show();
			isNFC_support = false;
		}
		if (!nfcAdapter.isEnabled()) {
			metaInfo = "请在系统设置中先启用NFC功能！";
			Toast.makeText(this, metaInfo, Toast.LENGTH_SHORT).show();
			isNFC_support = false;
		}

		if (isNFC_support == true) {
			this.init_NFC();
		} else {
			promt.setTextColor(Color.RED);
			promt.setText(metaInfo);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (isNFC_support == true) {
			stopNFC_Listener();
		}

		if (isNFC_support == false)
			return;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (isNFC_support == false)
			return;

		startNFC_Listener();

		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(this.getIntent()
				.getAction())) {
			// 处理该intent
			processIntent(this.getIntent());
		}
	}

	private Tag tagFromIntent;

	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	public void processIntent(Intent intent) {
		if (isNFC_support == false)
			return;

		// 取出封装在intent中的TAG
		tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		metaInfo = bytesToHexString(tagFromIntent.getId());
		Log.e("ID", metaInfo);

		// promt.setTextColor(Color.BLUE);
		Toast.makeText(NFCActivity.this, "匹配NFC成功", Toast.LENGTH_SHORT).show();
		image.clearAnimation();
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			;
		}

		Intent PayIntent = new Intent(NFCActivity.this, PayActivity.class);
		PayIntent.putExtra("buy", "true");
		PayIntent.putExtra("username", "324159");
		PayIntent.putExtra("ID", metaInfo);
		PayIntent.putExtra("type", "NFC");
		startActivity(PayIntent);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			processIntent(intent);
		}
	}

	private void startNFC_Listener() {
		nfcAdapter.enableForegroundDispatch(this, pi,
				new IntentFilter[] { tagDetected }, null);
	}

	private void stopNFC_Listener() {
		nfcAdapter.disableForegroundDispatch(this);
	}

	private void init_NFC() {
		nfcIntent = new Intent(getApplicationContext(), NFCActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pi = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
	}

	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();

		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.toUpperCase(Character.forDigit(
					(src[i] >>> 4) & 0x0F, 16));
			buffer[1] = Character.toUpperCase(Character.forDigit(src[i] & 0x0F,
					16));
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}
}
