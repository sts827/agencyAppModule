package kr.co.wayplus.travel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortData {
    private String sort;
    private String sortOrder;

    public SortData(String sort, String sortOrder ) {
		this.sort = sort;
		this.sortOrder = sortOrder;
	}
}
