package ar.edu.itba.g5.server.vote.star;

import models.Party;

public interface SecondRoundResult extends Comparable<SecondRoundResult> {
    Party getParty();

    double getPercentage();

    @Override
    default int compareTo(SecondRoundResult o) {
        int ans = Double.compare(o.getPercentage(), this.getPercentage());
        if (ans == 0) {
            return this.getParty().compareTo(o.getParty());
        } else {
            return ans;
        }
    }
}
