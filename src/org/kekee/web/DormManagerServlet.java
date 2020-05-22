package org.kekee.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.kekee.dao.DormManagerDao;
import org.kekee.dao.StudentDao;
import org.kekee.model.DormBuild;
import org.kekee.model.DormManager;
import org.kekee.model.PageBean;
import org.kekee.util.DbUtil;
import org.kekee.util.PropertiesUtil;
import org.kekee.util.StringUtil;

public class DormManagerServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	DormManagerDao dormManagerDao = new DormManagerDao();
	StudentDao studentDao = new StudentDao();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		String sDormManagerText = request.getParameter("s_dormManagerText");
		String searchType = request.getParameter("searchType");
		String page = request.getParameter("page");
		String action = request.getParameter("action");
		DormManager dormManager = new DormManager();
		if("preSave".equals(action)) {
			dormManagerPreSave(request, response);
			return;
		} else if("save".equals(action)){
			dormManagerSave(request, response);
			return;
		} else if("delete".equals(action)){
			dormManagerDelete(request, response);
			return;
		} else 
			if("list".equals(action)) {
			if(StringUtil.isNotEmpty(sDormManagerText)) {
				if("name".equals(searchType)) {
					dormManager.setName(sDormManagerText);
				} else if("userName".equals(searchType)) {
					dormManager.setUserName(sDormManagerText);
				}
			}
			session.removeAttribute("s_dormManagerText");
			session.removeAttribute("searchType");
			request.setAttribute("s_dormManagerText", sDormManagerText);
			request.setAttribute("searchType", searchType);
		} else if("search".equals(action)){
			if (StringUtil.isNotEmpty(sDormManagerText)) {
				if ("name".equals(searchType)) {
					dormManager.setName(sDormManagerText);
				} else if ("userName".equals(searchType)) {
					dormManager.setUserName(sDormManagerText);
				}
				session.setAttribute("searchType", searchType);
				session.setAttribute("s_dormManagerText", sDormManagerText);
			} else {
				session.removeAttribute("s_dormManagerText");
				session.removeAttribute("searchType");
			}
		} else {
			if(StringUtil.isNotEmpty(sDormManagerText)) {
				if("name".equals(searchType)) {
					dormManager.setName(sDormManagerText);
				} else if("userName".equals(searchType)) {
					dormManager.setUserName(sDormManagerText);
				}
				session.setAttribute("searchType", searchType);
				session.setAttribute("s_dormManagerText", sDormManagerText);
			}
			if(StringUtil.isEmpty(sDormManagerText)) {
				Object o1 = session.getAttribute("s_dormManagerText");
				Object o2 = session.getAttribute("searchType");
				if(o1!=null) {
					if("name".equals(o2)) {
						dormManager.setName((String)o1);
					} else if("userName".equals(o2)) {
						dormManager.setUserName((String)o1);
					}
				}
			}
		}
		if(StringUtil.isEmpty(page)) {
			page="1";
		}
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
		request.setAttribute("pageSize", pageBean.getPageSize());
		request.setAttribute("page", pageBean.getPage());
		try {
			con=dbUtil.getCon();
			List<DormManager> dormManagerList = dormManagerDao.dormManagerList(con, pageBean, dormManager);
			int total=dormManagerDao.dormManagerCount(con, dormManager);
			String pageCode = this.genPagation(total, Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			request.setAttribute("pageCode", pageCode);
			request.setAttribute("dormManagerList", dormManagerList);
			request.setAttribute("mainPage", "admin/dormManager.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
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

	private void dormManagerDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String dormManagerId = request.getParameter("dormManagerId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			dormManagerDao.dormManagerDelete(con, dormManagerId);
			request.getRequestDispatcher("dormManager?action=list").forward(request, response);
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

	private void dormManagerSave(HttpServletRequest request,
			HttpServletResponse response) {
		String dormManagerId = request.getParameter("dormManagerId");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String dormBuildId = request.getParameter("dormBuildId");
		String sex = request.getParameter("sex");
		String tel = request.getParameter("tel");
		DormManager dormManager = new DormManager(userName, password, name, sex, tel, Integer.parseInt(dormBuildId));
		if(StringUtil.isNotEmpty(dormManagerId)) {
			dormManager.setDormManagerId(Integer.parseInt(dormManagerId));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum;
			if(StringUtil.isNotEmpty(dormManagerId)) {
				saveNum = dormManagerDao.dormManagerUpdate(con, dormManager);
			} else if(dormManagerDao.haveManagerByUser(con, dormManager.getUserName())){
				request.setAttribute("dormManager", dormManager);
				request.setAttribute("error", "该用户名已存在");
				request.setAttribute("mainPage", "admin/dormManagerSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			} else {
				saveNum = dormManagerDao.dormManagerAdd(con, dormManager);
			}
			if(saveNum > 0) {
				request.getRequestDispatcher("dormManager?action=list").forward(request, response);
			} else {
				request.setAttribute("dormManager", dormManager);
				request.setAttribute("error", "保存失败");
				request.setAttribute("mainPage", "dormManager/dormManagerSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
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

	private void dormManagerPreSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String dormManagerId = request.getParameter("dormManagerId");
		Connection con = null;
		List<DormBuild> dormBuildList = new ArrayList<>();
		try {
			con = dbUtil.getCon();
			if(StringUtil.isNotEmpty(dormManagerId)) {
				DormManager dormManager = dormManagerDao.dormManagerShow(con, dormManagerId);
				request.setAttribute("dormManager", dormManager);
			}
			dormBuildList = studentDao.dormBuildList(con);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("dormBuildList", dormBuildList);
		request.setAttribute("mainPage", "admin/dormManagerSave.jsp");
		request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
	}

	private String genPagation(int totalNum, int currentPage, int pageSize){
		int totalPage = totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		StringBuilder pageCode = new StringBuilder();
		pageCode.append("<li><a href='dormManager?page=1'>首页</a></li>");
		if(currentPage==1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}else {
			pageCode.append("<li><a href='dormManager?page=").append(currentPage - 1).append("'>上一页</a></li>");
		}
		for(int i=currentPage-2;i<=currentPage+2;i++) {
			if(i<1||i>totalPage) {
				continue;
			}
			if(i==currentPage) {
				pageCode.append("<li class='active'><a href='#'>").append(i).append("</a></li>");
			} else {
				pageCode.append("<li><a href='dormManager?page=").append(i).append("'>").append(i).append("</a></li>");
			}
		}
		if(currentPage==totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		} else {
			pageCode.append("<li><a href='dormManager?page=").append(currentPage + 1).append("'>下一页</a></li>");
		}
		pageCode.append("<li><a href='dormManager?page=").append(totalPage).append("'>尾页</a></li>");
		return pageCode.toString();
	}
	
}
