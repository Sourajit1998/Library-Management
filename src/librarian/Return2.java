package librarian;

import java.io.*;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Return2 extends HttpServlet
{
	
	Connection cn;
	PreparedStatement ps,ps1,ps2,ps3,ps5,ps6;
	String name,id,issue;
	long total_fine,old_fine,new_fine;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
			ps=cn.prepareStatement("select issue_date,fine from issue_studs where name=? and book_name=?");
	        ps1=cn.prepareStatement("update issue_studs set return_date=?,fine=? where name=? and book_name=?");
	        ps5=cn.prepareStatement("select fine from fine_studs where name=? and id=?");
	        ps6=cn.prepareStatement("update fine_studs set fine=? where name=? and id=?");
	        ps2=cn.prepareStatement("update books set quantity=quantity+1 where name=?");
	        ps3=cn.prepareStatement("delete from issue_studs where return_date is not null");
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
		name=Return_check.name;
		id=Return_check.id;
		String bname=req.getParameter("bname");
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
		Calendar c=Calendar.getInstance();
		String current_date=dateFormat.format(c.getTime());
		//int quantity=Book_avail.quantity;
	
		if(hs!=null)
		{

			try
			{
				ps.setString(1, name);
				ps.setString(2, bname);
				ResultSet rs=ps.executeQuery();
				if(rs.next())
				{
					issue=rs.getString(1);
					old_fine=Long.parseLong(rs.getString(2));
				}
				Date cur=dateFormat.parse(current_date);
				Date old=dateFormat.parse(issue);
				long x=cur.getTime()-old.getTime();
				x=x/(24*60*60*1000);                                  //difference in no. of days between current date and issue_date
				
				if(x>0)                                              //calculate fine
				{
					new_fine = 2*(x-0);                             //fine 2 b included later......replace 0 wth 21
				}
				
				total_fine=old_fine+new_fine;
				ps1.setString(1,current_date);
				ps1.setLong(2,total_fine);
				ps1.setString(3, name);
				ps1.setString(4, bname);
				
				ResultSet rs2=ps1.executeQuery();
				
				
				if(rs2.next())               //Book successfully returned
				{
					long pending_fine=0;
					ps5.setString(1, name);
					ps5.setString(2, id);
					ResultSet rs3=ps5.executeQuery();
					if(rs3.next())
					{
						pending_fine=Long.parseLong(rs3.getString(1));                              //old dues if any
					}
					ps6.setLong(1,(pending_fine+total_fine));                                       //old_dues+(fine of the returned book)
					ps6.setString(2, name);
					ps6.setString(3, id);
					ps6.executeUpdate();
					
					ps2.setString(1, bname);
					ps2.executeUpdate();
					ps3.executeUpdate();
					out.println("<html><body><font color='blue' size='5'><center>");
					out.println("<b>Book returned successfully!!</b><br>");
					out.println("</center></font></body></html>");
					RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
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
