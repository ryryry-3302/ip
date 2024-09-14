package ryze.task;

public class Event extends Task {
    /**
     * Event start time.
     */
    private String from;
    /**
     * Event end time.
     */
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
    @Override
    public String toData() {
        return "E"+super.toData()+"~"+this.from+"~"+this.to+"\n";
    }
}
