      subroutine test1() bind(c, name='test1')
      use, intrinsic :: ISO_C_BINDING
      implicit none
cGCC$ ATTRIBUTES STDCALL, DLLEXPORT, CDECL :: test1
cDEC$ ATTRIBUTES STDCALL, DLLEXPORT, CDECL :: test1
      PRINT *, 'I am a fortran subroutine'
      return
      end subroutine test1
