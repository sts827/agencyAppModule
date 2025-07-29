var separator= ' ~ ';
var _daterangepickerOptionSingleOnlyDate = {
		minYear: 1999,
		maxDate:moment().add(-1, 'hour'),
		autoUpdateInput: true,
		singleDatePicker:true,
		showDropdowns: true,
		showCustomRangeLabel:false,
		timePicker: false,
		timePicker24Hour: true,
		timePickerMinutes:false,
		timePickerSeconds:false,
		drops:"auto",
		locale: {
			format: 'YYYY-MM-DD',
			applyLabel: "적용",
			cancelLabel: "취소",
			daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
			monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
			separator:separator
		},
		ranges:null
	};
var _daterangepickerOptionSingleDateTime = {
		minYear: 1999,
		maxDate:moment().add(-1, 'hour'),
		autoUpdateInput: true,
		singleDatePicker:true,
		showDropdowns: true,
		showCustomRangeLabel:false,
		timePicker: true,
		timePicker24Hour: true,
		timePickerMinutes:true,
		timePickerSeconds:true,
		drops:"auto",
		locale: {
			format: 'YYYY-MM-DD HH:mm:ss',
			applyLabel: "적용",
			cancelLabel: "취소",
			daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
			monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
			separator:separator
		},
		ranges:null
	};
var _daterangepickerOptionRangeOnlyDate = {
		minYear: 1999,
		maxDate:moment().add(-1, 'hour'),
		autoUpdateInput: true,
		singleDatePicker:false,
		showDropdowns: true,
		showCustomRangeLabel:false,
		timePicker: false,
		timePicker24Hour: true,
		timePickerMinutes:false,
		timePickerSeconds:false,
		drops:"auto",
		locale: {
			format: 'YYYY-MM-DD',
			applyLabel: "적용",
			cancelLabel: "취소",
			daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
			monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
			separator:separator
		},
		ranges:null
	};
var _daterangepickerOptionRangeDateTime = {
		minYear: 1999,
		maxDate:moment().add(-1, 'hour'),
		autoUpdateInput: true,
		singleDatePicker:false,
		showDropdowns: true,
		showCustomRangeLabel:false,
		timePicker: true,
		timePicker24Hour: true,
		timePickerMinutes:true,
		timePickerSeconds:true,
		drops:"auto",
		locale: {
			format: 'YYYY-MM-DD HH:mm:ss',
			applyLabel: "적용",
			cancelLabel: "취소",
			daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
			monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
			separator:separator
		},
		ranges:null
	};

/*
 * 경로(servletpath)를 얻어온다.
 * ex)
 * http://localhost:8080/board/notice.cs?action=view&articeId=1
 * 결과 : /board/notice.cs
 */
function getServletPath() {
	var servletPath = document.URL;
	servletPath = servletPath.substr(servletPath.indexOf("/", servletPath.indexOf("//")+2));
	if(servletPath.indexOf("?") != -1) {
		servletPath = servletPath.substr(0, servletPath.indexOf("?"));
	}
	servletPath = servletPath.replace("#;","");
	return servletPath;
}

/*
 * 경로(servletpath)+쿼리스트링을 얻어온다.
 * ex)
 * http://localhost:8080/board/notice.cs?action=view&articeId=1
 * 결과 : /board/notice.cs?action=view&articeId=1
 */

function getServletReferrer() {
	var servletPath = document.referrer;
	servletPath = servletPath.substr(servletPath.indexOf("/", servletPath.indexOf("//")+2));
	if(servletPath.indexOf("?") != -1) {
		servletPath = servletPath.substr(0, servletPath.indexOf("?"));
	}
	servletPath = servletPath.replace("#;","");
	return servletPath;
}

function getServletPathQueryString() {
	var servletPath = document.URL;
	servletPath = servletPath.substr(servletPath.indexOf("/", servletPath.indexOf("//")+2));
	servletPath = servletPath.replace("#;","");
	return servletPath;
}

