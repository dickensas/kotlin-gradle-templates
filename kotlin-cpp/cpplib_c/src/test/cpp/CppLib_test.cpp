#include "cpplib_c.h"
#include <cassert>
#include <string.h>

int main() {
    assert(strcmp( greeting(), "Hello, World! From C++") == 0);
    return 0;
}
