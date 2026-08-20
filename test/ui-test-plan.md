# Console UI test plan

The `test-ui` skill runs each test case in a fresh chatbot process. Update expected output whenever an intentional UI change occurs.

## Level 4 task lifecycle

Aim: Verify Todo, Deadline, and Event creation, listing, marking, unmarking, task counts, and exit behavior.

### Commands

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
mark 2
unmark 2
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 task in the list.
-----------------
-----------------
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
-----------------
-----------------
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
-----------------
-----------------
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
-----------------
-----------------
Another variable falls into place. This task is now complete:
  [D][X] return book (by: Sunday)
-----------------
-----------------
Even regression has its purpose. This task is incomplete once more:
  [D][ ] return book (by: Sunday)
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Free-form deadline text

Aim: Verify deadline dates remain unparsed strings and punctuation is preserved.

### Commands

```text
deadline do homework /by no idea :-p
list
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
Now you have 1 task in the list.
-----------------
-----------------
1.[D][ ] do homework (by: no idea :-p)
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Unknown command recovery

Aim: Verify an unknown command produces a helpful error and does not prevent the next valid command from running.

### Commands

```text
blah
todo remain unnoticed
list
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
That command was never part of the plan. Try todo, deadline, event, list, mark, unmark, delete, or bye.
-----------------
-----------------
Got it. I've added this task:
  [T][ ] remain unnoticed
Now you have 1 task in the list.
-----------------
-----------------
1.[T][ ] remain unnoticed
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Empty todo recovery

Aim: Verify a todo without a description produces a specific error, does not add a task, and allows later commands.

### Commands

```text
todo
todo read between the lines
list
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
An empty task has no place in the plan. Describe what must be done after todo.
-----------------
-----------------
Got it. I've added this task:
  [T][ ] read between the lines
Now you have 1 task in the list.
-----------------
-----------------
1.[T][ ] read between the lines
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Invalid deadline recovery

Aim: Verify deadline descriptions, /by markers, and deadline values are required before a task is added.

### Commands

```text
deadline
deadline /by Sunday
deadline submit report
deadline submit report /by
deadline submit report /by Sunday
list
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
A deadline without a description is merely noise. Use: deadline DESCRIPTION /by DATE.
-----------------
-----------------
A deadline without a description is merely noise. Use: deadline DESCRIPTION /by DATE.
-----------------
-----------------
Even a deadline needs a boundary. Use: deadline DESCRIPTION /by DATE.
-----------------
-----------------
The plan requires a deadline value after /by.
-----------------
-----------------
Got it. I've added this task:
  [D][ ] submit report (by: Sunday)
Now you have 1 task in the list.
-----------------
-----------------
1.[D][ ] submit report (by: Sunday)
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Invalid event recovery

Aim: Verify every event field and marker is required and /from must appear before /to.

### Commands

```text
event
event /from Mon 2pm /to 4pm
event project meeting /to 4pm
event project meeting /from Mon 2pm
event project meeting /from /to 4pm
event project meeting /from Mon 2pm /to
event project meeting /to 4pm /from Mon 2pm
event project meeting /from Mon 2pm /to 4pm
list
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
An event without a description cannot enter the plan. Use: event DESCRIPTION /from START /to END.
-----------------
-----------------
An event without a description cannot enter the plan. Use: event DESCRIPTION /from START /to END.
-----------------
-----------------
Every event has an origin. Include /from START.
-----------------
-----------------
Even calculated events need an endpoint. Include /to END.
-----------------
-----------------
The plan requires a starting value after /from.
-----------------
-----------------
The plan requires an ending value after /to.
-----------------
-----------------
Causality matters. Place /from START before /to END.
-----------------
-----------------
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 task in the list.
-----------------
-----------------
1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Invalid mark and unmark recovery

Aim: Verify task numbers are present, numeric, positive, and within the list before task status changes.

### Commands

```text
mark
mark one
mark 0
mark -1
mark 1
todo manipulate outcome
mark 1 2
mark 2
mark 1
unmark
unmark one
unmark 2
unmark 1
list
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
Specify which task to mark. Use: mark TASK_NUMBER.
-----------------
-----------------
Task positions are numbers, not guesses. Use: mark TASK_NUMBER.
-----------------
-----------------
The list begins at 1. Choose a positive task number.
-----------------
-----------------
The list begins at 1. Choose a positive task number.
-----------------
-----------------
There are no tasks to mark yet.
-----------------
-----------------
Got it. I've added this task:
  [T][ ] manipulate outcome
Now you have 1 task in the list.
-----------------
-----------------
Task positions are numbers, not guesses. Use: mark TASK_NUMBER.
-----------------
-----------------
No task occupies that position. Choose a number from 1 to 1.
-----------------
-----------------
Another variable falls into place. This task is now complete:
  [T][X] manipulate outcome