function getSearchCondition (form) {
	if (window.sessionStorage) {
		var query = "";
		var element;
		for(var i=0; i<form.length; i++){
			element = form.elements [form[i].name];
			if(element == null || element.value == '') continue;

			query += form[i].name + "=" + element.value + "&";
		}
		sessionStorage.setItem("searchCondition", query);
	}
}

function setSearchCondition (form) {
	if (window.sessionStorage) {
		var searchCondition = null;
		var searchCondition = sessionStorage.getItem("searchCondition");
		if(searchCondition != null || searchCondition == "object"){
//			console.log(searchCondition);
			var searchConditionList = searchCondition.split("&");
			for(idx in searchConditionList) {
				var val = searchConditionList[idx];
				var valArray = val.split("=");
				if(valArray[0] != '') {
					var element = form.elements[valArray[0]];
					element.value = valArray[1];
				}
			}
		}
	}
	sessionStorage.setItem("searchCondition", "");
}

//지정된 폼의 req class 항목들의 값 입력 여부 확인
function checkForm(formId){
	var result = true;
	$("#" + formId).find(".req:not([disabled='disabled']):not(label):not(.disabled)").each(function(){
		var tagName = $(this).prop('tagName');
		var dataName = $(this).data("name");

		if(dataName === undefined){
			console.error( `#${$(this).attr('id')} 'data-name' 누락!!!` );
		}

		if(tagName =='INPUT' && isEmptyString($(this).val())){ //정규식 체크하기
			alert(dataName+" 입력하세요!");
			result = false;
			$(this).focus();
			return false;
		} else if(tagName =='SELECT' && $('option:selected',$(this)).index() === 0){
			alert(dataName+" 선택하세요!");
			result = false;
			$(this).focus();
			return false;
		} else if(tagName =='TEXTAREA' && isEmptyString($(this).val())){
			alert(dataName+"을 입력하세요!");
			result = false;
			$(this).focus();
			return false;
		}
	});

	$("#" + formId).find("input:invalid").each(function(){
		alert($(this).data("name")+" 확인하십시오!");
		result = false;
		$(this).focus();
		console.log('case 2', result);
		return false;
	});


	return result;
}

function replaceNewline(str){
	return str.replaceAll(/(?:\r\n|\r|\n)/g, '<br />');
}

// 빈문자열 검사
function isEmptyString (value){
	return value == null || (/^\s*$/).test (value) ;
}

// 아이디를 사용한 빈문자열 검사
function isEmptyStringId (id) {
	return isEmptyString ($("#" + id).val ());
}

//Thread.sleep
function sleep(num){	//[1/1000초]
	var now = new Date();
	var stop = now.getTime() + num;
	while(true){
		now = new Date();
		if(now.getTime() > stop)return;
	}
}

//시작일과 종료일이 오늘안에 존재하는지 확인
function checkTodayIn(startDtm, endDtm) {
	var today = new Date();

	var startDate = new Date(startDtm);
	var endDate = new Date(endDtm);

	if (today.getTime() > startDate.getTime() && today.getTime() < endDate.getTime()) {
		return true;
	}else {
		return false;
	}
}

//쿠키정보를 조회한다.
function getCookie( name ) {
	var nameOfCookie = name + "=";
	var x = 0;
	while ( x <= document.cookie.length ) {
		var y = (x+nameOfCookie.length);
		if ( document.cookie.substring( x, y ) == nameOfCookie ) {
			if ( (endOfCookie=document.cookie.indexOf( ";", y )) == -1 )
				endOfCookie = document.cookie.length;

			return unescape( document.cookie.substring( y, endOfCookie ) );
		}
		x = document.cookie.indexOf( " ", x ) + 1;
		if ( x == 0 )
		break;
	}
	return "";
}

//쿠키정보를 저장한다.
function setCookie( name, value, expiredays ) {
	var todayDate = new Date();
	todayDate.setDate( todayDate.getDate() + expiredays );
	document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + todayDate.toGMTString() + ";"
}

Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};
String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

