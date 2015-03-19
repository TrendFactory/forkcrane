package io.rebot.forkcrane.mongodb;

import io.rebot.forkcrane.domain.Constant;
import io.rebot.forkcrane.domain.User;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

public class MongoConnect{
	
	private MongoClient mongoClient = null;
	private DB db = null;
	
	public MongoConnect(){
		if(Constant.DEBUGING) System.out.println("MongoManager Cunstruct");
	}
	
	public void connetDB(String ip, int port, String dbnamee) throws UnknownHostException{
		
		mongoClient = new MongoClient(new ServerAddress(ip, port));
		db = mongoClient.getDB(dbnamee);
		if(Constant.DEBUGING) System.out.println("connetDB() : Connect to database successfully");
	}	
	
	public void setAuth(String user, String passwd){
		boolean auth = db.authenticate(user, passwd.toCharArray());
		if(Constant.DEBUGING) System.out.println("setAuth() : Authentication: "+auth);
	}
	
	public List<User> getUsers(String CollectionName){
		DBCollection coll = db.getCollection(CollectionName);
		if(Constant.DEBUGING) System.out.println("getUsers() : Collection " + CollectionName + " selected successfully");
		
		DBCursor cursor = coll.find();
        List<User> users = new ArrayList<User>();

		while(cursor.hasNext()){
			
			DBObject obj = cursor.next();
			if(Constant.DEBUGING) System.out.println("cursor : " + obj);
			try {
				User user = new User(obj);
				users.add(user);
			}
			catch(NullPointerException e) { continue; } 
		}
		return users;
	}
	
	public void saveDB(JSONObject json, String CollectionName) throws JSONException{
		if(Constant.DEBUGING) System.out.println("saveDB() : " + CollectionName);
		DBCollection coll = db.createCollection(CollectionName, null);
		
		// add Date.Now()
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		json.put("date", format.format(date));
		System.out.println(json.toString());
		
		// insert DB
		coll.insert((DBObject)JSON.parse(json.toString()));
	}
}
