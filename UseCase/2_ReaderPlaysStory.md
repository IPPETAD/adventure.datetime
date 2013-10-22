Reader Plays Story
==================
Reader reads the Choose-Your-Own-Adventure story

Participating Actors
--------------------
- Reader
- System
- Server

Goal
----
- Reader reads story

Trigger
-------
- Reader selects story to read

Precondition
------------
- Reader has app installed
- Internet connection exists via data or wifi

Postcondition
-------------
- Reader completes the reading of a story.

Basic Flow
----------
1. Reader opens app.
2. Reader selectes story to read.
3. System fetches story information from server and displays information about
   the story.
4. Reader selectes "Play Story".
5. System fetches first story fragment from server and displays the fragment.
6. Reader reads fragment.
7. Reader makes Choice.
8. System fetches next fragment from server.
9. Steps 6-8 repeat until Reader completes story.

Exceptions
----------
3, 5, 8: If story resides on local storage:
- System fetches all story information and fragments from local storage.
