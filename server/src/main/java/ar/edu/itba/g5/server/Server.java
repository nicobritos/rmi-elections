package ar.edu.itba.g5.server;

import ar.edu.itba.g5.server.services.AdminServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AdminService;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static Registry registry;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        logger.info("elecciones Server Starting ...");

        Server.registry = LocateRegistry.createRegistry(1100); // TODO

        AdminService adminService = new AdminServiceImpl();
        registry.bind("admin", adminService);
    }
}
