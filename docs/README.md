### Ryze User Guide

#### Introduction
Welcome to **Ryze**, your personal task management assistant. Ryze helps you keep track of tasks such as Todos, Deadlines, and Events. You can add tasks, mark them as completed, delete them, and even search for specific tasks using keywords. This guide will walk you through the essential features of Ryze, complete with usage examples and expected outcomes.

---
# Quick Start
1. Please download the latest release along with Java 17
2. Create a folder and open CMD / Terminal inside it
3. Run this
  ```shell
  path/to/ur/folder java -jar ryze.jar
  ```


# Features

## Adding Todos
- **Action**: Add a simple task that doesn't have a specific deadline or time.
- **Usage**: `todo [task description]`
  
**Example**:
```
todo Finish reading the book
```

**Expected Output**:
```
Got it. I've added this task:
  [T][ ] Finish reading the book
Now you have 1 task in the list.
```

---

## Adding Deadlines
- **Action**: Add a task that has a deadline.
- **Usage**: `deadline [task description] /by [date/time]`
  
**Example**:
```
deadline Submit assignment /by Sunday 11:59PM
```

**Expected Output**:
```
Got it. I've added this task:
  [D][ ] Submit assignment (by: Sunday 11:59PM)
Now you have 2 tasks in the list.
```

---

## Adding Events
- **Action**: Add an event with a start and end time.
- **Usage**: `event [task description] /from [start time] /to [end time]`
  
**Example**:
```
event Team meeting /from 10AM /to 11AM
```

**Expected Output**:
```
Got it. I've added this task:
  [E][ ] Team meeting (from: 10AM to 11AM)
Now you have 3 tasks in the list.
```

---

## Listing All Tasks
- **Action**: List all tasks currently in your task list.
- **Usage**: `list`
  
**Example**:
```
list
```

**Expected Output**:
```
Here are the tasks in your list:
1.[T][ ] Finish reading the book
2.[D][ ] Submit assignment (by: Sunday 11:59PM)
3.[E][ ] Team meeting (from: 10AM to 11AM)
```

---

## Marking Tasks as Done
- **Action**: Mark a task as completed.
- **Usage**: `mark [task number]`
  
**Example**:
```
mark 1
```

**Expected Output**:
```
Nice! I've marked this task as done:
  [T][X] Finish reading the book
```

---

## Unmarking Tasks
- **Action**: Mark a completed task as not done.
- **Usage**: `unmark [task number]`
  
**Example**:
```
unmark 1
```

**Expected Output**:
```
Okay! I've marked this task as not done:
  [T][ ] Finish reading the book
```

---

## Deleting Tasks
- **Action**: Remove a task from the list.
- **Usage**: `delete [task number]`
  
**Example**:
```
delete 2
```

**Expected Output**:
```
Noted. I've removed this task:
  [D][ ] Submit assignment (by: Sunday 11:59PM)
Now you have 2 tasks in the list.
```

---

## Finding Tasks
- **Action**: Search for tasks containing a keyword in their description.
- **Usage**: `find [keyword]`
  
**Example**:
```
find meeting
```

**Expected Output**:
```
Here are the tasks in your list:
1.[E][ ] Team meeting (from: 10AM to 11AM)
```

---

## Exiting the Application
- **Action**: Exit the application.
- **Usage**: `bye`

**Expected Output**:
```
Bye! Hope to see you again soon!
```

---

That's the overview of Ryze's core features. Enjoy staying organized and on top of your tasks!
