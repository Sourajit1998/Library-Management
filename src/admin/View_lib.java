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

public class View_lib extends HttpServlet 
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
	        ps=cn.prepareStatement("select * from adm_lib");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
       
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
			HttpSession hs=req.getSession(false);
			res.setContentType("text/html");
			PrintWriter out=res.getWriter();
			if(hs!=null)
			{
				out.println("<html><body><font color='blue' size='8'><center><h1>Librarians List</h1></center></font></body></html>");
				String mssge="<html><body><br><br><br><br>";
				mssge+="<table border='3' width='500px'>";
				mssge+="<tr><th align='center'>Sl. No.</th><th align='center'>Name</th><th align='center'>Contact no.</th><th align='center'>Email-id</th><th align='center'>Password</th></tr>";
				try
				{
					ResultSet rs=ps.executeQuery();
					int i=1;
					while(rs.next())
					{
						mssge+="<tr><td align='center'>"+Integer.toString(i)+"."+"</td><td align='center'>"+rs.getString(1)+"</td><td align='center'>"+rs.getString(2)+"</td><td align='center'>"+rs.getString(3)+"</td><td align='center'>"+rs.getString(4)+"</td></tr>";	
						i++;
						
					}
					mssge+="</table></body></html>";
					out.print(mssge);
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


