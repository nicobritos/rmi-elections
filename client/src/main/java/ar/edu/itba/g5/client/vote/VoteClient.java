package ar.edu.itba.g5.client.vote;

import exceptions.ElectionFinishedException;
import exceptions.ElectionNotStartedException;
import models.Vote;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.VoteService;
import utils.CommandUtils;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Properties;

public class VoteClient {
    private static final Logger logger = LoggerFactory.getLogger(VoteClient.class);
    private static final String SERVER_ADDRESS_PARAMETER = "serverAddress";
    private static final String VOTES_PATH_PARAMETER = "votesPath";
    private static final String JAVA_OPT = "D";


    public static void main(String[] args) throws IOException, NotBoundException, ParseException {
        Properties properties = parseCommandLine(args);
        String filepath = properties.getProperty(VOTES_PATH_PARAMETER);
        Collection<Vote> votes = VoteParser.parse(filepath);

        VoteService voteService = (VoteService) Naming.lookup("//" + properties.getProperty(SERVER_ADDRESS_PARAMETER) + "/vote");
        int emittedVotes = 0;
        for (Vote vote : votes) {
            try {
                voteService.vote(vote);
                emittedVotes++;
            } catch (ElectionNotStartedException e) {
                System.err.println("The election has not started yet");
                System.exit(1);
            } catch (ElectionFinishedException e) {
                System.err.println("The election has already finished");
                System.exit(1);
            } catch (RemoteException e) {
                System.err.println("Unknown remote error"); // TODO: Retry vote
            }
        }

        System.out.println(emittedVotes + " votes registered");
    }

    private static Properties parseCommandLine(String[] args) throws ParseException {
        Option filepathOption = new Option(JAVA_OPT, "specifies the votes' file path");
        filepathOption.setArgName(VOTES_PATH_PARAMETER);
        filepathOption.setRequired(true);

        return CommandUtils.parseCommandLine(args, CommandUtils.serverAddressOption, filepathOption);
    }
}
