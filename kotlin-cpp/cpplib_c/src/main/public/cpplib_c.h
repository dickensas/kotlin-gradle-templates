#ifndef CPPLIB_C_H
#define CPPLIB_C_H

#ifdef __cplusplus
extern "C" {
#endif

    const char* greeting();
    const char* getline();
	void open(const char* fname);
	void close();
	int is_open();

#ifdef __cplusplus
}
#endif


#endif
