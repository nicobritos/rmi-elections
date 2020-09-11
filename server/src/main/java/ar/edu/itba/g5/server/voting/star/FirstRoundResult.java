package ar.edu.itba.g5.server.voting.star;

import models.Party;

public interface FirstRoundResult extends Comparable<FirstRoundResult> {
    Party getParty();
    long getScore();

    @Override
    default int compareTo(FirstRoundResult o) {
        int ans = Double.compare(o.getScore(), getScore());
        if(ans == 0){
            return getParty().compareTo(o.getParty());
        } else {
            return ans;
        }
    }
}
