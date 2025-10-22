public class SimulationResult {
    public final int planesTakenOff;
    public final int planesLanded;
    public final int planesCrashed;
    public final double avgTakeoffWait;
    public final double avgLandingWait;

    public SimulationResult(int planesTakenOff, int planesLanded, int planesCrashed,
                            double avgTakeoffWait, double avgLandingWait) {
        this.planesTakenOff = planesTakenOff;
        this.planesLanded = planesLanded;
        this.planesCrashed = planesCrashed;
        this.avgTakeoffWait = avgTakeoffWait;
        this.avgLandingWait = avgLandingWait;
    }
}
