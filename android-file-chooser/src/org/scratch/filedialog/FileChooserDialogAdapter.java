/**
 * Android File Chooser
 * Copyright (C) 2012  ScR4tCh
 * Contact scr4tch@scr4tch.org
 *
 * This is a fork of android-file-dialog (com.lamerman) licensed new BSD
 * Take a look at https://code.google.com/p/android-file-dialog/
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.scratch.filedialog;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.scratch.filedialog.R;


import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//TODO: multi selection function


public class FileChooserDialogAdapter implements ListAdapter
{
	protected static final String ROOT="/";
	
	private static final FileComparator fileComparator = new FileComparator();
	
	private int viewMode;
	
	private String currentPath=ROOT;
	
	private HashMap<String, Integer> lastPositions=new HashMap<String, Integer>();
	
	private Vector<File> files=new Vector<File>();
	private Map<File,String> aliasMap = new HashMap<File,String>();
	
	private String parentPath;
	private FileFilter ff;
	
	private Vector<DataSetObserver> dso = new Vector<DataSetObserver>();
	
	private FileChooserDialog fileDialog;
	
	private int selected=-1;
	
	private boolean browsing;

		
	public FileChooserDialogAdapter(FileChooserDialog fileDialog, String startPath,int viewMode,boolean browsing,final FileFilter ff)
	{
		this.fileDialog=fileDialog;
		this.viewMode=viewMode;
		this.browsing=browsing;
		this.ff=ff;
		
		updatefiles(startPath);
	}

	protected void update(final String dirPath)
	{
		updatefiles(dirPath);
		
		informObservers();
	}
	
	private synchronized void updatefiles(final String dirPath)
	{
		if(!currentPath.equals(dirPath))
			selected=-1;
		
		currentPath=dirPath;	
		
		aliasMap.clear();

		File f=new File(currentPath);
		
		parentPath=f.getParent();
		
		File[] filesa;
				
		if(ff!=null)
			filesa = f.listFiles(ff);
		else
			filesa = f.listFiles();
		
		if(filesa==null)
		{
			currentPath=ROOT;
			f=new File(currentPath);
			files=new Vector<File>(Arrays.asList(f.listFiles()));
		}
		else
		{
			files=new Vector<File>(Arrays.asList(filesa));
		}
		
		//sort root->folders->files
		Collections.sort(files,fileComparator);
		
		for(int i=0;i<files.size();i++)
		{
			File file=files.elementAt(i);
				
			switch(viewMode)
			{
				case FileChooserDialog.SELECT_FILE:
					if(file.isDirectory() && !browsing)
						files.remove(file);
					break;
						
				case FileChooserDialog.SELECT_FOLDER:
					if(!file.isDirectory())
						files.remove(file);
					break;
			}			
		}
		
		if(!currentPath.equals(ROOT) && browsing)
		{
			File up = f.getParentFile();
			files.add(0,up);
			aliasMap.put(up,"..");
		}
		
//		informObservers();
		for(int i=0;i<dso.size();i++)
			dso.elementAt(i).onInvalidated();

	}




	@Override
	public int getCount()
	{
		return files.size();
	}

	@Override
	public Object getItem(int position)
	{
		return files.elementAt(position);
	}



	@Override
	public long getItemId(int position)
	{
		return position;
	}



	@Override
	public int getItemViewType(int position)
	{
		return R.layout.file_dialog_row;
	}

	@Override
	public View getView(int position,View convertView,ViewGroup parent)
	{
		View v=convertView;

		if(v==null)
		{
			LayoutInflater vi=(LayoutInflater)parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=vi.inflate(R.layout.file_dialog_row,null);
		}

		TextView filename=(TextView)v.findViewById(R.id.fdrowtext);
		TextView info1=(TextView)v.findViewById(R.id.info1);
		TextView info2=(TextView)v.findViewById(R.id.info2);
		
		ImageView fileico=(ImageView)v.findViewById(R.id.fdrowimage);
		ImageView selectico=(ImageView)v.findViewById(R.id.selected);

		if(position==selected && selectico.getVisibility()==View.GONE)
		{
			selectico.setVisibility(View.VISIBLE);
		}
		else if(position!=selected && selectico.getVisibility()==View.VISIBLE)
		{
			selectico.setVisibility(View.GONE);
		}
		
		File f=files.elementAt(position);

		if(f!=null)
		{
			if(filename!=null)
			{
				if(aliasMap.containsKey(f))
					filename.setText(aliasMap.get(f));
				else
					filename.setText(f.getName());
			}

			//TODO: override for "defined" files/folders (i.e. given list) : mime-detect ?
			if(fileico!=null)
			{
				//Booo, not elegeant !
				if(aliasMap.containsKey(f) && f.isDirectory() && aliasMap.get(f).equals(".."))
					fileico.setImageBitmap(fileDialog.upDrawable);
				else if(f.isDirectory())
					fileico.setImageBitmap(fileDialog.folderDrawable);
				else
					fileico.setImageBitmap(fileDialog.fileDrawable);
			}
			
			//TODO: make optional (visibility)!
			info1.setText(readableFileSize(f.length()));
			info2.setText(android.text.format.DateFormat.getDateFormat(fileDialog.getBaseContext()).format(new Date(f.lastModified())));			

		}

		return v;
	}



	@Override
	public int getViewTypeCount()
	{
		return 1;
	}



	@Override
	public boolean hasStableIds()
	{
		return true;
	}



	@Override
	public boolean isEmpty()
	{
		return false;
	}



	@Override
	public void registerDataSetObserver(DataSetObserver observer)
	{
		dso.add(observer);
	}



	@Override
	public void unregisterDataSetObserver(DataSetObserver observer)
	{
		dso.remove(observer);
	}



	@Override
	public boolean areAllItemsEnabled()
	{
		return true;
	}



	@Override
	public boolean isEnabled(int position)
	{
		return true;
	}

	public void updateToParentPath()
	{
		if(parentPath!=null)
			update(parentPath);
	}

	public void putLastPositions(String currentPath,int position)
	{
		lastPositions.put(currentPath,position);
	}
	
	public final String getCurrentPath()
	{
		return currentPath;
	}
	
	public void setSelected(int selected)
	{
		if(this.selected==selected)
			this.selected=-1;
		else
			this.selected=selected;
		
		for(int i=0;i<dso.size();i++)
			dso.elementAt(i).onInvalidated();
	}
	
	public void informObservers()
	{
		for(int i=0;i<dso.size();i++)
				dso.elementAt(i).onChanged();
	}
	
	/**
	 * THX TO : http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
	 * @param size
	 * @return
	 */
	public static String readableFileSize(long size)
	{
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

}
