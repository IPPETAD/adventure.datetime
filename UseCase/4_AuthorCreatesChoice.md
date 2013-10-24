Author Creates Choice
=====================
An author with 2 story fragments done can combine them to create a choice.

Participating Actors
--------------------
- Author
- System

Goal
----
- Connect 2 story fragments together with a choice

Trigger
-------
- 2 story fragments exist and the Author wishes to join them.

Precondition
------------
- 2 story fragments exist as part of one story and the Author is in 'Edit' mode.
- Author is in "Edit Story Fragment Screen"

Postcondition
-------------
- The 2 story fragments are joined by a choice.

Basic Flow
----------
1. The Author selects "Edit Choices."
2. The System shows existing choices.
3. The Author selectes "New Choice."
4. The System shows a tree representation of the current story, as well as
   unconnected nodes. All nodes represent a different Story Fragment.
5. The Author selects a story fragment to be the 'child' of the relationship.
6. The System prompts the Author to input a description of the choice to be tied
   to the choice. This description will be used when a Reader is selecting a
   choice to continue the story.
7. The Author inputs their description, and selects 'OK'.
8. The Author saves the new choice.
9. The System prompts the Author for comfirmation.
10. The Author confirms their selection.

Exceptions
----------
3:  
- If the Author does not enter a description:  
	- The System cancels the choice creation.

6:  
- If the Author cancels the save:  
	- The System does not save the new choice.  
