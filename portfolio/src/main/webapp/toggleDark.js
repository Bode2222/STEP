
var mode;
function ToggleMode(){
    if(mode == 1){
        mode = 0;
    }
    else{
        mode = 1;
    }

    if(mode == 1){
        //toggle light mode
        document.getElementById("PageStyle").setAttribute('href', "lightStyle.css")
    }
    else{
        //toggle dark mode
        document.getElementById("PageStyle").setAttribute('href', "style.css")
    }
}