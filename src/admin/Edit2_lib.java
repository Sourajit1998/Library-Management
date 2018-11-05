package admin;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Edit2_lib extends HttpServlet 
{
	Connection cn;
	PreparedStatement ps;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
	        ps=cn.prepareStatement("update adm_lib set contact=?,email=?,password=? where name=?");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
       
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
			HttpSession hs=req.getSession(false);
			res.setContentType("text/html");
			PrintWriter out=res.getWriter();
			String name=Edit_lib.name;
			String cno=req.getParameter("cno");
			String eid=req.getParameter("eid");
			String pass=req.getParameter("pass");
			if(hs!=null)
			{
				try
				{
					ps.setString(1, cno);
					ps.setString(2, eid);
					ps.setString(3, pass);
					ps.setString(4, name);
					ResultSet rs=ps.executeQuery();
					out.println("<html><body><font color='blue' size='5'><center>Changes successfully updated!!!</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("Admin_pro.html");
					rd.include(req, res);
				}
				catch(Exception ee)
				{
					ee.printStackTrace();
				}
			}
			else
			{
				 out.println("<html><body><font color='red' size='5'><center>");
		         out.println("<b>Plz Login First!!!</b><br>");
		         out.println("</center></font></body></html>");
		         RequestDispatcher rd=req.getRequestDispatcher("Admin_login.html");
		         rd.include(req, res);
			}
	}
	
}


