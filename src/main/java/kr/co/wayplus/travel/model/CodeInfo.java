package kr.co.wayplus.travel.model;

import kr.co.wayplus.travel.base.model.CommonFileInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeInfo extends CommonFileInfo {
    private int codeSort;
    private String id
                    , code
                    , upperCode
                    , name
                    , codeName
                    , codeDesc
                    , useYn;

}
