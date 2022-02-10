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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userEmail = request.getParameter("username");
		String userPassword = request.getParameter("password");
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		Connection conn = null;
		
		String URL = "jdbc:mysql://localhost:3306/EndGameDB";
		String errorRemover = "?useSSL=false";
		URL += errorRemover;
		String DRIVER = "com.mysql.cj.jdbc.Driver";

		try {
			Class.forName(DRIVER);
			String user = "root";
			String password = "1234567890";
			conn = DriverManager.getConnection(URL, user, password);
			
			String fetchUser = "select * from EndGameDB.userInfo where userName = ? and userPassword = ?";
			PreparedStatement pst = conn.prepareStatement(fetchUser);
			pst.setString(1, "userName");
			pst.setString(2, "userPassword");
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				System.out.println("\n\nUser details : " + rs.getString("userName") + "  " 
						+ rs.getString("userPassword"));
				session.setAttribute("name", rs.getString("userName"));
				dispatcher = request.getRequestDispatcher("index.jsp");
			} else {
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("login.jsp");
				System.out.println("No data found!");
//				System.out.println();
			}
		} catch (Exception se) {
			se.printStackTrace();
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
}
