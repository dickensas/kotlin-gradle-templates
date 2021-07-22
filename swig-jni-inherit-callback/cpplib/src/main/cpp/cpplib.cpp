/*
 * This C++ source file was generated by the Gradle 'init' task.
 */

#include <iostream>
#include <stdlib.h>
#include "cpplib.h"

void jniexample::Callback::run() {
    std::cout << "From C++ Callback::run()" << std::endl;
}

jniexample::Callback::~Callback() { 
	std::cout << "From C++ Callback::~Callback()" << std:: endl; 
}

jniexample::Caller::~Caller() {
	delCallback();
}

void jniexample::Caller::delCallback() { 
	delete _callback; 
	_callback = 0; 
}

void jniexample::Caller::setCallback(Callback *cb) { 
	delCallback(); 
	std::cout << "From C++ Setting callback" << std:: endl; 
	_callback = cb; 
}

void jniexample::Caller::call() { 
	if (_callback) { 
		_callback->run();
	} 
}