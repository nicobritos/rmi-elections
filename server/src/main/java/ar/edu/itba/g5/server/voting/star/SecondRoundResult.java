package ar.edu.itba.g5.server.voting.star;

import models.Party;

public interface SecondRoundResult extends Comparable<SecondRoundResult>{
    Party getParty();
    double getPercentage();

    @Override
    default int compareTo(SecondRoundResult o) {
        int ans = Double.compare(o.getPercentage(), getPercentage());
        if(ans == 0){
            return getParty().compareTo(o.getParty());
        } else {
            return ans;
        }
    }
}
