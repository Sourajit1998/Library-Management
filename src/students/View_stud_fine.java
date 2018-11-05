package students;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class View_stud_fine extends HttpServlet 
{
	Connection cn;
	PreparedStatement ps1,ps2;
	String name,id;
	@Override
	public void init()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
			ps1=cn.prepareStatement("select * from issue_studs where name=? and id=?");
			ps2=cn.prepareStatement("select fine from fine_studs where name=? and id=?");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
       
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		    long total_fine=0;
			HttpSession hs=req.getSession(false);
			res.setContentType("text/html");
			PrintWriter out=res.getWriter();
			name=req.getParameter("name");
			id=req.getParameter("id");
			if(hs!=null)
			{
				out.println("<html><body><font color='blue' size='8'><center><h1>Fine</h1></center></font></body></html>");
				String mssge="<html><body><br><br><br><br>";
				mssge+="<table border='3' width='500px'>";
				mssge+="<tr><th align='center'>Book Name</th><th align='center'>Category</th><th align='center'>Fine(in Rs. upto current date)</th></tr>";
				try
				{
					ps1.setString(1, name);
					ps1.setString(2,id);
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
					Calendar c=Calendar.getInstance();
					String current_date=dateFormat.format(c.getTime());
					ResultSet rs=ps1.executeQuery();
					while(rs.next())
					{
						long old_fine,new_fine=0,cur_book_fine;
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
						mssge+="<tr><td align='center'>"+rs.getString(3)+"</td><td align='center'>"+rs.getString(4)+"</td><td align='center'>"+cur_book_fine+"</td></tr>";	
					
					}
					mssge+="</table></body></html>";
					out.print(mssge);
						
						//total_fine+=cur_book_fine;	
				    ps2.setString(1, name);
				    ps2.setString(2, id);
				    ResultSet rs2=ps2.executeQuery();
				    if(rs2.next())
				    {
				    	 total_fine+=rs2.getLong(1);
				    	 out.println("<br><html><body><font color='black' size='4'><leftr>");
				         out.println("<p>Previous pending dues=Rs."+rs2.getLong(1)+"</p><br><br>");
				         out.println("</left></font></body></html>"); 
				         
				         out.println("<br><br><html><body><font color='Red' size='5'><center>");
				         out.println("<p>Total fine to be paid=Rs."+total_fine+"</p><br><br>");
				         out.println("</center></font></body></html>");
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


