package Battery;

public interface IBattery {

    /**
     * the current battery level.
     *
     * @return    Current battery level.
     */
    public int getBatteryLevel();

    /**
     * Claculates an estimated value based on the passed parameter.
     *
     * @param destination    Destination of the package.
     * @return               Expected battery consumption.
     */
    public int expectedConsumptionToDestination(Location destination);

    /**
     * Based on the calculated consumption trueor false is returned.
     *
     * @return    true if delivery is possible false if not.
     */
    public boolean isDeliveryPossible();
}
