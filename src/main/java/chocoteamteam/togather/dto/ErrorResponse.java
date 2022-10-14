package chocoteamteam.togather.dto;

import chocoteamteam.togather.exception.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private ErrorCode errorCode;
    private String errorMessage;
}