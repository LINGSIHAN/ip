# Koji's Pawn User Guide

Koji's Pawn is a command-line chatbot that manages todos, deadlines, and events. It automatically saves tasks between sessions and can find deadlines occurring on a specified date.

## Command reference

Enter one command per line. Command words must be written in lowercase. Words shown in uppercase below, such as `DESCRIPTION`, are placeholders that you should replace with your own values.

| Command | What it does | What to enter |
|---|---|---|
| `todo` | Adds a task without a date or time. | `todo DESCRIPTION` |
| `deadline` | Adds a task that must be completed by a date. The date must use `yyyy-MM-dd`. | `deadline DESCRIPTION /by yyyy-MM-dd` |
| `event` | Adds an event with a start and end. Start and end values are currently stored as free-form text. | `event DESCRIPTION /from START /to END` |
| `list` | Displays all tasks and their task numbers. | `list` |
| `mark` | Marks the selected task as completed. | `mark TASK_NUMBER` |
| `unmark` | Marks the selected task as incomplete again. | `unmark TASK_NUMBER` |
| `delete` | Permanently removes the selected task from the list. | `delete TASK_NUMBER` |
| `on` | Displays deadlines occurring on a specified date. Todos and free-form events are not included. | `on yyyy-MM-dd` |
| `bye` | Exits the chatbot. Task changes have already been saved automatically. | `bye` |

## Command examples

### Adding a todo

```text
todo borrow book
```

### Adding a deadline

Deadline dates must use `yyyy-MM-dd`. Although the date is entered as `2019-12-02`, it is displayed as `Dec 2 2019`.

```text
deadline return book /by 2019-12-02
```

### Adding an event

```text
event project meeting /from Monday 2pm /to 4pm
```

### Listing tasks

```text
list
```

### Marking, unmarking, and deleting tasks

Use the number displayed by `list`. Task numbering starts at `1`.

```text
mark 2
unmark 2
delete 2
```

### Finding deadlines by date

```text
on 2019-12-02
```

If no deadline occurs on that date, Koji's Pawn reports that no dated tasks were found.

### Exiting the chatbot

```text
bye
```

## Task symbols

| Symbol | Meaning |
|---|---|
| `[T]` | Todo |
| `[D]` | Deadline |
| `[E]` | Event |
| `[X]` | Completed |
| `[ ]` | Incomplete |

For example, a completed deadline is displayed as:

```text
[D][X] return book (by: Dec 2 2019)
```

## Saving tasks

Tasks are saved automatically after a task is added, marked, unmarked, or deleted. They are loaded again when Koji's Pawn starts. The data is stored locally at:

```text
data/kojispawn.txt
```

You do not need to edit this file manually.
