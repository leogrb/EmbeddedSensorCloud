var caloperator=0;
var operatorcount=0;
var result;

function displayNum(num){
    if(num=='+'){
        caloperator='+';
    } 
    if(num=='-'){
        caloperator='-';
    }     
    if(num=='*'){
        caloperator='*';
    }     
    if(num=='/'){
        caloperator='/';
    }         
    textform.textinput.value+=num;
    
}
function split(){
    if(caloperator=='+'){
    var plus=textform.textinput.value.split("+");  
    var result=parseInt(plus[0])+parseInt(plus[1]);
    textform.textinput.value=result;
    }
    if(caloperator=='-'){
        var plus=textform.textinput.value.split("-");
        var result=parseInt(plus[0])-parseInt(plus[1]);
        textform.textinput.value=result;
    }
    if(caloperator=='/'){
        var plus=textform.textinput.value.split("/");
        var result=parseInt(plus[0])/parseInt(plus[1]);
        textform.textinput.value=result;
    }
    if(caloperator=='*'){
        var plus=textform.textinput.value.split("*");
        var result=parseInt(plus[0])*parseInt(plus[1]);
        textform.textinput.value=result;
    }
}
 
