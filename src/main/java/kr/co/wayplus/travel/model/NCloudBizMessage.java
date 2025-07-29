package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NCloudBizMessage {
    private String countryCode;
    private String to;
    private String title;
    private String content;
    private String headerContent;
    private NCloudBizMessageItemHighlight itemHighlight;
    private Object item;
    private List<Object> messages;
    private List<Object> buttons;
    private boolean useSmsFailover;
    private NCloudBizMessageFailOver failoverConfig;

}
