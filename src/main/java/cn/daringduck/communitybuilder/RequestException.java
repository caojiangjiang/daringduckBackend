package cn.daringduck.communitybuilder;

/**
 * Exception thrown when something goes wrong in one of the requests
 * to our server.
 * 
 * @author Jochem Ligtenberg
 *
 */
public class RequestException extends Exception{
	private static final long serialVersionUID = 1L;
	private final Error error;
	
	public RequestException(Error err) {
		super(err.getErrorMessage());
		this.error = err;
	}
	
	public Error getError() { return error; }
}
