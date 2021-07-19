#include "cpplib.h"
#include <stdlib.h>
#include <stdio.h>

void add(int *x, int *y, int *result) {
  *result = *x + *y;
}

void print_array(double x[10]) {
  printf("\nprint_array\n");
  int i;
  for (i = 0; i < 10; i++) {
    printf("[%d] = %g\n", i, x[i]);
  }
}

void modify_array(double x[10]) {
  printf("\nmodify_array\n");
  int i;
  for (i = 0; i < 10; i++) {
    x[i] = x[i] * 3.0;
  }
}

void copy_array(double x[10], double y[10]) {
  printf("\ncopy_array\n");
  int i;
  for (i = 0; i < 10; i++) {
    y[i] = x[i];
  }
}