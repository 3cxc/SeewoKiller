package com.github.seewokiller.natives;

import com.sun.jna.Library;
import com.sun.jna.WString;

public interface NativeWebLink extends Library {
    //传入一个URL，打开浏览器并访问该URL
    void openBrowser(WString url);
}
