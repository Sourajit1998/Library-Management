package librarian;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Add_books extends HttpServlet
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
	        ps1=cn.prepareStatement("select * from books where name=? and category=?");
			ps2=cn.prepareStatement("insert into books values(?,?,?)");
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
		String cat=req.getParameter("cat");
		String quantity=req.getParameter("quantity");
	
		if(hs!=null)
		{
			try
			{
				ps1.setString(1, name);
				ps1.setString(2,cat);
				ResultSet rs=ps1.executeQuery();
				if(rs.next())               //duplicate name exists
				{
					out.println("<html><body><font color='red' size='5'><center>");
					out.println("<b>Book with that name already exists</b><br>");
					out.println("<b><i>Go 2 Edit books if u want 2 update no. of books</i></b><br>");
					out.println("</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("add_books.html");
					rd.include(req, res);
				}
				else
				{
					ps2.setString(1,name);
					ps2.setString(2,cat);
					ps2.setString(3,quantity);
					int k= ps2.executeUpdate();
					
					out.println("<html><body><font color='blue' size='5'><center>");
					out.println("<b>Book successfully added!!</b><br>");
					out.println("</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
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
	         RequestDispatcher rd=req.getRequestDispatcher("Lib_login.html");
	         rd.include(req, res);
		}
	}
}
