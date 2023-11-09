import java.io.*;
import java.util.ArrayList;


public class Main {
   private static Admin admin;
   private static ArrayList<Teacher> teachers;
   private static int id;
  protected static ArrayList<Student> students;

    static private void viewAttendance(){
        if(!students.isEmpty()){
            String format = "%-20s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s%n";

            System.out.format(format, "Name of subjects", "OOP", "ISE", "LAAG", "PP", "PS", "IS");
            System.out.format(format, "Name of Teacher", "Sir Mohsin", "Ma'am Anoud", "Sir Saleem", "Sir Junaid", "Sir Ghulam", "Sir Abdul Aziz");
            String format1 = "%-20s | %-15d | %-15d | %-15d | %-15d | %-15d | %-15d%n";
            System.out.format(format1, "Class Conducted",teachers.get(0).getTotalClasses(),teachers.get(1).getTotalClasses(),teachers.get(2).getTotalClasses(),teachers.get(3).getTotalClasses(),teachers.get(4).getTotalClasses(),teachers.get(5).getTotalClasses());
            for (Student s : students){
                System.out.printf(format1,s.getRollNo(),s.getOOPatt(),s.getISEatt(),s.getLAAGatt(),s.getPPatt(),s.getPSatt(),s.getISatt());
            }

        }
    }
    static private void teacherPage(Scan scan,int id) {
        boolean isLoggedin = true;
        do {
            System.out.println("1.Take Attendance");
            System.out.println("2.Attendance History");
            System.out.println("0.Log Out");

            int choice = scan.getIntInput("Your Choice :");
            switch (choice) {
                case 1:
                    teachers.get(id).takeAttendance(scan);
                    updateList();
                    break;
                case 2:
                    viewAttendance();
                    break;
                case 0:
                    isLoggedin=false;
                    break;
                default:
                    System.out.println("Invalid Choice!!");

            }
        }while (isLoggedin);

    }

    static private void adminPage(Scan scan) {
        boolean isLoggedin = true;
        do {
            System.out.println("1.Add Student");
            System.out.println("2.Remove Student");
            System.out.println("0.Log Out");

            int choice = scan.getIntInput("Your Choice :");
            switch (choice) {
                case 1:
                    admin.addStudent(scan);
                    updateList();
                    break;
                case 2:
                    admin.removeStudent(scan);
                    updateList();
                    break;
                case 0:
                    isLoggedin =false;
                    break;
                default:
                    System.out.println("Invalid Choice!!");

            }
        } while (isLoggedin);
    }

    static private boolean  login(Scan scan, int option) {
        boolean loginSuccess = false;

        String userName = scan.getStringInput("UserName :");
        String password = scan.getStringInput("Password :");

        if (option == 2) {
            for (Teacher teacher : teachers) {
                if (teacher.loginAuthentication(userName, password)) {
                    loginSuccess = true;
                    id=teacher.id;
                    break;
                }

            }

        } else if (option == 3) {
            if (admin.loginAuthentication(userName, password)) {
                loginSuccess = true;
            }
        }
        return loginSuccess;
    }
   static private void updateList(){
        try {
            FileOutputStream fos = new FileOutputStream("Students.txt");
            ObjectOutputStream out = new ObjectOutputStream(fos);

            for(Student student : students){
                out.writeObject(student);
            }
            fos.close();
            out.close();
        }catch (IOException e){
            System.out.println("Error while Updating File  " + e.getMessage());
        }
    }
  static private void initialize(){
        File file = new File("Students.txt");
        if (file.exists() && file.length() !=0) {

            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

                while(true){
                    try {
                        Student student = (Student) objectIn.readObject();
                        students.add(student);
                    }catch (EOFException e){
                        objectIn.close();
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
        admin = new Admin("Zaeem", "admin", "admin");
        teachers=new ArrayList<>();
        students =new ArrayList<>();
        initialize();
        teachers.add(new Teacher("Sir Mohsin","oop","123","OOP",0));
        teachers.add(new Teacher("Ma'am Anoud","ise","123","ISE",1));
        teachers.add(new Teacher("Sir Saleem","laag","123","LAAG",2));
        teachers.add(new Teacher("Sir Junaid","pp","123","PP",3));
        teachers.add(new Teacher("Sir Ghulam Abbas","ps","123","PS",4));
        teachers.add(new Teacher("Sir Abdul Aziz","IS","123","IS",5));
        Scan scan = new Scan();
        boolean isRunning = true;
        do {
            System.out.println("1.Student");
            System.out.println("2.Teacher");
            System.out.println("3.Admin");
            System.out.println("0.Exit");
            int choice = scan.getIntInput("Your Choice :");
            switch (choice) {
                case 1:
                    viewAttendance();
                    break;
                case 2:
                    if ((login(scan, choice))) {
                        teacherPage(scan,id);
                    } else {
                        System.out.println("Invalid Credentials!!");
                    }
                    break;
                case 3:
                    if ((login(scan, choice))) {
                        adminPage(scan);
                    } else {
                        System.out.println("Invalid Credentials!!");
                    }
                    break;
                case 0:
                    isRunning = false;

            }

        } while (isRunning);

        scan.close();
    }


}
