var filesArr = new Array();
var fileNo = 0;
var _type = 'attach';

function initUploadFile(type){
	if(typeof type !== 'undefined' ){
		_type = type;
	}
	console.log(_type);
}

function uploadFile(tagId) {
	$(`#${tagId}`).click();
}
/* 첨부파일 추가 */
function addFile(obj){
    var maxFileCnt = 5;   // 첨부파일 최대 개수
    var attFileCnt = document.querySelectorAll('.filebox').length;    // 기존 추가된 첨부파일 개수
    var remainFileCnt = maxFileCnt - attFileCnt;    // 추가로 첨부가능한 개수
    var curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수

	if(typeof(attchFileSize) !== 'undefined'){
		if(attchFileSize == 0){
			maxFileCnt = 99999;
		} else {
			maxFileCnt = attchFileSize;
		}
	}

    // 첨부파일 개수 확인
    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
        return;
    }

    for (const file of obj.files) {
        // 첨부파일 검증
        if (validation(file)) {
            // 파일 배열에 담기
            var reader = new FileReader();
            reader.onload = function () {
                filesArr.push(file);
                filesArr[filesArr.length-1].fileId=`N${fileNo}`;
            };
            reader.readAsDataURL(file);

//            if( fileNo == 0  )
//            	filesArr[fileNo].is_thumnail = true;
//            else
//            	filesArr[fileNo].is_thumnail = false;

            // 목록 추가
            let htmlData = '';

            switch(_type){
				case 'attach':
		            htmlData += '<div id="file' + fileNo + '" class="filebox mt-10">';
		            htmlData += '   <button type="button" class="btn btn-primary thumnail" onclick="setThumnail(' + fileNo + ');">썸네일 지정</button>';
		            htmlData += '   <span class="no">' + (fileNo+1) + '.</span>';
		            htmlData += '   <span class="name">' + file.name + '</span>';
		            htmlData += '   <span class="size"> (' + formatFileSize(file.size) + ')</span>';
		            htmlData += `   <a class="delete" onclick="deleteFile('${fileNo}');"><i class="far fa-minus-square"></i></a>`;
		            htmlData += `   <div><img></div>`;
		            htmlData += '</div>';

		             $('#attach-file-list').append(htmlData);
	            	break;
	            case 'photo':
					htmlData += `<div class="board_photo" id="file${fileNo}">`;
					htmlData += `<div><img class="img"></div>`;
					htmlData += `<button type="button" class="board_btn thumnail p-0" onclick="setThumnail('${fileNo}');"><i class="way-nav-icon"></i></button>`;
					htmlData += `<button type="button" class="board_btn delete p-0" onclick="deleteFile('${fileNo}');"><i class="way-nav-icon wi-delete"></i></button>`;
					htmlData += `</div>`;

 					$('#attach-file-list').prepend(htmlData);
					break;
				case 'slide':
					htmlData += `<div class="slide_photo" id="file${fileNo}" data-fileid=N${fileNo}>`;
					htmlData += `<div ><img class="thumnail" ondblclick="setThumnail('${fileNo}');"></div>`;
					htmlData += `<button type="button" class="board_btn delete p-0" onclick="deleteFile('${fileNo}');"><i class="way-nav-icon wi-delete"></i></button>`;
					htmlData += `</div>`;

 					$('#slide-file-list').append(htmlData);
 					initSlideParam("slide-file-list");
					break;

				case 'file':
					htmlData += '<div id="file' + fileNo + '" class="filebox mt-10">';
					htmlData += '   <span class="no">' + (fileNo+1) + '.</span>';
					htmlData += '   <span class="name">' + file.name + '</span>';
					htmlData += '   <span class="size"> (' + formatFileSize(file.size) + ')</span>';
					htmlData += `   <a class="delete" onclick="deleteFile('${fileNo}');"><i class="far fa-minus-square"></i></a>`;
					htmlData += '</div>';
					$('#attach-file-list').append(htmlData);
					break;

			}

            readURL(file, fileNo);

			setTimeout(() => {
				if(fileNo === 0) setThumnail(`${fileNo}`);
			}, 100);
			fileNo++;

        } else {
            continue;
        }
    }
    // 초기화
    document.querySelector("input[type=file]").value = "";
}

function readURL(file, fileNo) {
	//console.log(file);
    if (file !== undefined) {
        var reader = new FileReader();

		reader.readAsDataURL(file);

        reader.onload = function (e) {
			console.log($(`#file${fileNo} img.img`).length);
            $(`#file${fileNo} img.img`).attr('src', e.target.result);
        };


    }
}


