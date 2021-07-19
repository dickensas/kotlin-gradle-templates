%include "cpointer.i"
%include "carrays.i"
%pointer_functions(int, intp);
%array_functions(double, doubleArray);

%{
#include "../public/cpplib.h"
%}

%include "../public/cpplib.h"
