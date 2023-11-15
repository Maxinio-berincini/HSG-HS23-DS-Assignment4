
package com.assignment4.tasks;

import java.util.Arrays;

public class VectorClock {

    private final int[] timestamps;
    public VectorClock(int numOfClients){
        timestamps = new int[numOfClients];
        Arrays.fill(timestamps, 0);
    }
    public void setVectorClock(int processId, int time){
        timestamps[processId] = time;
    }
    public void tick(int processId){
        timestamps[processId] = 0; // update the code to increment the clock for the given process id
    }
    public int getCurrentTimestamp(int processId){
        return timestamps[processId];
    }
    public void updateClock(VectorClock other){
        // update the clock with the incoming clock value
    }
    public String showClock(){
        return Arrays.toString(timestamps);
    }

}
