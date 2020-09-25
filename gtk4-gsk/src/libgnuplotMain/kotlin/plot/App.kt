package plot

import kotlinx.cinterop.*
import gtk4.*

var app:CPointer<GtkApplication>? = null

fun realize_callback(widget:CPointer<GtkWidget>?) = memScoped {
    println("realize_callback")
}

fun render_callback(glarea:CPointer<GtkDrawingArea>?, cr:CPointer<cairo_t>?) = memScoped {
    println("render_callback")
    var csurface = cairo_get_target(cr)
    var region = gdk_cairo_region_create_from_surface(csurface)
    var rbga = allocArray<GdkRGBA>(1).apply {
        this[0].red = 1.0f
        this[0].green = 0.0f
        this[0].blue = 0.0f
        this[0].alpha = 1.0f
    }
    
    var grect = graphene_rect_alloc()
    grect = graphene_rect_init(grect, 50.0f, 50.0f, 300.0f, 300.0f)
    var gsknode = gsk_color_node_new(rbga, grect)
    
    gsk_render_node_draw(gsknode, cr)
    
    graphene_rect_free(grect)
    cairo_region_destroy(region)
}

fun activate_callback(app:CPointer<GtkApplication>?) = memScoped {
    println("activate")
	
    var window = gtk_application_window_new (app);
    gtk_window_set_default_size(window!!.reinterpret<GtkWindow>(), 400, 400)
    gtk_window_set_title(window.reinterpret<GtkWindow>(), "Demo Window")
    
    gtk_window_set_resizable(window.reinterpret<GtkWindow>(), 0)
    gtk_window_set_decorated(window.reinterpret<GtkWindow>(), 0)
    gtk_window_set_mnemonics_visible(window.reinterpret<GtkWindow>(), 0)

    val box = gtk_drawing_area_new()
    gtk_widget_set_size_request (box, 400, 400);
    gtk_window_set_child (window.reinterpret(), box)
    
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
	
	gtk_widget_show (window);
}

fun main() = memScoped {
	app = gtk_application_new ("org.gtk.example", G_APPLICATION_FLAGS_NONE)
	
	g_signal_connect_data(
        app!!.reinterpret(), 
        "activate", 
        staticCFunction {  app:CPointer<GtkApplication>?
            -> activate_callback(app)
        }.reinterpret(), 
        null, 
        null, 
        0u
    )
	
	run_app(app, 0, null)
    g_object_unref (app)
    
}