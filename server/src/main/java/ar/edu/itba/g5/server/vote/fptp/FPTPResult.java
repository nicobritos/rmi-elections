package ar.edu.itba.g5.server.vote.fptp;

import models.Party;

public interface FPTPResult extends Comparable<FPTPResult> {
    Party getParty();

    double getPercentage();

    default int compareTo(FPTPResult o) {
        int ans = Double.compare(o.getPercentage(), this.getPercentage()); // Porcentaje mas grande primero
        if (ans == 0) {
            return this.getParty().compareTo(o.getParty()); // En caso de empate, el primero alfabeticamente
        } else {
            return ans;
        }
    }
}
