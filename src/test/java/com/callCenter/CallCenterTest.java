package com.callCenter;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


public class CallCenterTest {

    @Test
    public void Run() throws InterruptedException{
        CallCenter callCenter = new CallCenter();

        for (int i = 1; i <= 3; i++) {
            callCenter.getOperators().add(new Operator(i, callCenter));
        }

        for(Operator operator : callCenter.getOperators()){
            operator.start();
        }

        Thread.sleep(100);

        for(int i = 1; i <= 6; i++){
            (new Client(i, callCenter)).start();
        }

        Thread.sleep(100);

        for(int i = 7; i <= 10; i++){
            (new Client(i, callCenter)).start();
        }

        Thread.sleep(1000);
    }

}
