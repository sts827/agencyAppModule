package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NCloudSmsMessageContainer {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String subject;
    private String content;
    private List<NCloudSmsMessage> messages;
    private List<Object> files;
    private String reserveTime;
    private String reserveTimeZone;
}
