package kr.co.wayplus.travel.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import kr.co.wayplus.travel.base.model.CommonDataSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInventory extends CommonDataSet {
	private Integer id;	//재고관리 고유번호
	private String productSerial;	//상품고유번호
	private Integer storeCount;	//입고 개수
	private Integer releaseCount;	//출고 개수
	private String note;	//비고

	private Integer inventoryCount;	//출고 개수
	private String userName;

	public ProductInventory addProductSerial(String productSerial) {
		this.productSerial = productSerial;
		return this;
	}

	public ProductInventory addStoreCount(Integer storeCount) {
		this.storeCount = storeCount;
		return this;
	}
	public ProductInventory addReleaseCount(Integer releaseCount) {
		this.releaseCount = releaseCount;
		return this;
	}
	public ProductInventory addNote(String note) {
		this.note = note;
		return this;
	}
	public ProductInventory addCreateId(String createId) {
		this.setCreateId(createId);
		return this;
	}
}
