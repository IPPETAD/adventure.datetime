100 Reader Saves Story Locally
------------------------------

Participating Actors
--------------------
- Reader
- System
- Server

Goal
----
- All elements required to view a story are downloaded and stored locally on the Reader's phone.

Trigger
-------
- While on either the story description or fragment screen the reader pushes the 'Read Offline' button available via the action bar.

Precondition
------------
- Internet connection available

Postcondition
-------------
The story will be saved locally and be available for consumption regardless of the state of the phones internet connection.

Basic Flow
----------
1. Reader opens app.
2. Reader selects the browse button
3. Reader selects a story
4. Reader selects the 'Read Offline' button in the action bar.
5. System downloads all story fragments and resource files (images and videos) and stores them locally.

Exceptions
----------
If at step 3 the Reader chooses a story that is already saved locally then upon reaching step 4 the 'Read Offline' button will be replaced with the message "story available offline".