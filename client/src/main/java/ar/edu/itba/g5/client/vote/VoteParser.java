package ar.edu.itba.g5.client.vote;

import ar.edu.itba.g5.client.CSVUtils;
import com.opencsv.CSVReader;
import models.Party;
import models.PollingStation;
import models.Province;
import models.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public abstract class VoteParser {
    private static final Logger logger = LoggerFactory.getLogger(VoteParser.class);
    private static final int TABLE_INDEX = 0;
    private static final int PROVINCE_INDEX = 1;
    private static final int CANDIDATES_INDEX = 2;
    private static final int FPTP_CANDIDATE_INDEX = 3;

    public static Collection<Vote> parse(String filepath) throws IOException {
        Collection<Vote> votes = new LinkedList<>();

        CSVReader reader = CSVUtils.getReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filepath)));
        for (String[] line : reader) {
            PollingStation table = new PollingStation(Integer.parseInt(line[TABLE_INDEX]));
            Province province = Province.from(line[PROVINCE_INDEX]);
            Map<Party, Integer> rankedParties = parseRankedParties(line[CANDIDATES_INDEX]);
            Party fptpCandidate = Party.from(line[FPTP_CANDIDATE_INDEX]);

            votes.add(new Vote(table, province, rankedParties, fptpCandidate));
        }

        reader.close(); // TODO: Mejorar por excepcion en medio del for
        return votes;
    }

    private static Map<Party, Integer> parseRankedParties(String line) {
        if (line.length() == 0) return Collections.emptyMap();

        Map<Party, Integer> rankedParties = new HashMap<>();

        String[] rankedCandidates = line.split(",");
        for (int i = 0; i < rankedCandidates.length; i++) {
            String[] lines = rankedCandidates[i].split("\\|");

            Party party = Party.from(lines[0]);
            rankedParties.put(party, Integer.valueOf(lines[1]));
        }

        return rankedParties;
    }
}
