package RsoAggregator;

import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private Map<Integer, Integer> dictCandidatesMap;    // candidate_id, party_id
    private Map<Integer, Pair<Integer, Integer>> ageGroupMap;    // age_id, age_from, age_to
    public Map<Integer, Integer> getDictCandidateMap() { return this.dictCandidatesMap; }
    public Map<Integer, Pair<Integer, Integer>> getAgeGroupMap() { return this.ageGroupMap; }

    public Dictionary(){
        this.dictCandidatesMap = new HashMap<>();
        this.ageGroupMap = new HashMap<>();
    }
}
