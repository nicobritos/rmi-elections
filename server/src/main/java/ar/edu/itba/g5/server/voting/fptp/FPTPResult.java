package ar.edu.itba.g5.server.voting.fptp;

import models.Party;

public interface FPTPResult extends Comparable<FPTPResult>{
    Party getParty();
    double getPercentage();

    default int compareTo(FPTPResult o) {
        int ans = Double.compare(o.getPercentage(), getPercentage()); // Porcentaje mas grande primero
        if(ans == 0){
            return getParty().compareTo(o.getParty()); // En caso de empate, el primero alfabeticamente
        } else {
            return ans;
        }
    }
}
