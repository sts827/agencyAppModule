Date.prototype.getWeek = function (dowOffset) {
  /*getWeek() was developed by Nick Baicoianu at MeanFreePath: http://www.meanfreepath.com */

  dowOffset = typeof(dowOffset) == 'number' ? dowOffset : 0; //default dowOffset to zero
  var newYear = new Date(this.getFullYear(),0,1);
  var day = newYear.getDay() - dowOffset; //the day of week the year begins on
  day = (day >= 0 ? day : day + 7);
  var daynum = Math.floor((this.getTime() - newYear.getTime() -
    (this.getTimezoneOffset()-newYear.getTimezoneOffset())*60000)/86400000) + 1;
  var weeknum;
  //if the year starts before the middle of a week
  if(day < 4) {
    weeknum = Math.floor((daynum+day-1)/7) + 1;
    if(weeknum > 52) {
      let nYear = new Date(this.getFullYear() + 1,0,1);
      let nday = nYear.getDay() - dowOffset;
      nday = nday >= 0 ? nday : nday + 7;
      /*if the next year starts before the middle of
        the week, it is week #1 of that year*/
      weeknum = nday < 4 ? 1 : 53;
    }
  }
  else {
    weeknum = Math.floor((daynum+day-1)/7);
  }
  return weeknum;
};

/**
 * javascript SimpleDateFormat 함수
 * ex) 
 * new Date().format("yyyy-MM-dd");
 * new Date().format("yyyy-MM-dd HH:mm:ss");
 * new Date().format("yyyy-MM-dd a/p hh:mm:ss");
 */
function getToDate(format){
	var date = new Date();
	if(format == undefined){
		date = date.format("yyyy-MM-dd");
	} else {
		date = date.format(format);
	}
	
	return date;
}

function getFromDate(PlusDate, format){
	var date = new Date();
	var FromDate = date.getDate()+PlusDate; 
	
	date.setDate(FromDate);

	if(format === undefined){
		date = date.format("yyyy-MM-dd");
	} else {
		date = date.format(format);
	}
	
	return date;
}



//두날짜 차이를 일로 받아온다.
//function getGapDt(dt1, dt2) {
//	var stDt = new Date(dt1).format("yyyy-MM-dd");
//	var edDt = new Date(dt2).format("yyyy-MM-dd");
//	return (new Date(edDt).getTime() - new Date(stDt).getTime())/ 1000 / 60 / 60 / 24;
//}
//두날짜 차이를 일 목록으로 받아온다.
function getDateRange(startDate, endDate, listDate){
	var dateMove = new Date(startDate);
	var strDate = startDate;
	
	if (startDate == endDate){
		var strDate = dateMove.toISOString().slice(0, 10);
		listDate.push(strDate);
	} else {
		while (strDate < endDate){
			var strDate = dateMove.toISOString().slice(0, 10);
			listDate.push(strDate);
			dateMove.setDate(dateMove.getDate() + 1);
		}
	}
	return listDate;
};

function getClacDate(gubn,DnP,data){
	var nowDate = new Date();
	console.log(gubn,DnP,data,nowDate);
	
	if(DnP=="D") data = data * -1;
	
	switch (gubn) {
	case 'T':nowDate.setHours(nowDate.getHours() -1 + data );break;
	case 'D':nowDate.setDate(nowDate.getDate() + data);break;
	case 'M':nowDate.setMonth(nowDate.getMonth() + data);break;
	default:
		break;
	}
//	console.log(nowDate.format("yyyy-MM-dd HH:mm:ss"));
//	nowDate.setDate(nowDate.getDate()-1)
	return nowDate;
}