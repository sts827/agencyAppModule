fnGenJsonToStr = function(targetId){
	var _json = {'id':targetId};
	var _data ={}
	var _mp = new MapCustom();
	var _keyset = new Array();

	if( $(`.josnArea`, `#${targetId}`).length > 0 ){
/*
		$(`.josnGroup`, `#${targetId}`).each(function(i,e){
			var arr = new Array();
			var _subdata ={};

		    $(`input:not([disabled='disabled']), select:not([disabled='disabled'])`, $(e)).each(function(j,e2){
				_subdata[$(e2).attr('name')] = $(e2).val();
		    });

		    _data[i] = _subdata;
		});
*/
	} else if( $(`.josnGroup`, `#${targetId}`).length > 0 ){
		$(`.josnGroup`, `#${targetId}`).each(function(i,e){
			var arr = new Array();
			var _subdata ={};

		    $(`input:not([disabled='disabled']), select:not([disabled='disabled'])`, $(e)).each(function(j,e2){
				_subdata[$(e2).attr('name')] = $(e2).val();
		    });

		    _data[i] = _subdata;
		});
	} else {
		$(`input:not([disabled='disabled']), select:not([disabled='disabled'])`, `#${targetId}`).each(function(i,e){
			var size = $(`${$(e).prop('tagName')}[name='${$(e).attr('name')}']`).length;
			if( size > 1 ){
				var _key = $(e).attr('name');
				var arr;

				if( _mp.get(_key) === undefined  ){
					_keyset.push( _key );
					arr = new Array();
					_mp.put(_key, arr);
				} else {
					arr = _mp.get(_key);
				}

				arr.push($(e).val());
			} else {
				_data[$(e).attr('name')] = $(e).val();
			}
		});

		for(var i in _keyset){
			var _key = _keyset[i];
			var arr = _mp.get(_key);
			var _subData ={};

			for(var j in arr){
				_subData[ j ] = arr[j];
			}
			_data[ _key ] = _subData;
		}
	}

	_json["data"] = _data;

	return JSON.stringify(_json);
}