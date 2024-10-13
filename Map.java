import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Map")
public class Map extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Photo%20Diary?user=root&password=root");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT id, image_data, longitude, latitude FROM entries");
            
            StringBuilder json = new StringBuilder("[");
            while (rs.next()) {
            	int id = rs.getInt("id");
            	byte[] image = getImageData(id);
            	double longitude = rs.getDouble("longitude");
            	double latitude = rs.getDouble("latitude");
            	json.append("{\"longitude\": ").append(longitude)
            	    .append(", \"latitude\": ").append(latitude)
            	    .append(", \"id\": ").append(id)
            	    .append(", \"image\": \"").append(image).append("\"},");
            }
            
            json.setCharAt(json.length() - 1, ']'); // replace last comma with closing bracket
            out.println(json);
            
            rs.close();
            st.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public byte[] getImageData(int imageID) throws SQLException, ClassNotFoundException, IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        byte[] imageDataBytes = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Photo%20Diary?user=root&password=root");
            
            String sql = "SELECT image_data FROM entries WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, imageID);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                InputStream imageData = rs.getBinaryStream("image_data");
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                
                byte[] data = new byte[4096];
                int bytesRead;
                while ((bytesRead = imageData.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, bytesRead);
                }
                
                imageDataBytes = buffer.toByteArray();
            }
        } finally {
            // Close ResultSet, PreparedStatement, and Connection in finally block
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
        
        return imageDataBytes;
    }
    
}
