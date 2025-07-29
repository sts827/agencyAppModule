package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NCloudBizMessageContainer {
    private String plusFriendId;
    private String templateCode;
    private List<NCloudBizMessage> messages;
    private String reserveTime;
    private String reserveTimeZone;
}
