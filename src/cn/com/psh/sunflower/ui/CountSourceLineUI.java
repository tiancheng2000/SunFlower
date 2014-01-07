package cn.com.psh.sunflower.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cn.com.psh.sunflower.bussiness.CountSourceLines;
import cn.com.psh.sunflower.common.ConfigurationManager;
import cn.com.psh.sunflower.common.Utility;
import cn.com.psh.sunflower.control.ResultHandler;

public class CountSourceLineUI {

    private static ConfigurationManager _cm;			//common Layer
    private static CountSourceLines _countCodeLines;	//business Layer
    private static ResultHandler _resultHandler;		//control Layer
    
    private static final String _strProductName = "SunFlower";	//alternative: LinePoint(vs FunctionPoint), Phoenix
    private static final String _strVersion = "0.70-alpha";
    private static final String _strAboutMessage = _strProductName +  
    "- A Source Line-based Counting Tool\n" +
    "/*----------------------------------------          \n" +
    " Design:\n" +
	"   Xiao Chen, Tiancheng Hu\n" +
	" Develop/Test:\n" +
	"   Xiao Chen-> LiangHua Cao-> Tiancheng Hu\n" +
    "----------------------------------------*/           \n" +
	"CopyRight(C) 2005-2006 PFU Shanghai Co., Ltd.\n" +
	"Have fun! Count the result of your endeavour.\n" +
	"\n" +
	"Version: " + _strVersion + "\n" +
    "//ToDoList:-----------------------------//          \n" +
    "  -[medium]  windows launcher flash problem (execvp)\n" +
    "  -[minor]  automatic encoding-identify(file io->slower?)\n" +
    "  -[minor]  latest path list upgrade (user-specified)\n" +
    "  -[medium] sortable result list\n" +
    "  -[minor]  system.log output\n" +
    "  -[medium] a graphical About Dialog~\n" +
    "//--------------------------------------//           \n";
    
    private static final String NEWLINE_OUTPUT = System.getProperty("line.separator");
    private static final String _strImageFileLogo = "logo.jpg";
    private static final String _strImageFileItem = "item.gif";
    private static final String _strImageFileIconLogo = "iconLogo.png";

    //resources
    private static Image imageLogo;
    private static Image imageItem;
    private static Image imageIconLogo;
    
    //tab control
    private TabFolder tabFolder;
    
    //input controls
	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"
    private Combo comboLanguage;
    private Combo comboExtension;
    private Combo comboEncoding;
    private Combo comboPath;
    private Button buttonCheckSubDir;
    private Shell _dlgSetting = null;	//IMPROVE: subclass Shell to really contain setting data
    //output controls
    private Table tableResult;
    private Label labelStatusMessage;
    private ProgressBar pBar_status; 
    private int pBar_selection; 
    //other output controls declared directly in shell initialization block
    
    private CountSourceLineUI(){
    }
    
    //XDOC: --- Main() Entrance ---
    public static void main(String[] args) {

        //A windows launcher -- done. the VC source can be reused
        
        //initialize Model
        String strInitErrMsg = "";
        try {
    	    //initialize XML config manager and Language List. 
            _cm = ConfigurationManager.getInstance();
        } catch (Exception e) {
            strInitErrMsg = "Init Failure: " + e.getMessage();
        }
        _countCodeLines = new CountSourceLines();
        _resultHandler = new ResultHandler();
        _countCodeLines.set_resultHandler(_resultHandler);
        
        //initialize View
        Display display = Display.getDefault();
        imageLogo = new Image(display, _strImageFileLogo);
        //imageLogo = new Image(display, CountSourceLineUI.class.getResourceAsStream(_strFileNameLogo));
        imageItem = new Image(display, _strImageFileItem);
        imageIconLogo = new Image(display, _strImageFileIconLogo);
		CountSourceLineUI thisClass = new CountSourceLineUI();
		thisClass.createSShell(display);	//main GUI initialize procedure
		if(!strInitErrMsg.equals(""))
		    Utility.errorUser(strInitErrMsg);
                
		//thisClass.sShell.pack();
		thisClass.sShell.open();
		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
    }

