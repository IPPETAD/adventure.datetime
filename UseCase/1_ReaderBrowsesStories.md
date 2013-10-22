Reader Browses for Stories
==========================
Shows the stories available for reading.

Participating Actors
--------------------
- Reader
- System

Goal
----
- Reader finds story to read

Trigger
-------
- Reader presses "Browse"

Precondition
------------
- Reader has app installed
- Internet connection exists via data or wifi

Postcondition
-------------
- App has stories listed in order of age.

Basic Flow
----------
1. Reader opens app.
2. Reader queries system for story with specific parameters (title, author,
   etc.)
3. System returns a list of stories that match given parameters.
4. App lists stories returned by newest first.
