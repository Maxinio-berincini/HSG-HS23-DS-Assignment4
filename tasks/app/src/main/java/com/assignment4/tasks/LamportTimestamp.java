
package com.assignment4.tasks;


public class LamportTimestamp {
    private int timestamp;
    public LamportTimestamp(int time){

        timestamp = time;
    }
    public void tick(){
        timestamp++; // update the timestamp by 1
    }
    public int getCurrentTimestamp(){

        return timestamp;
    }
    public void updateClock(int receivedTimestamp){
        if (receivedTimestamp > timestamp) timestamp = receivedTimestamp + 1; // update the function to choose max out of the two received timestamps
    }

}
