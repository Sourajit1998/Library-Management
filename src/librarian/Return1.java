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

public class Return1 extends HttpServlet 
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
	        ps=cn.prepareStatement("select book_name,category,issue_date,due_date from issue_studs where name=? and id=? and return_date is null order by issue_date asc");
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
			String name=Return_check.name;
			String id=Return_check.id;
			if(hs!=null)
			{
				String bname=null;
				out.println("<html><body><font color='blue' size='8'><center><h1>List of Books</h1></center></font></body></html>");
				String mssge="<html><body><br><br><br><br>";
				mssge+="<form action='return2'>";
				mssge+="<table border='3' width='500px'>";
				mssge+="<tr><th align='center'>Sl. No.</th><th align='center'>Book Name</th><th align='center'>Category</th><th align='center'>Issue Date</th><th align='center'>Due Date</th><th align='center'>Return</th></tr>";
				try
				{
					ps.setString(1,name);
					ps.setString(2, id);
					ResultSet rs=ps.executeQuery();
					int i=1;
					while(rs.next())
					{
						bname=rs.getString(1);
						if(i==1)
						{
							mssge+="<tr><td align='center'>"+Integer.toString(i)+"."+"</td><td align='center'>"+rs.getString(1)+"</td><td align='center'>"+rs.getString(2)+"</td><td align='center'>"+rs.getString(3)+"</td><td align='center'>"+rs.getString(4)+"</td><td align='center'><input type='submit' value='return'></td></tr>";
							//mssge+="<input type='hidden' name='bname' value='"+bname+"'>";
							mssge+="<input type='hidden' name='bname' value='"+bname+"'>";
							mssge+="</table></form></body></html>";
							out.print(mssge);
						}
						else
						{
							String nmsg="<html><body><br><br>";
							nmsg+="<form action='return2'>";
							nmsg+="<table border='3' width='500px'>";
							nmsg+="<tr><td align='center'>"+Integer.toString(i)+"."+"</td><td align='center'>"+rs.getString(1)+"</td><td align='center'>"+rs.getString(2)+"</td><td align='center'>"+rs.getString(3)+"</td><td align='center'>"+rs.getString(4)+"</td><td align='center'><input type='submit' value='return'></td></tr>";
							nmsg+="<input type='hidden' name='bname' value='"+bname+"'>";
							nmsg+="</table></form></body></html>";
							out.print(nmsg);
						}
						i++;
						
					}
					//mssge+="<input type='hidden' name='bname' value='"+bname+"'>";
					//mssge+="</table></form></body></html>";
					//out.print(mssge);
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


