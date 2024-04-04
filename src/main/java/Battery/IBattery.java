package Battery;


public interface IBattery {

    /**
     * the current battery level.
     *
     * @return    Current battery level.
     */
    public int getCurrentBatteryLevel();

    /**
     * Claculates an estimated value based on the passed parameter.
     *
     * @return               Expected battery consumption.
     */
    public int expectedConsumptionToDestination();

    /**
     * Based on the calculated consumption trueor false is returned.
     *
     * @return    true if delivery is possible false if not.
     */
    public boolean isDeliveryPossible();

    /**
     * Returns the maximum flight range of the device.
     *
     * @return    Distance in km.
     */
    public int maxFlightRange();
}
