package com.github.seewokiller.natives;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;

import java.util.Vector;

/**
 * 调用C++ DLL方法
 * @since SeewoKiller-Core 1.0
 * @author 3cxc
 * @version 1.0
 */
//继承Library，用于加载库文件
public interface Win32ApiCalls extends Library {

    //动态调用SeewoCore.dll
    Win32ApiCalls LIBRARY = (Win32ApiCalls) Native.loadLibrary("SeewoCore", Win32ApiCalls.class);

    //用于检查指定进程是否有窗口，如果窗口不可见则返回false
    boolean HasWindow(int processID);
    // 检查指定的进程是否在前台
    boolean IsProcessForeground(int processId);
    // 杀死进程的函数
    boolean KillProcess(int processID);

    // 遍历系统中符合指定名称的进程并返回它们的进程ID
    Vector<Integer> TraversalProcesses(WString targetProcessName);

}
