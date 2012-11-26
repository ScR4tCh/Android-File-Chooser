package org.scratch.filedialog.filters;

import java.io.File;

import org.scratch.filedialog.FileChooserFileFilter;

import android.os.Parcel;

public class ExpressionFileFilter extends FileChooserFileFilter
{
	String expr=".";
	
	public ExpressionFileFilter(Parcel in)
	{
		expr = in.readString();
	}
	
	public ExpressionFileFilter(String expr)
	{
		this.expr=expr;
	}
	
	@Override
	public boolean accept(File pathname)
	{
		return pathname.getName().matches(expr);
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
		dest.writeString(expr);
	}

}
