package Battery;

public class Battery implements IBattery{
    @Override
    public int getBatteryLevel() {
        return 0;
    }

    @Override
    public int expectedConsumptionToDestination() {
        return 0;
    }

    @Override
    public boolean isDeliveryPossible() {
        return false;
    }

    @Override
    public long maxFlightRange() {
        return 0;
    }
}
