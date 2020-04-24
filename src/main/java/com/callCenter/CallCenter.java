package com.callCenter;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CallCenter {
    private static final Logger LOGGER = Logger.getLogger(CallCenter.class);

    private List<Operator> operators;
    private BlockingQueue<Client> clients;
    private int clientsToServe;

    public BlockingQueue<Client> getClients() {
        return clients;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public int getClientsToServe() {
        return clientsToServe;
    }

    public void setClientsToServe(int clientsToServe) {
        this.clientsToServe = clientsToServe;
    }
    public CallCenter(){
        operators = new ArrayList<Operator>();
        clients = new ArrayBlockingQueue<Client>(100,true);
        LOGGER.info("Call-center started working");
    }

    public void decrementClientsToServe(){
        clientsToServe--;
    }


}
