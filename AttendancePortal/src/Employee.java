import java.io.*;
import java.util.ArrayList;

abstract public class Employee {
    int id;


    protected String name;
    protected String userName;
    protected String password;

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Employee() {}

    public Employee(String name, String userName, String password) {
        this.name = name;
        this.userName = userName;
        this.password = password;
    }
    public boolean loginAuthentication(String userName , String password){
        return this.userName.equals(userName) && this.password.equals(password);
    }



}
