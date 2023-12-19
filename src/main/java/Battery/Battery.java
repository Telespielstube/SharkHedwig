package Battery;

public class Battery implements IBattery {

    public Battery() {

    }
    @Override
    public int getBatteryLevel() {
        return 80;
    }

    @Override
    public int expectedConsumptionToDestination() {
        return 0;
    }

    @Override
    public boolean isDeliveryPossible() {
        return false;
    }
}
