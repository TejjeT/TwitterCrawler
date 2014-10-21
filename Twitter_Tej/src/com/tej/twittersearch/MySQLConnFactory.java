package com.tej.twittersearch;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQLConnFactory {

	private static Connection connection = null;
    private static Statement statement = null;
    
    static
    {
    	String Url = "jdbc:MySql://localhost:3306/twitter dataset?" + "user=root";
    	 try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Url);
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
    public static Statement getsqlstatementforExec()
    {
    	
    	try {
			if(connection!=null&&!connection.isClosed())
			{
				 Class.forName("com.mysql.jdbc.Driver");
		            String Url = "jdbc:MySql://localhost:3306/twitter dataset?"  + "user=root";
			 connection = DriverManager.getConnection(Url);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	try {
			if(statement!=null&&!statement.isClosed())
			    statement = connection.createStatement();
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		return statement;
    }
    
    
    public static void closeAll()
    {
    	try {
			if(connection!=null&&!connection.isClosed())
			{
			 connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	try {
			if(statement!=null&&!statement.isClosed())
			    statement.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    }
}
