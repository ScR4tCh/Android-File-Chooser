package org.scratch.filedialog;

import java.io.File;
import java.io.FileFilter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity for file selection
 * 
 * @author ScR4tCH
 * 
 */
public class FileChooserDialog extends ListActivity
{
	//drawables for elements in list
	protected Bitmap folderDrawable;
	protected Bitmap fileDrawable;
	protected Bitmap upDrawable;

	//selection mode (which fs elements should appear as selection ?)
	public static final int SELECT_FOLDER=2;
	public static final int SELECT_FILE=4;
			
	private int selectMode=SELECT_FOLDER|SELECT_FILE;
	private int viewMode=SELECT_FOLDER|SELECT_FILE;
	
	// mode of operation (CREATE OR JUST OPEN)
	public static final int MODE_CREATE = 0;
	public static final int MODE_OPEN = 1;
	
	private int operationMode=MODE_OPEN;
	
	
	//list adapter
	private FileChooserDialogAdapter fileAdapter;
	
	//start path
	private String startPath;

	public static final String START_PATH="START_PATH";

	public static final String FORMAT_FILTER="FORMAT_FILTER";

	public static final String RESULT_PATH="RESULT_PATH";

	public static final String OPERATION_MODE="OPERATION_MODE";

	public static final String SELECT_MODE="SELECT_MODE";
	
	public static final String VIEW_MODE="VIEW_MODE";

	public static final String FILE_DRAWABLE="FILE_DRAWABLE";

	public static final String FOLDER_DRAWABLE="FOLDER_DRAWABLE";

	public static final String UP_DRAWABLE="UP_DRAWABLE";

	//view
	private TextView myPath;
	private EditText mFileName;

	private Button selectButton;
	private LinearLayout layoutSelect;
	private LinearLayout layoutCreate;
	
	private InputMethodManager inputManager;
	
	private FileFilter ff;

	private File selectedFile;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//set default drawables
		folderDrawable=BitmapFactory.decodeResource(getResources(), R.drawable.folder);
		fileDrawable=BitmapFactory.decodeResource(getResources(),   R.drawable.file);
		upDrawable=BitmapFactory.decodeResource(getResources(),     R.drawable.folder);

		
		setResult(RESULT_CANCELED,getIntent());

		setContentView(R.layout.file_dialog_main);
		myPath=(TextView)findViewById(R.id.path);
		mFileName=(EditText)findViewById(R.id.fdEditTextFile);

		inputManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

