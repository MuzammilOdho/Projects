
import java.util.ArrayList;

public class Seller extends Customer {
    ArrayList<Car> availableCar = new ArrayList<Car>();

    public void addCar() {
        Car car = new Car();
        availableCar.add(car);
    }

    public void removeCar() {

    }

    public void checkOffers() {

    }

    Seller() {
    }

}
