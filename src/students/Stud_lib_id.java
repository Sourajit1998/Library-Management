package students;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Stud_lib_id extends HttpServlet
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
	        ps1=cn.prepareStatement("select * from fine_studs where name=? and id=?");
	        ps2=cn.prepareStatement("insert into fine_studs values(?,?,?)");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	@Override
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{
		String name,id;
		HttpSession hs=req.getSession(false);
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		name=req.getParameter("name");
		id=req.getParameter("id");
		if(hs!=null)
		{
			try
			{
				ps1.setString(1, name);
				ps1.setString(2, id);
				ResultSet rs=ps1.executeQuery();
				if(rs.next())               //Student is already registered 
				{
					out.println("<html><body><font color='red' size='5'><center>");
					out.println("<b>Failed to created Lib_ID..Student is already registered</b><br>");
					out.println("</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
					rd.include(req, res);
				}
					
				else                        //Lib-ID created successfully
				{
					ps2.setString(1,name);
					ps2.setString(2, id);
					ps2.setInt(3, 0);
					ps2.execute();
					out.println("<html><body><font color='blue' size='5'><center>");
					out.println("<b>Lib-ID created successfully</b><br>");
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
