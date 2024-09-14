package ryze.task;

/**
 * Represents a task with a description and completion status.
 */
public class Task {

    /**
     * Description of the task.
     */
    protected String description;

    /**
     * Indicates whether the task is done.
     */
    protected boolean isDone;

    /**
     * Constructs a new Task with the given description.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }
    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return "X" if the task is done, otherwise a space character.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // Mark done task with X
    }

    /**
     * Returns the description of the task.
     *
     * @return The description of the task.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "["+ getStatusIcon() + "] " + description;
    }
    public String toData(){return ("~"+(this.isDone()?1:0)+"~"+this.description);}
}
