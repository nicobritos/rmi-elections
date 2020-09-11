package ar.edu.itba.g5.client.admin;

import exceptions.ElectionFinishedException;
import exceptions.ElectionNotStartedException;
import models.ElectionStatus;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AdminService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.util.Properties;

public class AdminClient {
    private static final Logger logger = LoggerFactory.getLogger(AdminClient.class);
    private static final String SERVER_ADDRESS_PARAMETER = "serverAddress";
    private static final String ACTION_PARAMETER = "action";
    private static final String JAVA_OPT = "D";

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, ParseException {
        Properties properties = parseCommandLine(args);

        AdminService adminService = (AdminService) Naming.lookup("//" + properties.getProperty(SERVER_ADDRESS_PARAMETER) + "/admin");

        String action = properties.getProperty(ACTION_PARAMETER);
        switch (action) {
            case "open": {
                try {
                    adminService.open();
                } catch (ElectionFinishedException e) {
                    System.err.println("You cannot open an election that has finished");
                    System.exit(1);
                } catch (RemoteException e) {
                    System.err.println("An unknown remote error occurred");
                    System.exit(1);
                }

                System.out.println("The election has started");
                break;
            }
            case "close": {
                try {
                    adminService.close();
                } catch (ElectionNotStartedException e) {
                    System.err.println("You cannot close an election that has not been started");
                    System.exit(1);
                } catch (RemoteException e) {
                    System.err.println("An unknown remote error occurred");
                    System.exit(1);
                }

                System.out.println("The election have started");
                break;
            }
            case "state": {
                ElectionStatus status = adminService.getState();
                System.out.println(status.toString());
                break;
            }
            default: throw new InvalidParameterException("\"" + action + "\" is not a supported action");
        }
    }

    private static Properties parseCommandLine(String[] args) throws ParseException {
        Option serverAddressOption = new Option(JAVA_OPT, "sets the server address in format: address:port");
        serverAddressOption.setArgName(SERVER_ADDRESS_PARAMETER);
        serverAddressOption.setRequired(true);
        serverAddressOption.setArgs(2);

        Option actionOption = new Option(JAVA_OPT, "specifies the action to perform: open, state or close");
        actionOption.setArgName(ACTION_PARAMETER);
        actionOption.setRequired(true);
        actionOption.setArgs(2);

        Options options = new Options();
        options.addOption(serverAddressOption);
        options.addOption(actionOption);

        return new DefaultParser().parse(options, args).getOptionProperties(JAVA_OPT);
    }
}
