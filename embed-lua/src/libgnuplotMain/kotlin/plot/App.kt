package plot

import lua.*
import kotlinx.cinterop.*

fun main() {
    var L = luaL_newstate()
    luaL_openlibs(L)
    
    //Load lua file from current directory
    luaL_loadfilex(L, "test.lua", null)
    lua_pcallk(L, 0, LUA_MULTRET, 0, 0, null)
    
    //trigger function "f1" defined in the lua file
    lua_getglobal(L, "f1")
    lua_pcallk(L, 0, LUA_MULTRET, 0, 0, null)
    
    //trigger function "square" defined in the lua file with parameters
    lua_getglobal(L, "square")
    lua_pushnumber(L, 6.toDouble())
    lua_pcallk(L, 1, LUA_MULTRET, 0, 0, null)
    
    //get the return value of previously invoked function
    var mynumber = lua_tonumberx(L, -1, null)
    println("Lua result value: ${mynumber}")
    
    lua_close(L)
}