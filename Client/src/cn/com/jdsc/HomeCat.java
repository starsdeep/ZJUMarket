package cn.com.jdsc;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Vector;
import java.net.URL;
import org.json.*;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
public class HomeCat {
	private static List<Product> catalog;
	public static List<Product> getCatalog(Resources res){
		catalog=new Vector<Product>();
		for(int i=0;i<20;i++){
			catalog.add(new Product("�������г�", "http://g.search.alicdn.com/img/bao/uploaded/i4/i1/T1qTO0FPlcXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "�������ľų����������г������Ⱥ��Ⱥ��Ȱ�������Զ���ҵ�СѽСƻ��", 700, "С��", "4008208820",1));
		}
		return catalog;
	}
}
