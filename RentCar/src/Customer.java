import java.io.Serializable;
import java.util.Scanner;

public class Customer implements Serializable {
    String name;
    String address;
    String phoneNo;
    String userName;
    String pass;

    public void signUp(Scanner scan) {

        System.out.print("Name :");
        this.name = scan.nextLine();

        System.out.print("User Name :");
        this.userName = scan.nextLine();

        System.out.print("Address :");
        this.address = scan.nextLine();

        System.out.print("Phone No. :");
        this.phoneNo = scan.nextLine();

        System.out.print("Password :");
        this.pass = scan.nextLine();

    }

    Customer() {

    }

}
