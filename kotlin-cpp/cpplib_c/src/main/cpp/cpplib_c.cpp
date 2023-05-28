#include <cstdlib>

#include "cpplib_c.h"
#include "cpplib.h"

#ifdef __cplusplus
extern "C" {
#endif

static CppLib::Library *lib = NULL;

void init() {
    if (lib == NULL) {
    	lib = new CppLib::Library();
    }
}

void open(const char *name) {
    init();
    lib->open(std::string(name));
}

void close() {
    init();
    lib->close();
    delete lib;
}

const char* greeting() {
	init();
	return lib->copy(lib->greeting());
}
const char* getline() {
	init();
	return lib->copy(lib->getline());
}
int is_open() {
	init();
	return int(lib->is_open());
}

#ifdef __cplusplus
}
#endif
