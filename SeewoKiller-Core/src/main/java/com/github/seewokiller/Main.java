package com.github.seewokiller;

import com.github.seewokiller.natives.Win32ApiCalls;
import com.sun.jna.WString;

public class Main {
    public static void main(String[] args) {
        //
        Win32ApiCalls.LIBRARY.monitorProcesses(new WString(""));
    }
}