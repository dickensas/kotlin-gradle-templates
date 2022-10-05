package com.zigma.notepadauto

import kotlinx.cinterop.*
import win32api.*

fun main() {
     memScoped {
	    var hWnd = FindWindowA(null, "Untitled - Notepad")
	    if(hWnd!=null) {
		    var hMenu = GetMenu(hWnd)
		    var count = GetMenuItemCount(hMenu)
		    for (i in 0 until count) {
			    var rec = alloc<RECT>()
			    var MATCHED_WORD_BUF_SIZE = 1024
			    var fileMenu = GetMenuItemRect(hWnd, hMenu, i.toUInt(), rec.ptr)
			    var buf = allocArray<TCHARVar>(MATCHED_WORD_BUF_SIZE)
			    var menuInfo = alloc<MENUITEMINFO>() {
			    	cbSize = sizeOf<MENUITEMINFO>().toUInt()
			    	fMask = MIIM_TYPE.toUInt() or MIIM_SUBMENU.toUInt()
			    	cch = MATCHED_WORD_BUF_SIZE.toUInt()
			    	dwTypeData = buf
			    }
			    GetMenuItemInfoA(hMenu,i.toUInt(),1,menuInfo.ptr)
			    println(menuInfo.dwTypeData!!.toKString())
		    }
    	}else{
    		println("please open a blank notepad window")
    	}
    }
}
