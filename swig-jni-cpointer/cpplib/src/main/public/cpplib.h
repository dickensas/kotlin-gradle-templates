#ifndef JNIEXAMPLE_H
#define JNIEXAMPLE_H

#ifdef _WIN32
#define JNIEXAMPLE_EXPORT_FUNC __declspec(dllexport)
#else
#define JNIEXAMPLE_EXPORT_FUNC
#endif

void JNIEXAMPLE_EXPORT_FUNC add(int *x, int *y, int *result);
void JNIEXAMPLE_EXPORT_FUNC print_array(double x[10]);
void JNIEXAMPLE_EXPORT_FUNC modify_array(double x[10]);
void JNIEXAMPLE_EXPORT_FUNC copy_array(double x[10], double y[10]);

#endif
