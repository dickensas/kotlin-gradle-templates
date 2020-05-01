function fromUTF16String(string, pointer, size) {
    var j=0;
    for (let i = pointer; i < pointer + size; i += 2) {
        if(j<string.length)
            heap[i] = string.charCodeAt(j);
        else
            heap[i] = 0
        heap[i + 1] = 0
        j++;
    }
}

konan.libraries.push ({
  domEval(arena, obj, idPtr, idLen, rdPtr, rdLen, resultArena) {
     var js = toUTF16String(idPtr, idLen);
     var result = eval(js);
     fromUTF16String(result, rdPtr, rdLen)
  },
  getMyModule: function(resultArena) {
    if(!window['mymodule']) window['mymodule'] = {}
    return toArena(resultArena, window['mymodule']);
  }
})

