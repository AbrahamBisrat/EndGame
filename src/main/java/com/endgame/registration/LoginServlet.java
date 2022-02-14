package com.endgame.registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("username");
		String userPassword = request.getParameter("password");
		HttpSession session = request.getSession();
		Connection conn = null;
		
		RequestDispatcher dispatcher = null;
		
		String URL = "jdbc:mysql://localhost:3306/EndGameDB";
		String errorRemover = "?useSSL=false";
		URL += errorRemover;
		String DRIVER = "com.mysql.cj.jdbc.Driver";  // fetch me this class on runtime

		try {
			Class.forName(DRIVER);			// create a class for Driver on runTime
			String user = "root";				// This is just horrible
			String password = "1234567890";		// salt it, hash it, never raw
			conn = DriverManager.getConnection(URL, user, password);
			
			String fetchUser = "select * from EndGameDB.userInfo "
					+ "where userName = ? and userPassword = ?";
			PreparedStatement pst = conn.prepareStatement(fetchUser);
			pst.setString(1, userName);
			pst.setString(2, userPassword);
			
			System.out.println("raw query: " + pst.toString());
			
			ResultSet rs = pst.executeQuery();
			
//			scrubbed(userName, userPassword, rs);
			
			if(rs.next()) {
				System.out.println("\n\nUser details : "
						+ rs.getString("userName") + "  "
						+ rs.getString("userPassword"));
				
				session.setAttribute("name", rs.getString("userName"));
				dispatcher = request.getRequestDispatcher("index.jsp");
			} else {
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("login.jsp");
				System.out.println("No data found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			dispatcher = request.getRequestDispatcher("login.jsp");
		} finally {
			try {
				conn.close();
				System.out.println("db conn closed");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		dispatcher.forward(request, response);
	}

	@SuppressWarnings("unused")
	private void scrubbed(String userName, String userPassword, ResultSet rs)
			throws SQLException {
		System.out.println("\nParsing ResultSet object");
		
		System.out.println(rs.getString("userName"));
		System.out.println(rs.getString("userPassword"));
		viewDataInResultSet(rs); 
		viewDataInResultSetImproved(rs); 
		if(userName.equals(rs.getString("userName"))) {
			System.out.println("User name found!");
			if(userPassword.equals(rs.getString("userPassword"))) {
				System.out.println("User password also matches username");
			}
		} else {
			System.out.println("\nNothing from DB matches result!");
		}
	}

	private void viewDataInResultSetImproved(ResultSet rs) 
			throws SQLException {
		System.out.println("Parsing ResultSet : ");
		while(rs.next()) {
			int id_num = rs.getInt("id");
			String uName = rs.getString("userName");
			String uPass = rs.getString("userPassword");
			String uEmail = rs.getString("userEmail");
			String uPhone = rs.getString("userPhone");
			System.out.println(id_num + " : "
					+ uName + " : "
					+ uPass + " : "
					+ uEmail + " : "
					+ uPhone + " : "
					);
		}
	}

	private void viewDataInResultSet(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		System.out.println("columns : " + columnsNumber 
				+ " rows : " + rsmd.getFields().toString());
		
		if(!rs.next())
			System.out.println("No data in requested ResultSet.");
		else {
			while (rs.next()) {
			    for (int i = 1; i <= columnsNumber; i++) {
			        if (i > 1) System.out.print(",  ");
			        String columnValue = rs.getString(i);
			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
			    }
			    System.out.println("--End of fetch");
			}
		}
	}
}
