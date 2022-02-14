package com.endgame.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {
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
		PrintWriter out = response.getWriter();
//		out.print("working");
		
		String userName = request.getParameter("name");
		String userEmail = request.getParameter("email");
		String userPassword = request.getParameter("pass");
		String userPhone = request.getParameter("contact");
		
		RequestDispatcher dispatcher = null;
		
//		out.print(userName + " ");
//		out.print(userEmail + " ");
//		out.print(userPassword + " ");
//		out.print(userPhone + " ");
		
		String URL = "jdbc:mysql://localhost:3306/EndGameDB";
		String errorRemover = "?useSSL=false";
		URL += errorRemover;
		
		String DRIVER = "com.mysql.cj.jdbc.Driver";
		String user = "root";
		String password = "1234567890";
		
		Connection conn = null;
		
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, user, password);
			
			PreparedStatement pst = conn.prepareStatement(
				"insert into userInfo(userName, userPassword, userEmail, userPhone)"
				+ " values(?, ?, ?, ?)"
			);	// ? -> are place-holders
			
			pst.setString(1, userName);
			pst.setString(2, userPassword);
			pst.setString(3, userEmail);
			pst.setString(4, userPhone);
			
			int rowCount = pst.executeUpdate();
			dispatcher = request.getRequestDispatcher("registration.jsp");
			if(rowCount > 0) {
				request.setAttribute("status", "success");
			}
			else {
				request.setAttribute("status", "failed");
			}
			dispatcher.forward(request, response);
		} catch (Exception e) {
			out.print("Something went wrong while creating account");
			out.println("please try again");
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e2) {
				out.print("Something went wrong while closing DB");
			}
		}
	}
}
