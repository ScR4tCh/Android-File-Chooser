Android-File-Chooser
====================
This is a fork of the android-file-dialog https://code.google.com/p/android-file-dialog/ by alexander.ponomarev.

Intentprocessing and file listing were changed as well as the layout (little improvements).

There are several fields that may be specified for the Intent calling the Activity:

###START_PATH
The directory the FileDialog should point to initially.

### FORMAT_FILTER
FileFilter(s)

### OPERATION_MODE
The mode of operation :), either MODE_CREATE to "save" files or MODE_OPEN to open/select files

### SELECT_MODE
Specifies the elements to be selectable by the user, this is a bitmask with may be constructed from SELECT_FOLDER and SELECT_FILE

### VIEW_MODE
Almost the same as SELECT_MODE, but will only affect the "viewable" (displayed) elements [files and/or folders] (viewMode can also be changed by FORMAT_FILTER)

### BROWSE_ENABLED
Allow user to browse inside the "START_PATH" (true by default)

### FILE_DRAWABLE
Bitmap for file icon.

### FOLDER_DRAWABLE
Bitmap for folder icon.


##example:
	Intent intent = new Intent(getBaseContext(),FileChooserDialog.class);
	intent.putExtra(FileChooserDialog.START_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
	intent.putExtra(FileChooserDialog.SELECT_MODE, FileChooserDialog.MODE_OPEN);

	//can user select directories or not
	intent.putExtra(FileChooserDialog.VIEW_MODE, FileChooserDialog.SELECT_FILE);
	intent.putExtra(FileChooserDialog.FILE_DRAWABLE, BitmapFactory.decodeResource(getResources(),R.drawable.fontfile));
	intent.putExtra(FileChooserDialog.FOLDER_DRAWABLE, BitmapFactory.decodeResource(getResources(),R.drawable.folder));
	intent.putExtra(FileChooserDialog.UP_DRAWABLE, BitmapFactory.decodeResource(getResources(),R.drawable.folderup));
                              
	FileChooserFileFilter[] cf = new FileChooserFileFilter[]{new SuffixFileFilter(new String[]{"ttf"}),new FolderFileFilter()}; 
	intent.putExtra(FileChooserDialog.FORMAT_FILTER,new CombinedFileFilter(cf,CombinedCheck.OR));

	startActivityForResult(intent,CHOOSE_FONT);


This lib is licensed under The LGPL.
