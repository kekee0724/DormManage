package org.kekee.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.kekee.dao.UserDao;
import org.kekee.model.Admin;
import org.kekee.model.DormManager;
import org.kekee.model.Student;
import org.kekee.util.DbUtil;

public class PasswordServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	UserDao userDao = new  UserDao();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		
		if("preChange".equals(action)) {
			passwordPreChange(request, response);
		} else if("change".equals(action)) {
			passwordChange(request, response);
		}
	}

	private void passwordChange(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			
			if("admin".equals(currentUserType)) {
				Admin admin = (Admin)(session.getAttribute("currentUser"));
				if(oldPassword.equals(admin.getPassword())) {
					userDao.adminUpdate(con, admin.getAdminId(), newPassword);
					admin.setPassword(newPassword);
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "两次密码不一致");
					request.setAttribute("mainPage", "admin/passwordChange.jsp");
					request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				} else {
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "密码错误");
					request.setAttribute("mainPage", "admin/passwordChange.jsp");
					request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				}
			} else if("dormManager".equals(currentUserType)) {
				DormManager manager = (DormManager)(session.getAttribute("currentUser"));
				if(oldPassword.equals(manager.getPassword())) {
					userDao.adminUpdate(con, manager.getDormManagerId(), newPassword);
					manager.setPassword(newPassword);
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "两次密码不一致");
					request.setAttribute("mainPage", "dormManager/passwordChange.jsp");
					request.getRequestDispatcher("mainManager.jsp").forward(request, response);
				} else {
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "密码错误");
					request.setAttribute("mainPage", "dormManager/passwordChange.jsp");
					request.getRequestDispatcher("mainManager.jsp").forward(request, response);
				}
			} else if("student".equals(currentUserType)) {
				Student student = (Student)(session.getAttribute("currentUser"));
				if(oldPassword.equals(student.getPassword())) {
					userDao.adminUpdate(con, student.getStudentId(), newPassword);
					student.setPassword(newPassword);
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "两次密码不一致");
					request.setAttribute("mainPage", "student/passwordChange.jsp");
					request.getRequestDispatcher("mainStudent.jsp").forward(request, response);
				} else {
					request.setAttribute("oldPassword", oldPassword);
					request.setAttribute("newPassword", newPassword);
					request.setAttribute("rPassword", newPassword);
					request.setAttribute("error", "密码错误");
					request.setAttribute("mainPage", "student/passwordChange.jsp");
					request.getRequestDispatcher("mainStudent.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void passwordPreChange(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		if("admin".equals(currentUserType)) {
			request.setAttribute("mainPage", "admin/passwordChange.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
		} else if("dormManager".equals(currentUserType)) {
			request.setAttribute("mainPage", "dormManager/passwordChange.jsp");
			request.getRequestDispatcher("mainManager.jsp").forward(request, response);
		} else if("student".equals(currentUserType)) {
			request.setAttribute("mainPage", "student/passwordChange.jsp");
			request.getRequestDispatcher("mainStudent.jsp").forward(request, response);
		}
	}
	
}
