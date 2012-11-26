package org.scratch.filedialog.filters;

import java.io.File;
import java.util.Collection;

import org.scratch.filedialog.FileChooserFileFilter;

import android.os.Parcel;
import android.os.Parcelable;

public class CombinedFileFilter extends FileChooserFileFilter
{
	private FileChooserFileFilter[] filters;
	private CombinedCheck check;

	public CombinedFileFilter(Parcel in)
	{
		super(in);
		
		Parcelable[] pa = in.readParcelableArray(FileChooserFileFilter.class.getClassLoader());
		filters = new FileChooserFileFilter[pa.length];
		
		for(int i=0;i<pa.length;i++)
			filters[i]=(FileChooserFileFilter)pa[i];
						
		try
		{
			check = CombinedCheck.values()[in.readInt()];
		}
		catch(ArrayIndexOutOfBoundsException aioobe)
		{
			check=CombinedCheck.AND;
		}
	}
	
	public CombinedFileFilter(final Collection<FileChooserFileFilter> filters,CombinedCheck check)
	{
		this.check=check;
		
		if(filters!=null)
		{
			this.filters=new FileChooserFileFilter[filters.size()];
			filters.toArray(this.filters);
		}
		else
		{
			this.filters=new FileChooserFileFilter[]{};
		}
	}
	
	public CombinedFileFilter(final FileChooserFileFilter[] filters,CombinedCheck check)
	{
		this.check=check;
		
		if(filters!=null)
		{
			this.filters=filters;
		}
		else
		{
			this.filters=new FileChooserFileFilter[]{};
		}
	}
	
	@Override
	public boolean accept(File file)
	{
		//use an AND conjunction
		boolean b=false;
		
		for(int i=0;i<filters.length;i++)
		{
			if(filters[i]!=null)
			{				
				switch(check)
				{
					case OR:	b = b||filters[i].accept(file);
								break;
									
					case AND:	b = b&&filters[i].accept(file);
								break;
				}
			}
		}
		
		return b;
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void write(Parcel dest,int flags)
	{
		dest.writeParcelableArray(filters,flags);
		dest.writeInt(check.ordinal());
	}

}
