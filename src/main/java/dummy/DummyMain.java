package dummy;

import io.rebot.forkcrane.domain.Constant;
import io.rebot.forkcrane.domain.User;
import io.rebot.forkcrane.mongodb.MongoConnect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class DummyMain {
	
	public static void main(String[] args) {
		System.out.println("Dummy Main Running..");
		MongoConnect conn = new MongoConnect();
		
		List<User> users = new ArrayList<User>();
		
		try{
			// 1. Mongo DB에서 User를 모두 가져온다
			conn.connetDB(Constant.IP, Constant.PORT, Constant.DB_NAME);
			conn.setAuth(Constant.USER, Constant.PASSWORD);
//			
			List<String> ids = DummyManager.getDummyIDs("src/main/resources/dummy.txt");
			for(String id : ids){
				User user = null;
				try {
					user = DummyManager.getDummyUser(id);
				}catch(Exception e)
				{ 
					System.out.println("Exception : " + e.getMessage());
					continue;
				}
				
				System.out.println(user.getJSON_addGithub_byDUMMY());
				conn.saveDB(user.getJSON_addGithub_byDUMMY(), Constant.COLLECTION_DUMMY_USERS);
			}
			
			// 1회 테스트
//			conn.saveDB(DummyManager.getDummyUser(ids.get(0)).getDUMMYJSON(), Constant.COLLECTION_DUMMY_USERS);
			
//			BufferedReader br = new BufferedReader(new FileReader(new File(FileURL)));
			 
			
//			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("src/main/resources/tmp1.txt")));
//			for(String id : ids){
//				String line = DummyManager.getDummyUser(id).getDUMMYJSON().toString();
//				System.out.println(line);
//				bw.write(line + "\n");
//			}
			
		} catch (Exception e) { System.out.println(e.toString()); }
	}
}
