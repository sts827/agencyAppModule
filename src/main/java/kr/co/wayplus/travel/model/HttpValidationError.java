package kr.co.wayplus.travel.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
class HttpValidationError extends HttpSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public HttpValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}