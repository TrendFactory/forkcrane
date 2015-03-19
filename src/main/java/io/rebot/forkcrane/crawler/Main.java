package io.rebot.forkcrane.crawler;

import io.rebot.forkcrane.domain.Constant;
import io.rebot.forkcrane.domain.Repository;
import io.rebot.forkcrane.domain.User;
import io.rebot.forkcrane.mongodb.MongoConnect;
import io.rebot.forkcrane.rating.TrendManager;

import java.util.List;

import org.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("JAVA Running..");
		MongoConnect conn = new MongoConnect();
		LanguageRank langRank = new LanguageRank();
		List<User> users = null;

		try{
			// 1. Mongo DB에서 User를 모두 가져온다
			conn.connetDB(Constant.IP, Constant.PORT, Constant.DB_NAME);
			conn.setAuth(Constant.USER, Constant.PASSWORD);
			users = conn.getUsers(Constant.COLLECTION_LOGIN_USERS);
	
	
			// 2. users Collection에 User정보 저장
			if(Constant.DEBUGING) System.out.print("[saving] users Collections..");
			for(User user : users){
				conn.saveDB(user.getJSON(), Constant.COLLECTION_USERS);
			}

			// 3. 각 user의 github정보 설정
			for(User user : users){
				if(Constant.DEBUGING) System.out.print("set Github");
				user.setGithub();
				if(Constant.DEBUGING) System.out.print("set Repo");
				user.setRepo();		// github에 총점을 계산해야 하므로 먼저 시행해야
				
				if(Constant.DEBUGING) System.out.print("[saving] Github Collection..");
				// 3-1. Github Collection 저장
				conn.saveDB(user.getGithub().getJSON(), Constant.COLLECTION_GITHUB);
				
				
				if(Constant.DEBUGING) System.out.print("[saving] Repo Collection..");
				for(Repository repo : user.getGithub().getRepos()){
					// 3-2. Repository Collection 저장
					conn.saveDB(repo.getJSON(), Constant.COLLECTION_REPOSITORY);
				}
				
				
				if(Constant.DEBUGING) System.out.print("set Events");
				// 4. Events Collection 저장
				user.setEvent();
				if(Constant.DEBUGING) System.out.print("[saving] Events Collection..");
				conn.saveDB(user.getGithub().getEventsJSON(), Constant.COLLECTION_EVENTS);
			}

			if(Constant.DEBUGING) System.out.print("set TrendLeader & LanguageRank");
			TrendManager trender = new TrendManager();
			trender.add(users);
			
			if(Constant.DEBUGING) System.out.print("[saving] LanguageRank Collection..");
			conn.saveDB(trender.getJSON_LanguageRank(), Constant.COLLECTION_LANGUAGE_RANK);
			
			if(Constant.DEBUGING) System.out.print("[saving] TrendLeader Collection..");
			conn.saveDB(trender.getJSON_TrendLeader(), Constant.COLLECTION_TRENDLEADER_RANK);

		}catch(Exception e){
			System.out.println("Main Exception : " + e.getMessage());
		}
		
		
//		System.out.println("JAVA Running..");
//		MongoConnect conn = new MongoConnect();
//		LanguageRank langRank = new LanguageRank();
//		List<User> users = null;
//		try {
//			
//			// 1. Mongo DB에서 User를 모두 가져온다
//			conn.connetDB(Constant.IP, Constant.PORT, Constant.DB_NAME);
//			conn.setAuth(Constant.USER, Constant.PASSWORD);
//			
//			// 2. 각 User의 Repositories를 모두 가져온다
//			users = conn.getUsers(Constant.COLLECTION_LOGIN_USERS);
//			
////			System.out.println("get Users...");
////			try {users = conn.getUsers(Constant.COLLECTION_DUMMY_USERS); }catch(Exception e){}
//			
//			
////			// 2-1 유저 및 Repo의 프로젝트/언어/영향력 출력
////			if(Constant.DEBUGING){
////				for(User user : users){
////					System.out.println(user.name + "(" + user.githubUniqId + ")");
////					for(int i=0; i<user.github.getRepos().size(); i++){
////						Repository repo = user.github.getRepos().get(i);
////						System.out.println("  " + (i+1) + " [" + repo.name + " / " + repo.language + "] " + repo.estimation);
////					}
////				}
////			}
//			
//			
//			for(User user : users){
//				
//				// 3. Users 정보를 통해 user, github
//				conn.saveDB(user.getJSON(), Constant.COLLECTION_USERS);
//				conn.saveDB(user.github.getJSON(), Constant.COLLECTION_GITHUB);
//				
////				System.out.println("save users...");
////				try {conn.saveDB(user.getJSON(), Constant.COLLECTION_USERS); }catch(Exception e){}
////				System.out.println("save github...");
////				try {conn.saveDB(user.github.getJSON(), Constant.COLLECTION_GITHUB); }catch(Exception e){}
//				
//				if(Constant.DEBUGING){
//					System.out.println("============================================================");
//					System.out.println(user.getJSON());
//					System.out.println(user.github.getJSON());
//				}
//				
//				// 3-1. repo정보 저장
//				for(Repository repo : user.github.getRepos()){
//					conn.saveDB(repo.getJSON(), Constant.COLLECTION_REPOSITORY);
//					
////					System.out.println("save repos...");
////					try{conn.saveDB(repo.getJSON(), Constant.COLLECTION_REPOSITORY);}catch(Exception e){}
//					if(Constant.DEBUGING) System.out.println(repo.getJSON());
//					
//					break;
//				}
//				break;
//			}
//			
//			// 4. Users의 모든 정보를 TrendManager에게 전달
//			// TreadLeader와 LanguageRank를 구함
//			if(Constant.DEBUGING) System.out.println("user count :" + users.size());
//			TrendManager trender = new TrendManager();
//			trender.add(users);
//			conn.saveDB(trender.getJSON_LanguageRank(), Constant.COLLECTION_LANGUAGE_RANK);
//			conn.saveDB(trender.getJSON_TrendLeader(), Constant.COLLECTION_TRENDLEADER_RANK);
//			
////			System.out.println("save language_rank...");
////			try{conn.saveDB(trender.getJSON_LanguageRank(), Constant.COLLECTION_LANGUAGE_RANK);}catch(Exception e){}
////			System.out.println("save trendleader_rank...");
////			try{conn.saveDB(trender.getJSON_TrendLeader(), Constant.COLLECTION_TRENDLEADER_RANK);}catch(Exception e){}
//			
//			
//			if(Constant.DEBUGING){
//				System.out.println("Language Rank");
//				System.out.println("  " + trender.getJSON_LanguageRank());
//				System.out.println("============================================================");
//				System.out.println("Trend Ldear");
//				System.out.println("  " + trender.getJSON_TrendLeader());
//			}
//			
//
//			// 5. Event 정보를 구함
//			for(User user : users){
//				System.out.println(user.getGithub().getEventsJSON());
//				conn.saveDB(user.getGithub().getEventsJSON(), Constant.COLLECTION_EVENTS);
//				
////				System.out.println("save user_events...");
////				try{conn.saveDB(user.getGithub().getEventsJSON(), Constant.COLLECTION_EVENTS);}catch(Exception e){}
//			}
//						
//		} catch (Exception e) { System.out.println(e.toString()); }
		
	}

}
