package DBHandler;

import RsoAggregator.Dictionary;
import RsoAggregator.Statistics;
import org.javatuples.Pair;

import java.sql.*;
import java.util.GregorianCalendar;
import java.util.Map;


public class PostgresHandler {
    public final static String[] D_CANDIDATES_COLS = {"candidate_id", "party_id"};
    public final static String D_CANDIDATES_TABLENAME = "d_candidates";
    public final static String[] D_AGE_COLS = {"age_id", "split_part(age_group, '-', 1), split_part(age_group, '-', 2)"};
    public final static String D_AGE_TABLENAME = "d_age";

    static public String dbOutName = "results";
    private Connection conn;
    private Statistics stats;
    public PostgresHandler(Statistics s){
        conn = null;
        stats = s;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/" + dbOutName,
                            "postgres", "123");
            conn.setAutoCommit(false);
        } catch (Exception e){
            //e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to Postgres...");
    }

    public void getDictionaries()
    {
        getDictData(D_CANDIDATES_TABLENAME, D_CANDIDATES_COLS);
        getDictData(D_AGE_TABLENAME, D_AGE_COLS);
    }

    private void getDictData(String dictName, String[] columns) {
        StringBuilder sb = new StringBuilder();
        for (String n : columns) {
            if (sb.length() > 0) sb.append(',');
            sb.append(n);
        }
        Statement stmt = null;
        ResultSet rs = null;
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
                    break;
                case D_AGE_TABLENAME:
                    Map<Integer, Pair<Integer, Integer>> ageMap = stats.getAgeGroupMap();
                    while (rs.next()) {
                        Pair p = new Pair<>(rs.getInt(2), rs.getInt(3));
                        ageMap.put(rs.getInt(1), p);
                        //System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
                    }
                    break;
                default:
                    break;

            }
            // switch - case END

            rs.close();
            stmt.close();
        } catch (SQLException e){
            System.err.println("Error while downloading dictionaries from DB! Error code: " + e.getClass().getName()+":"+e.getMessage());
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e1){
                System.err.println("Error while closing statement or result set! Error code: " + e1.getClass().getName()+":"+e1.getMessage());
            }
        }
    }

    public void insertStats(){
        System.out.println("Inserting data to Postgres started on " + new GregorianCalendar().getTime());
        insertResPartyCandidates();
        insertResPartyPercent();
        insertResPartyEducation();
        insertResPartySex();
        insertResPartyConstituency();
        insertResPartyAge();
        System.out.println("Inserting data to Postgres ended on " + new GregorianCalendar().getTime());
    }

    private void insertResPartyAge(){
        Map<Pair<Integer, Integer>, Integer> map = stats.getResPartyAgeMap();
        for(Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()){
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
                if(count > 0) {
                    conn.commit();
                    //System.out.println("Commit!");
                }
                else {
                    System.out.println("0 rows changed!");
                }
                stmt.close();
            } catch (Exception e){
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                try{
                    conn.rollback();
                    System.err.println("Rollback!");
                } catch (Exception e1){
                    System.err.println("Rollback failed!\n" + e1.getClass().getName()+ e1.getMessage());
                }
            }
        }
    }

    private void insertResPartyConstituency(){
        Map<Pair<Integer, Integer>, Integer> map = stats.getResPartyConstituencyMap();
        for(Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()){
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
                if(count > 0) {
                    conn.commit();
                    //System.out.println("Commit!");
                }
                else {
                    System.out.println("0 rows changed!");
                }
                stmt.close();
            } catch (Exception e){
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                try{
                    conn.rollback();
                    System.err.println("Rollback!");
                } catch (Exception e1){
                    System.err.println("Rollback failed!\n" + e1.getClass().getName()+ e1.getMessage());
                }
            }
        }
    }

    private void insertResPartySex(){
        Map<Pair<Integer, Integer>, Integer> map = stats.getResPartySexMap();
        for(Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()){
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
                if(count > 0) {
                    conn.commit();
                    //System.out.println("Commit!");
                }
                else {
                    System.out.println("0 rows changed!");
                }
                stmt.close();
            } catch (Exception e){
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                try{
                    conn.rollback();
                    System.err.println("Rollback!");
                } catch (Exception e1){
                    System.err.println("Rollback failed!\n" + e1.getClass().getName()+ e1.getMessage());
                }
            }
        }
    }

    private void insertResPartyEducation(){
        Map<Pair<Integer, Integer>, Integer> map = stats.getResPartyEducationMap();
        for(Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()){
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
                if(count > 0) {
                    conn.commit();
                    //System.out.println("Commit!");
                }
                else {
                    System.out.println("0 rows changed!");
                }
                stmt.close();
            } catch (Exception e){
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                try{
                    conn.rollback();
                    System.err.println("Rollback!");
                } catch (Exception e1){
                    System.err.println("Rollback failed!\n" + e1.getClass().getName()+ e1.getMessage());
                }
            }
        }
    }

    private void insertResPartyPercent(){
        Map<Integer, Integer> map = stats.getResPartyPercentMap();
        for(Map.Entry<Integer, Integer> entry : map.entrySet()){
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
                if(count > 0) {
                    conn.commit();
                    //System.out.println("Commit!");
                }
                else {
                    System.out.println("0 rows changed!");
                }
                stmt.close();
            } catch (Exception e){
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                try{
                    conn.rollback();
                    System.err.println("Rollback!");
                } catch (Exception e1){
                    System.err.println("Rollback failed!\n" + e1.getClass().getName()+ e1.getMessage());
                }
            }
        }
    }
    private void insertResPartyCandidates(){
        Map<Integer, Integer> map = stats.getResPartyCandidatesMap();
        for(Map.Entry<Integer, Integer> entry : map.entrySet()){
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
                if(count > 0) {
                    conn.commit();
                    //System.out.println("Commit!");
                }
                else {
                    System.out.println("0 rows changed!");
                }
                stmt.close();
            } catch (Exception e){
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                try{
                    conn.rollback();
                    System.err.println("Rollback!");
                } catch (Exception e1){
                    System.err.println("Rollback failed!\n" + e1.getClass().getName()+ e1.getMessage());
                }
            }
        }
    }
    public void insert(int id, String txt){
        try {
            String sql = "insert into table1 (id, txt) " +
                    "values " +
                    "(nextval('public.seq_id1'), ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            //stmt.setInt(1, id);
            stmt.setString(1, txt);
            int count = stmt.executeUpdate();
            if(count > 0) {
                conn.commit();
                //System.out.println("Commit!");
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
