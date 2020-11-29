package plot

import kotlinx.cinterop.*
import platform.windows.*
import directx.*

var PIXMAP_WIDTH = 640
var PIXMAP_HEIGHT = 480

@ExperimentalUnsignedTypes
fun WndProc(hwnd: HWND?, msg: UINT, wParam: WPARAM, lParam: LPARAM) : LRESULT {
    when(msg) {
        WM_COMMAND.toUInt() -> { }
        WM_CLOSE.toUInt() -> DestroyWindow(hwnd)
        WM_DESTROY.toUInt() -> PostQuitMessage(0)
        else -> return (DefWindowProc!!)(hwnd, msg, wParam, lParam)
    }
    return 0;
}

@ExperimentalUnsignedTypes
fun D3DCOLOR_ARGB(a : Int, r: Int, g: Int, b: Int) : UInt {
   return (((a and 0xff) shl 24) or ((r and 0xff) shl 16) or ((g and 0xff) shl 8) or (b and 0xff)).toUInt()
}

@ExperimentalUnsignedTypes
fun main() {
    
    memScoped {
        
        val hInst = GetModuleHandleW(null)
        val lpszClassName = "plot"

        val wc = alloc<WNDCLASSEX>();

        wc.cbSize = sizeOf<WNDCLASSEX>().toUInt();
        wc.hCursor = LoadCursorW(hInst, IDC_ARROW);
        wc.lpfnWndProc = staticCFunction(::WndProc)
        wc.cbClsExtra = 0;
        wc.cbWndExtra = 0;
        wc.hInstance = hInst;
        wc.hIcon = null;
        wc.lpszMenuName = null;
        wc.hIconSm = null;
        wc.lpszClassName = lpszClassName.wcstr.ptr
        wc.style = 0u;

        if(RegisterClassExW(wc.ptr) == 0u.toUShort()) {
            println("Failed to register!")
            return
        }

        val hwnd = CreateWindowExA(WS_EX_CLIENTEDGE, lpszClassName, "plot",
            (WS_OVERLAPPED or WS_CAPTION or WS_SYSMENU or WS_MINIMIZEBOX).toUInt(),
            CW_USEDEFAULT, CW_USEDEFAULT, 800, 600, null, null, hInst, NULL
        )

        ShowWindow(hwnd, 1);
        UpdateWindow(hwnd);
        
        val msg = alloc<MSG>();

        var d3d = Direct3DCreate9(D3D_SDK_VERSION)
        var d3ddev = alloc<LPDIRECT3DDEVICE9Var>()
        
        var d3dpp = alloc<D3DPRESENT_PARAMETERS>().apply {
           Windowed = 1;
           SwapEffect = D3DSWAPEFFECT_DISCARD;
           hDeviceWindow = hwnd;
           BackBufferFormat = D3DFMT_UNKNOWN.toUInt();
           BackBufferWidth = PIXMAP_WIDTH.toUInt();
           BackBufferHeight = PIXMAP_HEIGHT.toUInt();
           EnableAutoDepthStencil  = 1;
           AutoDepthStencilFormat  = D3DFMT_D16.toUInt();
        }
        
        var l:IDirect3D9Vtbl = d3d!!.pointed!!.lpVtbl!!.pointed
        
        l!!.CreateDevice!!(
           d3d, 
           D3DADAPTER_DEFAULT.toUInt(), 
           D3DDEVTYPE_HAL.toUInt(), 
           hwnd, 
           D3DCREATE_SOFTWARE_VERTEXPROCESSING.toUInt(), 
           d3dpp.ptr, 
           d3ddev.ptr 
        )
        
        var l1:IDirect3DDevice9Vtbl = d3ddev!!.value!!.pointed!!.lpVtbl!!.pointed
        
        var surf = alloc<LPDIRECT3DSURFACE9Var>()
        l1!!.CreateOffscreenPlainSurface!!(
           d3ddev.value,
           PIXMAP_WIDTH.toUInt(),
           PIXMAP_HEIGHT.toUInt(),
           D3DFMT_X8R8G8B8.toUInt(),
           D3DPOOL_DEFAULT,
           surf.ptr,
           null
        )
        
        var l2:IDirect3DSurface9Vtbl = surf!!.value!!.pointed!!.lpVtbl!!.pointed
        
        var lockRect = alloc<D3DLOCKED_RECT>() 
        
        l2!!.LockRect!!(
           surf.value,
           lockRect.ptr,
           null,
           D3DLOCK_DISCARD.toUInt()
        )
        
        var vdata = UIntArray(PIXMAP_WIDTH * PIXMAP_HEIGHT)
        for (y in 0..PIXMAP_HEIGHT-1) {
           for (x in 0..PIXMAP_WIDTH-1) {
              vdata[ x + PIXMAP_WIDTH*y ] = D3DCOLOR_ARGB(
                 0xff, 
                 255 - 255 * (y+1) / PIXMAP_HEIGHT,
                 0,
                 0
              )
           }
        }
        
        vdata.usePinned { buf ->
            platform.posix.memcpy(lockRect.pBits, buf.addressOf(0), (PIXMAP_WIDTH * PIXMAP_HEIGHT * 4).toULong())
        }
        
        l2!!.UnlockRect!!(
           surf.value
        )
        
        while((GetMessage!!)(msg.ptr, null, 0u, 0u) > 0) {
            TranslateMessage(msg.ptr);
            DispatchMessageW(msg.ptr);
            
            l1!!.Clear!!(d3ddev.value, 0.toUInt(), null, D3DCLEAR_TARGET.toUInt(), D3DCOLOR_ARGB(0xff, 0, 0, 170), 1.0f, 0.toUInt());
            l1!!.BeginScene!!(d3ddev.value)
            
            var backbuffer = alloc<LPDIRECT3DSURFACE9Var>()
            l1!!.GetBackBuffer!!(d3ddev.value, 0.toUInt(), 0.toUInt(), D3DBACKBUFFER_TYPE_MONO, backbuffer.ptr)
            l1!!.StretchRect!!(d3ddev.value, surf.value, null, backbuffer.value, null, D3DTEXF_LINEAR)
            backbuffer.value!!.pointed!!.lpVtbl!!.pointed!!.Release!!(backbuffer.value)
            
            l1!!.EndScene!!(d3ddev.value)
            l1!!.Present!!(d3ddev.value, null, null, null, null);
        }
    }
}