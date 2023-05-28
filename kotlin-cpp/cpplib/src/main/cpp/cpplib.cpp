#include <fstream>
#include <string>
#include <cstring>
#include "cpplib.h"

std::string CppLib::Library::greeting() {
	return "Hello, World! From C++";
}
const char* CppLib::Library::copy(std::string str) {
	char* arr = new char[str.length() + 1];
	strcpy(arr, str.c_str());
	return arr;
}

std::string CppLib::Library::getline() {
	std::string eof("\\0");
	if (file.is_open()) {
		std::string line;
		if(std::getline(file, line)) {
			return line;
		} else {
			return eof;
		}
	}
	return eof;
}

bool CppLib::Library::is_open() {
	return file.is_open();
}

void CppLib::Library::open(std::string fname) {
	file.open(fname, std::fstream::in);
}

void CppLib::Library::close() {
	if(file.is_open()) {
		file.close();
	}
}
