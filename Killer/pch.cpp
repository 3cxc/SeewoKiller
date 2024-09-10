// pch.cpp: 与预编译标头对应的源文件

#include "pch.h"

// 当使用预编译的头时，需要使用此源文件，编译才能成功。

#include <Psapi.h>
#include <string>
#include <tlhelp32.h>
#include <conio.h>
#include <algorithm>

using namespace std;

// 用于检查指定进程是否有窗口，如果窗口不可见则返回false
bool hasWindow(DWORD processID) {
    HWND hwnd = NULL;
    do {
        hwnd = FindWindowEx(NULL, hwnd, NULL, NULL);
        DWORD windowProcessID = 0;
        GetWindowThreadProcessId(hwnd, &windowProcessID);
        if (windowProcessID == processID) {
            if (IsWindowVisible(hwnd)) {
                // 检查窗口是否可见
                return true;
            }
        }
    } while (hwnd != NULL);
    return false;
}

// 杀死进程的函数
bool killProcess(DWORD processID) {
    HANDLE hProcess = OpenProcess(PROCESS_TERMINATE, FALSE, processID);
    if (hProcess != NULL) {
        TerminateProcess(hProcess, 0);
        CloseHandle(hProcess);
        return true;
    }
    return false;
}

static vector<DWORD> process;

// 遍历系统中符合指定名称的进程并返回它们的进程ID
void traversalProcesses(const wchar_t* targetProcessName) {
    vector<DWORD> processIDs; // 存储所有同名进程的ID
    PROCESSENTRY32 pe32{};
    pe32.dwSize = sizeof(PROCESSENTRY32);//获取系统中所有正在运行的进程
    HANDLE hProcessSnap = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
    if (Process32First(hProcessSnap, &pe32)) {
        //遍历检测进程，将名称符合的进程的ID存储到processIDs中
        do {
            if (wcscmp(pe32.szExeFile, targetProcessName) == 0) {
                processIDs.push_back(pe32.th32ProcessID);
            }
        } while (Process32Next(hProcessSnap, &pe32));
    }
    CloseHandle(hProcessSnap);

    process = processIDs;
}

// 提供访问 processID 的函数
DWORD* getProcessIDsData() {
    if (!process.empty()) {
        return process.data();
    }
    return NULL;
}

size_t getProcessIDsSize() {
    if (!process.empty()) {
        return process.size();
    }
    return NULL;
}