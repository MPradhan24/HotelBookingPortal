
import hotelman.Authenticate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arnav Buch
 */
public class Insertion {
    
    static final String JBDC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/";
    
    static final String user = " ";
    static final String pass=" ";
   
    static int b_id=0;
    
    public static Connection connect()
    {
        Connection conn = null;
	    	
                try 
	    	{
	    		Class.forName("com.mysql.jdbc.Driver");
	    		conn = DriverManager.getConnection(DB_URL, user, pass);
                }
	    	catch(SQLException se) 
	    	{
	    	
	    		se.printStackTrace();
	    	}
	    	catch(Exception e) 
	    	{
	    		
	    		e.printStackTrace();
	    	}
	    	
                return conn;
    }
    
    public static int SignUp(String username, String password, String confirm, String name, String dob, String address, String email, String aadhaar)
    {
    Connection conn = connect();
    try  //try block
    {       
    if (username.equals("")|| password.equals("")|| name.equals("")|| dob.equals("")|| address.equals("")|| name.equals("")|| email.equals("")|| aadhaar.equals(""))
    {
     return 1; //user and/or pass is null
    }
    else if (!password.equals(confirm))
    {
       return 2; //pass and conf not equal
    }
    else  //else insert query is run properly
 {
    String IQuery = "INSERT INTO user_details VALUES(?,?,?,?,?,?,?)";
    
    PreparedStatement statement = conn.prepareStatement(IQuery);
    statement.setString(1, name);
    statement.setString(2, dob);
    statement.setString(3, address);
    statement.setString(4, email);
    statement.setString(5, username);
    statement.setString(6, password);
    statement.setString(7, aadhaar);

    int rows = statement.executeUpdate();
    } 
    }
    catch (SQLException se) 
    {
    //handle errors for JDBC
    se.printStackTrace();
    }
    catch (Exception a) //catch block
    {
    a.printStackTrace();
    }
  
    return 0; 
  }
    
    public static int modify(int b_id, String in_new, String out_new, int num_rooms, int num_people, String r_type)
    {
        Connection conn = connect();
        try{
            
            if(num_people>num_rooms*2)
            {
                return -1;
            }
            String sql = "UPDATE Users SET date_of_checkin=?, date_of_checkout=?, no_of_rooms=?, type_of_room=?, email=? WHERE booking_id=?";
 
            PreparedStatement statement = conn.prepareStatement(sql);
            
            statement.setString(1, in_new);
            statement.setString(2, out_new);
            statement.setInt(3, num_rooms);
            statement.setInt(4, num_people);
            statement.setString(5, r_type);
            statement.setInt(6, b_id);
    
             //System.out.println("Connected database successfully...");
    
            int rows = statement.executeUpdate();
        
        }
         catch (SQLException se) 
        {
            //handle errors for JDBC
            se.printStackTrace();
        }
        catch (Exception a) //catch block
        {
            a.printStackTrace();
        }
        return 0;
    }  
    
    public static int Book(String name, String dob, String doi, String doo, String hname, int num_room, int num_people, String type)
    {
    Connection conn = connect();
    
    try  //try block
    {
    int cost;
        String sql = "SELECT hotel_no, rooms, cost_normal, cost_luxury from hotel WHERE name ="+hname;
    
        Statement stm = conn.createStatement();
    
    ResultSet num_temp = stm.executeQuery(sql);
    
    
    int num = Integer.parseInt(num_temp.getString(0));
    int room= Integer.parseInt(num_temp.getString(1));
    int cost_n = Integer.parseInt(num_temp.getString(2));
    int cost_l= Integer.parseInt(num_temp.getString(3));
    
    if(type == "Normal")
    {
        cost=cost_n;
    }
    else
    {
        cost=cost_l;
    }
    //int temp = Authenticate.no_of_rooms_occupied[num];
    if(Authenticate.no_of_rooms_occupied[num]+num_room>room)
            {
                return -1; //WL
            }
    else
    {
        b_id++;
        String IQuery = "INSERT INTO bookings (`booking_id`,`name`,`date_of_booking`,`date_of_checkin`,`date_of_checkout`,`cost`,`hotel`,`no_of_rooms`,`no_of_people`,`type_of_room`,`status`) VALUES(?,?,?,?,?,?,?,?,?,?.?)";
        PreparedStatement statement = conn.prepareStatement(IQuery);
        statement.setInt(1, b_id);
        statement.setString(2, name);
        statement.setString(3, dob);
        statement.setString(4, doi);
        statement.setString(5, doo);
        statement.setInt(6, cost);
        statement.setString(7, hname);
        statement.setInt(8, num_room);
        statement.setInt(9, num_people);
        statement.setString(10, type);
        statement.setInt(11, 1);  
        
        int rows = statement.executeUpdate();
    }  
}
    catch(SQLException se)
    {
        System.out.println(se.getMessage());
    }
    return 1;
}
    
 public static void WL(String name, String dob, String doi, String doo, String hname, int num_room, int num_people, String type)
    {
    Connection conn = connect();
    
    try  //try block
    {
    int cost;
        String sql = "SELECT hotel_no, rooms, cost_normal, cost_luxury from hotel WHERE name ="+hname;
    Statement stm = conn.createStatement();
    
    ResultSet num_temp = stm.executeQuery(sql);
    
    
    int num = Integer.parseInt(num_temp.getString(0));
    int room= Integer.parseInt(num_temp.getString(1));
    int cost_n = Integer.parseInt(num_temp.getString(2));
    int cost_l= Integer.parseInt(num_temp.getString(3));
    
    if(type == "Normal")
    {
        cost=cost_n;
    }
    else
    {
        cost=cost_l;
    }
        b_id++;
        String IQuery = "INSERT INTO bookings (`booking_id`,`name`,`date_of_booking`,`date_of_checkin`,`date_of_checkout`,`cost`,`hotel`,`no_of_rooms`,`no_of_people`,`type_of_room`,`status`) VALUES(?,?,?,?,?,?,?,?,?,?.?)";
        PreparedStatement statement = conn.prepareStatement(IQuery);
        statement.setInt(1, b_id);
        statement.setString(2, name);
        statement.setString(3, dob);
        statement.setString(4, doi);
        statement.setString(5, doo);
        statement.setInt(6, cost);
        statement.setString(7, hname);
        statement.setInt(8, num_room);
        statement.setInt(9, num_people);
        statement.setString(10, type);
        statement.setInt(11, 0); 
        
        int rows=statement.executeUpdate();
    }  
    catch(SQLException se)
    {
        System.out.println(se.getMessage());
    }
}  
 
public static void WL_CHANGE(int BId)
{
    Connection conn = connect();
    
    try
    {
        String UQuery = "UPDATE bookings SET status= ? WHERE booking_id=?";
        PreparedStatement statement = conn.prepareStatement(UQuery);
        
        statement.setInt(0,1);
        statement.setInt(1,BId);
        
    }
    catch (SQLException ex)
    {
        System.out.println(ex.getMessage());
    }
}
}