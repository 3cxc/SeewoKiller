package com.github.seewokiller.system;

import com.github.basiclib.Logger.Logger;
import com.github.basiclib.Logger.LoggerBase;
import com.github.seewokiller.natives.Win32ApiCalls;
import com.sun.jna.WString;

import java.util.Vector;

import static java.lang.Thread.sleep;

public class SystemMonitor implements Runnable{

    @SuppressWarnings("BusyWait")
    @Override
    public void run(){

        Logger logger = new LoggerBase();

        WString processName = new WString("EasiNote.exe");
        // 获取并存储所有同名进程的ID
        Vector<Integer> processIDs = Win32ApiCalls.LIBRARY.TraversalProcesses(processName);

        try {
            while (Thread.currentThread().isInterrupted()) {
                if (processIDs.isEmpty()) {
                    logger.info("监视的进程 "+ processName +"没有在运行，继续监测.");

                    sleep(3000); // 3秒检查一次
                    continue;
                }

                boolean allProcessesWithoutWindow = true;
                for (int id : processIDs) {
                    sleep(3000);
                    if (Win32ApiCalls.LIBRARY.HasWindow(id) || Win32ApiCalls.LIBRARY.IsProcessForeground(id)) {
                        allProcessesWithoutWindow = false;
                        logger.info("监视的进程 "+ processName +" 正在运行且未退出窗口，继续监测.");
                        break;
                    }
                }

                boolean iskill = false;

                if (allProcessesWithoutWindow) {
                    for (int id : processIDs) {
                        if (Win32ApiCalls.LIBRARY.KillProcess(id)) {
                            logger.info("PID为 "+ id +" 的进程已被杀死。");
                            iskill = true;
                        }
                        else {
                            logger.error("无法杀死PID为 "+ id +" 的进程，可能是无权限原因.");
                        }
                    }
                }

                if (iskill) OpenBrowser("https://www.bilibili.com/video/BV1he4y1w7wB/?spm_id_from=333.337.search-card.all.click");

                sleep(1000);
            }
        }catch (InterruptedException e){

        }
    }
}
