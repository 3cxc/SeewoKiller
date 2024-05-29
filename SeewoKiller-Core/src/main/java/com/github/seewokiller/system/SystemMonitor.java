package com.github.seewokiller.system;

import com.github.basiclib.Logger.Logger;
import com.github.basiclib.Logger.LoggerBase;
import com.github.seewokiller.natives.WebLinkCalls;
import com.github.seewokiller.natives.Win32Api;
import com.github.seewokiller.natives.SeewoCoreCalls;
import com.sun.jna.WString;

import java.util.Vector;

import static java.lang.Thread.sleep;

public class SystemMonitor implements Runnable{

    @SuppressWarnings("BusyWait")
    @Override
    public void run(){

        Logger logger = new LoggerBase();

        WString processName = new WString("EasiNote.exe");
        //加载SeewoCore.dll
        SeewoCoreCalls dll = (SeewoCoreCalls) Win32Api.loadLibrary("SeewoCore", SeewoCoreCalls.class);
        //加载WebLink.dll
        WebLinkCalls web = (WebLinkCalls) Win32Api.loadLibrary("WebLink", WebLinkCalls.class);

        // 获取并存储所有同名进程的ID
        Vector<Integer> processIDs = dll.TraversalProcesses(processName);

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
                    if (dll.HasWindow(id) || dll.IsProcessForeground(id)) {
                        allProcessesWithoutWindow = false;
                        logger.info("监视的进程 "+ processName +" 正在运行且未退出窗口，继续监测.");
                        break;
                    }
                }

                boolean isKill = false;

                if (allProcessesWithoutWindow) {
                    for (int id : processIDs) {
                        if (dll.KillProcess(id)) {
                            logger.info("PID为 "+ id +" 的进程已被杀死。");
                            isKill = true;
                        }
                        else {
                            logger.error("无法杀死PID为 "+ id +" 的进程，可能是无权限原因.");
                        }
                    }
                }

                if (isKill) web.OpenBrowser(new WString("https://www.bilibili.com/video/BV1he4y1w7wB/?spm_id_from=333.337.search-card.all.click"));

                sleep(1000);
            }
        }catch (InterruptedException e){
            logger.info(Thread.interrupted());
        }
    }
}
