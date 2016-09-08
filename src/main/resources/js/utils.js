window.getElementsByClassName = function (className) {// Get a list of all elements in the document
    // For IE    
    if (document.all) {
        var allElements = document.all;
    } else {
        var allElements = document.getElementsByTagName("*");
    }

    // Empty placeholder to put in the found elements with the class name
    var foundElements = [];

    for (var i = 0, ii = allElements.length; i < ii; i++) {
        if (allElements[i].className == className) {
            foundElements[foundElements.length] = allElements[i];
        }
    }

    return foundElements;
};


/*refedined function to open ShopViewImportViewDialog as modal window*/

window.onready = function(handler) {
    // window is loaded already - just run the handler
    if(document && document.readyState==="complete") return handler();

    // non-IE: DOMContentLoaded event
    if(window.addEventListener) window.addEventListener("DOMContentLoaded",handler,false);

    // IE top frame: use scroll hack
    else if(window.attachEvent && window==window.top) { if(_readyQueue.push(handler)==1) window.readyIEtop(); }

    // IE frame: use onload
    else if(window.attachEvent) window.attachEvent("onload",handler);
};

// IE stuff
var _readyQueue = [];
window.readyIEtop = function() {
    try {
        document.documentElement.doScroll("left");
        var fn; while((fn=_readyQueue.shift())!=undefined) fn();
    }
    catch(err) { setTimeout(window.readyIEtop,50); }
};

onready(function(){
    window.openShopViewImportViewWin= function (inURL,inType,inLeft){
        var intX
        var intY
        var intWidth
        var intHeight
        var intTop = 40
//////Shop View Page////////////
        if (inType==1){
            intWidth=1050
            intHeight=800
        }
//////Import View Page////////////
        else if (inType==2){
            intWidth=1024
            intHeight=500
        }
        else{
            intWidth=600
            intHeight=500
        }

        if ( navigator.appName.indexOf("Netscape") > -1 ){
            intX = screen.availHeight / 2 - ( intHeight / 2) ;
            intY = screen.availWidth / 2 - ( intWidth / 2 );
        } else {
            intX = window.screenTop + intTop;
            intY = window.screenLeft + inLeft;
        }

        var strFeatures = 'width=' + intWidth + ',height=' + intHeight + ',top=' + intX + ',left=' + intY +',statusbar=no,menubar=no,toolbar=no,resizable=yes,scrollbars=no,hotkeys=no';

        if(ShopViewImportViewDialog && !ShopViewImportViewDialog.closed){
            ShopViewImportViewDialog = window.showModalDialog( inURL,'ShopViewImportViewDialog', strFeatures);
            ShopViewImportViewDialog.resizeTo(intWidth,intHeight);
            //ShopViewImportViewDialog.focus();
        } else {
            ShopViewImportViewDialog = window.showModalDialog( inURL,'ShopViewImportViewDialog', strFeatures);
            //ShopViewImportViewDialog.focus();
        }
    };

    window.populateRichTextEditorWithText = function (iframeId, text) {
        document.getElementById(iframeId).contentWindow.document.getElementsByTagName('body')[0].innerHTML = text;
    };
});


