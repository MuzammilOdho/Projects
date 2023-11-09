public class Admin extends Employee {

    public Admin(String name, String userName, String password) {
        super(name, userName, password);

    }

    public void addStudent(Scan scan) {
        Student student = new Student(scan.getStringInput("Roll No :"));
        if (alreadyExist(student.getRollNo())){
            System.out.println("Roll No Already Exist");
        }
        Main.students.add(student);
    }

    public void removeStudent(Scan scan){
        String rollNo= scan.getStringInput("Roll No ");
        for (Student student : Main.students){
            if (student.getRollNo().equals(rollNo)){
              Main.students.remove(student);
               return;
            }
        }
        System.out.println("Roll No not found!!");
    }
    public boolean alreadyExist(String rollNo){
        for (Student student : Main.students){
            if (student.getRollNo().equals(rollNo)){
                return true;
            }
        }
        return false;
    }



}
