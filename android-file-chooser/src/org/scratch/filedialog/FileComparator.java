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
