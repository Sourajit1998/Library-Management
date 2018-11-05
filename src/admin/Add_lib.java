package admin;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Add_lib extends HttpServlet
{
	Connection cn;
	PreparedStatement ps1,ps2;;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
	        ps1=cn.prepareStatement("select * from adm_lib where name=?");
			ps2=cn.prepareStatement("insert into adm_lib values(?,?,?,?)");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	@Override
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{
		HttpSession hs=req.getSession(false);
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		String name=req.getParameter("name");
		String cno=req.getParameter("cno");
		String eid=req.getParameter("eid");
		String pass=req.getParameter("pass");
		if(hs!=null)
		{
			try
			{
				ps1.setString(1, name);
				ResultSet rs=ps1.executeQuery();
				if(rs.next())               //duplicate name exists
				{
					out.println("<html><body><font color='red' size='5'><center>");
					out.println("<b>Registration failed..Username already exists</b><br>");
					out.println("<b><i>Try Again</i></b><br>");
					out.println("</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("add_lib.html");
					rd.include(req, res);
				}
				else
				{
					ps2.setString(1,name);
					ps2.setString(2,cno);
					ps2.setString(3,eid);
					ps2.setString(4,pass);
					int k= ps2.executeUpdate();
					
					out.println("<html><body><font color='red' size='5'><center>");
					out.println("<b>Registration successful</b><br>");
					out.println("</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("Admin_pro.html");
					rd.include(req,res);
						
				}
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
