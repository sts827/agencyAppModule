package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardCategory {
    private int id;
    private int boardId;
    private String title;
    private String note;
    private int orderNum;
    private String useYn;
    private String startDate;
    private String expireDate;
    private String createId;
    private String createDate;
    private String lastUpdateId;
    private String lastUpdateDate;
}
