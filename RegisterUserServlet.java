import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/RegisterUserServlet")
public class RegisterUserServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		PrintWriter pw = response.getWriter();	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Gson gson = new Gson();
		
		// Create a new user with the JSON file and grab the username and password
		User user = new Gson().fromJson(request.getReader(), User.class);
		String username = user.getUser();
		String password = user.getPass();
		
		if (username == null || username.isBlank() || password == null || 
			password.isBlank()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "User information is missing.";
			
			pw.write(gson.toJson(error));
			pw.flush();
			
			return;
		}
		
		Integer userID = 0; // Connector.registerUser(username, password);
		if (userID == -1) { // Username is taken
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "Username is taken.";
			
			pw.write(gson.toJson(error));
			pw.flush();
		}
		else { // The user was successfully registered!
			response.setStatus(HttpServletResponse.SC_OK);
			pw.write(gson.toJson(userID));
			pw.flush();
		}
	}
}
