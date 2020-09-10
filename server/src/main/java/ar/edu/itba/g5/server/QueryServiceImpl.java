package ar.edu.itba.g5.server;

import ar.edu.itba.g5.server.voting.FPTPVoting;
import ar.edu.itba.g5.server.voting.SPAVVoting;
import ar.edu.itba.g5.server.voting.STARVoting;
import models.Party;
import models.Province;
import models.Vote;
import service.QueryService;
import utils.Pair;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QueryServiceImpl implements QueryService {
    // TODO: comunicarse con VotingServiceImpl para tener estas instancias compartidas
    private final STARVoting nationalStarVoting = new STARVoting();
    private final FPTPVoting nationalFptpVoting = new FPTPVoting();

    private static final int SPAV_WINNER_QTY = 3;
    private final Map<Province, SPAVVoting> provinceSpavVoting = new HashMap<>();
    private final Map<Province, FPTPVoting> provinceFptpVoting = new HashMap<>();

    private final Map<Integer, FPTPVoting> tableFptpVoting = new HashMap<>();



    public QueryServiceImpl() {
        for (Province province: Province.values()){
            provinceSpavVoting.put(province, new SPAVVoting());
            provinceFptpVoting.put(province, new FPTPVoting());
        }
    }

    // TODO: esto lo deberia hacer VotingServiceImpl
    public void registerVote(Vote vote) {
        nationalFptpVoting.registerVote(vote.getFPTPVote());
        nationalStarVoting.registerVote(vote.getSTARVotes());

        provinceSpavVoting.get(vote.getProvince()).registerVote(vote.getSPAVVotes());
        provinceFptpVoting.get(vote.getProvince()).registerVote(vote.getFPTPVote());

        tableFptpVoting.getOrDefault(vote.getTable(), new FPTPVoting()).registerVote(vote.getFPTPVote());
    }

    @Override
    public void nationalPercentages(String csvPath) {
        if(areComissionsOpen()){
            printFPTP(nationalFptpVoting, csvPath, false);
        } else {
            List<Pair<Party, Long>> firstRoundResults = nationalStarVoting.firstRound();
            StringBuilder s = new StringBuilder();
            if(firstRoundResults.isEmpty()){
                s.append("No votes");
            } else {
                s.append("Score;Party");
            }
            for (Pair<Party, Long> result : firstRoundResults){
                s.append(String.format("\n%d;%s", result.getRight(), result.getLeft().name()));
            }

            List<Pair<Party, Double>> secondRoundResults = nationalStarVoting.secondRound(firstRoundResults.get(0).getLeft(), firstRoundResults.get(1).getLeft());
            s.append("\nPercentage;Party");
            for(Pair<Party, Double> result: secondRoundResults){
                s.append(String.format("\n%.2f%%;%s", result.getRight(), result.getLeft().name()));
            }
            s.append("\nWinner\n").append(secondRoundResults.get(0).getLeft().name());
            writeStringToFile(csvPath, s.toString());
        }
    }

    @Override
    public void provincialPercentages(String csvPath, Province province) {
        if(areComissionsOpen()){
            printFPTP(provinceFptpVoting.get(province), csvPath, false);
        } else {
            List<Party> previousWinners = Collections.emptyList();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < SPAV_WINNER_QTY; i++) {
                Pair<List<Pair<Party, Double>>,List<Party>> roundResults =
                        provinceSpavVoting.get(province).nextRound(previousWinners);
                if(i != 0){
                    s.append('\n');
                }
                s.append("Round ").append(i + 1);
                s.append("\nApproval;Party");
                for(Pair<Party, Double> approvals : roundResults.getLeft()){
                    s.append(String.format("\n%.2f;%s", approvals.getRight(), approvals.getLeft()));
                }
                s.append("\nWinners\n");
                for (Party winner : roundResults.getRight()){
                    s.append(winner.name()).append(",");
                }
                s.deleteCharAt(s.length() - 1); // borro la ultima coma
            }
            writeStringToFile(csvPath, s.toString());
        }
    }

    @Override
    public void tablePercentages(String csvPath, int table) {
        printFPTP(tableFptpVoting.getOrDefault(table, new FPTPVoting()), csvPath, true);

    }

    private void printFPTP(FPTPVoting fptpVoting, String csvPath, boolean printWinner) {
        List<Pair<Party, Double>> results = fptpVoting.results();
        StringBuilder s = new StringBuilder();
        if(results.isEmpty()){
            s.append("No votes");
        } else {
            s.append("Percentage;Party");
            for (Pair<Party, Double> result : results) {
                s.append(String.format("\n%.2f%%;%s", result.getRight(), result.getLeft().name()));
            }
            if (printWinner) {
                s.append("\nWinner\n").append(results.get(0).getLeft().name());
            }
        }
        writeStringToFile(csvPath, s.toString());
    }

    private void writeStringToFile(String csvPath, String s){
        try {
            Files.write(Paths.get(csvPath), s.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: comunicarse con AdminServiceImpl para saber esto
    private boolean areComissionsOpen(){
        return false;
    }
}
