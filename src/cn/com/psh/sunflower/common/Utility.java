/*
 * All Rights Reserved. Copyright (c) PFU Limited -- prj://countfd
 * Filename: Utility.java
 * Created on {2006/04/09}, by {Timothy}.
 */
package cn.com.psh.sunflower.common;

import org.eclipse.swt.widgets.Label;

/**
 * @author Timothy
 * @version $Revision: 1.0 $ $Date: 2006/04/09 0:54:13 $
 * Description:
 * Relatives:
 */
//----------------------------------------------------- Instance Variables
//----------------------------------------------------------- Constructors
//------------------------------------------------------------- Properties
//--------------------------------------------------------- Public Methods
public class Utility {
    private static boolean _bTraceSystemOut = false;
    private static boolean _bNotifyUserSystemOut = false;
    private static boolean _bErrorAdminSystemOut = true;
    private static boolean _bErrorUserSystemOut = false;
    private static Label _labelUserGUIOutput = null;
    private static final String _strFilePathSeparator = "\\";

    public static void trace(String strMsg){
        if(_bTraceSystemOut)
            System.out.println(strMsg);
    };

    public static void notifyUser(String strMsg){
        if(_bNotifyUserSystemOut)
            System.out.println(strMsg);
        if(_labelUserGUIOutput != null)
            _labelUserGUIOutput.setText(strMsg);
    };

    public static void errorAdmin(String strMsg){
        if(_bErrorAdminSystemOut)
            System.out.println(strMsg);
    };

    public static void errorUser(String strMsg){
        if(_bErrorUserSystemOut)
            System.out.println(strMsg);
        if(_labelUserGUIOutput != null)
            _labelUserGUIOutput.setText(strMsg);
    };

    /**
     * @param userGUIOutput The _labelUserGUIOutput to set.
     */
    public static void set_labelUserGUIOutput(Label userGUIOutput) {
        _labelUserGUIOutput = userGUIOutput;
    }

    public static String appendPath(String strPathPrev, String strPathPost)
    {
        if (strPathPrev.endsWith(_strFilePathSeparator))
            strPathPrev = strPathPrev.substring(0,strPathPrev.length() - 1);
        if (!strPathPost.startsWith(_strFilePathSeparator))
            strPathPost = _strFilePathSeparator + strPathPost;
        return strPathPrev + strPathPost;
    }
    public static String appendIfNotExists(String strSource, String strAppend){
        assert(strSource!=null && strAppend!=null);
        if(strSource.length()<strAppend.length()
           || !strSource.endsWith(strAppend))
            return strSource.concat(strAppend);
        
        return strSource;
    };
}
