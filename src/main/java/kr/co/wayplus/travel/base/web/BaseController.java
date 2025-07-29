package kr.co.wayplus.travel.base.web;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.wayplus.travel.model.SortData;

public class BaseController {
	protected List<SortData> getListOrder(HttpServletRequest request){
		//Long draw = Long.parseLong(request.getParameter("draw"));
		List<SortData> sortOrder = new ArrayList();

		for (int i = 0; i < 20; i++) {
			String column = request.getParameter("columns[" + request.getParameter("order["+i+"][column]") + "][name]");
			String sort = request.getParameter("order["+i+"][dir]");

			if(column == null || sort == null) {
				break;
			}

			sortOrder.add(new SortData(column, sort));
		}
		return sortOrder;
	}
}
