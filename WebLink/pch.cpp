// pch.cpp: 与预编译标头对应的源文件

#include "pch.h"

// 当使用预编译的头时，需要使用此源文件，编译才能成功。

#include <iostream>
#include <shellapi.h>
#include <assert.h>
#include <tchar.h>

//传入一个URL，打开浏览器并访问该URL
void openBrowser(const wchar_t* url) {
    const TCHAR szOperation[] = _T("open");
    LPCTSTR szAddress = url;
    //调用Win32API进行操作
    HINSTANCE hRslt = ShellExecute(NULL, szOperation,
        szAddress, NULL, NULL, SW_SHOWNORMAL);
    assert(hRslt > (HINSTANCE)HINSTANCE_ERROR);
}