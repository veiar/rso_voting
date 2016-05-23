package RsoAggregator;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Statistics {
    private Map<Integer, Integer> resPartyCandidatesMap;    // <candidate_id, votes_sum>

    public Statistics(){
        this.resPartyCandidatesMap = new HashMap<>();

    }
//select age_id, age_group, split_part(age_group, '-', 1), split_part(age_group, '-', 2) from d_age ;

    public int calcAgeFromPesel(String pesel){
        Date now = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyMMdd");
        long diff = 0;
        try{
            Date birth = df.parse(pesel.substring(0, 6));
            diff = (now.getTime() - birth.getTime())/ 1000 / 60 / 60 / 24 / 365;
            return (int)diff;
        }
        catch (Exception e){
            System.err.println(e.getClass().getName()+ e.getMessage());
        }
        return 0;
    }


    public void calcResPartyCandidates(AggregateIterable output){
        output.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                resPartyCandidatesMap.put((Integer) document.get("_id"), (Integer) document.get("sum"));
                Object temp = (String)(document.get("_id") + ": " + document.get("sum"));
                System.out.println(temp);
            }
        });
    }
}
