// pch.cpp: 与预编译标头对应的源文件

#include "pch.h"
#include <vector>
#include <string>
#include <tlhelp32.h>
#include <conio.h>
#include <iostream>

//定义函数类型
typedef bool(*AddFunc)(DWORD processId);
typedef void(*AddFuncVoid)(const wchar_t* url);
typedef std::wstring(*AddFuncWstring)(DWORD pid);

//引用外部变量
static AddFunc HasWindow;
static AddFunc IsProcessForeground;
static AddFunc KillProcess;
static AddFuncWstring GetExecutablePathByPID;
static AddFuncVoid OpenBrowser;

using namespace std;

// 当使用预编译的头时，需要使用此源文件，编译才能成功。

int LoadLib() {
    //加载DLL运行库
    HMODULE DLLmodule = LoadLibrary(L"PluginEasyLib.dll");
    HMODULE DLLmodule_WebLink = LoadLibrary(L"WebLink.dll");
    if (DLLmodule == NULL) {
        cout << "无法找到动态运行库 PluginEasyLib.dll" << endl;
        cout << "Press any key to exit..." << endl;
        _getch();
        return 1;
    }
    if (DLLmodule_WebLink == NULL) {
        cout << "无法找到动态运行库 WebLink.dll" << endl;
        cout << "Press any key to exit..." << endl;
        _getch();
        return 1;
    }

    //导出函数地址
    HasWindow = (AddFunc)GetProcAddress(DLLmodule, "HasWindow");//用于检查指定进程是否有窗口，如果窗口不可见则返回false
    IsProcessForeground = (AddFunc)GetProcAddress(DLLmodule, "IsProcessForeground");// 检查指定的进程是否在前台
    KillProcess = (AddFunc)GetProcAddress(DLLmodule, "KillProcess");// 杀死进程的函数
    OpenBrowser = (AddFuncVoid)GetProcAddress(DLLmodule_WebLink, "OpenBrowser");//传入一个URL，打开浏览器并访问该URL

    //无法获取函数时的处理
    if (HasWindow == nullptr || IsProcessForeground == nullptr || KillProcess == nullptr) {
        cout << "获取动态库函数失败." << endl;
        FreeLibrary(DLLmodule);//释放DLL库
        cout << "Press any key to exit..." << endl;
        _getch();
        return 1;
    }

    return 0;
}


int monitorProcesses(const wchar_t* targetProcessName) {
    LoadLib();
    while (true) {
        std::vector<DWORD> processIDs; // 存储所有同名进程的ID
        PROCESSENTRY32 pe32;
        pe32.dwSize = sizeof(PROCESSENTRY32);
        HANDLE hProcessSnap = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
        if (Process32First(hProcessSnap, &pe32)) {
            do {
                if (wcscmp(pe32.szExeFile, targetProcessName) == 0) {
                    processIDs.push_back(pe32.th32ProcessID);
                }
            } while (Process32Next(hProcessSnap, &pe32));
        }
        CloseHandle(hProcessSnap);

        if (processIDs.empty()) {
            wcout << "监视的进程 " << targetProcessName << " 没有在运行，继续监测." << endl;
            Sleep(3000); // 3秒检查一次
            continue;
        }

        bool allProcessesWithoutWindow = true;
        for (DWORD id : processIDs) {
            Sleep(3000);
            if (HasWindow(id) || IsProcessForeground(id)) {
                allProcessesWithoutWindow = false;
                wcout << "监视的进程 " << targetProcessName << " 正在运行且未退出窗口，继续监测." << endl;
                break;
            }
        }

        bool iskill = false;

        if (allProcessesWithoutWindow) {
            for (DWORD id : processIDs) {
                if (KillProcess(id)) {
                    cout << "PID为 " << id << " 的进程已被杀死。" << endl;
                    iskill = true;
                }
                else {
                    cout << "无法杀死PID为 " << id << " 的进程，可能是无权限原因." << endl;
                }
            }
        }

        if (iskill) OpenBrowser(L"https://www.bilibili.com/video/BV1he4y1w7wB/?spm_id_from=333.337.search-card.all.click");

        Sleep(1000);
    }

    return 0;
}