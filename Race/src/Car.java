import java.util.Random;

public class Car implements Vehicle {
   private String model;
   private String brand;

   private int speed;
    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    @Override
    public int getSpeed() {
        return speed;
    }
    @Override
    public  void start(){
        this.speed=0;
    }
    @Override
    public void driving(){
        if (speed < 80 ){
            speed+=20;

        }else {
            int speedLimit = new Random().nextInt(100,220);
            if (speed < speedLimit) {
                speed += 20;
            }
        }
    }



    Car(){}
    Car(String brand, String model){
        this.brand=brand;
        this.model=model;
    }
}
