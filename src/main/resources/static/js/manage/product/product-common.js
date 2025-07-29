const currency = 'USD';
//const currency = 'KRW';

function checkInputNum(event) {
    let regExp;
	switch(currency){
		case 'KRW':regExp = /[^0-9]/g;break;
		case 'USD':regExp =  /^[^0-9\.]/g;break;
	}
    //const regExp = /[^0-9]/g;
    const ele = event.target;
    var targetTxt = ele.value;
    var replaceTxt;

    if (regExp.test(targetTxt)) {
		replaceTxt = targetTxt.replace(regExp, '');
        ele.value = replaceTxt;
    	//console.log(targetTxt, '==>>', replaceTxt);
    }
}

//뒤에서부터 n자리마다 원하는 문자 넣기
function insertSpecialCharReverse(str, n, char){
    let regex ;
    let replace;

	switch(currency){
		case 'KRW':
			regex = new RegExp(`(.)(?=(.{${n}})+$)`, "g");
			replace = `$1${char}`;
		break;
		case 'USD':
			regex = new RegExp(`\d{1,3}(?=(\d{3})+$)`, "g");
			//regex = /\d{1,3}(?=(\d{3})+\b(?![^/.\d]))/g
			replace = `$&${char}`;
		break;
	}

	console.log(currency, regex, str.toString().replace(regex, replace) );

	if(regex !== undefined){
    	return str.toString().replace(regex, replace);
    } else {
    	return str;
    }
}

//상품 숫자 형식용 input 감지 함수들
function checkInputNumValidation(event) {
    checkInputNum(event);

    event.target.value = insertSpecialCharReverse(event.target.value, 3, ',')
}

function formSerialize(obj) {
    var formData = new FormData(obj);
    var jsonObject = {};

	$('input[type=checkbox]:not(:checked)', $('#basic-info-add-form')).each(function(i,e){
		formData.set( $(e).attr('name'), 'N');
	});

    formData.forEach(function(value, key) {
        if (jsonObject[key] === undefined && key != "images") {
            jsonObject[key] = value;
        }else if(key =="images"){
            jsonObject[key] = [value];
        } else {
            if (!Array.isArray(jsonObject[key])) {
                jsonObject[key] = [jsonObject[key]];
            }
            jsonObject[key].push(value);
        }
    });

    jsonObject.productTourId = $("#product-tour-id").val();

    var jsonString = JSON.stringify(jsonObject);
    //console.log("jsonObject: ",jsonObject);
    return jsonString
}