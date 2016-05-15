package DBHandler;

import java.sql.*;


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

    public void insert(int id, String txt){
        try {
            conn.setAutoCommit(false);
            String sql = "insert into table1 (id, txt) " +
                    "values " +
                    "(nextval('public.seq_id1'), ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            //stmt.setInt(1, id);
            stmt.setString(1, txt);
            int count = stmt.executeUpdate();
            if(count > 0) {
                conn.commit();
                System.out.println("Commit!");
            }
            else {
                System.out.println("0 rows changed!");
            }
            stmt.close();
        }
        catch (Exception e){
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            try{
                conn.rollback();
                System.err.println("Rollback!");
            }
            catch (Exception e1){
                System.err.println("Rollback failed!\n" + e1.getClass().getName()+ e1.getMessage());
            }
        }
    }
    public void close() throws SQLException{
        this.conn.close();
    }
}
