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
public class Reissue2 extends HttpServlet
{
	
	Connection cn;
	PreparedStatement ps,ps1,ps2;
	String name,id,issue,due;
	long total_fine,old_fine,new_fine;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
	        ps=cn.prepareStatement("select fine from fine_studs where name=? and id=?");
	        ps1=cn.prepareStatement("select * from issue_studs where name=? and id=?");
	        ps2=cn.prepareStatement("update issue_studs set issue_date=?,due_date=?,fine=? where name=? and book_name=?");	        
	        
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}

	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{
		
		long total_fine,reissue_fine=0;
		String cur_bname,cur_issue_date;
		HttpSession hs=req.getSession(false);
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		name=Reissue_check.name;
		id=Reissue_check.id;
		String bname=req.getParameter("bname");
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
		Calendar c=Calendar.getInstance();
	
		if(hs!=null)
		{
			try
			{
				
				ps.setString(1,name);
				ps.setString(2, id);
				ResultSet rs1=ps.executeQuery();
				if(rs1.next())                                  //he has Lib-ID
				{
					total_fine=Long.parseLong(rs1.getString(1));                 //initial value of total fine=value of pending dues from returned books
					
					ps1.setString(1, name);
					ps1.setString(2, id);
					ResultSet rs=ps1.executeQuery();
					
					String current_date=dateFormat.format(c.getTime());
					long old_fine,new_fine=0,cur_book_fine;
					while(rs.next())               //He has been issued book earlier
					{
						String issue_date=rs.getString(5);
						cur_bname=rs.getString(3);
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
						
						if(cur_bname.equals(bname))                          //if it is d book which we want 2 reissue then save d details:issue_date,total_fine
						{
							cur_issue_date=rs.getString(5);
							reissue_fine=cur_book_fine;
						}
					}
					
					if(total_fine<=50)                        //if total fine upto current date <=50 then only book can be reissued 
					{
				
						c.add(Calendar.DAY_OF_MONTH, 21);                            //due date=issue_date+21 days
						String due=dateFormat.format(c.getTime());
						ps2.setString(1,current_date);
						ps2.setString(2, due);
						ps2.setLong(3,reissue_fine);
						ps2.setString(4, name);
						ps2.setString(5, bname);
						
						ResultSet rs2=ps2.executeQuery();
						if(rs2.next())               //Book successfully reissued
						{
							out.println("<html><body><font color='blue' size='5'><center>");
							out.println("<b>Book reissued successfully!!</b><br>");
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
					else
					{
							out.println("<html><body><font color='red' size='5'><center>");
							out.println("<b>Book reissue failed..Plz clear ur pending dues first!!!</b><br>");
							out.println("</center></font></body></html>");
							RequestDispatcher rd=req.getRequestDispatcher("Lib_pro.html");
							rd.include(req, res);
						
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
