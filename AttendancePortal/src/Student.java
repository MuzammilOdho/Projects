import java.io.Serializable;

public class Student implements Serializable {
    private String rollNo;
    private int OOPatt;
    private int LAAGatt;
    private int ISatt;
    private int PSatt;
    private int PPatt;
    private int ISEatt;

    public Student(String rollNo) {
        this.rollNo = rollNo;
        this.ISatt=0;
        this.ISEatt=0;
        this.LAAGatt=0;
        this.PPatt=0;
        this.OOPatt=0;
        this.PSatt=0;

    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public int getOOPatt() {
        return OOPatt;
    }



    public int getLAAGatt() {
        return LAAGatt;
    }



    public int getISatt() {
        return ISatt;
    }



    public int getPSatt() {
        return PSatt;
    }


    public int getPPatt() {
        return PPatt;
    }


    public int getISEatt() {
        return ISEatt;
    }
    public void attendance(String subject) {
        switch (subject) {
            case "OOP":
                OOPatt++;
                break;

            case "ISE":
                ISEatt++;
                break;
            case "LAAG":
                LAAGatt++;
                break;
            case "PS":
                PSatt++;
                break;
            case "IS":
                ISatt++;
                break;
            case "PP":
                PPatt++;
                break;
        }
    }
}