/**
 * 3자리마다 콤마(,)를 찍는다.
 * @param x
 * @returns
 */
function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

/**
 * 좌측문자열채우기
 * @params
 *  - str : 원 문자열
 *  - padLen : 최대 채우고자 하는 길이
 *  - padStr : 채우고자하는 문자(char)
 */
function lpad(str, padLen, padStr) {
    if (padStr.length > padLen) {
        console.log("오류 : 채우고자 하는 문자열이 요청 길이보다 큽니다");
        return str;
    }
    str += ""; // 문자로
    padStr += ""; // 문자로
    while (str.length < padLen)
        str = padStr + str;
    str = str.length >= padLen ? str.substring(0, padLen) : str;
    return str;
}

/**
 * 우측문자열채우기
 * @params
 *  - str : 원 문자열
 *  - padLen : 최대 채우고자 하는 길이
 *  - padStr : 채우고자하는 문자(char)
 */
function rpad(str, padLen, padStr) {
    if (padStr.length > padLen) {
        console.log("오류 : 채우고자 하는 문자열이 요청 길이보다 큽니다");
        return str + "";
    }
    str += ""; // 문자로
    padStr += ""; // 문자로
    while (str.length < padLen)
        str += padStr;
    str = str.length >= padLen ? str.substring(0, padLen) : str;
    return str;
}


//전화번호 포맷팅
function checkTelNum(telNum){

	if(telNum != null){
		return telNum.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3");
	}else{
		return "";
	}
}

function searchReset(){
	var targetClass = 'search-body';
	var strParams = "";

	$('input, select','.'+targetClass).each(function(index, element){
//		console.log(index, element);
		var el = $(element);

		var target = (typeof el.attr('id') != 'undefined'? el.attr('id') : el.attr('name'));
		var attr = (typeof el.attr('id') == 'undefined'? 'n' : 'i');
		var type = "";
		var data = el.val();
		var gubn = "";

		if(el.is('input')){
			type = 'ip';
			$(this).val("");
		} else if(el.is('select')) {
			type = 'sl';
			$('option',this).eq(0).prop('selected',true);
		}

//		if(data != ""){
//			if(strParams.length >= 1) gubn="¿";
//			strParams += gubn+attr+'ˇ'+type+'ˇ'+target +'ˇ'+data;
//		}
	});
	return strParams;
}

let searchParams;
let parmas;

$(document).ready(function() {
//	console.log('initParam');
	initParam();
});

function initParam(){
	searchParams = new URLSearchParams(window.location.search);
	if(searchParams.has('params')){
		parmas = searchParams.get('params');
    	setParams( parmas );
	}
}

function getParams(){
	var targetid = 'search-body';
	var strParams = "";

	$('input, select','.'+targetid).each(function(index, element){
//		console.log(index, element);
		var el = $(element);

		var target = (typeof el.attr('id') != 'undefined'? el.attr('id') : el.attr('name'));
		var attr = (typeof el.attr('id') == 'undefined'? 'n' : 'i');
		var type = "";
		var data = el.val();
		var gubn = "";

		if(el.is('input')){
			type = 'ip';
		} else if(el.is('select')) {
			type = 'sl';
		}

		if(data != ""){
			if(strParams.length >= 1) gubn="¿";
			strParams += gubn+attr+'ˇ'+type+'ˇ'+target +'ˇ'+data;
		}
	});
	return strParams;
}

function setParams(params){
	if(params.length >= 1){
		var array1 = params.split('¿');

		for(var i=0;i<array1.length;i++){
			var array2 = array1[i].split('ˇ');

			if(array2[0] == 'i'){
				$('#'+array2[2]).val(array2[3]);
			} else {
				console.log(array2);
				if(array2[1] == 'ip'){
					$('input[name='+array2[2]+']').val(array2[3]);
				} else if(array2[1] == 'sl'){
					$('select[name='+array2[2]+']').val(array2[3]).prop('selected',true);
				} else if(array2[1] == 'bt'){
					$(`button[${array2[2]}='${array2[3]}']`).trigger('click');
				}
			}
		}
	}
}

