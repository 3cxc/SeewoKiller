package com.github.seewokiller.modules;

import com.github.seewokiller.natives.NativeKiller;
import com.github.seewokiller.natives.NativeWebLink;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import static com.github.seewokiller.Main.logger;
import static java.lang.Thread.sleep;

/**
 * 希沃杀手模块类
 * @version 1.0
 */
public class SeewoKiller {

    private static final NativeKiller killer = (NativeKiller) Native.loadLibrary("Killer", NativeKiller.class);
    private static final NativeWebLink webLink = (NativeWebLink) Native.loadLibrary("WebLink", NativeWebLink.class);

    /**
     * 该方法用以检测希沃白板是否运行
     * <p>
     * 注意: 为了防止卡死主线程，该方法需要开新线程运行
     */
    public void runKiller(WString processName){
        try{
            while (!Thread.currentThread().isInterrupted()) {
                int[] processIDs;

                //获取进程ID
                killer.traversalProcesses(processName);
                Pointer dataPointer = killer.getProcessIDsData();

                if (dataPointer == null){
                    logger.info("监视的程序 "+ processName +" 没有在运行，继续监测.");

                    sleep(3000); // 3秒检查一次
                    continue;
                }

                //获取vector的大小，用以后面初始化数组
                long size = killer.getProcessIDsSize();

                //转换为Java的数组
                processIDs = dataPointer.getIntArray(0, (int) size);

                //为false则为程序未退出窗口
                boolean allProcessesWithoutWindow = true;


                for (long id : processIDs) {
                    sleep(3000);
                    if (killer.hasWindow(id)) {
                        allProcessesWithoutWindow = false;
                        logger.info("监视的程序 "+ processName + "(" + id + ") 正在运行且未退出窗口，继续监测.");
                        break;
                    }
                }

                boolean isKill = false;

                if (allProcessesWithoutWindow) {
                    for (long id : processIDs) {
                        if (killer.killProcess(id)) {
                            logger.info("进程 "+ processName + "(" + id + ") 已被杀死.");
                            isKill = true;
                        }
                        else {
                            logger.error("进程 "+ processName + "(" + id + ") 无法杀死，可能是操作被拦截(无权限).");
                        }
                    }
                }

                if (isKill) webLink.openBrowser(new WString("https://www.bilibili.com/video/BV1he4y1w7wB"));

                sleep(1000);
            }
        }catch (InterruptedException e){
            logger.info("SeewoKiller 模块已退出");
            NativeLibrary.getInstance("Killer").dispose();
            NativeLibrary.getInstance("WebLink").dispose();
        }
    }
}
