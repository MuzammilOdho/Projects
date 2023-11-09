import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Racer extends Thread {
    String name;
    int currentLap;
    Vehicle vehicle;
    int hitCounter;
    RaceTrack raceTrack;

    static String winner=null;
    CountDownLatch raceFinishedLatch ;




    @Override
    public void run() {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        vehicle.start();

        int remainlapLength = raceTrack.lapLength;
        while(currentLap<=raceTrack.totalLaps){
            if (remainlapLength<=0){
                currentLap++;
                if(currentLap==4){

                    break;
                }
                remainlapLength = raceTrack.lapLength;

            }

        vehicle.driving();
            if(vehicle.getSpeed()>80) {

                double num = Math.random();
                if (num < 0.05) {
                    decreaseSpeed();
                }
            }

            raceTrack.race(this);
            remainlapLength-= vehicle.getSpeed();


        }
        synchronized (Racer.class) {
            if (winner == null) {
                winner = name;
            }
        }
        raceFinishedLatch.countDown();


    }



    public void decreaseSpeed(){
        int speed = vehicle.getSpeed()-20;
        vehicle.setSpeed(speed);
        hitCounter++;
        System.out.println(this.name + " hit an obstacle!!!");
    }


    Racer(){}
    Racer(String name,Vehicle vehicle ,CountDownLatch raceFinishedLatch,RaceTrack raceTrack){
        this.name=name;
        this.vehicle=vehicle;
        this.raceFinishedLatch=raceFinishedLatch;
        this.currentLap=1;
        this.raceTrack=raceTrack;
    }

}