//두날짜 차이를 일로 받아온다.
function getGapDt(dt1, dt2) {
	var stDt = new Date(dt1).format("yyyy-MM-dd");
	var edDt = new Date(dt2).format("yyyy-MM-dd");
	return (new Date(edDt).getTime() - new Date(stDt).getTime())/ 1000 / 60 / 60 / 24;
}


//$(".content input").keypress(function(event) {
//	if (event.which == 13) {
//		event.preventDefault();
//		datatable.ajax.reload();
//	}
//});

function objectifyForm(formArray) {//serialize data function
	console.log(formArray);
	var returnArray = {};
	for (var i = 0; i < formArray.length; i++){

		returnArray[formArray[i]['name']] = formArray[i]['value'];
		console.log(formArray[i]['name']);
	}
	return returnArray;
}

/*
$.ajaxSetup({
	beforeSend: function(xhr) {
		xhr.setRequestHeader("AJAX", true);
	},
	error: function(xhr, status, err) {
		console.log("ajaxSetup : " + xhr.status);
		var servletPath = document.URL;
		servletPath = servletPath.substr(servletPath.indexOf("/", servletPath.indexOf("//")+2));
		if(servletPath.indexOf("?") != -1) {
			servletPath = servletPath.substr(0, servletPath.indexOf("?"));
		}
		servletPath = servletPath.substr(0,7);

		if(xhr.status == 401) {
			if("/mngr/" == servletPath){
				alert("인증에 실패 했습니다. 로그인 페이지로 이동합니다.");
				location.href = '/login/loginForm.do';
			}else{
				alert("인증에 실패 했습니다. 로그인 페이지로 이동합니다.");
				location.href = '/login/loginForm.do';
			}

//			location.href = ""
		}else if (xhr.status == 403) {
			if("/mngr/" == servletPath){
				alert("세션이 만료가 되었습니다. 로그인 페이지로 이동합니다.");
				location.href = '/login/loginForm.do';
			}else{
				alert("세션이 만료가 되었습니다. 로그인 페이지로 이동합니다.");
				location.href = '/login/loginForm.do';
			}
		}
	}
});
*/
(function($){
    $.fn.serializeObject = function(){

        var self = this,
            json = {},
            push_counters = {},
            patterns = {
                "validate": /^[a-zA-Z][a-zA-Z0-9_]*(?:\[(?:\d*|[a-zA-Z0-9_]+)\])*$/,
                "key":      /[a-zA-Z0-9_]+|(?=\[\])/g,
                "push":     /^$/,
                "fixed":    /^\d+$/,
                "named":    /^[a-zA-Z0-9_]+$/
            };


        this.build = function(base, key, value){
            base[key] = value;
            return base;
        };

        this.push_counter = function(key){
            if(push_counters[key] === undefined){
                push_counters[key] = 0;
            }
            return push_counters[key]++;
        };

        $.each($(this).serializeArray(), function(){

            // skip invalid keys
            if(!patterns.validate.test(this.name)){
                return;
            }

            var k,
                keys = this.name.match(patterns.key),
                merge = this.value,
                reverse_key = this.name;

            while((k = keys.pop()) !== undefined){

                // adjust reverse_key
                reverse_key = reverse_key.replace(new RegExp("\\[" + k + "\\]$"), '');

                // push
                if(k.match(patterns.push)){
                    merge = self.build([], self.push_counter(reverse_key), merge);
                }

                // fixed
                else if(k.match(patterns.fixed)){
                    merge = self.build([], k, merge);
                }

                // named
                else if(k.match(patterns.named)){
                    merge = self.build({}, k, merge);
                }
            }

            json = $.extend(true, json, merge);
        });

        return json;
    };
})(jQuery);



