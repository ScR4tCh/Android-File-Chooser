package org.scratch.filedialog;

import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class FileChooserFileFilter implements FileFilter,Parcelable
{
			public FileChooserFileFilter()
			{
				
			}
			
			public FileChooserFileFilter(Parcel in)
			{
				
			}

			public static final Parcelable.Creator<FileChooserFileFilter> CREATOR = new Parcelable.Creator<FileChooserFileFilter>()
			{
				public  FileChooserFileFilter createFromParcel(final Parcel in)
				{
					FileChooserFileFilter ret = null;
					try
					{
						String clazz = in.readString();
						
						System.err.println("trying to instatntiate "+clazz);
						
						try
						{
							ret=(FileChooserFileFilter)getClass().getClassLoader().loadClass(clazz).getConstructor(Parcel.class).newInstance(in);
						}
						catch(NoSuchMethodException e)
						{
							ret=(FileChooserFileFilter)getClass().getClassLoader().loadClass(clazz).newInstance();
							ret.createFilterFromParcel(in);
						}
					}
					catch(IllegalAccessException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(InstantiationException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(ClassNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(IllegalArgumentException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(SecurityException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch(InvocationTargetException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					return ret;
				}

				public FileChooserFileFilter[] newArray(int size)
				{
					return new FileChooserFileFilter[size];
				}
			};
			
			@Override
			public void writeToParcel(Parcel dest,int flags)
			{
				dest.writeString(getClass().getName());
				write(dest,flags);
			}
			
			public void createFilterFromParcel(final Parcel in)
			{
				
			}
			
			public abstract void write(Parcel dest,int flags);
}
