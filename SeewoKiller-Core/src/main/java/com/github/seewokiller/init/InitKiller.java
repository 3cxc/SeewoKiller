package com.github.seewokiller.init;

import com.github.seewokiller.modules.SeewoKiller;
import com.github.tlib.init.Initable;
import com.sun.jna.WString;

public class InitKiller implements Initable {
    @Override
    public void run() {
        Thread thread = new Thread(() -> {
            SeewoKiller seewoKiller = new SeewoKiller();
            seewoKiller.runKiller(new WString("EasiNote.exe"));
        });
        thread.setName("Killer");
        thread.start();
    }
}
