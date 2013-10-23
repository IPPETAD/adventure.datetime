Author Adds Story Fragment To Story
===================================
Adds a fragment to be part of a story.

Participating Actors
--------------------
- Author
- System

Goal
----
- Include a story fragment as part of a story.

Trigger
-------
- Author creates a fragment and selects "Add to Current Story".

Precondition
------------
- Author has begun to create a new Story and has completed creating a Story
  Fragment.

Postconditions
--------------
- The Story Fragment has been added to the story.

Basic Flow
----------
1. Author selects "Add to Current Story".
2. System prompts Author to confirm.
3. Author confirms.

Exceptions
----------
2:
- If the Author cancels the addition:
	- The fragment is not added.
