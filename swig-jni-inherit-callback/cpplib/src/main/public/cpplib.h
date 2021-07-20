#ifndef JNIEXAMPLE_H
#define JNIEXAMPLE_H

#ifdef _WIN32
#define JNIEXAMPLE_EXPORT_FUNC __declspec(dllexport)
#else
#define JNIEXAMPLE_EXPORT_FUNC
#endif

namespace jniexample {
    class Callback {
        public:
        virtual void JNIEXAMPLE_EXPORT_FUNC run();
        virtual JNIEXAMPLE_EXPORT_FUNC ~Callback();
    };
    
    class Caller {
		private:
			Callback *_callback;
		public:
			virtual JNIEXAMPLE_EXPORT_FUNC ~Caller();
			void JNIEXAMPLE_EXPORT_FUNC delCallback();
			void JNIEXAMPLE_EXPORT_FUNC setCallback(Callback *cb);
			void JNIEXAMPLE_EXPORT_FUNC call();
	};
}

#endif
