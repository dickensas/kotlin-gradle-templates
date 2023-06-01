#include <cstdlib>

#include "cpplib_c.h"
#include "cpplib.h"

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
const char* libgetline() {
	init();
	return lib->copy(lib->libgetline());
}
int is_open() {
	init();
	return int(lib->is_open());
}
