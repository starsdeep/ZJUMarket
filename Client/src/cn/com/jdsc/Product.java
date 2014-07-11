package cn.com.jdsc;

import java.io.Serializable;

public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int ID;
	public String title;
	public String productImage; 
	public String description;  
	public double price;
	public String seller;
	public String phone;
	public Boolean issold;
	public Product(String title, String productImage, String description,double price,int ID)
	{ 
		this.title = title;   
		this.productImage = productImage;  
		this.description = description;
		this.price=price;
		this.seller="Ð¡Æ»¹û";
		this.phone="13513531535";
		this.issold=false;
		this.ID=ID;
	}
	public Product(String title, String productImage, String description,double price,String seller,String number,int ID){
		this.title = title;   
		this.productImage = productImage;  
		this.description = description;
		this.price=price;
		this.seller=seller;
		this.phone=number;
		this.issold=false;
		this.ID=ID;
	}
	public Product(int ID,Boolean issold){
		this.title = "";   
		this.productImage = "";  
		this.description = "";
		this.price=0;
		this.seller="";
		this.phone="";
		this.issold=false;
		this.ID=ID;
	}
	public void setInfo(String title, String productImage, String description,double price,String seller,String number){
		this.title=title;
		this.productImage=productImage;
		this.description=description;
		this.price=price;
		this.seller=seller;
		this.phone=number;
	}
}
