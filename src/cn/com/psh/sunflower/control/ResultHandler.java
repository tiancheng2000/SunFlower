/*
 * All Rights Reserved. Copyright (c) PFU Limited -- prj://countfd
 * Filename: ResultHandler.java
 * Created on {2006/04/09}, by {Timothy}.
 */
package cn.com.psh.sunflower.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author Timothy
 * @version $Revision: 1.0 $ $Date: 2006/04/09 19:04:26 $
 * Description:
 * Relatives:
 */
//----------------------------------------------------- Instance Variables
//----------------------------------------------------------- Constructors
//------------------------------------------------------------- Properties
//--------------------------------------------------------- Public Methods
public class ResultHandler {

	private static boolean _bInitialized = false;	
	private static Table _tableResult;	
	private static Image _imageItem;
	private static Text _textFiles, _textSize;
	private static Text _textTotalLines, _textCodeLines, _textCommentLines, _textBlankLines;

	public void setControls(Table tableResult, Image imageItem, Text textFiles, Text textSize,
	        Text textTotalLines, Text textCodeLines, Text textCommentLines, Text textBlankLines)
	{
	    assert (tableResult!=null && imageItem!=null && textFiles!=null && textSize!=null && 
		        textTotalLines!=null && textCodeLines!=null && textCommentLines!=null && textBlankLines!=null);

	    _tableResult = tableResult;
	    _imageItem = imageItem;
	    _textFiles = textFiles;
	    _textSize = textSize;
	    _textTotalLines = textTotalLines;
	    _textCodeLines = textCodeLines;
	    _textCommentLines = textCommentLines;
	    _textBlankLines = textBlankLines;
	    
	    _bInitialized = true;
	}

	public void updateFileCountResult(String strFileName, String strFileFolder, 
	        int cntTotalLine, int cntSourceLine, int cntCommentLine, int cntBlankLine, 
	        String strLangName){
	    if(!_bInitialized)
	        return;
	    
	    TableItem item = new TableItem(_tableResult, SWT.NULL);
	    String[] strTextArray = new String[]{strFileName, strFileFolder,
	            String.valueOf(cntTotalLine), String.valueOf(cntSourceLine), 
	            String.valueOf(cntCommentLine), String.valueOf(cntBlankLine),
	            strLangName};
		item.setText(strTextArray);
		item.setImage(_imageItem); 
	}

	public void updateFinalCountResult(long lCntFileNum, long lCntFileSize, 
	        long lCntTotalLine, long lCntSourceLine, long lCntCommentLine, long lCntBlankLine){
	    if(!_bInitialized)
	        return;

	    _textFiles.setText(String.valueOf(lCntFileNum));
	    String strFileSize;
		if (lCntFileSize >= 1024) {
			strFileSize = String.valueOf(lCntFileSize / 1024) + "."
					+ String.valueOf(lCntFileSize % 1024).substring(0,1) + " KB";
		}
		else
			strFileSize = String.valueOf(lCntFileSize) + " byte";
	    _textSize.setText(strFileSize);
	    _textTotalLines.setText(String.valueOf(lCntTotalLine));
	    _textCodeLines.setText(String.valueOf(lCntSourceLine));
	    _textCommentLines.setText(String.valueOf(lCntCommentLine));
	    _textBlankLines.setText(String.valueOf(lCntBlankLine));
	    
	}
}
