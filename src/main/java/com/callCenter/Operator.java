package com.callCenter;

import org.apache.log4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Operator extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Operator.class);

    private int number;
    private boolean isFree = true;
    CallCenter callCenter;
    Client currentClient;

    private static Lock lock;
    private static Condition condition;

    public void setCurrentClient(Client currentClient){
        this.currentClient = currentClient;
    }

    public int getNumber() { return number;}

    public boolean isFree(){
        return isFree;
    }

    public Operator(int number, CallCenter center){
        super("Operator " + number);
        this.number = number;
        this.callCenter = center;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public void takeACall(Client client) throws InterruptedException {
        isFree = false;
        currentClient = client;
        LOGGER.info("Operator " + number + " took a call of client " + client.getNumber());
        sleep(100);
        LOGGER.info("Operator " + number + " finished dialog with client " + client.getNumber());
        currentClient = null;
        isFree = true;

        callCenter.decrementClientsToServe();
    }

    private boolean canTakeMore(){
        if(callCenter.getClients().size() != 0 && callCenter.getClientsToServe() > 0) return true;
        return false;
    }

    @Override
    public void run(){
       try {
           Thread.sleep(20);
           if (lock.tryLock()) {

               while (!canTakeMore())
                   condition.await();

               while (canTakeMore()) {
                   if (currentClient == null) {

                       if ((currentClient = callCenter.getClients().poll()) != null) {
                           currentClient.startDialog();
                           callCenter.decrementClientsToServe();
                       }

                       else {
                           LOGGER.info("Queue is empty, operator " + number + " is waiting");
                       }
                   }
               }

               condition.signal();
           }
       }
       catch(InterruptedException e){
            LOGGER.error("Operator " + number + "was killed ", e);
       }
       finally{
           lock.unlock();
       }
    }
}
