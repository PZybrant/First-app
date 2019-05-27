import JFXControllers.LoginController;
import SQL.DatabaseConnector;

public class Main  {

    private static DatabaseConnector databaseConnector;

    public static void main(String[] args) {
        databaseConnector = new DatabaseConnector();
        databaseConnector.createConnection();
        new LoginController().begin(args);
    }
}
