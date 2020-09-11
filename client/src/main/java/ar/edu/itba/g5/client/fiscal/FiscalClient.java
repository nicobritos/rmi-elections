package ar.edu.itba.g5.client.fiscal;

import models.Party;
import models.PollingStation;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FiscalService;
import utils.CommandUtils;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Properties;

public class FiscalClient {
    private static final Logger logger = LoggerFactory.getLogger(FiscalClient.class);
    private static final String SERVER_ADDRESS_PARAMETER = "serverAddress";
    private static final String POLLING_STATION_PARAMETER = "id";
    private static final String PARTY_NAME_PARAMETER = "party";
    private static final String JAVA_OPT = "D";

    public static void main(String[] args) throws IOException, NotBoundException, ParseException {
        Properties properties = parseCommandLine(args);

        FiscalService fiscalService = (FiscalService) Naming.lookup("//" + properties.getProperty(SERVER_ADDRESS_PARAMETER) + "/fiscal");
        PollingStation pollingStation = new PollingStation(Integer.parseInt(properties.getProperty(POLLING_STATION_PARAMETER)));
        Party party = Party.from(properties.getProperty(PARTY_NAME_PARAMETER));

        fiscalService.registerFiscal(
                pollingStation,
                party,
                () -> System.out.println("New vote for " + party + " registered on polling place " + pollingStation)
        );
        System.out.println("Fiscal of " + party + " registered on polling place " + pollingStation);
    }

    private static Properties parseCommandLine(String[] args) throws ParseException {
        Option pollingStationOption = new Option(JAVA_OPT, "specifies the polling place number");
        pollingStationOption.setArgName(POLLING_STATION_PARAMETER);
        pollingStationOption.setRequired(true);

        Option partyNameOption = new Option(JAVA_OPT, "specifies the party name");
        partyNameOption.setArgName(PARTY_NAME_PARAMETER);
        partyNameOption.setRequired(true);

        return CommandUtils.parseCommandLine(args, CommandUtils.serverAddressOption, pollingStationOption, partyNameOption);
    }
}
