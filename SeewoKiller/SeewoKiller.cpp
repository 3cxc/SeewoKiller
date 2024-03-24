#include <Windows.h>
#include <iostream>
#include <vector>
#include <tlhelp32.h>
#include <conio.h>

using namespace std;

const wchar_t* targetProcessName = L"EasiNote.exe"; // 修改为你要监视的进程名


// 主函数
int main() {

    //加载DLL运行库
    HMODULE DLLmodule = LoadLibrary(L"SeewoKiller.dll");
    if (DLLmodule == NULL) {
        cout << "无法找到动态运行库 SeewoKiller.dll" << endl;
        cout << "Press any key to exit..." << endl;
        _getch();
        return 1;
    }
    //定义函数类型
    typedef int(*AddFunc)(const wchar_t* targetProcessName);
    //导出函数地址
    AddFunc monitorProcesses = (AddFunc)GetProcAddress(DLLmodule, "monitorProcesses");//监视进程并进行处理

    //无法获取函数时的处理
    if (monitorProcesses == nullptr) {
        cout << "获取动态库函数失败！" << endl;
        FreeLibrary(DLLmodule);//释放DLL库
        cout << "Press any key to exit..." << endl;
        _getch();
        return 1;
    }

    monitorProcesses(targetProcessName);
}