//pc, mobile 구분(가이드를 위한 샘플 함수입니다.)
function checkPlatform(ua) {
	if(ua === undefined) {
		ua = window.navigator.userAgent;
	}

	ua = ua.toLowerCase();
	var platform = {};
	var matched = {};
	var userPlatform = "pc";
	var platform_match = /(ipad)/.exec(ua) || /(ipod)/.exec(ua)
		|| /(windows phone)/.exec(ua) || /(iphone)/.exec(ua)
		|| /(kindle)/.exec(ua) || /(silk)/.exec(ua) || /(android)/.exec(ua)
		|| /(win)/.exec(ua) || /(mac)/.exec(ua) || /(linux)/.exec(ua)
		|| /(cros)/.exec(ua) || /(playbook)/.exec(ua)
		|| /(bb)/.exec(ua) || /(blackberry)/.exec(ua)
		|| [];

	matched.platform = platform_match[0] || "";

	if(matched.platform) {
		platform[matched.platform] = true;
	}

	if(platform.android || platform.bb || platform.blackberry
			|| platform.ipad || platform.iphone
			|| platform.ipod || platform.kindle
			|| platform.playbook || platform.silk
			|| platform["windows phone"]) {
		userPlatform = "mobile";
	}

	if(platform.cros || platform.mac || platform.linux || platform.win) {
		userPlatform = "pc";
	}

	return userPlatform;
}


function platformCheck(){
	if(checkPlatform(window.navigator.userAgent) == "mobile"){//모바일 결제창 진입
		return 'M';
	}else{//PC 결제창 진입
		return 'P';
	}
}


