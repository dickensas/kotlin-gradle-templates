package plot

import kotlinx.cinterop.*
import gtk4.*

fun realize_callback(widget:CPointer<GtkWidget>?) = memScoped {
    println("realize_callback")
}

fun render_callback(glarea:CPointer<GtkDrawingArea>?, cr:CPointer<cairo_t>?) = memScoped {
    println("render_callback")
    var csurface = cairo_get_target(cr)
    var region = gdk_cairo_region_create_from_surface(csurface)
    var rbga = allocArray<GdkRGBA>(1).apply {
        this[0].red = 1.0
        this[0].green = 0.0
        this[0].blue = 0.0
        this[0].alpha = 1.0
    }
    
    var grect = graphene_rect_alloc()
    grect = graphene_rect_init(grect, 50.0f, 50.0f, 300.0f, 300.0f)
    var gsknode = gsk_color_node_new(rbga, grect)
    
    gsk_render_node_draw(gsknode, cr)
    
    graphene_rect_free(grect)
    cairo_region_destroy(region)
}

fun main() {
    
    gtk_init()
        
    var builder = gtk_builder_new()
    gtk_builder_add_from_file (builder, "glade/window_main.glade", null)
    
    var window = gtk_builder_get_object(builder, "window_main")!!.reinterpret<GtkWidget>()
    gtk_window_set_position(window.reinterpret<GtkWindow>(), GtkWindowPosition.GTK_WIN_POS_CENTER)
    gtk_window_set_default_size(window.reinterpret<GtkWindow>(), 400, 400)
    gtk_window_set_title(window.reinterpret<GtkWindow>(), "Demo Window")
    
    gtk_window_set_resizable(window.reinterpret<GtkWindow>(), 0)
    gtk_window_set_decorated(window.reinterpret<GtkWindow>(), 0)
    gtk_window_set_mnemonics_visible(window.reinterpret<GtkWindow>(), 0)
    
    val box = gtk_drawing_area_new()
    gtk_widget_set_size_request (box, 400, 400);
    gtk_container_add (window.reinterpret(), box)
    
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
    
    g_signal_connect_data(
        box!!.reinterpret(), 
        "realize", 
        staticCFunction {  glarea:CPointer<GtkWidget>?
            -> realize_callback(glarea)
        }.reinterpret(), 
        null, 
        null, 
        0u
    )
    
    gtk_drawing_area_set_draw_func(
        box.reinterpret(),
        staticCFunction {  glarea:CPointer<GtkDrawingArea>?, cr:CPointer<cairo_t>?
            -> render_callback(glarea,cr)
        }.reinterpret(),
        null, 
        null
    )
    
    gtk_widget_show(window)
    gtk_widget_show(box.reinterpret<GtkWidget>())
    gtk_main()
}