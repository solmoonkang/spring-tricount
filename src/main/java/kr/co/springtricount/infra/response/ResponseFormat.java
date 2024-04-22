package kr.co.springtricount.infra.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFormat<T> {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private HttpStatus status;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public static <T> ResponseFormat<T> successMessage(ResponseStatus responseStatus) {

		return ResponseFormat.<T>builder()
			.status(responseStatus.getStatus())
			.message(responseStatus.getMessage())
			.data(null)
			.build();
	}

	public static <T> ResponseFormat<T> successMessage(ResponseStatus responseStatus,
		String message) {

		return ResponseFormat.<T>builder()
			.status(responseStatus.getStatus())
			.message(message)
			.data(null)
			.build();
	}

	public static <T> ResponseFormat<T> successMessageWithData(ResponseStatus responseStatus,
		T data) {
		return ResponseFormat.<T>builder()
			.status(responseStatus.getStatus())
			.message(responseStatus.getMessage())
			.data(data)
			.build();
	}

	public static ResponseFormat<Void> failureMessage(ResponseStatus responseStatus,
		String message) {
		return ResponseFormat.<Void>builder()
			.status(responseStatus.getStatus())
			.message(message)
			.data(null)
			.build();
	}

	public static <T> ResponseFormat<T> failureMessageWithData(ResponseStatus responseStatus,
		T data) {
		return ResponseFormat.<T>builder()
			.status(responseStatus.getStatus())
			.message(responseStatus.getMessage())
			.data(data)
			.build();
	}
}
