package lk.ijse.cloudops.billing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandardResponse<T> {
    private String status;
    private int code;
    private String message;
    private T data;

    public static <T> StandardResponse<T> success(int code, String message, T data) {
        return StandardResponse.<T>builder()
                .status("SUCCESS")
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> StandardResponse<T> error(int code, String message) {
        return StandardResponse.<T>builder()
                .status("ERROR")
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