		selectButton=(Button)findViewById(R.id.fdButtonSelect);
		selectButton.setEnabled(false);
		selectButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if(selectedFile!=null)
				{
					getIntent().putExtra(RESULT_PATH,selectedFile.getPath());
					setResult(RESULT_OK,getIntent());
					finish();
				}
			}
		});

		final Button newButton=(Button)findViewById(R.id.fdButtonNew);
		newButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				setCreateVisible(v);

				mFileName.setText("");
				mFileName.requestFocus();
			}
		});

		//get "settings" from Intent
		Intent i = getIntent();
		
		operationMode=i.getIntExtra(OPERATION_MODE,MODE_OPEN);
		viewMode=i.getIntExtra(VIEW_MODE,viewMode);
		selectMode=i.getIntExtra(SELECT_MODE,selectMode);
		
		if(i.getParcelableExtra(FORMAT_FILTER) instanceof FileFilter)
			ff=(FileFilter)i.getParcelableExtra(FORMAT_FILTER);
		
		
		selectMode=i.getIntExtra(SELECT_MODE,SELECT_FOLDER|SELECT_FILE);
		viewMode=i.getIntExtra(VIEW_MODE,SELECT_FOLDER|SELECT_FILE);

		if(i.getParcelableExtra(FOLDER_DRAWABLE) instanceof Bitmap)
			folderDrawable=(Bitmap)(i.getParcelableExtra(FOLDER_DRAWABLE));
		
		if(i.getParcelableExtra(FILE_DRAWABLE) instanceof Bitmap)
			fileDrawable=(Bitmap)(i.getParcelableExtra(FILE_DRAWABLE));

		if(i.getParcelableExtra(UP_DRAWABLE) instanceof Bitmap)
			upDrawable=(Bitmap)(i.getParcelableExtra(UP_DRAWABLE));
		
		//TODO: add other view specific settings as fontcolor
		//		"style" for not readable
		//		and so on ...
		
		//hide new button in "open" mode
		if(operationMode==MODE_OPEN)
		{
			newButton.setVisibility(View.GONE);
		}

		layoutSelect=(LinearLayout)findViewById(R.id.fdLinearLayoutSelect);
		layoutCreate=(LinearLayout)findViewById(R.id.fdLinearLayoutCreate);
		layoutCreate.setVisibility(View.GONE);

		final Button cancelButton=(Button)findViewById(R.id.fdButtonCancel);
		cancelButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				setSelectVisible(v);
			}

		});
		final Button createButton=(Button)findViewById(R.id.fdButtonCreate);
		createButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if(mFileName.getText().length()>0)
				{
					getIntent().putExtra(RESULT_PATH,fileAdapter.getCurrentPath()+File.separator+mFileName.getText());
					setResult(RESULT_OK,getIntent());
					finish();
				}
			}
		});

		startPath=getIntent().getStringExtra(START_PATH);
		startPath=startPath!=null ? startPath : FileChooserDialogAdapter.ROOT;
		
		File file=new File(startPath);
		selectedFile=file;
		
		if(selectedFile.isDirectory() && (selectMode&SELECT_FOLDER)==SELECT_FOLDER)
			selectButton.setEnabled(true);

		fileAdapter = new FileChooserDialogAdapter(this,startPath,viewMode,ff);
		getListView().setAdapter(fileAdapter);
		
	}
	
	@Override
	protected void onListItemClick(ListView l,View v,int position,long id)
	{
		File file=(File)fileAdapter.getItem(position);

		if(file==null)
			return;
		
		setSelectVisible(v);
		
		if(file.isDirectory())
		{
			selectButton.setEnabled(false);
			if(file.canRead())
			{
				fileAdapter.putLastPositions(fileAdapter.getCurrentPath(),position);
				fileAdapter.update(file.getAbsolutePath());
				
				myPath.post(new Runnable()
							{
								public void run()
								{
									myPath.setText(getText(R.string.location)+": "+fileAdapter.getCurrentPath());
								}
							}
						   );
				
				if((selectMode&SELECT_FOLDER)==SELECT_FOLDER)
				{
					selectedFile=file;
					v.setSelected(true);
					selectButton.setEnabled(true);
				}
			}
			else
			{
				new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(
								"["+file.getName()+"] "
										+getText(R.string.cant_read_folder))
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog,int which)
									{

									}
								}).show();
			}
		}
		else
		{
			selectedFile=file;
			v.setSelected(true);
			selectButton.setEnabled(true);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		
		if((keyCode==KeyEvent.KEYCODE_BACK))
		{
			selectButton.setEnabled(false);

			if(layoutCreate.getVisibility()==View.VISIBLE)
			{
				layoutCreate.setVisibility(View.GONE);
				layoutSelect.setVisibility(View.VISIBLE);
			}
			else
			{
				if(!fileAdapter.getCurrentPath().equals(startPath))
				{
					fileAdapter.updateToParentPath();
				}
				else
				{
					return super.onKeyDown(keyCode,event);
				}
			}

			return true;
		}
		else
		{
			return super.onKeyDown(keyCode,event);
		}
	}

	/**
	 * Define se o botao de CREATE e visivel.
	 * 
	 * @param v
	 */
	private void setCreateVisible(View v)
	{
		layoutCreate.setVisibility(View.VISIBLE);
		layoutSelect.setVisibility(View.GONE);

		inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);
		selectButton.setEnabled(false);
	}

	
	private void setSelectVisible(View v)
	{
		layoutCreate.setVisibility(View.GONE);
		layoutSelect.setVisibility(View.VISIBLE);

		inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);
		selectButton.setEnabled(false);
	}
}
