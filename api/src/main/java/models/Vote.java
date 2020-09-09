package models;

import com.sun.istack.internal.NotNull;

import java.util.Map;

public class Vote {
    private final int table;
    private final Province province;
    private final Map<Party, Integer> rankedVotes;
    private final Party fptpVote;

    public Vote(int table, @NotNull Province province, @NotNull Map<Party, Integer> rankedVotes, @NotNull Party fptpVote) {
        this.table = table;
        this.province = province;
        this.rankedVotes = rankedVotes;
        this.fptpVote = fptpVote;
    }

    public int getTable() {
        return table;
    }

    public Province getProvince() {
        return province;
    }

    public Map<Party, Integer> getRankedVotes() {
        return rankedVotes;
    }

    public Party getFptpVote() {
        return fptpVote;
    }
}
