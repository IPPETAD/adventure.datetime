103 Author Publishes Created Story
----------------------------------

Participating Actors
--------------------
- Author
- System
- Server

Goal
----
- Author is able to publish story to the server

Trigger
-------
- Author selects publish button while in any of the develop screens.

Precondition
------------
- Internet connection available
- Author has a story available for publish

Postcondition
-------------
- Story is stored on remote server and is available for online browsing by all users.

Basic Flow
----------
1. Author launches app.
2. Author selects develop.
3. Author selects a story that they already have created.
4. Author selects the publish button.
5. System gathers up resources needed to publish story.
6. System transmits all resources to the server.
7. Server stores story and allows other users to browse or download it.

Exceptions
----------
- If at 5 there are missing resources inform the Author of the missing resource and its location in the story. Proceed to terminate the operation.
- If at 6 there is any network failure terminate the operation and inform the Author. The user may retry after the server connection is re-established.
- If at 7 and there is a pre-existing version of this story then the server is to cease to provide the previous story version and now provide the newly uploaded version.