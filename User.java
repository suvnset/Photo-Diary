
public class User {
	private String user;
	private String pass;
	
	User(){
		user = "";
		pass = "";
	}
	
	User(String u, String p){
		user = u;
		pass = p;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPass() {
		return pass;
	}
}
