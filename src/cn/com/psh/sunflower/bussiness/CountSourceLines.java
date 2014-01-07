package cn.com.psh.sunflower.bussiness;

import java.io.File;
import java.util.StringTokenizer;

import cn.com.psh.sunflower.common.ConfigurationManager;
import cn.com.psh.sunflower.common.Utility;
import cn.com.psh.sunflower.control.ResultHandler;

public class CountSourceLines {
    //result output handler (e.g. GUI display)
	private ResultHandler _resultHandler;

	//member variables - need to be initialized
	private ConfigurationManager _cm;

	//constants
	private static final String SEPARATOR_FILEEXTENSION = ";";
	
	public CountSourceLines(){
        try {
            initialize();
        } catch (Exception e) {
            return;
        }
	}

	private void initialize() throws Exception{
	    _cm = ConfigurationManager.getInstance();
        if (_cm!=null){
            //...
	    }
	}

	private void countFileProcess(String strCountFilePath, String strEncoding) throws Exception {

		Utility.trace("-----------------------------");
		Utility.trace("strCountFilePath = " + strCountFilePath);
		Utility.trace("is not counted as a source file.");
		File countFile = new File(strCountFilePath);
		if(_resultHandler != null)
		    _resultHandler.updateFileCountResult(countFile.getName(), countFile.getParent(),
		            0, 0, 0, 0, "-- NOT specified --");
		return;
	}

	public void analyzeParameter(String strSelectedLang, 
	        String strFilePath, String strFileExtensions, boolean bCheckSubDir, 
	        String strEncoding)
			throws Exception {
		try 
		{
		    if(_cm==null)	//not successfully initialized
		        initialize();
		    
		    //count according to the path & list of extensions
			StringTokenizer st = new StringTokenizer(strFileExtensions, SEPARATOR_FILEEXTENSION);
		    String[] strFileExtensionList = null;
		    if(st.countTokens()==0)
		        strFileExtensionList = new String[]{""};
		    else{
			    strFileExtensionList = new String[st.countTokens()];
			    int i=0;
				while(st.hasMoreTokens()){
				    strFileExtensionList[i] = st.nextToken().trim().toLowerCase();
				    if(strFileExtensionList[i].equals("*.*") || strFileExtensionList[i].equals("*"))
				        strFileExtensionList[i] = "";
				    if(strFileExtensionList[i].startsWith("*."))
				        strFileExtensionList[i]=strFileExtensionList[i].substring(1); //trim to be ".xxx"
				    //IMPROVE: to deal with file extension "."(file with no extension)
				    i++;
				}
		    }
			getcountFileDir(strFilePath, strFileExtensionList, bCheckSubDir, strEncoding);

			//deliver the lCounter to Controller, and to Viewer then
			if(_resultHandler != null)
			    _resultHandler.updateFinalCountResult(0, 0, 0, 0, 0, 0);
			
		} catch (Exception e) {
			throw e;
		}
	}

	private void getcountFileDir(String strFileDirPath, String[] strFileExtensionList, boolean bCheckSubDir, String strEncoding) throws Exception {

		//IMPROVE: even if the extension not matched, count the manually [selected files]
	    
	    strFileDirPath = strFileDirPath.trim().toLowerCase();
	    
	    File fileTemp = null;
		String strCountFilePath = "";
		try {
			fileTemp = new File(strFileDirPath);
			if (fileTemp.isDirectory()) {
				File[] f = fileTemp.listFiles();
				
				for (int j = 0; j < f.length; j++) {
				    String strFileInDir = f[j].getPath();	//file/dir in this dir
					if (f[j].isDirectory()){	//dir 
					    if(bCheckSubDir)	//check subdir
					        getcountFileDir(strFileInDir, strFileExtensionList, bCheckSubDir, strEncoding);
					}
					else{	//file
						for (int k = 0; k < strFileExtensionList.length; k++) {
							if (strFileInDir.endsWith(strFileExtensionList[k])) {
								strCountFilePath = strFileInDir;
								countFileProcess(strFileInDir, strEncoding);//
								break;
							}
		                }
					}
				}
			} else {
				for (int k = 0; k < strFileExtensionList.length; k++) {
					if (strFileDirPath.endsWith(strFileExtensionList[k])) {
						strCountFilePath = strFileDirPath;
						countFileProcess(strCountFilePath, strEncoding);//
						break;
					}
                }
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
    public void set_resultHandler(ResultHandler handler) {
        _resultHandler = handler;
    }

}