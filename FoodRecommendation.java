import java.sql.*;
import java.util.Scanner;

public class FoodRecommendation {

    Scanner sc = new Scanner(System.in);

    //Customer Mobile Number
    long mobileNumber;

    String customerName;

    //For static query
    Statement stmt;

    //For parameterized queries
    PreparedStatement psmt;

    ResultSet rs;

    Connection con;

    FoodRecommendation (Connection con) {
        this.con = con;
    }

    public void welcomeMessage() throws SQLException {
        //Creating a statement object.
        stmt = con.createStatement();

        System.out.println("Welcome to Food Cart. Please enter your name.");

        //Read customer name from command line
        customerName = sc.nextLine();

        System.out.println("Hi " + customerName + ".");

        System.out.println("Please enter your mobile number");

        //Read customer mobile number from command line
        mobileNumber = sc.nextLong();

        System.out.println("Please enter restaurant's code to order food");

        rs = stmt.executeQuery("select id, restaurant from restaurants");

        //Display list of restaurants from restaurant table
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " " + rs.getString("restaurant"));
        }
    }

    public void recommendFood() throws SQLException {

        //Read restaurant code from command line
        int restaurant_code = sc.nextInt();

        sc.nextLine();

        //Query to check if user already has any order history with respect to chosen restaurant
        psmt = con.prepareStatement("select ordered_items from userdata " +
                "where id = ? and restaurant_code = ?");

        psmt.setLong(1, mobileNumber);

        psmt.setInt(2, restaurant_code);

        ResultSet rs = psmt.executeQuery();

        //If there are no previous orders with chosen restaurant
        if (!rs.next()) {

            System.out.println("New customer");

            //Method to display full menu for new users
            newUserOrderRecommendation(restaurant_code);

            con.close();

        }

        else {

            existingUserOrderRecommendations(restaurant_code);

            con.close();
        }
    }

    //Food recommendation for new customer
    public void newUserOrderRecommendation(int restaurant_code) throws SQLException {

        //Display full menu
        displayMenu(restaurant_code);

        System.out.println("Choose your favourite food to order");

        //Read order details from command line
        String ordered_items = sc.nextLine();

        //Insert customer chosen dishes into database
        psmt = con.prepareStatement("INSERT INTO userdata(id, restaurant_code, ordered_items, user_name) " +
                "VALUES (?,?,?,?)");

        psmt.setLong(1, mobileNumber);

        psmt.setInt(2, restaurant_code);

        psmt.setString(3, ordered_items);

        psmt.setString(4, customerName);

        int i = psmt.executeUpdate();

        System.out.println("Order Placed Successfully");

    }

    //Display list of dishes available in a restaurant
    public void displayMenu(int restaurant_code) throws SQLException {

        System.out.println("Here is the menu :");

        psmt = con.prepareStatement("select menu from restaurants where id = ?");

        psmt.setInt(1, restaurant_code);

        rs = psmt.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("menu"));
        }
    }

    //Food recommendation for new customer
    public void existingUserOrderRecommendations(int restaurant_code) throws SQLException {

        System.out.println("Your previous orders");

        //Query to fetch previous orders based on mobile number and restaurant code
        psmt = con.prepareStatement("select ordered_items from userdata where id = ? and restaurant_code = ?");

        psmt.setLong(1, mobileNumber);

        psmt.setInt(2, restaurant_code);

        rs = psmt.executeQuery();

        if (rs.next()) {
            System.out.println(rs.getString("ordered_items"));
        }

        System.out.println("Would you like to go with previous orders? (Y/N)");

        String response = sc.nextLine();

        if (response.equals("Y")) {

            System.out.println("Choose your dish from previous orders");

            updateExistingOrder(mobileNumber, restaurant_code);
        }

        else {
              displayMenu(restaurant_code);

              updateExistingOrder(mobileNumber, restaurant_code);
        }

    }

    //Method to update customer's existing orders
    public void updateExistingOrder(long id, int restaurant_code) throws SQLException {

        String updateOrder = ", "+sc.nextLine();

        psmt = con.prepareStatement("update userdata set ordered_items = CONCAT(ordered_items,?) " +
                "where id = ? and restaurant_code = ?");

        psmt.setString(1, updateOrder);

        psmt.setLong(2, id);

        psmt.setInt(3, restaurant_code);

        psmt.executeUpdate();

        System.out.println("Order Placed Successfully");
    }


}
