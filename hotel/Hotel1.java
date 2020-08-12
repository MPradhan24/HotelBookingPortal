/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelman;

/**
 *
 * @author user
 */
public class Hotel1 {

    /**
     * @param args the command line arguments
     */
    
    public static int loginButtonClick(String username,String password){
        int state=0;
     
        String checkUserStatus=Authenticate.authenticateUser(username,password);
        if(checkUserStatus.equals("Login Successful")){//navigate to next page
            state=1;
        }
        else if(checkUserStatus.equals("Login Not Successful")){//display incorrect password in invisible label
            state=0;
        }
        else{//User Details Not Found. Sign up message in invisible label
            state=-1;
        }
        return state;
    }
    public static String[] getHotelInfo(String cityName,String roomType,int no_of_rooms,int no_of_days){
        String hList=Authenticate.fetchHotelInCity(cityName,roomType);
        String temp[]=hList.split("$");
        /*for(int i=0;i<temp[i].length();i++){
            String h=temp[i].split(":")[0];     //to extract
            String p=temp[i].split(":")[1];
            int price=Integer.parseInt(p);
            int total_price=price*no_of_rooms*no_of_days;*/
    
            //print these
        
        return temp;
    }
    public static String[] getRoomInfo(String hName){
        String roomDetails=Authenticate.roomAmenities(hName);
        String[] temp=roomDetails.split("$");// demarcate fields later
        return temp;
    }
    
    public static int cancelBooking(String bookID){
        int rs=Authenticate.cancelBook(bookID);
        return rs;
        /*if(rs==-1){
            System.out.println("50% will be charged due to late cancellation. Cancellation Successful");
        }
        else if(rs==1)
            System.out.println("Cancellation successful");
        else
            System.out.println("Error. Record not found.");
            */
    }
}
