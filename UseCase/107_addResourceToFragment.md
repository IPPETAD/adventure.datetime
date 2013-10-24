107 Author add resource to fragment
-----------------------------------

Actors
-----
- Author
- System

Goal
----
- Author can select a local resource (image or video) and add it to the fragment.

Trigger
-------
- Author is editing story fragment and selects the add image/video button

Precondition
------------
- Story fragment must exist
- Video/Image must exist and be available on the phones local storage.

Postcondition
-------------
- Fragment contains image or video resource and displays it during reading.

Basic Steps
-----------
1. Author launches app.
2. Author selects develop.
3. Author selects story.
4. Author selects fragment.
5. Author selects 'Add image/video' button.
6. Author selects an image or video from a list.
7. System saves the resource information locally with the fragment.

Aternate Flow
-----------
1. At step 6, the Author could choose to take a photo with their camera.  The photo could be retaken until it is satisfactory.