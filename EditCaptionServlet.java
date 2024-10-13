import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EditCaptionServlet")
public class EditCaptionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageIDParam = request.getParameter("imageID");
        String newCaption = request.getParameter("newCaption");
        if (imageIDParam == null || imageIDParam.isEmpty() || newCaption == null || newCaption.isEmpty()) {
            response.setContentType("text/plain");
            response.getWriter().write("Image ID or new caption parameter is missing");
            return;
        }

        int imageID = Integer.parseInt(imageIDParam);
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://localhost/Photo%20Diary?user=root&password=root");
            String sql = "UPDATE entries SET caption = ? WHERE id = ?";
            
            

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newCaption);
            pstmt.setInt(2, imageID);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                response.setContentType("text/plain");
                response.getWriter().write("Caption updated successfully");
            } else {
                response.setContentType("text/plain");
                response.getWriter().write("No image found with the specified ID");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Error updating caption");
        } finally {
            try {
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
}