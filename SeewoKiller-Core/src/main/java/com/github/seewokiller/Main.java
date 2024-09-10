package com.github.seewokiller;

import com.github.seewokiller.init.InitKiller;
import com.github.tlib.Logger.Logger;
import com.github.tlib.Logger.LoggerBase;
import com.github.tlib.init.InitManager;

public class Main {

    public static final Logger logger = new LoggerBase();
    private static final InitManager manager = new InitManager();

    public static void main(String[] args) {
        manager.putLoadMethodToMap(InitKiller.class,new InitKiller());
        manager.load();
    }
}