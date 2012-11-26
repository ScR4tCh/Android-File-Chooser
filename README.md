Android-File-Chooser
====================
This is a fork of the android-file-dialog https://code.google.com/p/android-file-dialog/ by alexander.ponomarev.

Intentprocessing and file listing were changed as well as the layout (little improvements).
Also it is now possible to set the Bitmaps for files/folders by Intent.

There are several fields that may be specified for the Intent calling the Activity:

START_PATH
----------
The directory the FileDialog should point to initially.

FORMAT_FILTER
-------------
FileFilter(s)

OPERATION_MODE
--------------
The mode of operation :), either MODE_CREATE to "save" files or MODE_OPEN to open/select files

SELECT_MODE
-----------
Specifies the elements to be selectable by the user, this is a bitmask with may be constructed from SELECT_FOLDER and SELECT_FILE

VIEW_MODE
---------
Almost the same as SELECT_MODE, but will only affect the "viewable" (displayed) elements [files and/or folders] (viewMode can also be changed by FORMAT_FILTER)


FILE_DRAWABLE
-------------
Bitmap for file icon.

FOLDER_DRAWABLE
---------------
Bitmap for folder icon.


This lib is licensed under The LGPL.
