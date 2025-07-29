package kr.co.wayplus.travel.util;

import kr.co.wayplus.travel.model.LogMailSend;
import kr.co.wayplus.travel.service.common.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class LoggerUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final LoggerService loggerService;

    public LoggerUtil(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public static void writeBindingResultErrorLog(BindingResult bindingResult, Logger log) {
        List<ObjectError> errors = bindingResult.getAllErrors();
        for (ObjectError error : errors) {
            log.error("Parameter Binding Error : " + error.getDefaultMessage());
        }
    }

    public static String getPropertiesAsString(Properties properties) {
        return properties.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(", "));
    }

    public static String getObjectListToString(List<Object> messages) {
        String resultString = null;
        for(Object message : messages){
            if(resultString == null){
                resultString = message.toString();
            }else{
                resultString += System.lineSeparator() + message.toString();
            }
        }
        return resultString;
    }
}


