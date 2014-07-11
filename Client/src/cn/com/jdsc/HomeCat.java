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
			catalog.add(new Product("二手自行车", "http://g.search.alicdn.com/img/bao/uploaded/i4/i1/T1qTO0FPlcXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "超级棒的九成新永久自行车，好萌好萌好萌啊，你永远是我的小呀小苹果", 700, "小明", "4008208820",1));
		}
		return catalog;
	}
}
