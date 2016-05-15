package DBHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class PostgresHandler {
    static public String dbOutName = "dbout";
    private Connection conn;


    public PostgresHandler(){
        conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/" + dbOutName,
                            "postgres", "123");
        }
        catch (Exception e){
            //e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to Postgres...");

    }

    public void close() throws SQLException{
        this.conn.close();
    }
}
