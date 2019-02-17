package farmground.logic.exceptions;

public class UserNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7380359962598794533L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable throwable) {
		super(throwable);
	}

	
}
