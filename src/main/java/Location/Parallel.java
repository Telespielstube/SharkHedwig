package Location;

public enum Parallel {
    // location constants.
    ParallelOfLatitude(111.3),
    ParallelOfLongitude(71.5);

    private final double distance;

    Parallel(double distance) {
        this.distance = distance;
    }

    public double getParallel() {
        return this.distance;
    }
}
