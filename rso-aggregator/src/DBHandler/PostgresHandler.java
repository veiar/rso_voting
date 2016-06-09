package DBHandler;

import RsoAggregator.Statistics;
import org.javatuples.Pair;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import static RsoAggregator.RsoAggregator.logger;
public class PostgresHandler {
    public final static String[] D_CANDIDATES_COLS = {"candidate_id", "party_id"};
    public final static String D_CANDIDATES_TABLENAME = "d_candidates";
    public final static String[] D_AGE_COLS = {"age_id", "split_part(age_group, '-', 1), split_part(age_group, '-', 2)"};
    public final static String D_AGE_TABLENAME = "d_age";
    //private static String postgresAddress[] = {"52.40.243.126"};
    private static String m_userName = "postgres";
    private static String m_password = "admin123";//"123";
    private static String m_dbName = "results";
    private static String m_dbName2 = "results2";   // TODO tylko do testow!
    private static String m_port = "5432";

    private List<PostgresInstance> mapPostgresInstances;

    //private Connection conn;
    private Statistics stats;
    public PostgresHandler(Statistics s){
        //conn = null;
        stats = s;
        mapPostgresInstances = new ArrayList<>();
        getProperties();
        while(!setConnectionToAllPostgres()){
            logger.log(Level.SEVERE, "Unable to connect to any Postgres instance, retrying in 5 sec...");
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e){
                logger.log(Level.SEVERE, "Internal error!");
            }
        }

/*
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager
                    .getConnection("jdbc:postgresql://127.0.0.1:5432/" + dbOutName,       //52.36.29.80:5432
                            "postgres", "123");                                        // pass: admin123
            conn.setAutoCommit(false);
        } catch (Exception e){
            //e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            //System.exit(0);
        }*/
        System.out.println("Connected to Postgres...");
    }

    private class PostgresInstance {
        private String user;
        private String pass;
        private String dbName;
        private String dbAddress;

        private Connection conn;
        boolean active;

        public Connection getConnection(){
            return this.conn;
        }
        public void close(){
            try{
                if(this.active && conn != null) {
                    this.conn.close();
                }
            } catch (SQLException e){
                logger.log(Level.SEVERE, "Failed to close connection with Postgres!");
            }
        }

        private boolean isActive(){
            Statement stmt = null;
            try{
                stmt = conn.createStatement();
                String query = "select 1;";
                stmt.executeQuery(query);
                this.active = true;
            } catch (Exception e){
                this.active = false;
                if(stmt != null){
                    try {
                        stmt.close();
                    }catch (SQLException e1){
                    }
                }
            }
            return this.active;
        }

        PostgresInstance(String _user, String _pass, String _dbName, String _dbAddress) {
            this.user = _user;
            this.pass = _pass;
            this.dbName = _dbName;
            this.dbAddress = _dbAddress;
            this.conn = null;
            this.active = false;
        }

        public boolean setConnection(){
            try{
                Class.forName("org.postgresql.Driver");
                conn = DriverManager
                        .getConnection("jdbc:postgresql://" + dbAddress + "/" + dbName,
                                user, pass);
                conn.setAutoCommit(false);
                this.active = true;
                logger.log(Level.INFO, "Connected to Postgres! " + dbAddress + " " + dbName);
            } catch (Exception e){
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                logger.log(Level.SEVERE, "Failed to connect to Postgres! " + dbAddress);
                return false;
            }
            return true;
        }
    }

    private boolean setConnectionToAllPostgres(){
        int postgresCount = 0;
        for(PostgresInstance pi : mapPostgresInstances){
            if(pi.setConnection()) {
                postgresCount++;
            }
        }

        return postgresCount > 0;
    }

    private void getProperties() {

        Map<String, String> env = System.getenv();
        if (!env.containsKey("MONGO_HOSTS") || !env.containsKey("POSTGRES_HOSTS")) {
            System.err.println("No environment variables set! Bye!");
            logger.log(Level.SEVERE, "No environment variables set! Bye!");
            System.exit(2);
        }
        String postgresAllHosts = env.get("POSTGRES_HOSTS");
        String[] postgresHosts = postgresAllHosts.split(",");

        for (String t : postgresHosts) {
            PostgresInstance pi = new PostgresInstance(
                    m_userName,
                    m_password,
                    m_dbName,
                    t + ":" + m_port
            );
            mapPostgresInstances.add(pi);
        }

        //try {
          /*  PostgresInstance pi = new PostgresInstance(
                    m_userName,
                    m_password,
                    m_dbName,
                    postgresAddress[0] + ":" + m_port
            );
            mapPostgresInstances.add(pi);
            /*Scanner scanner = new Scanner(new File("/etc/hosts")).useDelimiter("\n");
            int i = 0;
            while (scanner.hasNext()){
                String content = scanner.next();
                System.out.println(content);
                String[] tab = content.split("\t");
                if(tab.length > 1){
                    if(tab[1].equals("POSTGRES_HOSTS")){
                        System.out.println("\tFound POSTGRES_HOST! " + tab[0]);
                        i++;
                        PostgresInstance pi = new PostgresInstance(
                                m_userName,
                                m_password,
                                m_dbName,
                                tab[0] + ":" + m_port
                        );
                        mapPostgresInstances.add(pi);
                    }
                }

            }
            if(i == 0){
                System.err.println("No POSTGRES_HOSTS found in /etc/hosts! Bye!");
                logger.log(Level.SEVERE, "No POSTGRES_HOSTS found in /etc/hosts! Bye!");
                System.exit(2);
            }*/
/*
        } catch (Exception e){
            System.err.println("Exception while reading /etc/hosts! Bye!");
            logger.log(Level.SEVERE, "Exception while reading /etc/hosts! Bye!");
            System.exit(2);

        }*/
        /*
        Map<String, String> env = System.getenv();
        if (!env.containsKey("MONGO_HOSTS") || !env.containsKey("POSTGRES_HOSTS")) {
            System.err.println("No environment variables set! Bye!");
            logger.log(Level.SEVERE, "No environment variables set! Bye!");
            System.exit(2);
        }
        String postgresAllHosts = env.get("POSTGRES_HOSTS");
        String[] postgresHosts = postgresAllHosts.split(",");
        boolean lol = false;
        for (String t : postgresHosts) {
            PostgresInstance pi = new PostgresInstance(
                    m_userName,
                    m_password,
                    lol? m_dbName : m_dbName2,      // TODO wypieprzyc w wersji produkcyjnej
                    t + ":" + m_port
            );
            lol = true;
            mapPostgresInstances.add(pi);
        }*/
    }

    public void getDictionaries()
    {
        logger.log(Level.INFO, "Getting dictonaries from Postgres...");
        getDictData(D_CANDIDATES_TABLENAME, D_CANDIDATES_COLS);
        getDictData(D_AGE_TABLENAME, D_AGE_COLS);
        logger.log(Level.INFO, "Getting dictonaries from Postgres succesfull!");
    }

    private boolean getDictData(String dictName, String[] columns) {
        StringBuilder sb = new StringBuilder();
        for (String n : columns) {
            if (sb.length() > 0) sb.append(',');
            sb.append(n);
        }
        Statement stmt = null;
        ResultSet rs = null;
        boolean dataDownloaded = false;
        for(PostgresInstance pi : mapPostgresInstances) {
            if(dataDownloaded){
                break;
            }
            if (!pi.isActive()) {
                continue;
            }
            Connection conn = pi.getConnection();
            try {
                stmt = conn.createStatement();
                String query = "select " + sb.toString() + " from " + dictName + ";";
                rs = stmt.executeQuery(query);

                // switch - case START
                switch (dictName) {
                    case D_CANDIDATES_TABLENAME:
                        Map<Integer, Integer> candidateMap = stats.getDictCandidateMap();
                        while (rs.next()) {
                            candidateMap.put(rs.getInt(1), rs.getInt(2));
                            //System.out.println(rs.getString(1) + " " + rs.getString(2));
                        }
                        System.out.println("candidateMap size = " + candidateMap.size());
                        logger.log(Level.INFO, "candidateMap size = " + candidateMap.size());
                        break;
                    case D_AGE_TABLENAME:
                        Map<Integer, Pair<Integer, Integer>> ageMap = stats.getAgeGroupMap();
                        while (rs.next()) {
                            Pair p = new Pair<>(rs.getInt(2), rs.getInt(3));
                            ageMap.put(rs.getInt(1), p);
                            //System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
                        }
                        System.out.println("ageMap size = " + ageMap.size());
                        logger.log(Level.INFO, "ageMap size = " + ageMap.size());
                        break;
                    default:
                        break;

                }
                // switch - case END

                rs.close();
                stmt.close();
                dataDownloaded = true;
                logger.log(Level.INFO, "Dictionary " + dictName + " downloaded from Postgres! ");
            } catch (SQLException e) {
                System.err.println("Error while downloading dictionaries from DB! Error code: " + e.getClass().getName() + ":" + e.getMessage());
                logger.log(Level.SEVERE, "Error while downloading dictionaries from DB!");
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e1) {
                    System.err.println("Error while closing statement or result set! Error code: " + e1.getClass().getName() + ":" + e1.getMessage());
                }
            }
        }
        return dataDownloaded;
    }

    public void insertStats(){
        System.out.println("Inserting data to Postgres started on " + new GregorianCalendar().getTime());
        logger.log(Level.INFO, "Inserting data to Postgres started...");
        insertResPartyCandidates();
        insertResPartyPercent();
        insertResPartyEducation();
        insertResPartySex();
        insertResPartyConstituency();
        insertResPartyAge();
        System.out.println("Inserting data to Postgres ended on " + new GregorianCalendar().getTime());
        logger.log(Level.INFO, "Inserting data to Postgres ended!");
    }

    private void insertResPartyAge(){
        logger.log(Level.INFO, "Inserting data to ResPartyAge started...");
        Map<Pair<Integer, Integer>, Integer> map = stats.getResPartyAgeMap();
        for(PostgresInstance pi : mapPostgresInstances) {
            if (!pi.isActive())
                continue;
            Connection conn = pi.getConnection();
            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()) {
                try {
                    String sql =
                            "insert into res_party_age (party_id, age_id, votes_sum) " +
                                    "values " +
                                    "(?, ?, ?)" +
                                    "on conflict (party_id, age_id) do update set " +
                                    "votes_sum = excluded.votes_sum;";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, entry.getKey().getValue0());
                    stmt.setInt(2, entry.getKey().getValue1());
                    stmt.setInt(3, entry.getValue());
                    int count = stmt.executeUpdate();
                    if (count > 0) {
                        conn.commit();
                        //System.out.println("Commit!");
                    } else {
                        System.out.println("0 rows changed!");
                    }
                    stmt.close();
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    logger.log(Level.SEVERE, "Error!");
                    try {
                        conn.rollback();
                        System.err.println("Rollback!");
                    } catch (Exception e1) {
                        System.err.println("Rollback failed!\n" + e1.getClass().getName() + e1.getMessage());
                        logger.log(Level.SEVERE, "Error!");
                    }
                }
            }
        }
        logger.log(Level.INFO, "Inserting data to ResPartyAge done!");
    }

    private void insertResPartyConstituency(){
        logger.log(Level.INFO, "Inserting data to ResPartyConstituency started...");
        Map<Pair<Integer, Integer>, Integer> map = stats.getResPartyConstituencyMap();
        for(PostgresInstance pi : mapPostgresInstances) {
            if (!pi.isActive())
                continue;
            Connection conn = pi.getConnection();
            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()) {
                try {
                    String sql =
                            "insert into res_party_constituency (party_id, constituency_id, votes_sum) " +
                                    "values " +
                                    "(?, ?, ?)" +
                                    "on conflict (party_id, constituency_id) do update set " +
                                    "votes_sum = excluded.votes_sum;";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, entry.getKey().getValue0());
                    stmt.setInt(2, entry.getKey().getValue1());
                    stmt.setInt(3, entry.getValue());
                    int count = stmt.executeUpdate();
                    if (count > 0) {
                        conn.commit();
                        //System.out.println("Commit!");
                    } else {
                        System.out.println("0 rows changed!");
                    }
                    stmt.close();
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    logger.log(Level.SEVERE, "Error!");
                    try {
                        conn.rollback();
                        System.err.println("Rollback!");
                    } catch (Exception e1) {
                        System.err.println("Rollback failed!\n" + e1.getClass().getName() + e1.getMessage());
                        logger.log(Level.SEVERE, "Error!");
                    }
                }
            }
        }
        logger.log(Level.INFO, "Inserting data to ResPartyConstituency done!");
    }

    private void insertResPartySex(){
        logger.log(Level.INFO, "Inserting data to ResPartySex started...");
        Map<Pair<Integer, Integer>, Integer> map = stats.getResPartySexMap();
        for(PostgresInstance pi : mapPostgresInstances) {
            if (!pi.isActive())
                continue;
            Connection conn = pi.getConnection();
            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()) {
                try {
                    String sql =
                            "insert into res_party_sex (party_id, sex_id, votes_sum) " +
                                    "values " +
                                    "(?, ?, ?)" +
                                    "on conflict (party_id, sex_id) do update set " +
                                    "votes_sum = excluded.votes_sum;";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, entry.getKey().getValue0());
                    stmt.setInt(2, entry.getKey().getValue1());
                    stmt.setInt(3, entry.getValue());
                    int count = stmt.executeUpdate();
                    if (count > 0) {
                        conn.commit();
                        //System.out.println("Commit!");
                    } else {
                        System.out.println("0 rows changed!");
                    }
                    stmt.close();
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    logger.log(Level.SEVERE, "Error!");
                    try {
                        conn.rollback();
                        System.err.println("Rollback!");
                    } catch (Exception e1) {
                        System.err.println("Rollback failed!\n" + e1.getClass().getName() + e1.getMessage());
                        logger.log(Level.SEVERE, "Error!");
                    }
                }
            }
        }
        logger.log(Level.INFO, "Inserting data to ResPartySex done!");
    }

    private void insertResPartyEducation(){
        logger.log(Level.INFO, "Inserting data to ResPartyEducation started...");
        Map<Pair<Integer, Integer>, Integer> map = stats.getResPartyEducationMap();
        for(PostgresInstance pi : mapPostgresInstances) {
            if (!pi.isActive())
                continue;
            Connection conn = pi.getConnection();
            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()) {
                try {
                    String sql =
                            "insert into res_party_education (party_id, education_id, votes_sum) " +
                                    "values " +
                                    "(?, ?, ?)" +
                                    "on conflict (party_id, education_id) do update set " +
                                    //"party_id = excluded.party_id, " +
                                    "votes_sum = excluded.votes_sum;";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, entry.getKey().getValue0());
                    stmt.setInt(2, entry.getKey().getValue1());
                    stmt.setInt(3, entry.getValue());
                    int count = stmt.executeUpdate();
                    if (count > 0) {
                        conn.commit();
                        //System.out.println("Commit!");
                    } else {
                        System.out.println("0 rows changed!");
                    }
                    stmt.close();
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    logger.log(Level.SEVERE, "Error!");
                    try {
                        conn.rollback();
                        System.err.println("Rollback!");
                    } catch (Exception e1) {
                        System.err.println("Rollback failed!\n" + e1.getClass().getName() + e1.getMessage());
                        logger.log(Level.SEVERE, "Error!");
                    }
                }
            }
        }
        logger.log(Level.INFO, "Inserting data to ResPartyEducation done!");
    }

    private void insertResPartyPercent(){
        logger.log(Level.INFO, "Inserting data to ResPartyPercent started...");
        Map<Integer, Integer> map = stats.getResPartyPercentMap();
        for(PostgresInstance pi : mapPostgresInstances) {
            if (!pi.isActive())
                continue;
            Connection conn = pi.getConnection();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                try {
                    String sql =
                            "insert into res_party_percent (party_id, votes_sum) " +
                                    "values " +
                                    "(?, ?)" +
                                    "on conflict (party_id) do update set " +
                                    //"party_id = excluded.party_id, " +
                                    "votes_sum = excluded.votes_sum;";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, entry.getKey());
                    stmt.setInt(2, entry.getValue());
                    int count = stmt.executeUpdate();
                    if (count > 0) {
                        conn.commit();
                        //System.out.println("Commit!");
                    } else {
                        System.out.println("0 rows changed!");
                    }
                    stmt.close();
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    logger.log(Level.SEVERE, "Error!");
                    try {
                        conn.rollback();
                        System.err.println("Rollback!");
                    } catch (Exception e1) {
                        System.err.println("Rollback failed!\n" + e1.getClass().getName() + e1.getMessage());
                        logger.log(Level.SEVERE, "Error!");
                    }
                }
            }
        }
        logger.log(Level.INFO, "Inserting data to ResPartyPercent done!");
    }
    private void insertResPartyCandidates(){
        logger.log(Level.INFO, "Inserting data to ResPartyCandidates started...");
        Map<Integer, Integer> map = stats.getResPartyCandidatesMap();
        for(PostgresInstance pi : mapPostgresInstances) {
            if(!pi.isActive())
                continue;
            Connection conn = pi.getConnection();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                try {
                    String sql =
                            "insert into res_party_candidates (candidate_id, votes_sum) " +
                                    "values " +
                                    "(?, ?)" +
                                    "on conflict (candidate_id) do update set " +
                                    //"candidate_id = excluded.candidate_id, " +
                                    "votes_sum = excluded.votes_sum;";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, entry.getKey());
                    stmt.setInt(2, entry.getValue());
                    int count = stmt.executeUpdate();
                    if (count > 0) {
                        conn.commit();
                        //System.out.println("Commit!");
                    } else {
                        System.out.println("0 rows changed!");
                    }
                    stmt.close();
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    logger.log(Level.SEVERE, "Error!");
                    try {
                        conn.rollback();
                        System.err.println("Rollback!");
                    } catch (Exception e1) {
                        System.err.println("Rollback failed!\n" + e1.getClass().getName() + e1.getMessage());
                        logger.log(Level.SEVERE, "Error!");
                    }
                }
            }
        }
        logger.log(Level.INFO, "Inserting data to ResPartyCandidates done!");
    }

    public void close() throws SQLException {
        for (PostgresInstance pi : mapPostgresInstances) {
            pi.close();
        }
    }
}
