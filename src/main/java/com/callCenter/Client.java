package com.callCenter;

import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Client.class);

    private int number;
    private boolean isTaken = false;
    private boolean bored = false;
    private CallCenter callCenter;

    private static Lock lock;
    private static Condition condition;

    public int getNumber() { return number;}

    public Client(int number, CallCenter callCenter){
        super("Client " + number);
        this.number = number;
        this.isTaken = false;
        this.callCenter = callCenter;

        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    @Override
    public void run(){
        if(lock.tryLock()){
            try{
                call();

                if (!isTaken)
                    condition.await(50, TimeUnit.MILLISECONDS);

                if(!isTaken) {
                    bored = true;
                }

                if(bored){
                    leaveQueue();
                }
            }
            catch(InterruptedException e){
                LOGGER.error("Client " + number + "was killed ", e);
            }
            finally{
                lock.unlock();
            }
        }
    }

    private void call() throws InterruptedException {
        LOGGER.info("Client " + number + " is calling");
        for(Operator op : callCenter.getOperators()){
            if(op.isFree()){
                op.takeACall(this);
                isTaken = true;
                break;
            }
        }

        if(!isTaken){
            LOGGER.info("Client " + number + " is waiting for free operator in the queue");
            callCenter.getClients().offer(this);
        }

        callCenter.decrementClientsToServe();
    }

    public void startDialog(){
            isTaken = true;
            condition.signal();
            leaveQueue();
            LOGGER.info("Client " + number + " started a dialog after waiting in the queue");
    }

    private void leaveQueue(){
        callCenter.getClients().remove(this);
        LOGGER.info("Client " + number + " left the queue");
    }
}
