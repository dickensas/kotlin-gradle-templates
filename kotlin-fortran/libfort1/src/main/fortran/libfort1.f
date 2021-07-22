      subroutine test1() bind(c)
      use ISO_C_BINDING
      implicit none
cGCC$ ATTRIBUTES STDCALL, DLLEXPORT :: test1
cDEC$ ATTRIBUTES STDCALL, DLLEXPORT :: test1
      PRINT *, 'I am a fortran subroutine'
      return
          
      end subroutine test1
