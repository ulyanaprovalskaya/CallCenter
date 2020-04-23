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

    public int getClientsToServe() {
        return clientsToServe;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public CallCenter(int clientsAmount){
        operators = new ArrayList<Operator>();
        clients = new ArrayBlockingQueue<Client>(clientsAmount,true);
        clientsToServe = clientsAmount;
        LOGGER.info("Call-center started working");
    }

    public int decrementClientsToServe(){
        return --clientsToServe;
    }
}
