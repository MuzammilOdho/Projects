import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        Scanner scanner =new Scanner(System.in);
        RaceTrack raceTrack = new RaceTrack();
        CountDownLatch raceFinishedLatch =new CountDownLatch(2);

        Vehicle vehicle1 =new Car("Ford","Mustang");
        System.out.print("Enter Name of First Racer :");
        Racer racer1 =new Racer(scanner.nextLine(),vehicle1,raceFinishedLatch,raceTrack);

        Vehicle vehicle2 =new Car("Ford","Mustang");
        System.out.print("Enter Name of Second Racer :");
        Racer racer2 =new Racer(scanner.nextLine(),vehicle2,raceFinishedLatch,raceTrack);

//        Vehicle car = new Car("ferrari","i7");
//        System.out.print("Enter Name of Third Racer :");
//        Racer racer3 =new Racer(scanner.nextLine(),vehicle1,raceFinishedLatch,raceTrack);

        racer1.start();
        racer2.start();
//        racer3.start();
        while (racer1.isAlive() || racer2.isAlive()) {

               raceTrack.displayRaceStatus(racer1);
               raceTrack.displayRaceStatus(racer2);
//               raceTrack.displayRaceStatus(racer3);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            raceTrack.clearConsole();
        }

        try {
            raceFinishedLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (racer1.isAlive()){

            racer1.interrupt();
        }
        if (racer2.isAlive()){

            racer2.interrupt();
        }
        System.out.println(Racer.winner+ "  has won the race");
    }
}