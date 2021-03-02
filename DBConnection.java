import java.sql.*;
import java.util.Scanner;

class DBConnection{
    public Connection connect(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/foodrecommendation","root","12345");
        }catch(Exception e) {
            System.out.println(e);
        }
        return con;
    }
}