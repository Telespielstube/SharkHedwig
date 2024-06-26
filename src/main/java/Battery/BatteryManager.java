package Battery;

public class BatteryManager implements Battery {

    private int batteryLevel = 100;
    private int maxFlightRange = 100;
    private boolean deliveryPossible = false;
    private int expectedbatteryConsuption = 0;

    public BatteryManager() {}

    @Override
    public int getCurrentBatteryLevel() {
        return this.batteryLevel;
    }

    @Override
    public int expectedConsumptionToDestination() {
        return this.expectedbatteryConsuption;
    }

    @Override
    public boolean isDeliveryPossible() {
        return this.deliveryPossible;
    }

    @Override
    public int maxFlightRange() {
        return this.maxFlightRange;
    }
}
