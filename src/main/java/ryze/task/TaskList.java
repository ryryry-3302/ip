package ryze.task;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }


    public Task getTask(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public void removeTask(int index) {
        tasks.remove(index);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
