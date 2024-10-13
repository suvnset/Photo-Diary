import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCConnector {
static public Integer registerUser(String user, String pass) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = null;
		Statement st = null;
		ResultSet rs= null;
		Integer userID = -1;
		
		try {
	            conn = DriverManager.getConnection("jdbc:mysql://localhost/Photo%20Diary?user=root&password=root");

			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM Users WHERE UserName='" + user + "'");
			if (!rs.next()) { // There's no user w/ that username
				rs.close();
				st.execute("INSERT INTO Users (UserName, Password) VALUES ('" + user + "','" + pass + "')");
				rs = st.executeQuery("SELECT LAST_INSERT_ID()");
				rs.next();
				userID = rs.getInt(1);
			}
			else {
				userID = -2;
			}
		}
		catch(SQLException sqle) {
			System.out.println("SQLException in registerUser().");
			sqle.printStackTrace();
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
			catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return userID;
	}

	public static int storeImage(String username, InputStream imageStream, String caption, String date, double longitude, double lat, String privacy) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Photo%20Diary?user=root&password=root");     
            
            String sql = "INSERT INTO entries (user, image_data, caption,time, longitude, latitude, likeCount, privacy) VALUES (?, ?, ?, ?, ?, ?, 0, ?)";
            
            
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
              

            pstmt.setString(1, username);
            pstmt.setBlob(2, imageStream);
            pstmt.setString(3, caption);
            pstmt.setString(4, date);
            pstmt.setDouble(5, longitude);
            pstmt.setDouble(6, lat);
            pstmt.setString(7, privacy);
            
            pstmt.executeUpdate();
            
     
            
            
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); 
            } else {
                return -1; 
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return -1; 
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	
	
	public static String getName(int userID) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null; 
	    String userName = null;

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        conn = DriverManager.getConnection("jdbc:mysql://localhost/Photo%20Diary?user=root&password=root");     

	        String sql = "SELECT UserName FROM Users WHERE UserID = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, userID);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            userName = rs.getString("UserName");
	        } 
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	            if (pstmt != null) {
	                pstmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return userName;
	}

}
