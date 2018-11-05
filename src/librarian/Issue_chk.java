package librarian;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Issue_chk extends HttpServlet
{
	Connection cn;
	PreparedStatement ps;
	public static String name,id;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
	        ps=cn.prepareStatement("select * from issue_studs where name=? and id=?");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{
		HttpSession hs=req.getSession(false);
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		name=req.getParameter("name");
		id=req.getParameter("id");
		if(hs!=null)
		{
			try
			{
				ps.setString(1, name);
				ps.setString(2, id);
				ResultSet rs=ps.executeQuery();
				if(rs.next())               //He has been issued book earlier
				{
					                        //list of books issued
					
					RequestDispatcher rd=req.getRequestDispatcher("issued");
					rd.include(req, res);
				}
					
				else
				{
					out.println("<html><body><font color='red' size='5'><center>");
					out.println("<b>Issue books first</b><br>");
					out.println("</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
					rd.include(req, res);
						
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
	         RequestDispatcher rd=req.getRequestDispatcher("Lib_login.html");
	         rd.include(req, res);
		}
	}
}
