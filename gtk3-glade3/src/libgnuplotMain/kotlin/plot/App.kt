package plot

import kotlinx.cinterop.*
import gtk3.*


fun main() = memScoped {
    var argc = alloc<IntVar>().apply { this.value = 0 }
    gtk_init(argc.ptr, null)
    
    var builder = gtk_builder_new()
    gtk_builder_add_from_file (builder, "glade/window_main.glade", null)
    
    var window = gtk_builder_get_object(builder, "window_main")!!.reinterpret<GtkWidget>()
    
    g_signal_connect_data(
        window.reinterpret(), 
        "destroy", 
        staticCFunction {  
            -> gtk_main_quit()
        }.reinterpret(), 
        null, 
        null, 
        0u
    )
    
    g_object_unref(builder)

    gtk_widget_show(window)
    gtk_main()
}


