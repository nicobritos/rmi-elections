package ar.edu.itba.g5.server.services;

import exceptions.ElectionFinishedException;
import exceptions.ElectionNotStartedException;
import models.ElectionStatus;
import service.AdminService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AdminServiceImpl extends UnicastRemoteObject implements AdminService {
    private ElectionStatus electionStatus = ElectionStatus.UNINITIALIZED;

    public AdminServiceImpl() throws RemoteException {
    }

    @Override
    public synchronized void open() throws RemoteException, ElectionFinishedException {
        if (this.electionStatus == ElectionStatus.FINISHED) throw new ElectionFinishedException();
        this.electionStatus = ElectionStatus.OPEN;
    }

    @Override
    public synchronized void close() throws RemoteException, ElectionNotStartedException {
        if (this.electionStatus == ElectionStatus.UNINITIALIZED) throw new ElectionNotStartedException();
        this.electionStatus = ElectionStatus.FINISHED;
    }

    @Override
    public ElectionStatus getState() throws RemoteException {
        return this.electionStatus;
    }
}
