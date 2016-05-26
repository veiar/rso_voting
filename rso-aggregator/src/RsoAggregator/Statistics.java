package RsoAggregator;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Statistics extends Dictionary{
    private Map<Integer, Integer> resPartyCandidatesMap;    // <candidate_id, votes_sum>
    private Map<Integer, Integer> resPartyPercentMap;    // <party_id, votes_sum>
    private Map<Pair<Integer, Integer>, Integer> resPartyAgeMap;    // <(party_id, age_id), votes_sum>
    private Map<Pair<Integer, Integer>, Integer> resPartyEducationMap;    // <(candidate_id, education_id), votes_sum>
    private Map<Pair<Integer, Integer>, Integer> resPartySexMap;    // <(candidate_id, sex_id), votes_sum>
    private Map<Pair<Integer, Integer>, Integer> resPartyConstituencyMap;    // <(candidate_id, constituency_id), votes_sum>

    public Statistics(){
        this.resPartyCandidatesMap = new HashMap<>();
        this.resPartyPercentMap = new HashMap<>();
        this.resPartyEducationMap = new HashMap<>();
        this.resPartySexMap = new HashMap<>();
        this.resPartyConstituencyMap = new HashMap<>();
        this.resPartyAgeMap = new HashMap<>();
    }
//select age_id, age_group, split_part(age_group, '-', 1), split_part(age_group, '-', 2) from d_age ;

    public Map<Integer, Integer> getResPartyCandidatesMap() {return this.resPartyCandidatesMap; }
    public Map<Integer, Integer> getResPartyPercentMap() {return this.resPartyPercentMap; }
    public Map<Pair<Integer, Integer>, Integer> getResPartyAgeMap() {return this.resPartyAgeMap; }
    public Map<Pair<Integer, Integer>, Integer> getResPartyEducationMap() {return this.resPartyEducationMap; }
    public Map<Pair<Integer, Integer>, Integer> getResPartySexMap() {return this.resPartySexMap; }
    public Map<Pair<Integer, Integer>, Integer> getResPartyConstituencyMap() {return this.resPartyConstituencyMap; }
    public int calcAgeFromPesel(String pesel){
        Date now = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyMMdd");
        long diff = 0;
        try{
            Date birth = df.parse(pesel.substring(0, 6));
            diff = (now.getTime() - birth.getTime())/ 1000 / 60 / 60 / 24 / 365;
            if(diff < 18){
                diff += 100;
            }
            return (int)diff;
        }
        catch (Exception e){
            System.err.println(e.getClass().getName()+ e.getMessage());
        }
        return 0;
    }
    private int getAgeIdBasedOnAge(int age){
        final Map<Integer, Pair<Integer, Integer>> d_ageGroupMap = getAgeGroupMap();
        for(Map.Entry<Integer, Pair<Integer, Integer>> entry : d_ageGroupMap.entrySet()) {
            if (age >= entry.getValue().getValue0() && age <= entry.getValue().getValue1()) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public void calcResPartyAge(AggregateIterable output){
        final Map<Integer, Integer> candPartyMap = this.getDictCandidateMap();
        output.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                String pesel = document.getString("Birth");
                int age = calcAgeFromPesel(pesel);
                int ageId = getAgeIdBasedOnAge(age);
                int party_id = candPartyMap.get(document.getInteger("Vote"));
                Pair p = new Pair<>(party_id, ageId);
                if (resPartyAgeMap.containsKey(p)) {
                    resPartyAgeMap.put(p, resPartyAgeMap.get(p) + 1);
                } else {
                    resPartyAgeMap.put(p, 1);
                }
                //System.out.println(pesel + ": " + age);
            }
        });
    }


    public void calcResPartyPercent(){
        Map<Integer, Integer> candPartyMap = this.getDictCandidateMap();
        for(Map.Entry<Integer, Integer> entry : resPartyCandidatesMap.entrySet()) {
            int candId = entry.getKey();
            int party = candPartyMap.get(candId);
            if (this.resPartyPercentMap.containsKey(party)) {
                this.resPartyPercentMap.put(party, this.resPartyPercentMap.get(party) + entry.getValue());
            } else {
                this.resPartyPercentMap.put(party, entry.getValue());
            }
        }
    }

    public void calcResPartyCandidates(AggregateIterable output){
        output.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                resPartyCandidatesMap.put(document.getInteger("_id"), document.getInteger("sum"));
                /*Object temp = (String)(document.get("_id") + ": " + document.get("sum"));
                System.out.println(temp);*/
            }
        });

        // liczenie statystyk dla partii ogolem
        calcResPartyPercent();
    }

    public void calcResPartyEducation(AggregateIterable output){
        final Map<Integer, Integer> candPartyMap = this.getDictCandidateMap();
        output.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                JSONObject obj = new JSONObject(document.toJson());
                int party_id = candPartyMap.get(obj.getJSONObject("_id").getInt("Vote"));
                Pair p = new Pair<>(party_id, obj.getJSONObject("_id").getString("Education").equals("High") ? 1 : 0);  // TODO
                resPartyEducationMap.put(p, obj.getInt("sum"));
                //System.out.println(obj.getJSONObject("_id").getInt("Vote") +" " + obj.getJSONObject("_id").getString("Education") +
                  //      " " + obj.get("sum").toString());
            }
        });
    }

    public void calcResPartySex(AggregateIterable output){
        final Map<Integer, Integer> candPartyMap = this.getDictCandidateMap();
        output.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                JSONObject obj = new JSONObject(document.toJson());
                int party_id = candPartyMap.get(obj.getJSONObject("_id").getInt("Vote"));
                Pair p = new Pair<>(party_id, obj.getJSONObject("_id").getString("Gender").equals("M") ? 1 : 0);    // TODO
                resPartySexMap.put(p, obj.getInt("sum"));
                //System.out.println(obj.getJSONObject("_id").getInt("Vote") +" " + obj.getJSONObject("_id").getString("Education") +
                //      " " + obj.get("sum").toString());
            }
        });
    }

    public void calcResPartyConstituency(AggregateIterable output){
        final Map<Integer, Integer> candPartyMap = this.getDictCandidateMap();
        output.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                JSONObject obj = new JSONObject(document.toJson());
                int party_id = candPartyMap.get(obj.getJSONObject("_id").getInt("Vote"));
                Pair p = new Pair<>(party_id, obj.getJSONObject("_id").getInt("VotingArea"));
                resPartyConstituencyMap.put(p, obj.getInt("sum"));
                //System.out.println(obj.getJSONObject("_id").getInt("Vote") +" " + obj.getJSONObject("_id").getString("Education") +
                //      " " + obj.get("sum").toString());
            }
        });
    }

}
