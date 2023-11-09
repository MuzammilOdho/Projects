import java.util.Scanner;

public class Car {
    String carName;
    String carModel;
    String year;
    String price;

    public void setCar() {
        Scanner scan = new Scanner(System.in);

        System.out.print("Brand Name :");
        this.carName = scan.nextLine();

        System.out.print("Model :");
        this.carModel = scan.nextLine();

        System.out.print("Year:");
        this.year = scan.nextLine();

        System.out.print("Price :");
        this.price = scan.nextLine();

        scan.close();
    }

    public Car() {
    }

}
