function toUint8Array(value) {
   var buffer = new Uint8Array(value.length);
   for(var i=0;i<value.length;i++)
     buffer[i] = value.charCodeAt(i)
   return buffer;
}

konan.libraries.push ({
  sayHello: function(arena, obj, idPtr, idLen, resultArena) {
	var text1 = toUTF16String(idPtr, idLen);
    console.log("stub: " + text1);
  },
  wasmReady: function(arena, obj, resultArena) {
	  console.log("wasmReady function triggered")
	  var event = new Event('wasmReady');
	  dispatchEvent(event);
  },
  getMyModule: function(resultArena) {
	if(!window['mymodule']) window['mymodule'] = {}
    return toArena(resultArena, window['mymodule']);
  },
  getInnerHTML: function(arena, obj, resultArena) {
	var result = kotlinObject(arena, obj).innerHTML;
	return toArena(resultArena, toUint8Array(result));
  },
  setInnerHTML: function(arena, obj, idPtr, idLen, resultArena) {
	var text1 = toUTF16String(idPtr, idLen);
    kotlinObject(arena, obj).innerHTML = text1;
  },
  getId: function(arena, obj, resultArena) {
    var result = kotlinObject(arena, obj).getAttribute("id");
    return toArena(resultArena, toUint8Array(result));
  },
  getValue: function(arena, obj, resultArena) {
    var result = kotlinObject(arena, obj).value;
    return toArena(resultArena, toUint8Array(result));
  },
  setValue: function(arena, obj, idPtr, idLen, resultArena) {
	var text1 = toUTF16String(idPtr, idLen);
	kotlinObject(arena, obj).value = text1;
  },
  getTarget: function(arena, obj, resultArena) {
    var result = kotlinObject(arena, obj).srcElement;
    return toArena(resultArena, result);
  },
  getChar: function (arena, obj, index) {
    var arena = konan_dependencies.env.arenas.get(arena);
    var value = arena[obj][index];
    return value;
  }
})

