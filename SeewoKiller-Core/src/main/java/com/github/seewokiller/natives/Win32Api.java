package com.github.seewokiller.natives;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 用以动态加载C++ DLL
 * @since SeewoKiller-Core 1.0
 * @author 3cxc
 * @version 1.1
 */
//继承Library，用于加载库文件
abstract public class Win32Api implements Library {

    //动态DLL库的存储
    static Object LIBRARY;

    //加载库文件
    public static Object loadLibrary(String libraryName,Class objectClass){
        LIBRARY = Native.loadLibrary(libraryName, objectClass);
        return LIBRARY;
    }


}
