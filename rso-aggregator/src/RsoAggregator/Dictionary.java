package RsoAggregator;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private Map<Integer, Integer> dictCandidatesMap;    // candidate_id, party_id

    public Map<Integer, Integer> getDictCandidateMap() { return this.dictCandidatesMap; }

    public Dictionary(){
        this.dictCandidatesMap = new HashMap<>();
    }
}
