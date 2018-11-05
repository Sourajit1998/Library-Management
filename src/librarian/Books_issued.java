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

public class Books_issued extends HttpServlet 
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
	        ps=cn.prepareStatement("select book_name,category,issue_date,due_date from issue_studs where name=? and id=? order by issue_date asc");
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
			String name=Issue_chk.name;
			String id=Issue_chk.id;
			if(hs!=null)
			{
				out.println("<html><body><font color='blue' size='8'><center><h1>Issued Books</h1></center></font></body></html>");
				String mssge="<html><body><br><br><br><br>";
				mssge+="<table border='3' width='500px'>";
				mssge+="<tr><th align='center'>Sl. No.</th><th align='center'>Book name</th><th align='center'>Category</th><th align='center'>Issue Date</th><th align='center'>Due Date</th></tr>";
				try
				{
					ps.setString(1, name);
					ps.setString(2, id);
					ResultSet rs=ps.executeQuery();
					int i=1;
					while(rs.next())
					{
						mssge+="<tr><td align='center'>"+Integer.toString(i)+"."+"</td><td align='center'>"+rs.getString(1)+"</td><td align='center'>"+rs.getString(2)+"</td><td align='center'>"+rs.getString(3)+"</td><td align='center'>"+rs.getString(4)+"</td></tr>";	
						i++;
						
					}
					mssge+="</table></body></html>";
					out.print(mssge);
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