function showAttachFileList(tagId, files){
	if(files !== null){
		for(let i=0; i<files.length; i++){
			let file = '<li>';
			file += files.item(i).name;
			file += '<span>'+new Intl.NumberFormat('ko-KR', {minimumFractionDigits:0,maximumFractionDigits:0}).format( formatFileSize(files.item(i).size) )+'</span>';
			file += '</li>';
			$(`#${tagId}-file-list`).append(file);
		}
	}
}

/* 첨부파일 검증 */
function validation(obj){
    const fileTypes = ['application/pdf', 'image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif', 'application/haansofthwp', 'application/x-hwp'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (100 * 1024 * 1024)) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}

function setThumnail(num) {
    for(let i=0; i<filesArr.length; i++){
		_file = filesArr[i];

		if(_type == 'attach'){
    		$(`#file${i} button.thumnail`).removeClass("btn-danger").addClass("btn-primary").html('썸네일 지정');
    	} else if(_type == 'photo'){
			$(`#file${i} .board_btn.thumnail`).removeClass("check");
		} else if(_type == 'slide'){
			$(`#file${i}`).removeClass("check");
		}

    	filesArr[i].isThumnail = false;
    }

    filesArr[num].isThumnail = true;

    if(_type == 'attach'){
    	$(`#file${num} button.thumnail`).removeClass("btn-primary").addClass("btn-danger").html('썸네일');
	} else if(_type == 'photo'){
		$(`#file${num} .board_btn.thumnail`).addClass("check");
	} else if(_type == 'slide'){
		$(`#file${num}`).addClass("check");
		$('.slide_photo.thumnail img').attr('src', $(`#file${num} img`).attr('src'));
	}

}
/* 첨부파일 삭제 */
function deleteFile(num) {
	var isDelete = true;
	if(_type == 'slide'){
		if($('.slide_photo:not(.thumnail)').length > 1){
			if( $('.slide_photo.thumnail img').attr('src') === $(`#file${num} img`).attr('src')){


				if( confirm('썸네일 등록된 이미지 입니다. 삭제 하시겟습니까?') ){
					isDelete = true;
				} else {
					isDelete = false;
				}
			} else {
				isDelete = true;
			}
		} else {
			isDelete = false;
			alert('※[알림]등록된 이미지가 1개 미만일경우 삭제 할 수 없습니다.');
		}
	} else {
		isDelete = true;
	}

	if(isDelete){
	    document.querySelector("#file" + num).remove();
	    filesArr[num].isDelete = true;
    }
}

function formatFileSize(bytes) {
	if (typeof bytes !== 'number') {
    	return '';
	}
	if (bytes >= 1000000000) {
    	return (bytes / 1000000000).toFixed(2) + ' GB';
	}
	if (bytes >= 1000000) {
    	return (bytes / 1000000).toFixed(2) + ' MB';
	}
	if (bytes >= 1000) {
    	return (bytes / 1000).toFixed(2) + ' KB';
	}
	return (bytes) + ' B';
}

//썸네일 제외용도
//param : file, maxFileCount
function putFile(obj, maxFileCount) {
	let maxFileCnt = maxFileCount;
	const attFileCnt = document.querySelector('#attach-file-list')?.children.length;
	const remainFileCnt = maxFileCnt - attFileCnt;
	const curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수

	// 첨부파일 개수 확인
	if (curFileCnt > remainFileCnt) {
		alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
		return;
	}

	for (const file of obj.files) {
		// 첨부파일 검증
		if (validation(file)) {
			// 파일 배열에 담기
			const reader = new FileReader();
			reader.onload = function () {
				filesArr.push(file);
				filesArr[filesArr.length-1].fileId=`N${fileNo}`;
			};
			reader.readAsDataURL(file);

			document.querySelector('#attach-file-list').insertAdjacentHTML('beforeend',
		`<div id="file${fileNo}">
				<span>${fileNo+1}.</span>
				<span>${file.name}</span>
				<span>${formatFileSize(file.size)}</span>
				<button type="button" onclick="deleteFile('${fileNo}');"><i class="far fa-minus-square"></i></button>
				<div><img alt=""></div>
			   </div>`)
			readURL(file, fileNo);
			fileNo++;
		} else {
			continue;
		}

	}
	// 초기화
	// obj.value = "";
}