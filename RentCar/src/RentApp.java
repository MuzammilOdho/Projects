
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;

import java.util.Scanner;

public class RentApp {
    boolean isSeller;
    ArrayList<Buyer> buyersList = new ArrayList<>();
    ArrayList<Seller> sellersList = new ArrayList<>();
    Buyer buyer = new Buyer();
    Seller seller = new Seller();

    public boolean login(String userName, String pass) {

        for (Seller s : sellersList) {

            if (userName.equals(s.userName) && pass.equals(s.pass)) {

                isSeller = true;

                return true;
            }
        }
        for (Buyer b : buyersList) {

            if (userName.equals(b.userName) && pass.equals(b.pass)) {
                isSeller = false;

                return true;
            }
        }

        return false;

    }

    public boolean userAlreadyExist() {
        String str = isSeller ? seller.userName : buyer.userName;
        System.out.println(str);
        if (sellersList.size() != 0) {

            for (int i = 0; i < sellersList.size(); i++) {

                if (str.equals(sellersList.get(i).userName)) {

                    return true;
                }
            }
        }

        if (buyersList.size() != 0) {

            for (int i = 0; i < buyersList.size(); i++) {

                if (str.equals(buyersList.get(i).userName)) {

                    return true;
                }
            }
        }
        return false;
    }

    public void addCustomer() {

        try {

            if (isSeller) {

                sellersList.add(seller);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream("Sellers.ser"));
                for (Seller s : sellersList) {

                    objectOutputStream.writeObject(s);
                }
                objectOutputStream.close();

            } else {

                buyersList.add(buyer);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream("Buyers.ser"));
                for (Buyer a : buyersList) {
                    objectOutputStream.writeObject(a);

                }
                objectOutputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sellerDisplay(Scanner scan) {

        System.out.println("1.Add Car");
        System.out.println("2.Remove Car");
        System.out.println("3.Update Car");
        System.out.println("4.Car list");
        System.out.println("5.Offers Recieved");
        System.out.println("6.Rent History");
        try {
            int ch = scan.nextInt();
            scan.nextInt();

            switch (ch) {
                case 1:

            }
        } catch (InputMismatchException e) {

        }
        scan.close();
    }

    public void buyerDisplay(Scanner scan) {
        System.out.println("1.Available Cars");
        System.out.println("2.Return Car");
        System.out.println("3.Rent History");
        int choice = scan.nextInt();
        switch (choice) {
            case 1: {

            }
        }
    }

    static public void loading() throws InterruptedException {
        System.out.print("Loading");
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            Thread.sleep(500);
        }

    }

    public RentApp() {

        try {
            File buyersFile = new File("Buyers.ser");
            File sellersFile = new File("Sellers.ser");
            if (!buyersFile.exists() && !sellersFile.exists()) {

                buyersFile.createNewFile();
                sellersFile.createNewFile();

            } else {
                if (buyersFile.length() != 0) {
                    ObjectInputStream buyerReader = new ObjectInputStream(new FileInputStream(buyersFile));
                    while (true) {
                        try {

                            Buyer b = (Buyer) buyerReader.readObject();
                            buyersList.add(b);
                        } catch (EOFException e) {
                            break;
                        }
                    }
                    buyerReader.close();
                }
                if (sellersFile.length() != 0) {
                    ObjectInputStream sellerReader = new ObjectInputStream(new FileInputStream(sellersFile));

                    while (true) {

                        try {
                            Seller s = (Seller) sellerReader.readObject();
                            sellersList.add(s);

                        } catch (EOFException e) {
                            break;
                        }
                    }
                    sellerReader.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Buyer buyer : buyersList) {

            System.out.println(buyer.name);
            System.out.println(buyer.userName);
            System.out.println(buyer.pass);
            System.out.println(buyer.address);
            System.out.println(buyer.phoneNo);
        }
        for (Seller b : sellersList) {
            System.out.println(b.name);
            System.out.println(b.userName);
            System.out.println(b.pass);
            System.out.println(b.address);
            System.out.println(b.phoneNo);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("\033[H\033[2J");
        RentApp app = new RentApp();

        Scanner scan = new Scanner(System.in);
        boolean exitRequested = false;
        while (!exitRequested) {
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            try {

                int input = scan.nextInt();
                scan.nextLine();
                switch (input) {

                    case 1: {

                        // System.out.println("\033[H\033[2J");
                        System.out.println("1. Buyer ");
                        System.out.println("2. Seller ");
                        System.out.println("0. Return");

                        int ch = scan.nextInt();
                        scan.nextLine();
                        switch (ch) {
                            case 1:
                            case 2:
                                if (ch == 1) {
                                    app.isSeller = false;
                                    app.buyer.signUp(scan);
                                } else {

                                    app.isSeller = true;
                                    app.seller.signUp(scan);
                                }
                                if (!(app.userAlreadyExist())) {
                                    app.addCustomer();
                                    System.out.println("Signed Up Successfully!!");

                                } else {
                                    System.out.println("UserName Already Exist !!");

                                }

                                break;
                            case 0:
                                loading();

                                break;

                            default:
                                System.out.println("Invalid choice!! Try again.");
                                loading();

                        }

                    }
                        break;
                    case 2: {

                        System.out.print("UserName :");
                        String userName = scan.nextLine();

                        System.out.print("Password :");
                        String pass = scan.nextLine();

                        if (app.login(userName, pass)) {
                            if (app.isSeller) {
                                app.sellerDisplay(scan);
                            } else {
                                app.buyerDisplay(scan);
                            }
                        } else
                            System.out.println("UserName or Password is incorrect ");
                    }
                        break;
                    case 0:
                        exitRequested = true;
                        break;
                    default:
                        System.out.println("Invalid Choice!! Try again! ");

                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input!! Please Select from given options.");

            }

            scan.close();
            loading();
        }

    }
}
