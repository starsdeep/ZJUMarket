package net.loonggg.fragment;


import java.util.ArrayList;
import java.util.Iterator;

import android.app.Application;
import android.util.Log;

public class CGCApp extends Application{

	State s = State.INIT;
	private boolean islogin = false;
	private String username = "";
	private String password ="" ;
	private String email = "";
	private String address = "";
	private String balance = "";
	private String recordCount = "";
	private String historyCount = "";
	private String totalMoney="";
	private String weekly_in = "";
	private String weekly_out = "";
	private ArrayList<Double> balanceList = new ArrayList<Double>(); 
	enum State {  
		INIT,UCENTER,  HOME, SEARCH, ACCOUNT, RECORD,HISTORY,NFC,PIC,SETTING
	}
	
	public ArrayList<Double> getBalanceList(){
		return this.balanceList;
	}
	
	public void printBalanceList(){
		Iterator it = balanceList.iterator();
        while(it.hasNext()){
            Log.e("in CGCApp", "" + it.next());
        }
		
	}
	
	
	public void setState(State s) {
        this.s = s;
    }
	
	public State getState() {
        return this.s;
    }
	
	
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
	
	public void setTotalMoney(String u) {
        this.totalMoney = u;
    }
	
	public String getTotalMoney() {
        return this.totalMoney;
    }
	
	public void setWeekIn(String u) {
        this.weekly_in = u;
    }
	
	public String getWeekIn() {
        return this.weekly_in;
    }
	
	public void setWeekOut(String u) {
        this.weekly_out = u;
    }
	
	public String getWeekOut() {
        return this.weekly_out;
    }
	
	
	
	
	public void printState(){
		Log.e("appState", "&username:" + username + "&email:" + email);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
