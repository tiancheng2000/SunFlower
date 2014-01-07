/*
 * All Rights Reserved. Copyright (c) PFU Limited -- prj://SunFlower_SWT
 * Filename: GUIUpdater.java
 * Created on {2006-12-19}, by {Timothy}.
 */
package cn.com.psh.sunflower.ui;

public class Transaction implements Runnable {

    private CountSourceLineUI _gui = null; 
    
    public Transaction() {
    }

    public void registerGUIUpdater(CountSourceLineUI gui) {
        _gui = gui; 
    }

    public void run() {
        int current_seleciton_value = 0; 

        while( current_seleciton_value <= 100 ) 
        { 
            if(_gui!=null)
                _gui.UpdateProgressBar( current_seleciton_value );
            
            //do some thing
            //...
            
            //update the progress value
            current_seleciton_value += 10; 

            try{ 
                Thread.sleep(600); // 0.6 sec
            } 
            catch( Exception e ){
                ;
            } 
        } 
        
        if(_gui!=null)
            _gui.UpdateProgressBar(0);
    }

}
