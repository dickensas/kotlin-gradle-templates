function f1()
    io.write("f1 Lua function called from kotlin.\n")
end

function square(n)
    io.write("square Lua called from Kotlin, with parameter=")
    io.write(tostring(n))
    n = n * n
    return(n)
end