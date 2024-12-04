package com.mayab.quality.unittest;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App{
    public static void main(String args[]) throws SQLException {
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3307/calidad";
            System.out.println("jdbcurl=" + dbURL);
            String strUserID = "root";
            String strPassword = "123456";
            con=DriverManager.getConnection(dbURL,strUserID,strPassword);
            System.out.println("Connected to the database.");
            java.sql.Statement stmt=con.createStatement();
            System.out.println("Executing query");
            ResultSet rs=stmt.executeQuery("SELECT * FROM usuarios");
            while(rs.next())
                System.out.println(rs.getInt("1"));
            con.close();
        }catch(Exception e){ System.out.println(e);}
        finally {
            con.close();
        }
}
}
