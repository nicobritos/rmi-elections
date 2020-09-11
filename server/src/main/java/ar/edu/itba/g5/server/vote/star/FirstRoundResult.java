package ar.edu.itba.g5.server.vote.star;

import models.Party;

public interface FirstRoundResult extends Comparable<FirstRoundResult> {
    Party getParty();

    long getScore();

    @Override
    default int compareTo(FirstRoundResult o) {
        int ans = Double.compare(o.getScore(), this.getScore());
        if (ans == 0) {
            return this.getParty().compareTo(o.getParty());
        } else {
            return ans;
        }
    }
}
