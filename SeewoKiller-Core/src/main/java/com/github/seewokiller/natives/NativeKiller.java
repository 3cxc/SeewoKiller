package com.github.seewokiller.natives;

import com.sun.jna.*;

public interface NativeKiller extends Library {
    // 用于检查指定进程是否有窗口，如果窗口不可见则返回false
    boolean hasWindow(long processID);
    // 杀死进程的函数
    boolean killProcess(long processID);
    // 遍历系统中符合指定名称的进程
    void traversalProcesses(WString targetProcessName);
    // 提供访问 processID 的函数
    Pointer getProcessIDsData();
    long getProcessIDsSize();

}
