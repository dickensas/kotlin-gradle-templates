package plot

import ruby.*
import kotlinx.cinterop.*

var rubyscript =
"""
#!/mingw64/bin/ruby
# I'm a ruby script!
print "Hello, world! - ruby\n"
def testfun(a)
   puts "The first parameter from kotlin is #{a}\n"
end
"""

fun main() {
    ruby_init()
    rb_eval_string(rubyscript)
    rb_funcall(Qnil, rb_intern("testfun"), 1, rb_str_new_cstr("dickens"))
    ruby_cleanup(0)
}