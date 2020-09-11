package ar.edu.itba.g5.server.voting.spav;

import models.Party;

public interface SPAVResult extends Comparable<SPAVResult>{
    Party getParty();
    double getApprovalScore();

    default int compareTo(SPAVResult o) {
        int ans = Double.compare(o.getApprovalScore(), getApprovalScore());
        if(ans == 0){
            return getParty().compareTo(o.getParty());
        } else {
            return ans;
        }
    }
}
