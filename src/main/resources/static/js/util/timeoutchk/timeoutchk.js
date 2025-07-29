var timeOutSec; //초단위로 환산
var timeOutChecker = null;

function timeoutchkClear() {
//    timeOutSec = 2*3600;
    timeOutSec = 30*60;
    console.log(timeOutSec +"초 후 자동로그아웃!!");
}
 
Lpad = function(str, len) {
    str = str + "";
    while (str.length < len) {
        str = "0" + str;
    }
    return str;
}
 
timeoutchk = function() {
//    var timer = document.getElementById("timer");
    rHour = parseInt(timeOutSec / 3600);
    rHour = rHour % 60;
 
    rMinute = parseInt(timeOutSec / 60);
    rMinute = rMinute % 60;
 
    rSecond = timeOutSec % 60;
    
//    if(timeOutSec / 60 == 0) 
//    	console.log(rMinute +"초 후 자동로그아웃!!");
//    console.log(timeOutSec +"초 후 자동로그아웃!!");
    
    
    if (timeOutSec > 0) {
//    	$('#timer').html("&nbsp;" + Lpad(rHour, 2) + "시간 " + Lpad(rMinute, 2) + "분 " + Lpad(rSecond, 2) + "초 ");
    	$('#timer').html(Lpad(rMinute, 2) + "분 " + Lpad(rSecond, 2) + "초 ");
//    	timer.innerHTML = "&nbsp;" + Lpad(rHour, 2) + "시간 " + Lpad(rMinute, 2) + "분 " + Lpad(rSecond, 2) + "초 ";
        timeOutSec--;
        timeOutChecker = setTimeout("timeoutchk()", 1000); // 1초 간격으로 체크
    } else {
    	console.log("go logout!!!");
        logoutUser();
//    	$('.HDLogBtn').trigger('click');
    }
}
 
function logoutUser() {
    clearTimeout(timeOutChecker);
//	console.log("logoutUser!!!");
    var xhr = initAjax();
    xhr.open("POST", "/public/logout.do", false);
    xhr.send();
    location.reload();
}
 
function initAjax() { // 브라우저에 따른 AjaxObject 인스턴스 분기 처리
    var xmlhttp;
    if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return xmlhttp;
}

$(window).bind('load',function(){
    timeoutchkClear();
    timeoutchk();
    
    $("#refleshTimer").click(function(){
    	timeoutchkClear();
    });
});



//출처: http://devsh.tistory.com/entry/자바스크립트로-구현한-실시간-타임아웃-기능 [날샘 코딩]