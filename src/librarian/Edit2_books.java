package librarian;


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

public class Edit2_books extends HttpServlet 
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
	        ps=cn.prepareStatement("update books set quantity=? where name=? and category=?");
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
			String name=Edit_books.name;
			String cat=Edit_books.cat;
			String quantity=req.getParameter("quantity");
			int quantity_chk=(Edit_books.old_quantity)+(Integer.parseInt(quantity));
			if(hs!=null)
			{
				try
				{
					if(quantity_chk>=0)
					{
						ps.setString(1,Integer.toString(quantity_chk));
						ps.setString(2, name);
						ps.setString(3, cat);
						ResultSet rs=ps.executeQuery();
						out.println("<html><body><font color='blue' size='5'><center>Changes successfully updated!!!</center></font></body></html>");
						RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
						rd.include(req, res);
					}
					else
					{
						out.println("<html><body><font color='red' size='5'><center>You dont have sufficient no. of books..Try Again!!!</center></font></body></html>");
						RequestDispatcher rd=req.getRequestDispatcher("edit2_books.html");
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


