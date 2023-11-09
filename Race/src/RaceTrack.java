import java.util.Random;

class RaceTrack  {
      int totalLaps;
      int lapLength;
    int carPosition = 0;
    int length = 200;

    public RaceTrack() {
        this.totalLaps = 3;
        this.lapLength = 500;
    }

    public  void displayRaceStatus(Racer racer) {
        String car = " .-'--`-.";
        String car1 = "'-O---O--' ";

        System.out.println("Driver: " + racer.name);
        System.out.println("Current Lap: " + racer.currentLap);
        System.out.println("Hit Counter : " + racer.hitCounter);
        System.out.println("Speed: " + racer.vehicle.getSpeed() + "Km/h");

        System.out.println("  ".repeat(carPosition) + car);
        System.out.println("  ".repeat(carPosition) + car1);

        carPosition = (carPosition + 1) % (length + 1);

        System.out.println();
    }

    public  void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Failed to clear the console: " + e.getMessage());
        }
    }
    public  void race(Racer racer)  {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}