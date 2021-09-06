package kr.co.smartcube.xcube.exception;

public class XcubeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;

	public XcubeException(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}