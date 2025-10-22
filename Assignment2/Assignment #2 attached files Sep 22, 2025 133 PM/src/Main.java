import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Amount of minutes to land: ");
        int minutesToLand = sc.nextInt();

        System.out.print("Amount of minutes to take off: ");
        int minutesToTakeoff = sc.nextInt();

        System.out.print("Probability of arrival during a minute: ");
        double pArrive = sc.nextDouble();

        System.out.printf("Average amount of time between planes to land: %.1f%n",
                (pArrive > 0.0 ? 1.0 / pArrive : Double.POSITIVE_INFINITY));

        System.out.print("Probability of departure during a minute: ");
        double pDepart = sc.nextDouble();

        System.out.printf("Average amount of time between planes to take off: %.1f%n",
                (pDepart > 0.0 ? 1.0 / pDepart : Double.POSITIVE_INFINITY));

        System.out.print("Maximum amount of time in the air before crashing: ");
        int maxAirWait = sc.nextInt();

        System.out.print("Total simulation minutes: ");
        int totalMinutes = sc.nextInt();

        // Fixed seed for reproducibility (change number if you want different runs).
        Long seed = 42L;

        SimulationConfig cfg = new SimulationConfig(
                minutesToLand, minutesToTakeoff,
                pArrive, pDepart,
                maxAirWait, totalMinutes,
                seed
        );

        Simulator sim = new Simulator(cfg);
        SimulationResult res = sim.run();

        System.out.println();
        System.out.println("Number of planes taken off: " + res.planesTakenOff);
        System.out.println("Number of planes landed: " + res.planesLanded);
        System.out.println("Number of planes crashed: " + res.planesCrashed);
        System.out.printf("Average waiting time for taking off: %.2f minutes%n", res.avgTakeoffWait);
        System.out.printf("Average waiting time for landing: %.2f minutes%n", res.avgLandingWait);
    }
}
