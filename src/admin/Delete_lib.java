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

public class Delete_lib extends HttpServlet 
{
	Connection cn;
	PreparedStatement ps1,ps2;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
	        ps1=cn.prepareStatement("select * from adm_lib where name=?");
	        ps2=cn.prepareStatement("delete from adm_lib where name=?");
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
			String name=req.getParameter("name");
			if(hs!=null)
			{
				try
				{
					ps1.setString(1, name);
					ResultSet rs=ps1.executeQuery();
					if(rs.next())          //name is present
					{
						ps2.setString(1, name);
						ps2.executeUpdate();
						out.println("<html><body><font color='blue' size='5'><center>Librarian successfully removed</center></font></body></html>");
					    RequestDispatcher rd=req.getRequestDispatcher("Admin_pro.html");
					    rd.include(req, res);
					}
					else              //name not present
					{
						out.println("<html><body><font color='red' size='5'><center>No such name found..Try Again!!!</center></font></body></html>");
						RequestDispatcher rd=req.getRequestDispatcher("delete_lib.html");
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
		         RequestDispatcher rd=req.getRequestDispatcher("Admin_login.html");
		         rd.include(req, res);
			}
	}
}


