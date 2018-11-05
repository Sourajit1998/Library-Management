package librarian;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Book_avail extends HttpServlet
{
	Connection cn;
	PreparedStatement ps;
	public static int quantity;
	public static String book_name;
	public static String book_cat;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
	        ps=cn.prepareStatement("select * from books where name=? and category=?");
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
		book_name=req.getParameter("name");
		book_cat=req.getParameter("cat");
		if(hs!=null)
		{
			try
			{
				ps.setString(1, book_name);
				ps.setString(2,book_cat);
				ResultSet rs=ps.executeQuery();
				if(rs.next())               //book with dat name exists
				{
					quantity=Integer.parseInt(rs.getString(3));
					if(quantity>0)
					{
						out.println("<html><body><font color='blue' size='5'><center>");
						out.println("<b>Book is Available!!</b><br>");
						out.println("</center></font></body></html>");
						RequestDispatcher rd=req.getRequestDispatcher("issue_book.html");
						rd.include(req, res);
					}
					else
					{
						out.println("<html><body><font color='red' size='5'><center>");
						out.println("<b>Sorry..Book is currently out of stock!!</b><br>");
						out.println("</center></font></body></html>");
						RequestDispatcher rd=req.getRequestDispatcher("book_avail.html");
						rd.include(req, res);
						
					}
				}
				else
				{					
					out.println("<html><body><font color='red' size='5'><center>");
					out.println("<b>Sorry..No such book found!!</b><br>");
					out.println("</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("book_avail.html");
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
