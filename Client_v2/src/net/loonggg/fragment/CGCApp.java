package net.loonggg.fragment;

import android.app.Application;
import android.util.Log;

public class CGCApp extends Application{

	private boolean islogin = false;
	private String username = "";
	private String password ="" ;
	private String email = "";
	private String address = "";
	private String balance = "";
	private String recordCount = "";
	private String historyCount = "";
	
	
	
	public void setLoginState(boolean b) {
        this.islogin = b;
    }
	
	public boolean getLoginState() {
        return this.islogin;
    }
	
	public void setUsername(String u) {
        this.username = u;
    }
	
	public String getUsername() {
        return this.username;
    }
	
	public void setPassword(String u) {
        this.password = u;
    }
	
	public String getPassword() {
        return this.password;
    }
	
	public void setEmail(String u) {
        this.email = u;
    }
	
	public String getEmail() {
        return this.email;
    }
	
	public void setAddress(String u) {
        this.address = u;
    }
	
	public String getAddress() {
        return this.address;
    }
	
	public void setBalance(String u) {
        this.balance = u;
    }
	
	public String getBalance() {
        return this.balance;
    }
	
	public void setRecordCount(String u) {
        this.recordCount = u;
    }
	
	public String getRecordCount() {
        return this.recordCount;
    }
	
	public void setHistoryCount(String u) {
        this.historyCount = u;
    }
	
	public String getHistoryCount() {
        return this.historyCount;
    }
	
	public void printState(){
		Log.e("appState", "&username:" + username + "&email:" + email);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
