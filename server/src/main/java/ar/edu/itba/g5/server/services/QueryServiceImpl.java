package ar.edu.itba.g5.server.services;

import ar.edu.itba.g5.server.voting.fptp.FPTPResults;
import ar.edu.itba.g5.server.voting.fptp.FPTPVoting;
import ar.edu.itba.g5.server.voting.spav.SPAVResult;
import ar.edu.itba.g5.server.voting.spav.SPAVResults;
import ar.edu.itba.g5.server.voting.spav.SPAVVoting;
import ar.edu.itba.g5.server.voting.star.FirstRoundResult;
import ar.edu.itba.g5.server.voting.star.STARResults;
import ar.edu.itba.g5.server.voting.star.STARVoting;
import ar.edu.itba.g5.server.voting.fptp.FPTPResult;
import ar.edu.itba.g5.server.voting.star.SecondRoundResult;
import models.Party;
import models.Province;
import models.PollingStation;
import models.Vote;
import service.QueryService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QueryServiceImpl implements QueryService {
    // TODO: comunicarse con VotingServiceImpl para tener estas instancias compartidas
    private final STARVoting nationalSTARVoting = new STARVoting();
    private final FPTPVoting nationalFPTPVoting = new FPTPVoting();

    private static final int SPAV_WINNER_QTY = 3;
    private final Map<Province, SPAVVoting> provinceSPAVVoting = new HashMap<>();
    private final Map<Province, FPTPVoting> provinceFPTPVoting = new HashMap<>();

    private final Map<PollingStation, FPTPVoting> pollingStationFPTPVoting = new HashMap<>();

    public QueryServiceImpl() {
        for (Province province: Province.values()){
            provinceSPAVVoting.put(province, new SPAVVoting());
            provinceFPTPVoting.put(province, new FPTPVoting());
        }
    }

    // TODO: esto lo deberia hacer VotingServiceImpl
    public void registerVote(Vote vote) {
        nationalFPTPVoting.registerVote(vote.getFPTPVote());
        nationalSTARVoting.registerVote(vote.getSTARVotes());

        provinceSPAVVoting.get(vote.getProvince()).registerVote(vote.getSPAVVotes());
        provinceFPTPVoting.get(vote.getProvince()).registerVote(vote.getFPTPVote());

        if(!pollingStationFPTPVoting.containsKey(vote.getPollingStation())){
            pollingStationFPTPVoting.put(vote.getPollingStation(), new FPTPVoting());
        }
        pollingStationFPTPVoting.get(vote.getPollingStation()).registerVote(vote.getFPTPVote());
    }

    @Override
    public String nationalResults() {
        if(areComissionsOpen()){
            return fptpResultsString(nationalFPTPVoting, false);
        } else {
            STARResults starResults = nationalSTARVoting.getResults();
            STARResults.FirstRound firstRound = starResults.getFirstRound();
            StringBuilder s = new StringBuilder();
            if(firstRound.getResults().isEmpty()){
                s.append("No votes");
            } else {
                s.append("Score;Party");
            }
            for (FirstRoundResult result : firstRound){
                s.append(String.format("\n%d;%s", result.getScore(), result.getParty()));
            }

            STARResults.SecondRound secondRoundResults = starResults.getSecondRound();

            s.append("\nPercentage;Party");
            for(SecondRoundResult result: secondRoundResults){
                s.append(String.format("\n%.2f%%;%s", result.getPercentage(), result.getParty()));
            }
            s.append("\nWinner\n").append(starResults.getWinner());
            return s.toString();
        }
    }

    @Override
    public String provinceResults(Province province){
        if(areComissionsOpen()){
            return fptpResultsString(provinceFPTPVoting.get(province), false);
        } else {
            List<Party> previousWinners = Collections.emptyList();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < SPAV_WINNER_QTY; i++) {
                SPAVResults roundResults = provinceSPAVVoting.get(province).nextRound(previousWinners);
                if(i != 0){
                    s.append('\n');
                }
                s.append("Round ").append(i + 1);
                s.append("\nApproval;Party");
                for(SPAVResult approvals : roundResults){
                    s.append(String.format("\n%.2f;%s", approvals.getApprovalScore(), approvals.getParty()));
                }
                s.append("\nWinners\n");
                for (Party winner : roundResults.getWinners()){
                    s.append(winner.name()).append(",");
                }
                s.deleteCharAt(s.length() - 1); // borro la ultima coma
            }
            return s.toString();
        }
    }

    @Override
    public String pollingStationResults(PollingStation pollingStation) {
        return fptpResultsString(pollingStationFPTPVoting.getOrDefault(pollingStation, new FPTPVoting()), true);
    }

    private String fptpResultsString(FPTPVoting fptpVoting, boolean includeWinner) {
        FPTPResults results = fptpVoting.getResults();
        StringBuilder s = new StringBuilder();
        if(results.getSortedResultsList().isEmpty()){
            s.append("No votes");
        } else {
            s.append("Percentage;Party");
            for (FPTPResult result : results) {
                s.append(String.format("\n%.2f%%;%s", result.getPercentage(), result.getParty()));
            }
            if (includeWinner) {
                s.append("\nWinner\n").append(results.getWinner());
            }
        }
        return s.toString();
    }

    //TODO: comunicarse con AdminServiceImpl para saber esto
    private boolean areComissionsOpen(){
        return false;
    }
}
