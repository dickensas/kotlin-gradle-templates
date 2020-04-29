konan.libraries.push ({
  sayHello: function(arena, obj, idPtr, idLen, resultArena) {
	var text1 = toUTF16String(idPtr, idLen);
    console.log("stub: " + text1);
  },
  getMyModule: function(resultArena) {
	if(!window['mymodule']) window['mymodule'] = {}
    return toArena(resultArena, window['mymodule']);
  },
  getInnerHTML: function(arena, obj) {
    var result = kotlinObject(arena, obj).innerHTML;
    return result;
  },
  setInnerHTML: function(arena, obj, idPtr, idLen, resultArena) {
	  var text1 = toUTF16String(idPtr, idLen);
      kotlinObject(arena, obj).innerHTML = text1;
  }
})

