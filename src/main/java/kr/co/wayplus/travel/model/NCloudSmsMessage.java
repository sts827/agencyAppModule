package kr.co.wayplus.travel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NCloudSmsMessage {
    private String to;
    private String subject;
    private String content;
}
