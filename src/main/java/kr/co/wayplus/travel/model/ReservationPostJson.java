package kr.co.wayplus.travel.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ReservationPostJson {
    // 상품 일정
	private Reservation data;
    private ArrayList<UserCustomerOrderList> orderList;
    private String mode;
}