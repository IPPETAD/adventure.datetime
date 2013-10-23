101 Reader Reads a story that was stored locally
------------------------------------------------

Participating Actors
--------------------
- Reader
- System

Goal
----
- A story that was previously saved for offline viewing is presented to the Reader.
- The Reader must percieve the story as being the same as if it were being viewed from an online source.

Trigger
-------
- User selects to either begin or continue a story that was previously stored locally for offline reading (see use case 100).

Precondition
------------
- Use Case 100 was already executed
- Desired story was successfully stored locally.

Postcondition
-------------
- Reader is able to view any and all elements of the selected offline story.

Basic Flow
----------
1. Reader opens app.
2. Reader browses available stories.
3. Reader selects a story that was stored offline (please see preconditions).
4. System loads story from local storage.
5. Reader proceeds to interact with the story as in use case 1.

Exceptions
----------
- If at step 2 the user selects to continue a story rather than browse then proceed with steps:
   2b. Reader browses available offline bookmarks.
   3b. Reader selects a story/bookmark that was stored offline.
       Continue from step 4 of basic flow.