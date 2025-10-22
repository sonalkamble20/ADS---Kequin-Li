import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Simulator {

    private enum Op { LAND, TAKEOFF, NONE }

    private final SimulationConfig cfg;
    private final Queue<Plane> landingQ = new ArrayDeque<>();
    private final Queue<Plane> takeoffQ = new ArrayDeque<>();
    private final Random rng;
    private int nextId = 1;

    private Op currentOp = Op.NONE;
    private int busyUntil = 0;

    private int landed = 0;
    private int tookOff = 0;
    private int crashed = 0;
    private long sumLandWait = 0;
    private long sumTakeoffWait = 0;

    public Simulator(SimulationConfig cfg) {
        this.cfg = cfg;
        this.rng = (cfg.randomSeed == null) ? new Random() : new Random(cfg.randomSeed);
    }

    public SimulationResult run() {
        for (int t = 0; t < cfg.totalMinutes; t++) {
            freeRunwayIfDone(t);      // free runway first
            tryGenerateArrivals(t);

            if (currentOp == Op.NONE) {
                if (!tryStartLanding(t)) {
                    tryStartTakeoff(t);
                }
            }
        }

        finalizeCrashesAtEnd(cfg.totalMinutes);

        double avgTakeoff = (tookOff == 0) ? 0.0 : (sumTakeoffWait * 1.0) / tookOff;
        double avgLanding = (landed == 0) ? 0.0 : (sumLandWait * 1.0) / landed;

        return new SimulationResult(tookOff, landed, crashed, avgTakeoff, avgLanding);
    }

    private void tryGenerateArrivals(int t) {
        if (rng.nextDouble() < cfg.pArrive) {
            landingQ.add(new Plane(nextId++, t, Plane.Type.LANDING));
        }
        if (rng.nextDouble() < cfg.pDepart) {
            takeoffQ.add(new Plane(nextId++, t, Plane.Type.TAKEOFF));
        }
    }

    private void freeRunwayIfDone(int t) {
        if (t >= busyUntil) {
            currentOp = Op.NONE;
        }
    }

    private boolean tryStartLanding(int t) {
        boolean started = false;
        while (!landingQ.isEmpty() && currentOp == Op.NONE) {
            Plane p = landingQ.remove();
            int wait = t - p.enqueueTime;
            if (wait > cfg.maxAirWait) { // STRICTLY greater
                crashed++;
                continue;
            }
            landed++;
            sumLandWait += wait;
            currentOp = Op.LAND;
            busyUntil = t + cfg.minutesToLand;
            started = true;
        }
        return started;
    }

    private boolean tryStartTakeoff(int t) {
        if (takeoffQ.isEmpty() || currentOp != Op.NONE) return false;
        Plane p = takeoffQ.remove();
        int wait = t - p.enqueueTime;
        tookOff++;
        sumTakeoffWait += wait;
        currentOp = Op.TAKEOFF;
        busyUntil = t + cfg.minutesToTakeoff;
        return true;
    }

    private void finalizeCrashesAtEnd(int tEnd) {
        while (!landingQ.isEmpty()) {
            Plane p = landingQ.remove();
            int wait = tEnd - p.enqueueTime;
            if (wait > cfg.maxAirWait) crashed++;
        }
    }
}
