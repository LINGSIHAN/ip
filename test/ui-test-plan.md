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
That command was never part of the plan. Try todo, deadline, event, list, mark, unmark, or bye.
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
