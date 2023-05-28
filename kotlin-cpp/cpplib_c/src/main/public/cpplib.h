#ifndef CPPLIB_H
#define CPPLIB_H

#include <fstream>
#include <string>

namespace CppLib {
    class Library {
    	private:
    		std::fstream file;
        public:
    		const char* copy(std::string str);
    		std::string greeting();
    		std::string getline();
			void open(std::string fname);
			void close();
			bool is_open();
    };
}

#endif
