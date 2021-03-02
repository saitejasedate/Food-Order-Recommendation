import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

public class Main {
    public static void main (String[] args) throws SQLException {
        //DBConnection class has code to connect MySQL database.
        DBConnection dbConnection = new DBConnection();

        //Using DBConnection class object, calling "connect" method.
        Connection con = dbConnection.connect();

        //After successful DB connection.
        FoodRecommendation foodRecommendation = new FoodRecommendation(con);

        //Display Welcome Message.
        foodRecommendation.welcomeMessage();

        //Food Recommendation code.
        try {
            foodRecommendation.recommendFood();
        } catch (SQLNonTransientConnectionException e) {
            System.out.println("Exit successful");
        }
    }
}
