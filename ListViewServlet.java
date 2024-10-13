import javax.servlet.annotation.WebServlet;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;

import com.google.gson.JsonArray;
import com.google.gson.*;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Given the logged in user
// Get it's friends list

// From the friend list, 
// Get the friend's posts
// Display back to server as a JSON file for now 

// MYSQL Stuff.
//private (just you) , public (anyone) , friends (just your friends)

@WebServlet("/ListViewServlet")
public class ListViewServlet extends HttpServlet {

	// *** INPUT ACCORDING TO WHO'S SQL WE ARE USING ***
	protected static final String jdbcurl = "";
	protected static final String username = "";
	protected static final String password = "";
	// *** INPUT ACCORDING TO WHO'S SQL WE ARE USING ***

	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) {
		// Get the logged in usersID
		// From our Users class, similar to assignment 4 I think
		// *** Need information from the JS when user is logged in ***
		String userIDParameter = request.getParameter("userID");
		int userID = -1;
		
		if (userIDParameter != null && !userIDParameter.isEmpty()) {
	        try {
	            userID = Integer.parseInt(userIDParameter);
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); // Handle parsing error
	        }
	    }

		JsonArray allPosts = new JsonArray();

		if (userID <= 1) {
			// It is a GUEST user.
			JsonArray publicPost = getPublicPost();
			allPosts.addAll(publicPost);
		} else {
			JsonArray publicAndPrivatePost = getPublicAndPrivatePost(userID);
			allPosts.addAll(publicAndPrivatePost);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			// Print the JSON arrays to the server as a string for now.
			PrintWriter out = response.getWriter();
			out.print(allPosts.getAsString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// For guest users/not logged in, can only see entries that are PUBLIC.
	public JsonArray getPublicPost() {
		JsonArray publicPost = new JsonArray();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;
		Statement statement = null;
		ResultSet resultset = null;

		try {
			connection = DriverManager.getConnection(jdbcurl, username, password);
			statement = connection.createStatement();

			// Searches for entries in the SQL database where privacy="public".
			resultset = statement.executeQuery("SELECT * FROM Entries WHERE privacy = " + "public");

			while (resultset.next()) {
				JsonObject entry = new JsonObject();

				entry.addProperty("id", resultset.getInt("id"));
				entry.addProperty("user", resultset.getString("user"));
				entry.addProperty("image", resultset.getString("image"));
				entry.addProperty("caption", resultset.getString("caption"));
				entry.addProperty("time", resultset.getTimestamp("time").toString());
				entry.addProperty("longitude", resultset.getDouble("longitude"));
				entry.addProperty("latitude", resultset.getDouble("latitude"));
				entry.addProperty("likeCount", resultset.getInt("likeCount"));
				entry.addProperty("privacy", resultset.getInt("privacy"));
				publicPost.add(entry);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultset != null) {
					resultset.close();
				}

				if (connection != null) {
					connection.close();
				}

				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Returns a JsonArray of all the posts.
		return publicPost;
	}

	// For logged in users, can see entries that are PUBLIC & FRIEND.
	public JsonArray getPublicAndPrivatePost(int userID) {
		JsonArray publicAndPrivatePost = new JsonArray();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DriverManager.getConnection(jdbcurl, username, password);

			// Get list of friends for the given userID to find out which 'friends' privacy
			// post to show.
			List<String> friendIDs = getFriendIDs(userID, connection);

			// Get all the post that are 'PUBLIC' and 'FRIENDS' that match friend ID in
			// list.
			statement = connection.createStatement();
			String query = "SELECT * FROM Entries WHERE privacy = 'public' OR (privacy = 'friends' AND user IN (SELECT UserName FROM Users WHERE UserID IN ("
					+ String.join(",", friendIDs) + ")))";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				JsonObject entry = new JsonObject();
				entry.addProperty("id", resultSet.getInt("id"));
				entry.addProperty("user", resultSet.getString("user"));
				entry.addProperty("image", resultSet.getString("image"));
				entry.addProperty("caption", resultSet.getString("caption"));
				entry.addProperty("time", resultSet.getTimestamp("time").toString());
				entry.addProperty("longitude", resultSet.getDouble("longitude"));
				entry.addProperty("latitude", resultSet.getDouble("latitude"));
				entry.addProperty("likeCount", resultSet.getInt("likeCount"));
				entry.addProperty("privacy", resultSet.getString("privacy"));
				publicAndPrivatePost.add(entry);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Returns a JsonArray of all the posts.
		return publicAndPrivatePost;
	}

	// Gets all friendIDs of userID.
	private List<String> getFriendIDs(int userID, Connection connection) throws SQLException {
		List<String> friendIDs = new ArrayList<>();
		String query = "SELECT friendID FROM Friends WHERE userID = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				friendIDs.add(resultSet.getString("friendID"));
			}
		}
		return friendIDs;
	}
}

