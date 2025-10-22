public class Plane {
    public enum Type { LANDING, TAKEOFF }

    public final int id;
    public final int enqueueTime; // minute entered the queue
    public final Type type;

    public Plane(int id, int enqueueTime, Type type) {
        this.id = id;
        this.enqueueTime = enqueueTime;
        this.type = type;
    }
}