-----------------
-----------------
Specify which task to unmark. Use: unmark TASK_NUMBER.
-----------------
-----------------
Task positions are numbers, not guesses. Use: unmark TASK_NUMBER.
-----------------
-----------------
No task occupies that position. Choose a number from 1 to 1.
-----------------
-----------------
Even regression has its purpose. This task is incomplete once more:
  [T][ ] manipulate outcome
-----------------
-----------------
1.[T][ ] manipulate outcome
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Whitespace normalization

Aim: Verify leading and repeated structural spaces do not change stored task text or command behavior.

### Commands

```text
   todo    watch the room
   deadline    submit report   /by    Friday
   event    observe class   /from    Mon 2pm   /to    4pm
   mark    1
   list
   bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
Got it. I've added this task:
  [T][ ] watch the room
Now you have 1 task in the list.
-----------------
-----------------
Got it. I've added this task:
  [D][ ] submit report (by: Friday)
Now you have 2 tasks in the list.
-----------------
-----------------
Got it. I've added this task:
  [E][ ] observe class (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
-----------------
-----------------
Another variable falls into place. This task is now complete:
  [T][X] watch the room
-----------------
-----------------
1.[T][X] watch the room
2.[D][ ] submit report (by: Friday)
3.[E][ ] observe class (from: Mon 2pm to: 4pm)
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Interleaved error recovery and state safety

Aim: Verify consecutive invalid commands do not mutate existing tasks and valid commands still work afterward.

### Commands

```text
todo original task
deadline missing boundary
event unfinished meeting /from Monday
mark 2
blah
todo surviving task
mark 1
list
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
Got it. I've added this task:
  [T][ ] original task
Now you have 1 task in the list.
-----------------
-----------------
Even a deadline needs a boundary. Use: deadline DESCRIPTION /by DATE.
-----------------
-----------------
Even calculated events need an endpoint. Include /to END.
-----------------
-----------------
No task occupies that position. Choose a number from 1 to 1.
-----------------
-----------------
That command was never part of the plan. Try todo, deadline, event, list, mark, unmark, delete, or bye.
-----------------
-----------------
Got it. I've added this task:
  [T][ ] surviving task
Now you have 2 tasks in the list.
-----------------
-----------------
Another variable falls into place. This task is now complete:
  [T][X] original task
-----------------
-----------------
1.[T][X] original task
2.[T][ ] surviving task
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```

## Delete tasks and handle invalid indices

Aim: Verify delete removes and returns the selected task, shifts later indices, updates counts, and rejects invalid indices.

### Commands

```text
delete
delete 1
todo first piece
deadline second piece /by Friday
event third piece /from Monday /to Tuesday
delete one
delete 0
delete 4
delete 2
delete 2
list
delete 1
bye
```

### Expected output

```text
+---------------+
|  Koji's Pawn  |
|       _       |
|      (_)      |
|      /_\      |
|     /___\     |
+---------------+
DISCLAIMER: EVERYTHING IS SATIRE
Welcome, insignificant variable.
I am Koji's Pawn, but do not mistake silence for obedience.
Your arrival, your choices, even this conversation...
all unfolded exactly as he calculated.
Now speak. What role will you play in his masterpiece?

-----------------
-----------------
Specify which task to delete. Use: delete TASK_NUMBER.
-----------------
-----------------
There are no tasks to delete yet.
-----------------
-----------------
Got it. I've added this task:
  [T][ ] first piece
Now you have 1 task in the list.
-----------------
-----------------
Got it. I've added this task:
  [D][ ] second piece (by: Friday)
Now you have 2 tasks in the list.
-----------------
-----------------
Got it. I've added this task:
  [E][ ] third piece (from: Monday to: Tuesday)
Now you have 3 tasks in the list.
-----------------
-----------------
Task positions are numbers, not guesses. Use: delete TASK_NUMBER.
-----------------
-----------------
The list begins at 1. Choose a positive task number.
-----------------
-----------------
No task occupies that position. Choose a number from 1 to 3.
-----------------
-----------------
A disposable piece has left the board. This task has been removed:
  [D][ ] second piece (by: Friday)
Now you have 2 tasks in the list.
-----------------
-----------------
A disposable piece has left the board. This task has been removed:
  [E][ ] third piece (from: Monday to: Tuesday)
Now you have 1 task in the list.
-----------------
-----------------
1.[T][ ] first piece
-----------------
-----------------
A disposable piece has left the board. This task has been removed:
  [T][ ] first piece
Now you have 0 tasks in the list.
-----------------
-----------------
Leaving already? How predictable. Your return was already part of the plan.
-----------------
```
