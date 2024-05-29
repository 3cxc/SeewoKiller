package com.github.seewokiller.natives;

import com.sun.jna.WString;

import java.util.Vector;

/**
 * C++ DLL方法的接口定义
 * <p>
 * 该接口包含了SeewoCore.dll的相关方法
 * @since SeewoKiller-Core 1.0
 * @author 3cxc
 * @version 1.1
 */
public interface SeewoCoreCalls {
    //用于检查指定进程是否有窗口，如果窗口不可见则返回false
    boolean HasWindow(int processID);
    // 检查指定的进程是否在前台
    boolean IsProcessForeground(int processId);
    // 杀死进程的函数
    boolean KillProcess(int processID);

    // 遍历系统中符合指定名称的进程并返回它们的进程ID
    Vector<Integer> TraversalProcesses(WString targetProcessName);

}