function formValidCheck(item, type, classname, regexType){
	if($(item) === undefined) return false;
	let result = true;
	switch (type){
		case 'input':
			if($(item).val() === undefined || $(item).val() === null || $(item).val().trim() === ''){
				result = false;
			}else {
				if(regexType !== null && regexType.trim() !== ''){
					switch (regexType){
						case 'email':
							if($(item).val().match( /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/ ) === null) {
								result = false;
							}
							break;
						case 'mobile':
							if($(item).val().match( /^0[0-9]{2}-?[0-9]{3,4}-?[0-9]{4}$/ ) === null) {
								result = false;
							}
							break;
						case 'tel':
							if($(item).val().match( /^0[0-9]{1,2}-?[0-9]{3,4}-?[0-9]{4}$/ ) === null) {
								result = false;
							}
							break;
						case 'count':
							if($(item).val().match( /^[0-9]+$/ ) === null) {
								result = false;
							}
							break;
						case 'medium-pass':
							if($(item).val().match(/^.*(?=^.{8,16}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/) === null) {
								result = false;
							}
							break;
						case 'strong-pass':
							if($(item).val().match(/^.*(?=^.{8,30}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/) === null) {
								result = false;
							}
							break;
						case 'hex-color':
							if($(item).val().match(/^#[0-9a-fA-F]{6}$/) === null) {
								result = false;
							}
							break;
						case 'url':
							if($(item).val().match(/^(\/[a-zA-Z0-9\-]+)(\/[a-zA-Z0-9\-]+)?$/g) === null) {
								// /[^\"'\s()]+/i
								result = false;
							}
							break;
						case 'text':
						default: break;
					}
				}
			}
			break;
		case 'select':
		default:
			if($(item).val() === undefined || $(item).val() === null){
				result = false;
			}
			break;
	}

	if(result){
		$(item).removeClass(classname);
	}else{
		$(item).addClass(classname);
		$(item).focus();
	}

	return result;
}

loadMakeUpHtmlInput = function(type, upperCode, value, name, classappend, labelYn){
	loadMakeUpHtmlInput(type, upperCode, value, name, classappend, labelYn);
}

loadMakeUpHtmlInput = function(type, upperCode, value, name, classappend, labelYn, labelClass=undefined){
	let _htmlData = "";
	var divYn=undefined, divClass=undefined;
	if( type !== undefined ){
		let list = loadCodeList(upperCode);
//		console.log(list);

		if(type == 'radio' || type == 'check'){
			for( idx in list){
				var data = list[idx];
				var itemId = name+"-"+data.code; /*라디오 ID값 중복처리 안되게*/
				var inputType = (type == 'radio' ? 'radio' : 'checkbox');

				if(divYn){
					                            _htmlData += `<div `;

					if(name != undefined)       _htmlData += `class='${divClass}'>`;
				}
				if(labelYn){
					                            _htmlData += `<label `;

					if(name != undefined)       _htmlData += `class='${labelClass}' `;
					                            _htmlData += `for='${itemId}'>`;
				}
				_htmlData += `<input type='${inputType}' `;
				if(name != undefined)           _htmlData += `name='${name}' id='${itemId}' `;
				if(classappend != undefined)    _htmlData += `class='${classappend}' `;
				                                _htmlData += `value='${data.code}' data-upperCode='${data.upperCode}' `;
				if(data.codeAcronym !== undefined)
				                                _htmlData += `data-acronym='${data.codeAcronym}' `;
				if(data.code === value)         _htmlData += `checked='checked' `;

				_htmlData += `>`;
				                                _htmlData += data.name;
				if(labelYn)                     _htmlData += `</label> `;
				if(divYn)                       _htmlData += `</div> `;

				_htmlData += `\r\n`;
			}
		} else if(type == 'select'){
			/**/
		}
	}

	//console.log( _htmlData );

	return _htmlData;
}

loadMakeUpHtmlSelect = function( upperCode, value, id, name, classappend, headerYn = false, headerTitle = undefined, headerValue = undefined, event = undefined){
	let _htmlData = "";
	let list = loadCodeList(upperCode);
//		console.log(list);

	if(headerYn){
	                            	_htmlData += `<select `;
	if(id != undefined)         	_htmlData += `id='${id}' `;
	if(name != undefined)       	_htmlData += `name='${name}' `;
	if(classappend != undefined)	_htmlData += `class='${classappend}' `;
	if(event != undefined)      	_htmlData += `${event} `;
	                            	_htmlData += `>\r\n`;
		                        	_htmlData += `<option value='${headerValue}' >` + headerTitle; + `</option>` + `\r\n`;
	}
	for( idx in list){
		var data = list[idx];
		                        	_htmlData += `<option value='${data.code}' data-upperCode='${data.upperCode}' `;
		if(data.codeAcronym !== undefined)
	                            	_htmlData += `data-acronym='${data.codeAcronym}' `;
		if(data.code === value) 	_htmlData += `selected`;
	                            	_htmlData += `>`;
	                            	_htmlData += data.name;
	                            	_htmlData += `</option>`;
		                        	_htmlData += `\r\n`;
	}
	if(headerYn){
	                            	_htmlData += `</select>`;
	                            	_htmlData += `\r\n`;
	}

	//console.log( _htmlData );

	return _htmlData;
}

/* 윤재웅
 * 공통코드정보를 가져온다.
 */

loadCodeList = function(code){
	let list;
	$.ajax({
        url: '/code-list',
        type: "GET",
        data:{'upperCode':code, 'useYn':'Y'},
        dataType : "json",
        async:false,
        success: function (res) {
            if ("success" == res.result) {
				list = res.data;
            } else {
                alert(res.message);
            }
        }
    });

    return list;
}

const _datetimepickerKor = {
	prevText: '이전 달',
	nextText: '다음 달',
	months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	dayNames: ['일', '월', '화', '수', '목', '금', '토'],
	dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
	dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
	showMonthAfterYear: true,
	yearSuffix: '년',
}

/* 윤재웅
 * 특정문자열 개수를 센다.
 */

fnCmmnFindStrLength = function(text, findStr){
	var count = 0;
	var pos = text.indexOf(findStr); //pos는 0의 값을 가집니다.

	while (pos !== -1) {
		count++;
		pos = text.indexOf(findStr, pos + 1); //
	}

	return count;
}
/*
//뒤에서부터 n자리마다 원하는 문자 넣기
insertSpecialCharReverse = function (str, n, char){
	const regex = new RegExp(`(.)(?=(.{${n}})+$)`, "g");
	return str.toString().replace(regex, `$1${char}`);
}
*/