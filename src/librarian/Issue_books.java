package librarian;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Issue_books extends HttpServlet
{
	Connection cn;
	PreparedStatement ps,ps1,ps2,ps3;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
			ps=cn.prepareStatement("select * from fine_studs where name=? and id=?");
	        ps1=cn.prepareStatement("select * from issue_studs where name=? and id=?");
			ps2=cn.prepareStatement("insert into issue_studs values(?,?,?,?,?,?,?,?)");
			ps3=cn.prepareStatement("update books set quantity=? where name=? and category=?");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	@Override
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{
		long total_fine;
		HttpSession hs=req.getSession(false);
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		String name=req.getParameter("name");
		String id=req.getParameter("id");
		String bname=Book_avail.book_name;
		String bcat=Book_avail.book_cat;
		int quantity=Book_avail.quantity;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
		Calendar c=Calendar.getInstance();
		
		if(hs!=null)
		{
			try
			{
				ps.setString(1,name);
				ps.setString(2, id);
				ResultSet rs1=ps.executeQuery();
				if(rs1.next())                                  //he has Lib-ID and can b issued books
				{
					total_fine=Long.parseLong(rs1.getString(3));                 //initial value of total fine=value of pending dues from returned books
					ps1.setString(1, name);
					ps1.setString(2, id);
					ResultSet rs=ps1.executeQuery();
					
					String current_date=dateFormat.format(c.getTime());
					int count=0;
					long old_fine,new_fine=0,cur_book_fine;
					while(rs.next())               //He has been issued book earlier
					{
						count++;
						String issue_date=rs.getString(5);
						old_fine=Long.parseLong(rs.getString(8));
						Date cur=dateFormat.parse(current_date);
						Date old=dateFormat.parse(issue_date);                  //issue date of d current book
						long x=cur.getTime()-old.getTime();
						x=x/(24*60*60*1000);                                  //difference in no. of days between current date and issue_date of d current book
						if(x>0)                                              //calculate fine
						{
								new_fine = 2*(x-0);                            //replace 0 wth 21 
						}
					    cur_book_fine=old_fine+new_fine;                    //total fine of current book
						total_fine+=cur_book_fine;	
					}
					
					if(count<4 && total_fine<=50)                        //no.of issued books of a student should not exceed 4 
					{
						c.add(Calendar.DAY_OF_MONTH, 21);                            //due date=issue_date+21 days
						String due=dateFormat.format(c.getTime());
						ps2.setString(1,name);
						ps2.setString(2,id);
						ps2.setString(3, bname);
						ps2.setString(4, bcat);
						ps2.setString(5,current_date);
						ps2.setString(6,due);
						ps2.setString(7, null);
						ps2.setInt(8, 0);
						ps2.executeUpdate();
					
						quantity--;
						ps3.setString(1, Integer.toString(quantity));
						ps3.setString(2, bname);
						ps3.setString(3, bcat);
						ps3.executeUpdate();
					
						out.println("<html><body><font color='blue' size='5'><center>");
						out.println("<b>Book issued successfully!!</b><br>");
						out.println("</center></font></body></html>");
						RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
						rd.include(req, res);
						
				     }
					else
					{
						if(total_fine>50)
						{
							out.println("<html><body><font color='red' size='5'><center>");
							out.println("<b>Book issue failed..Plz clear ur pending dues first!!!</b><br>");
							out.println("</center></font></body></html>");
							RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
							rd.include(req, res);
							
						}
						else
						{
							out.println("<html><body><font color='red' size='5'><center>");
							out.println("<b>Student has already been issued 4 books..</b><br>");
							out.println("<i>Return previous books 2 issue new ones!!</i><br>");
							out.println("</center></font></body></html>");
							RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
							rd.include(req, res);
						}
			       }
				}
				else
				{
					out.println("<html><body><font color='red' size='5'><center>");
					out.println("<b>Create Lib-ID first!!</b><br>");
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
