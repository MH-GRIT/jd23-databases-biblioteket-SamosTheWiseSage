import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
public class Database {


        /*
         database contruction
         */
        static String url = "localhost";
        static int port = 3306;
        static String database = "";
        static String username = "";
        static String password = "";
        /*
        private varibles
         */
        private static Database db;
        private MysqlDataSource dataSource;
        private Database() {

        }
        private void initializeDataSource() {
            //try {

            dataSource = new MysqlDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setURL("jdbc:mysql://" + url + ":" + port + "/" + database + "?serverTimezone=UTC");
            //} catch(SQLException ex){
            //jdbc:mysq
            //}
        }
        private Connection createConnection() {
            try {
                return dataSource.getConnection();
            }catch (SQLException ex){
                return null;
            }
        }
        public static  Connection getConnection() {
            if (db == null){
                db = new Database();
                db.initializeDataSource();
            }
            return db.createConnection();
        }
        public static void PrintSQLException(SQLException sqle) {
            PrintSQLException(sqle, false);
        }
        public static void PrintSQLException(SQLException sqle, Boolean printStackTrace) {
            while (sqle != null) {
                System.out.println("\n---SQLException Caught---\n");
                System.out.println("SQLState: " + sqle.getSQLState());
                System.out.println("ErrorCode: " + sqle.getErrorCode());
                System.out.println("Message: " + sqle.getMessage());
                if (printStackTrace) sqle.printStackTrace();
                sqle = sqle.getNextException();
            }
        }
    }


