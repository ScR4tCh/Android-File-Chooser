package org.scratch.filedialog.filters;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.scratch.filedialog.FileChooserFileFilter;

import android.R;
import android.os.Parcel;

public class SuffixFileFilter extends FileChooserFileFilter
{
	String[] suffixList;
	
	public SuffixFileFilter(Parcel in)
	{
		String[] suffixList=in.createStringArray();
		Arrays.sort(suffixList);
		this.suffixList=suffixList;
	}
	
	public SuffixFileFilter(Collection<String> suffixList)
	{
		if(suffixList!=null)
		{
			this.suffixList=new String[suffixList.size()];
			Iterator<String> si = suffixList.iterator();
			int i=0;
			while(si.hasNext())
			{
				this.suffixList[i]=si.next().toLowerCase();
				i++;
			}
			
			Arrays.sort(this.suffixList);
		}
		else
		{
			this.suffixList=new String[]{};
		}
	}
	
	public SuffixFileFilter(String[] suffixList)
	{
		if(suffixList!=null)
		{
			this.suffixList=new String[suffixList.length];
			
			for(int i=0;i<suffixList.length;i++)
			{
				this.suffixList[i]=suffixList[i].toLowerCase();
			}
			
			Arrays.sort(this.suffixList);
		}
		else
		{
			this.suffixList=new String[]{};
		}
	}
	
	@Override
	public boolean accept(File pathname)
	{
		//note: we are sarching for a suffix not a file "prefixed" by a dot like hiden UN*X files
		String name = pathname.getName();
		if(pathname!=null && pathname.isFile() && name.indexOf('.')>0)
		{
			if( Arrays.binarySearch(suffixList,name.substring(name.indexOf('.')+1).toLowerCase())>=0 )
				return true;
		}
		
		return false;
		
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void write(Parcel dest,int flags)
	{
		dest.writeStringArray(suffixList);
	}

}