    /**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {

        //GUI components
        final int WIDTH_SHELL = 600; 
	    final int HEIGHT_SHELL = 600; 
	    
	    //sShell = new Shell(display, SWT.SHELL_TRIM);
	    //sShell = new Shell(display, SWT.DIALOG_TRIM);
	    sShell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN );
        sShell.setSize(WIDTH_SHELL, HEIGHT_SHELL);
        sShell.setText(_strProductName);
        sShell.setImage(imageIconLogo);
        
        //tab control
        tabFolder = new TabFolder(sShell, SWT.BORDER);
		TabItem tidetail = new TabItem(tabFolder, SWT.BORDER);
		tidetail.setText("  Detail  ");
		TabItem tiabout = new TabItem(tabFolder, SWT.BORDER);
		tiabout.setText("  Tab About  ");
		tabFolder.setBounds(0, 0, WIDTH_SHELL, WIDTH_SHELL);
		
		//group control
		Group gdetail = new Group(tabFolder, SWT.NONE);
		Group gabout = new Group(tabFolder, SWT.NULL);
		
		
		//about controls
		Label labelAbout;
        //input controls
        Button buttonCount, buttonExport, buttonSetting, buttonAbout;
        Label labelImage;
        Button buttonSelectDir, buttonSelectFile;
        Label labelLanguage, labelExtension, labelEncoding;
        Label labelPath, labelSubFolder;
        //output controls
        Label labelResult, labelFiles, labelSize;
        Label labelTotalLines, labelSourceLines, labelCommentLines, labelBankLines;
        Text textFiles, textSize;
        Text textTotalLines, textSourceLines, textCommentLines, textBlankLines;
        
        //Position Setter
        final int LEFT_MARGIN = 25, TOP_MARGIN = 30;
        final int VSPACING = 50;
        int LEFT = LEFT_MARGIN, TOP = TOP_MARGIN;
        int WIDTH_BTN_SYSTEM = 90, HEIGHT_BTN_SYSTEM = 35;
        int WIDTH_IMAGE = WIDTH_BTN_SYSTEM, HEIGHT_IMAGE = 110;
        int WIDTH_COMBO = 440, HEIGHT_COMBO = 28;
        int WIDTH_COMBO_EXTENSION = 330, WIDTH_COMBO_ENCODING = 109;
        int WIDTH_COMBO_PATH = 440, HEIGHT_COMBO_PATH = 28;
        int WIDTH_BTN_SMALL = 30, HEIGHT_BTN_SMALL = 22;
        int WIDTH_TABLE = 542, HEIGHT_TABLE = 200;	//width should be divided by 6
        int WIDTH_GROUPRESULT = WIDTH_TABLE, HEIGHT_GROUPRESULT = 65;
        int WIDTH_LABEL = 60, HEIGHT_LABEL = 18;
        int WIDTH_TEXT = 90, HEIGHT_TEXT = 18;
        int WIDTH_STATUSMSG = (WIDTH_SHELL-2*LEFT_MARGIN)/2, HEIGHT_STATUSMSG = 15;
        int WIDTH_PROGRESSBAR = (WIDTH_SHELL-2*LEFT_MARGIN)/2, HEIGHT_PROGRESSBAR = 15;
        int HSPACING_1 = 115, HSPACING_2 = 2, HSPACING_3 = 5;
        int VSPACING_1 = 20, VSPACING_2 = 2, VSPACING_3 = 5;
        int pos_X, pos_Y;
        
        
        //--Label labelAbout----------------------------------------------
        labelAbout=new Label(gabout,SWT.NONE|SWT.CENTER);
        int TMPWIDTH=4*WIDTH_LABEL;
        int TMPHEIGHT=HEIGHT_LABEL;
        labelAbout.setBounds((WIDTH_SHELL-TMPWIDTH)/2,(HEIGHT_SHELL-TMPHEIGHT)/4,TMPWIDTH,TMPHEIGHT);
        labelAbout.setText("Tab Control Added By Endless");
        
        
        
        //-- Button Count ----------------------------------------------
        pos_X = LEFT; pos_Y = TOP;
        buttonCount = new Button(gdetail, SWT.BORDER | SWT.PUSH);
        buttonCount.setBounds(pos_X, pos_Y, WIDTH_BTN_SYSTEM, HEIGHT_BTN_SYSTEM);
        buttonCount.setText("Count"); //IMPROVE: image button
        buttonCount.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			    try {
                    //NOTE: activate count
			        if(tableResult != null)
			            tableResult.removeAll();
			        labelStatusMessage.setText("");

                    _countCodeLines.analyzeParameter(comboLanguage.getText(), comboPath.getText(), 
                            comboExtension.getText(), buttonCheckSubDir.getSelection(), comboEncoding.getText());
                    if(labelStatusMessage.getText().equals(""))
                        labelStatusMessage.setText("Done!");

                    //add the MRU path list
                    String strPath = comboPath.getText();
                    int idxCombo;
                    for (idxCombo = 0; idxCombo < comboPath.getItemCount(); idxCombo++) {
                        if(comboPath.getItem(idxCombo).equals(strPath))
                            break;
                    }
                    if(idxCombo == 0 && comboPath.getItemCount()>0) 
                        return; //already exists and is the top
                    if(idxCombo < comboPath.getItemCount())
                        comboPath.remove(idxCombo); //remove from the old position
                    comboPath.add(strPath, 0); comboPath.select(0);	//add it to the top
                } catch (Exception e1) {
                    Utility.errorUser(e1.getMessage());
                }
			}
		});
        
        
        //-- Button Export ----------------------------------------------
        pos_X = LEFT+1*HSPACING_1; pos_Y = TOP;
        buttonExport = new Button(gdetail, SWT.BORDER | SWT.PUSH);
        buttonExport.setBounds(pos_X, pos_Y, WIDTH_BTN_SYSTEM, HEIGHT_BTN_SYSTEM);
        buttonExport.setText("Export");
	        buttonExport.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			        //export counting result to CSV file
				    exportCSVFile();
				}
			});              

        //-- Button Setting ----------------------------------------------
        pos_X = LEFT+2*HSPACING_1; pos_Y = TOP;
        buttonSetting = new Button(gdetail, SWT.BORDER | SWT.PUSH);
        buttonSetting.setBounds(pos_X, pos_Y, WIDTH_BTN_SYSTEM, HEIGHT_BTN_SYSTEM);
        buttonSetting.setText("Setting");
        buttonSetting.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			    try {
			        createSettingDialog(sShell);
                } catch (Exception e1) {
                    Utility.errorUser(e1.getMessage());
                }
			}
		});              

        //-- Button About ----------------------------------------------
        pos_X = LEFT+3*HSPACING_1; pos_Y = TOP;
        buttonAbout = new Button(gdetail, SWT.BORDER | SWT.PUSH);
        buttonAbout.setBounds(pos_X, pos_Y, WIDTH_BTN_SYSTEM, HEIGHT_BTN_SYSTEM);
        buttonAbout.setText("About");
        buttonAbout.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			    try {
			        //TEST: width/height test 
			        //Utility.trace("width=" + sShell.getSize().x + ", height=" + sShell.getSize().y);
			        //IMPROVE: About MessageBox
		            MessageBox dialog = new MessageBox(sShell, SWT.ICON_INFORMATION | SWT.OK);
		            dialog.setText("About");
		            dialog.setMessage(_strAboutMessage);
		            if (dialog.open() == SWT.YES) {
		                
		            }
                } catch (Exception e1) {
                    Utility.errorUser(e1.getMessage());
                }
			}
		});              

        //-- Lable Image ----------------------------------------------
        pos_X = LEFT+4*HSPACING_1; pos_Y = TOP;
        labelImage = new Label(gdetail, SWT.CENTER);
        labelImage.setBounds(pos_X, pos_Y, WIDTH_IMAGE, HEIGHT_IMAGE);
        labelImage.setImage(imageLogo);
        labelImage.addMouseListener(new MouseListener() {
            public void mouseDoubleClick(MouseEvent arg0) {
            }

            public void mouseDown(MouseEvent arg0) {
            }

            public void mouseUp(MouseEvent arg0) {
			    try {
			        createTestGridLayout(sShell);
                } catch (Exception e1) {
                    Utility.errorUser(e1.getMessage());
                }
            }
		});              
        
        LEFT = LEFT_MARGIN; TOP = TOP_MARGIN + HEIGHT_BTN_SYSTEM + VSPACING_3;
        //-- Language ----------------------------------------------
        pos_X = LEFT; pos_Y = TOP;
        labelLanguage = new Label(gdetail, SWT.NONE);
        labelLanguage.setBounds(pos_X, pos_Y, 0, 0);
        labelLanguage.setText("Language:"); labelLanguage.pack(); 
        comboLanguage = new Combo(gdetail, SWT.NONE);
        comboLanguage.setBounds(pos_X, pos_Y+HEIGHT_LABEL, WIDTH_COMBO, HEIGHT_COMBO);
        //use ModifyListener instead of SelectionListener, 'cause 1.initialization 2. manual input
        //comboLanguage.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
		//	public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
        comboLanguage.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                try {
                    //Utility.trace("Language=" + comboLanguage.getText());
                    //Utility.trace("FileExt No=" + ((ReadXmlParameterBean)(_cm.getLangToBeanMap().get(comboLanguage.getText())))
                    //        .getStrExtensionNameList().length);
	                if(comboExtension!=null){
	                    comboExtension.removeAll();
	                    //HashMap langToBeanMap = _cm.getLangToBeanMap();
	                    //ReadXmlParameterBean bean = (ReadXmlParameterBean)(langToBeanMap.get(comboLanguage.getText().toLowerCase()));
	                    //if(bean == null)
	                    //    return;
	                    //String[] strExtension = bean.getStrExtensionNameList();
	                    //for (int i = 0; i < strExtension.length; i++) {
	                    //    comboExtension.add(strExtension[i]);
	                    //}
	                    //comboExtension.select(0);
		            }

                } catch (Exception ex) {
                    Utility.errorUser(ex.getMessage());
                }
			}
		});

        
        LEFT = LEFT_MARGIN; TOP += HEIGHT_LABEL + HEIGHT_COMBO + VSPACING_2;
        //-- File Extension ----------------------------------------------
        pos_X = LEFT; pos_Y = TOP;
        labelExtension = new Label(gdetail, SWT.NONE);
        labelExtension.setBounds(pos_X, pos_Y, 0, 0);
        labelExtension.setText("File Extension(separate by ;):"); labelExtension.pack(); 
        comboExtension = new Combo(gdetail, SWT.NONE);
        comboExtension.setBounds(pos_X, pos_Y+HEIGHT_LABEL, WIDTH_COMBO_EXTENSION, HEIGHT_COMBO);

        //-- File Encoding ----------------------------------------------
        pos_X = LEFT+comboExtension.getBounds().width+HSPACING_2; pos_Y = TOP;
        labelEncoding = new Label(gdetail, SWT.NONE);
        labelEncoding.setBounds(pos_X, pos_Y, 0, 0);
        labelEncoding.setText("File Encoding:"); labelEncoding.pack(); 
        comboEncoding = new Combo(gdetail, SWT.NONE);
        comboEncoding.setBounds(pos_X, pos_Y+HEIGHT_LABEL, WIDTH_COMBO_ENCODING, HEIGHT_COMBO);

        LEFT = LEFT_MARGIN; TOP += HEIGHT_LABEL + HEIGHT_COMBO + VSPACING_2;
        //-- File/Directory Path ----------------------------------------------
        pos_X = LEFT; pos_Y = TOP;
        labelPath = new Label(gdetail, SWT.NONE);
        labelPath.setBounds(pos_X, pos_Y, 0, 0);
        labelPath.setText("Look in:"); labelPath.pack(); 
        comboPath = new Combo(gdetail, SWT.NONE);
        comboPath.setBounds(pos_X, pos_Y+HEIGHT_LABEL, WIDTH_COMBO_PATH, HEIGHT_COMBO_PATH);
        //select dir
        buttonSelectDir = new Button(gdetail, SWT.PUSH);
        buttonSelectDir.setText("dir..");
        buttonSelectDir.setBounds(pos_X+WIDTH_COMBO_PATH+HSPACING_2, pos_Y+HEIGHT_LABEL, WIDTH_BTN_SMALL, HEIGHT_BTN_SMALL);
        buttonSelectDir.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                DirectoryDialog directoryDialog = new DirectoryDialog(sShell);
                directoryDialog
                        .setMessage("Please select a directory and click OK");
                directoryDialog.setFilterPath(comboPath.getText()); //useless? no, can be properly used
                String dir = directoryDialog.open();
                if (dir != null) {
                    comboPath.setText(dir);
                }
            }
        });
        //select file
        buttonSelectFile = new Button(gdetail, SWT.PUSH);
        buttonSelectFile.setText("file.."); //IMPROVE: upgrade to multiple file
        buttonSelectFile.setBounds(pos_X+WIDTH_COMBO_PATH+HSPACING_2+WIDTH_BTN_SMALL+HSPACING_2, pos_Y+HEIGHT_LABEL, 
                					WIDTH_BTN_SMALL, HEIGHT_BTN_SMALL);
        buttonSelectFile.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                FileDialog fileDialog = new FileDialog(sShell, SWT.SINGLE);
                fileDialog.setFilterPath(comboPath.getText());
                fileDialog.setFilterExtensions(new String[]{"*.*"});
                fileDialog.setFilterNames(new String[] {"All Files(*.*)"});
                String firstFile = fileDialog.open();
                if (firstFile != null) {
                    String[] selectedFiles = fileDialog.getFileNames();
                    StringBuffer sb = new StringBuffer(fileDialog
                            .getFilterPath()
                            + "\\" + selectedFiles[0]);
                    String strPath = sb.toString();
                    comboPath.setText(strPath);
                    //String strLang = _cm.determineLangSetByExt(strPath, comboLanguage.getText());
                    //comboLanguage.setText(strLang);
            	    //String strExt = strPath.substring(strPath.lastIndexOf("."), strPath.length());
            	    //if(comboExtension.getText().indexOf(strExt)==-1)
            	    //    comboExtension.setText("");
                }
            }
        });
        //check subdir or not
        buttonCheckSubDir = new Button(gdetail, SWT.CHECK);
        buttonCheckSubDir.setBounds(pos_X, pos_Y+HEIGHT_LABEL+HEIGHT_COMBO_PATH, 0, 0);
        buttonCheckSubDir.setText("include subdir"); buttonCheckSubDir.pack();

        LEFT = LEFT_MARGIN; TOP += HEIGHT_LABEL + HEIGHT_COMBO_PATH + HEIGHT_LABEL + VSPACING_2;
        //-- Counting Result List ----------------------------------------------
        tableResult = new Table(gdetail, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tableResult.setHeaderVisible(true);
        tableResult.setLinesVisible(true);
        tableResult.setBounds(LEFT, TOP, WIDTH_TABLE, HEIGHT_TABLE);
        String[] titles = { "FileName", "Path", "Total Lines", "Source/", "Comment/", "Blank/", "Language"};
        int[] widths = {WIDTH_TABLE*2/6-tableResult.getVerticalBar().getSize().x-2, 
                		WIDTH_TABLE*1/6,
                		WIDTH_TABLE*1/8+2, WIDTH_TABLE*1/8,
                		WIDTH_TABLE*1/8, WIDTH_TABLE*1/8,
                		WIDTH_TABLE*1/6};
        for (int idxColumn = 0; idxColumn < titles.length; idxColumn++) {
            TableColumn column = new TableColumn(tableResult, SWT.NULL);
            column.setText(titles[idxColumn]);
            column.setWidth(widths[idxColumn]);
            if(idxColumn==2 || idxColumn==3 || idxColumn==4 || idxColumn==5 )
                column.setAlignment(SWT.RIGHT);
            //column.pack();
        }

        //-- Counting Total Result Area ----------------------------------------------
        LEFT = LEFT_MARGIN; TOP += HEIGHT_TABLE + VSPACING_2;
        HSPACING_1 = 185;
        VSPACING_1 = 20;
        //Result
        pos_X = LEFT; pos_Y = TOP;
        Group groupResult = new Group(gdetail, SWT.NONE);
        groupResult.setText("Final Result");
        //groupResult.setLayout(new GridLayout());
        groupResult.setBounds(pos_X, pos_Y, WIDTH_GROUPRESULT, HEIGHT_GROUPRESULT);
//      labelResult = new Label(groupResult, SWT.NONE);
//      labelResult.setBounds(pos_X, pos_Y, WIDTH_LABEL, HEIGHT_LABEL);
//      labelResult.setText("Final Result:"); labelResult.pack(); 

        int LEFT_LOCAL = 10, TOP_LOCAL = 0;
        //TotalLine
        pos_X = LEFT_LOCAL; pos_Y = TOP_LOCAL+1*VSPACING_1;
        labelTotalLines = new Label(groupResult, SWT.NONE);
        labelTotalLines.setBounds(pos_X, pos_Y, WIDTH_LABEL, HEIGHT_LABEL);
        labelTotalLines.setText("Total:");
        textTotalLines = new Text(groupResult, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
        textTotalLines.setBounds(pos_X+WIDTH_LABEL+HSPACING_2, pos_Y, WIDTH_TEXT, HEIGHT_TEXT);
        //FileNum
        pos_X = LEFT_LOCAL+1*HSPACING_1; pos_Y = TOP_LOCAL+1*VSPACING_1;
        labelFiles = new Label(groupResult, SWT.NONE);
        labelFiles.setBounds(pos_X, pos_Y, WIDTH_LABEL, HEIGHT_LABEL);
        labelFiles.setText("Files:");
        textFiles = new Text(groupResult, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
        textFiles.setBounds(pos_X+WIDTH_LABEL+HSPACING_2, pos_Y, WIDTH_TEXT, HEIGHT_TEXT);
        //FileSize
        pos_X = LEFT_LOCAL+2*HSPACING_1; pos_Y = TOP_LOCAL+1*VSPACING_1;
        labelSize = new Label(groupResult, SWT.NONE);
        labelSize.setBounds(pos_X, pos_Y, WIDTH_LABEL, HEIGHT_LABEL);
        labelSize.setText("Size:");
        textSize = new Text(groupResult, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
        textSize.setBounds(pos_X+WIDTH_LABEL+HSPACING_2, pos_Y, WIDTH_TEXT, HEIGHT_TEXT);
        //SourceLine
        pos_X = LEFT_LOCAL; pos_Y = TOP_LOCAL+2*VSPACING_1;
        labelSourceLines = new Label(groupResult, SWT.NONE);
        labelSourceLines.setBounds(pos_X, pos_Y, WIDTH_LABEL, HEIGHT_LABEL);
        labelSourceLines.setText("Source:");
        textSourceLines = new Text(groupResult, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
        textSourceLines.setBounds(pos_X+WIDTH_LABEL+HSPACING_2, pos_Y, WIDTH_TEXT, HEIGHT_TEXT);
        //CommentLine
        pos_X = LEFT_LOCAL+1*HSPACING_1; pos_Y = TOP_LOCAL+2*VSPACING_1;
        labelCommentLines = new Label(groupResult, SWT.NONE);
        labelCommentLines.setBounds(pos_X, pos_Y, WIDTH_LABEL, HEIGHT_LABEL);
        labelCommentLines.setText("Comment:");
        textCommentLines = new Text(groupResult, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
        textCommentLines.setBounds(pos_X+WIDTH_LABEL+HSPACING_2, pos_Y, WIDTH_TEXT, HEIGHT_TEXT);
        //BankLine
        pos_X = LEFT_LOCAL+2*HSPACING_1; pos_Y = TOP_LOCAL+2*VSPACING_1;
        labelBankLines = new Label(groupResult, SWT.NONE);
        labelBankLines.setBounds(pos_X, pos_Y, WIDTH_LABEL, HEIGHT_LABEL);
        labelBankLines.setText("Blank:");
        textBlankLines = new Text(groupResult, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
        textBlankLines.setBounds(pos_X+WIDTH_LABEL+HSPACING_2, pos_Y, WIDTH_TEXT, HEIGHT_TEXT);
        //groupResult.pack();

        //StatusMessage
        pos_X = LEFT; pos_Y = TOP+3*VSPACING_1+2*VSPACING_3;
        labelStatusMessage = new Label(gdetail, SWT.NONE);
        labelStatusMessage.setBounds(pos_X, pos_Y, WIDTH_STATUSMSG, HEIGHT_STATUSMSG);
        
        //Progress bar 
        pos_X = LEFT + WIDTH_STATUSMSG; pos_Y = TOP+3*VSPACING_1+2*VSPACING_3;
        pBar_status = new ProgressBar(gdetail, SWT.SMOOTH ); 
        pBar_status.setBounds(pos_X, pos_Y, WIDTH_PROGRESSBAR, HEIGHT_PROGRESSBAR); 
        pBar_status.setMaximum(100); 
        pBar_status.setMinimum(0); 
        
        //-- Initialize Controls ----------------------------------------------
        //initialize comboLanguage & comboExtension
        //...
        
        //initialize check subdir checkbox
        buttonCheckSubDir.setSelection(true);
        //initialize Status Message
        if(labelStatusMessage.getText().equals(""))
            labelStatusMessage.setText("Welcome");
        sShell.setDefaultButton(buttonCount);
        
        //-- Initialize Controller Layer ----------------------------------------------
        _resultHandler.setControls(tableResult, imageItem, textFiles, textSize,
                		textTotalLines, textSourceLines, textCommentLines, textBlankLines);
        Utility.set_labelUserGUIOutput(labelStatusMessage);
        
        //add group to tab
        tidetail.setControl(gdetail);
        tiabout.setControl(gabout);
	}
	
	//Export Count Result as CSV file
	private void exportCSVFile(){
	    FileOutputStream fos = null;
	    OutputStreamWriter osw = null;
	    BufferedWriter bw = null;
	    try {
            FileDialog fileDialog = new FileDialog(sShell, SWT.SAVE);
            fileDialog.setFilterPath(comboPath.getText());
            fileDialog.setFilterExtensions(new String[]{"*.csv"});
            fileDialog.setFilterNames(new String[] {"CSV file(*.csv)"});
            String firstFile = fileDialog.open();
            if (firstFile != null) {
                String[] selectedFiles = fileDialog.getFileNames();
                String strFilePath = fileDialog.getFilterPath() 
                	+ "\\" + Utility.appendIfNotExists(selectedFiles[0], ".csv");
                Utility.trace("-- Export to file: " + strFilePath);
    			File countFile = new File(strFilePath);
    			//overwrite confirm
    			if(countFile.exists()){
		            int style = SWT.ICON_INFORMATION | SWT.YES | SWT.NO;
		            MessageBox dialog = new MessageBox(sShell, style);
		            dialog.setText("Export to file");
		            dialog.setMessage("Overwrite the existing file?\n");
		            if (dialog.open() == SWT.NO)
		                return;
    			}

                //TEMP: kw: SWT进度条+事务处理线程、以Display.asyncExec()方式实现线程进度更新
                new Thread(new GUIUpdater(this)).start(); //GUIUpdater 会回调 UpdateProgressBar()

                fos = new FileOutputStream(countFile);
    			osw = new OutputStreamWriter(fos, comboEncoding.getText());
    			bw = new BufferedWriter(osw, 5 * 1021 * 1024);
    			StringBuffer strBuffer = new StringBuffer();
    			for (int i = 0; i < tableResult.getItemCount(); i++) {
        			for (int j = 0; j < tableResult.getColumnCount(); j++) {
        			    strBuffer.append(tableResult.getItem(i).getText(j) + ",");
        			}
        			if(strBuffer.length()<=0)
        			    break;
        			//remove the last seperator, replace it with a newline
        			strBuffer.replace(strBuffer.length()-1, strBuffer.length(), NEWLINE_OUTPUT);
        			bw.write(strBuffer.toString());
        			strBuffer.setLength(0);
                }
            }
        } catch (Exception e1) {
            Utility.errorUser("Failed to export file: " + e1.getMessage());
		} finally {
            try {
				if (bw != null) bw.close();
				if (osw != null) osw.close();
				if (fos != null) fos.close();
            } catch (IOException e2) {
                Utility.errorUser("Failure: Failed to close export file.");
            }
		}
	}

	private void createSettingDialog(Shell parent) {

        final int WIDTH_DLGSETTING = 300; 
	    final int HEIGHT_DLGSETTING = 100; 
	    
	    _dlgSetting = new Shell(parent, SWT.CLOSE | SWT.RESIZE);
        _dlgSetting.setSize(WIDTH_DLGSETTING, HEIGHT_DLGSETTING);
        _dlgSetting.setText("Setting");

        //-- Comment-related Setting -----------------------------------------
        int LEFT_GROUPCOMMENT = 10, TOP_GROUPCOMMENT = 5;
        int WIDTH_GROUPCOMMENT = WIDTH_DLGSETTING-2*LEFT_GROUPCOMMENT-8;
        int HEIGHT_GROUPCOMMENT = HEIGHT_DLGSETTING-2*TOP_GROUPCOMMENT-30;
        Group groupComment = new Group(_dlgSetting, SWT.NONE);
        groupComment.setText("Comment");
        FillLayout layout = new FillLayout();
        layout.type = SWT.VERTICAL;
        layout.marginWidth = 5;
        layout.marginHeight = 5;
        layout.spacing = 2;
        groupComment.setLayout(layout);

        Label labelComment = new Label(groupComment, SWT.NONE);
        labelComment.setText("comment within a source line: ");
        Button checkComment = new Button(groupComment, SWT.CHECK);
        checkComment.setText("count as a comment line and a source line");
        
        //groupComment.pack();
        groupComment.setBounds(LEFT_GROUPCOMMENT, TOP_GROUPCOMMENT, WIDTH_GROUPCOMMENT, HEIGHT_GROUPCOMMENT);

        //-- Initialize Controls ----------------------------------------------
        checkComment.setSelection(true);
        checkComment.setEnabled(false);
        
        _dlgSetting.open();
	}
	
	private Shell _dlgGridLayout = null;
	private Text textName = null;
	private Text textPassword = null;
	private Table tableNameList = null;
	private void createTestGridLayout(Shell parent) {
		Label labelName = null;
		Label labelPassword = null;
		Button buttonLogin = null;
		Label labelGrid = null;

		_dlgGridLayout = new Shell(parent, SWT.CLOSE | SWT.RESIZE);
		org.eclipse.swt.layout.GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		gridLayout.makeColumnsEqualWidth = true;
		_dlgGridLayout.setLayout(gridLayout);
		_dlgGridLayout.setText("Login");
		_dlgGridLayout.setSize(new org.eclipse.swt.graphics.Point(565,360));

		labelGrid = new Label(_dlgGridLayout, SWT.NONE);	//white space
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);	//white space
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);	//white space
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);	//white space
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);	//white space
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);	//white space
		
		GridData gd;
		labelName = new Label(_dlgGridLayout, SWT.NONE);
		labelName.setBounds(new org.eclipse.swt.graphics.Rectangle(0,0,0,0));
		labelName.setText("Name:");		
		gd = new org.eclipse.swt.layout.GridData();
		gd.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gd.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gd.widthHint = -1;
		labelName.setLayoutData(gd);
		
		textName = new Text(_dlgGridLayout, SWT.BORDER);
		textName.setBounds(new org.eclipse.swt.graphics.Rectangle(0,0,0,0));
		gd = new org.eclipse.swt.layout.GridData();
		gd.widthHint = 80;
		textName.setLayoutData(gd);
		
		buttonLogin = new Button(_dlgGridLayout, SWT.NONE);
		buttonLogin.setBounds(new org.eclipse.swt.graphics.Rectangle(0,0,0,0));
		buttonLogin.setText("Login");
		gd = new org.eclipse.swt.layout.GridData();
		gd.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gd.widthHint = 80;
		buttonLogin.setLayoutData(gd);
		buttonLogin.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				Login();
			}
		});
		
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);	//white space
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);	//white space
		
		labelPassword = new Label(_dlgGridLayout, SWT.NONE);
		labelPassword.setBounds(new org.eclipse.swt.graphics.Rectangle(0,0,0,0));
		labelPassword.setText("Password:");		
		gd = new org.eclipse.swt.layout.GridData();
		gd.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gd.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gd.widthHint = -1;
		labelPassword.setLayoutData(gd);
		
		textPassword = new Text(_dlgGridLayout, SWT.BORDER);
		textPassword.setBounds(new org.eclipse.swt.graphics.Rectangle(0,0,0,0));
		gd = new org.eclipse.swt.layout.GridData();
		gd.widthHint = 80;
		textPassword.setLayoutData(gd);
		
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);
		labelGrid = new Label(_dlgGridLayout, SWT.NONE);
		
		createTableNameList();
		
		_dlgGridLayout.open();
	}
	
	//called only by createGridLayoutDlg
	private void createTableNameList() {
	    
		GridData gd = new org.eclipse.swt.layout.GridData();
		gd.horizontalSpan = 5;
		gd.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gd.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		tableNameList = new Table(_dlgGridLayout, SWT.NONE);
		tableNameList.setHeaderVisible(true);
		tableNameList.setLinesVisible(true);
		tableNameList.setBounds(new org.eclipse.swt.graphics.Rectangle(330,118,360,143));
		tableNameList.setLayoutData(gd);
		
		//getClientArea().width
		int nTableWidth = tableNameList.getBounds().width;
		System.out.println("nGridWidth=" + nTableWidth);

		TableColumn tblCol;
        tblCol = new TableColumn(tableNameList, SWT.NONE);
        //tblCol.setWidth(160);
		tblCol.setWidth(nTableWidth*1/5);
		tblCol.setText("Name");
		tblCol = new TableColumn(tableNameList, SWT.NONE);
		tblCol.setWidth(nTableWidth*2/5);
		tblCol.setText("Attribute1");
		tblCol = new TableColumn(tableNameList, SWT.NONE);
		tblCol.setWidth(nTableWidth*2/5);
		tblCol.setText("Attrbute2");
	
		addItem_TableNameList(new String[]{"Chocolate","sweet","甘い-甜巧克力"});
		addItem_TableNameList(new String[]{"Strawberry","sour","すっぱい-酸草莓",});
	}

	//called only by createGridLayoutDlg
	private void addItem_TableNameList(String[] strTextArray) {
	    
	    if(tableNameList==null)
	        createTableNameList();
		TableItem item = new TableItem(tableNameList, SWT.NULL); 
		item.setText(strTextArray); 

	}
	
	//called only by createGridLayoutDlg
	private void Login() {
		String strLoginName = textName.getText();
		String strLoginPass = textPassword.getText();
		//to add your sourse here
		//TEMP: addItem 
		addItem_TableNameList (new String[]{strLoginName, strLoginPass, strLoginName + "-" + strLoginPass});
	}

    public void UpdateProgressBar( final int selection_value ) 
    { 
        if( pBar_status.isDisposed() ) return; 
    
        Display.getDefault().asyncExec( 
            new Runnable(){ 
                public void run(){
                    pBar_status.setSelection( selection_value );
                } 
            } 
        ); 
    } 
    
}

