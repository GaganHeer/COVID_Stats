package covidcases;

import java.io.IOException;
import io.github.alexarchambault.windowsansi.WindowsAnsi;

public class TextColour {
    private static final String ANSI_END = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    private boolean isWindows = false;
    private boolean setupSuccessful = false;

    public TextColour(){
        isWindows = System.getProperty("os.name").toLowerCase(java.util.Locale.ROOT).contains("windows");
        if(isWindows){
            try {
                setupSuccessful = WindowsAnsi.setup();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }   
        }
    }

    public void printWarning(String warningMsg){
        if(setupSuccessful || !(isWindows)){
            System.out.println(ANSI_RED + warningMsg + ANSI_END);
        }else{
            System.out.println(warningMsg);
        }
    }

    public void printTip(String tipMsg){
        if(setupSuccessful || !(isWindows)){
            System.out.println(ANSI_GREEN + tipMsg + ANSI_END);
        }else{
            System.out.println(tipMsg);
        }
    }

    public void printTitle(String title){
        if(setupSuccessful || !(isWindows)){
            System.out.println(ANSI_PURPLE + title + ANSI_END);
        }else{
            System.out.println(title);
        }
    }
}
