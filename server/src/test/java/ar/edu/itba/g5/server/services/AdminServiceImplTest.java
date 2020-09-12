package ar.edu.itba.g5.server.services;

import exceptions.ElectionNotStartedException;
import models.ElectionStatus;
import org.junit.Test;
import org.junit.Assert;

import java.rmi.RemoteException;

public class AdminServiceImplTest {
    @Test
    public void testOpen() throws RemoteException {
        AdminServiceImpl service = new AdminServiceImpl();
        service.open();
        Assert.assertEquals(ElectionStatus.OPEN,service.getState());
    }
    @Test
    public void testClose() throws RemoteException{
        AdminServiceImpl service = new AdminServiceImpl();
        service.open();
        service.close();
        Assert.assertEquals(ElectionStatus.FINISHED,service.getState());
    }
    @Test
    public void testDoubleClose() throws RemoteException{
        AdminServiceImpl service = new AdminServiceImpl();
        service.open();
        service.close();
        service.close();
        Assert.assertEquals(ElectionStatus.FINISHED,service.getState());
    }
    @Test(expected=ElectionNotStartedException.class)
    public void testCloseUnopened() throws RemoteException{
        AdminServiceImpl service = new AdminServiceImpl();
        service.close();
    }
    //TODO: add concurrency test
}
