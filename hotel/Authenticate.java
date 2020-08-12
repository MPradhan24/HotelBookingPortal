/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelman;
import java.sql.*;
import java.math.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date; 
import java.time.temporal.ChronoUnit;
/**
 *
 * @author user
 */
public class Authenticate {
    public static Connection con=null;
    public static int no_of_rooms_occupied[]=new int[1010];//this is global, for status 
    public static int rooms_made_free[]=new int[1010];
    public static Connection connect(){
        try{
            if(con==null){
           Class.forName("con.mysql.jdbc.Driver");
           con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+"Database name(oops)","root","password");
            }
        }catch(SQLException | ClassNotFoundException ex){
           System.out.println(ex.getMessage());
        }
        return con;
    }
    public static String authenticateUser(String uname,String password){
        String validUser="";
        try{
            Connection con=connect();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select * from user_details where username= "+uname);
            if(rs.next()){
                Statement st1=con.createStatement();
                ResultSet rs1=st.executeQuery("select * from user_details where username= "+uname+" and password= "+password);
                if(rs1.next()){
                    validUser="Login Successful";
                }
                else{
                    validUser="Login Not Successful";
                }
            }
            else{
                validUser="User Details Not Found. Please Sign Up First";
            }
            return validUser;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return validUser;
    }
    public static String fetchHotelInCity(String cityName,String roomType){
        String hotelList="";
        try{
            Connection con=connect();
            Statement st=con.createStatement();
            ResultSet rs;
            if(roomType.equals("Normal"))    
                rs=st.executeQuery("select name,price from hotel where city= "+cityName+"and rowcount<=4 order by price");
            else
                rs=st.executeQuery("select name,lux_price from hotel where city= "+cityName+"and rowcount<=4 order by price");
            while(rs.next()){
                String hName=rs.getString(0);//or 1 check
                String p=rs.getString(1);
                hotelList+=hName+":"+p+"$";//delimeters
            }
            return hotelList;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
             
        }
        return hotelList;
    }
    public static String roomAmenities(String hName){
        String roomDetails="";
        try{
            Connection con=connect();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select amenities from hotel where name= "+hName);//facilities
            rs.next();
            roomDetails=rs.getString(0);
            return roomDetails;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return roomDetails;
    }
    public static int cancelBook(String bookID){
        int rs=1,hotel_serial_no=0;
        try{
            Connection con=connect();
            Statement st1=con.createStatement();
            Statement st2=con.createStatement();
            ResultSet rs1=st1.executeQuery("select date_of_checkin,no_of_rooms,hotel_no from bookings where booking_id="+bookID);
            int rs2=st2.executeUpdate("delete from bookings where booking_id="+bookID);
            rs1.next();
            String ch_indate=rs1.getString(0);
            Date date1=new SimpleDateFormat("yyyy/MM/dd").parse(ch_indate);
            long milli_ch=date1.getTime();//from 1 Jan 1970
            hotel_serial_no=Integer.parseInt(rs1.getString(2));
            rooms_made_free[hotel_serial_no]=Integer.parseInt(rs1.getString(1));
            long milli_cancel=System.currentTimeMillis();
            int diff_in_Days=(int)(Math.abs(milli_cancel-milli_ch))/(1000*60*60*24);
            if(diff_in_Days<3)
                rs=-1;
            if(rs2!=0)
                no_of_rooms_occupied[hotel_serial_no]-=rooms_made_free[hotel_serial_no];
            if(rs2==0)
                rs=0;
            return rs;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        firstAdmittableFromWait(hotel_serial_no);
        return rs;
    }
    public static void firstAdmittableFromWait(int hSerial){
        int b_id=0;
        try{
            
            Connection con=connect();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select booking_id,no_of_rooms from bookings where status="+0);
          
            while(rs.next()){                
                int reqdRooms=Integer.parseInt(rs.getString(1));
                if(reqdRooms<=rooms_made_free[hSerial]){
                    b_id=Integer.parseInt(rs.getString(0));  
                    break;
                }
            }
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        finally{
            if(b_id!=0)
                Insertion.WL_CHANGE(b_id);//arnav's function
        }
    }
    public static void LogOut(){
        try{
            con=null;
            con.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }   
    }
}
