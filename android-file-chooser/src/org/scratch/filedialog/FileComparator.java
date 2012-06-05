package org.scratch.filedialog;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File>
{
		@Override
		public int compare(File one,File two)
		{
			if(one.isDirectory() && !two.isDirectory())
			{
				return -1;
			}
			else if(!one.isDirectory() && two.isDirectory())
			{
				return 1;
			}
			else
			{
				return one.compareTo(two);
			}
		}
}
