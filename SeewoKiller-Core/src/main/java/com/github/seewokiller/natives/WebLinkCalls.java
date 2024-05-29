package com.github.seewokiller.natives;

import com.sun.jna.WString;

/**
 * C++ DLL方法的接口定义
 * <p>
 * 该接口包含了WebLink.dll的相关方法
 * @since SeewoKiller-Core 1.0
 * @author 3cxc
 * @version 1.0
 */
public interface WebLinkCalls {
    //传入一个URL，打开浏览器并访问该URL
    void OpenBrowser(WString url);
}
