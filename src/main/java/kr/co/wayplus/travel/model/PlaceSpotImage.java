package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import kr.co.wayplus.travel.base.model.CommonFileInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceSpotImage extends CommonFileInfo {
	private Integer tsId;
	private Integer sortOrder;


	public PlaceSpotImage addTsId( Integer tsId ) {
		this.tsId = tsId;
		return this;
	}

	public PlaceSpotImage addFileId( Integer fileId ) {
		this.setFileId(fileId);
		return this;
	}

	public PlaceSpotImage addSortOrder( Integer sortOrder ) {
		this.sortOrder = sortOrder;
		return this;
	}


}
