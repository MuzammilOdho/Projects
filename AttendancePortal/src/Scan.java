import java.util.InputMismatchException;
import java.util.Scanner;

public class Scan {
    private Scanner scanner;

    public Scan() {
        scanner = new Scanner(System.in);
    }

    public int getIntInput(String prompt) {
        int input = 0;
        boolean validInput = false;

        do {
            try {
                System.out.print(prompt);
                input = scanner.nextInt();
                scanner.nextLine();
                validInput = true;

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume the invalid input
            }
        } while (!validInput);

        return input;
    }
    public long getLongInput(String prompt) {
        long input = 0;
        boolean validInput = false;

        do {
            try {
                System.out.print(prompt);
                input = scanner.nextLong();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume the invalid input
            }
        } while (!validInput);

        return input;
    }

    public String getStringInput(String prompt) {
        String input =null;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("Please enter a non-empty string.");
            }
        } while (input.isEmpty());
        return input;
    }

    public void pressAnyKey() {
        try {
            System.out.println("Press Any Key!!");
            char inputChar = (char) System.in.read();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        scanner.close();
    }
}
