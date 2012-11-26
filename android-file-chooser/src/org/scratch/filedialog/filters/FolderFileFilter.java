package org.scratch.filedialog.filters;

import java.io.File;

import org.scratch.filedialog.FileChooserFileFilter;

import android.os.Parcel;

public class FolderFileFilter extends FileChooserFileFilter
{
	@Override
	public boolean accept(File pathname)
	{
		return pathname.isDirectory();
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void write(Parcel dest,int flags)
	{
	}
	
}
