package admin;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Admin_login extends HttpServlet 
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
	        ps=cn.prepareStatement("select * from Admin where name=? and password=? ");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	@Override
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		String name=req.getParameter("name");
		String pass=req.getParameter("pass");
		 
		try
		{
			ps.setString(1,name);
			ps.setString(2, pass);
			ResultSet rs=ps.executeQuery();
			if(rs.next())               //authorised admin
			{
				out.println("<html><body><font color='red' size='5'><center>");
				out.println("<b>Admin logged in successfully</b>");
				out.println("</center></font></body></html>");
				HttpSession hs=req.getSession();
				RequestDispatcher rd=req.getRequestDispatcher("Admin_pro.html");
				rd.include(req,res);
            }
			else                       //not authorised admin
			{
				out.println("<html><body><font color='red' size='5'><center>");
				out.println("<b>Admin login failed..Try Again!!!</b>");
				out.println("</center></font></body></html>");				
				RequestDispatcher rd=req.getRequestDispatcher("Admin_login.html");
				rd.include(req,res);
			} 
		}
		
	   catch(Exception ee)
	   {
		   ee.printStackTrace();
	   }
		
    }
}

	
	
	