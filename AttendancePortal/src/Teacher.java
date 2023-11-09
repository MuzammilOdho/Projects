public class Teacher extends Employee{
    int id;
   private  String subject;
   private  int totalClasses;
    public Teacher(String name, String userName, String password,String subject,int id) {
        super(name, userName, password);
        this.subject = subject;
        this.totalClasses=0;
        this.id=id;
    }

    public String getSubject() {
        return subject;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }
    public void takeAttendance(Scan scan){
        if (!Main.students.isEmpty()) {
            for (Student s : Main.students) {
                System.out.print(s.getRollNo() + "(A/P):");
                String ch = scan.getStringInput("");
                if (ch.equalsIgnoreCase("P")) {
                    s.attendance(subject);
                }
            }
            totalClasses++;
        }
    }


}
