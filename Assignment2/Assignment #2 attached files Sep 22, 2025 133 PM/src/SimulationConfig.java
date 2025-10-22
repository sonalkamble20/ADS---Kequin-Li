//Name: Sonal Kamble
//
 public class SimulationConfig {
    public final int minutesToLand;
    public final int minutesToTakeoff;
    public final double pArrive;
    public final double pDepart;
    public final int maxAirWait;
    public final int totalMinutes;
    public final Long randomSeed;

    public SimulationConfig(int minutesToLand,
                            int minutesToTakeoff,
                            double pArrive,
                            double pDepart,
                            int maxAirWait,
                            int totalMinutes,
                            Long randomSeed) {
        if (minutesToLand <= 0 || minutesToTakeoff <= 0)
            throw new IllegalArgumentException("Service times must be positive.");
        if (pArrive < 0 || pArrive > 1 || pDepart < 0 || pDepart > 1)
            throw new IllegalArgumentException("Probabilities must be in [0,1].");
        if (maxAirWait < 0)
            throw new IllegalArgumentException("maxAirWait must be >= 0.");
        if (totalMinutes < 0)
            throw new IllegalArgumentException("totalMinutes must be >= 0.");

        this.minutesToLand = minutesToLand;
        this.minutesToTakeoff = minutesToTakeoff;
        this.pArrive = pArrive;
        this.pDepart = pDepart;
        this.maxAirWait = maxAirWait;
        this.totalMinutes = totalMinutes;
        this.randomSeed = randomSeed;
    }
}
