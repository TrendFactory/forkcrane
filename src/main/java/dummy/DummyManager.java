package dummy;

import io.rebot.forkcrane.crawler.HttpCrane;
import io.rebot.forkcrane.domain.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class DummyManager {
	
	public static List<String> getDummyIDs(String FileURL) throws Exception{
		List<String> ids = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(FileURL)));
		String line = "";
		
		while ( (line=br.readLine()) != null ){
			//System.out.println(line);
			ids.add(line);
		}
		
		br.close();
		
		return ids;
	}
	
	public static User getDummyUser(String id) throws IOException, JSONException {
		System.out.println("getDummyUser() " + id);
		
		String url = "https://api.github.com/users/" + id + "";//"eb0f80a284e93b63255b6b8f1386d62931756dd3";

		String jsonStr = HttpCrane.getGithubPage(url);
		JSONObject obj = new JSONObject(jsonStr);
		System.out.println(obj.toString());
		JSONObject dummyObj = new JSONObject();
		
		dummyObj.put("githubUniqId", "" + obj.get("id"));
		dummyObj.put("name", obj.get("login"));
		dummyObj.put("provider", "dummy");
		
		JSONObject github = new JSONObject();
		github.put("accessToken", "dummy");
		github.put("updated_at", obj.get("updated_at"));
		github.put("created_at", obj.get("created_at"));
		github.put("following", obj.get("following"));
		github.put("followers", obj.get("followers"));
		github.put("public_gists", obj.get("public_gists"));
		github.put("repos_url", obj.get("repos_url"));
		github.put("email", "dummy@dummy.com");
		github.put("location", "Dummy House");
		github.put("blog", "http://127.0.0.1");
		github.put("company", "Dummy co");
		github.put("avatar_url", obj.get("avatar_url"));
		github.put("repos_url", obj.get("repos_url"));
		github.put("organizations_url", obj.get("organizations_url"));
		github.put("starred_url", obj.get("starred_url"));
		github.put("following_url", obj.get("following_url"));
		github.put("followers_url", obj.get("followers_url"));
		
		dummyObj.put("github", github);
		
		User user = new User((DBObject) JSON.parse(dummyObj.toString()));
		user.setGithub();
		
		return user;
	}
}
