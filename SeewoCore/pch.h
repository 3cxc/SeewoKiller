// pch.h: 这是预编译标头文件。
// 下方列出的文件仅编译一次，提高了将来生成的生成性能。
// 这还将影响 IntelliSense 性能，包括代码完成和许多代码浏览功能。
// 但是，如果此处列出的文件中的任何一个在生成之间有更新，它们全部都将被重新编译。
// 请勿在此处添加要频繁更新的文件，这将使得性能优势无效。

#ifndef PCH_H
#define PCH_H

// 添加要在此处预编译的标头
#include "framework.h"
#include <iostream>
#include <vector>

using namespace std;

extern "C" {
	//用于检查指定进程是否有窗口，如果窗口不可见则返回false
	_declspec(dllexport) bool HasWindow(DWORD processID);
	// 检查指定的进程是否在前台
	_declspec(dllexport) bool IsProcessForeground(DWORD processId);
	// 杀死进程的函数
	_declspec(dllexport) bool KillProcess(DWORD processID);

}

// 遍历系统中符合指定名称的进程并返回它们的进程ID
_declspec(dllexport) vector<DWORD> TraversalProcesses(const wchar_t* targetProcessName);

#endif //PCH_H
