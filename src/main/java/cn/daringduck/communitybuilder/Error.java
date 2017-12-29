package cn.daringduck.communitybuilder;

import javax.ws.rs.core.Response.Status;

/**
 * Error messages for our RequestException
 * 
 * @author Jochem Ligtenberg
 */
public enum Error {
	NO_TOKEN_FOUND(1, "No token found", Status.FORBIDDEN),
	NO_VALID_TOKEN(2, "Token is not valid", Status.FORBIDDEN),
	NO_PERMISSION(3, "User has no permission to access this resource", Status.FORBIDDEN),
	USER_ALREADY_MEMBER_OF_CLASS(4, "User is already member of class", Status.BAD_REQUEST), 
	USER_NOT_MEMBER_OF_CLASS(5, "User is not member of class", Status.BAD_REQUEST),
	COURSE_DOES_NOT_EXIST(6, "Course does not exist", Status.BAD_REQUEST), 
	PICTURE_DOES_NOT_EXIST(7, "Picture does not exist", Status.BAD_REQUEST), 
	CHAPTER_DOES_NOT_EXIST(8, "Chapter does not exist", Status.BAD_REQUEST), 
	CHAPTER_PART_DOES_NOT_EXIST(9, "Chapter Part does not exist", Status.BAD_REQUEST),
	USER_DOES_NOT_EXIST(10, "User does not exist", Status.BAD_REQUEST), 
	MOMENT_DOES_NOT_BELONG_TO_USER(11, "Moment does not belong to the user", Status.BAD_REQUEST), 
	CLUB_USED(12, "Club used by other parts of the system", Status.BAD_REQUEST), 
	NO_USERNAME_OR_PASSWORD(13, "No Username or Password provided", Status.BAD_REQUEST),
	UNSAFE_PASSWORD(14, "Password needs 8 characters and at least 1 small letter, 1 Capital letter and 1 number", Status.BAD_REQUEST),
	GENDER_INVALID(15, "Gender needs to be 1 for male, 2 for female", Status.BAD_REQUEST), 
	USERNAME_SHORT(16, "Username need at least 6 characters", Status.BAD_REQUEST), 
	USERNAME_NOT_UNIQUE(17, "Username is already used", Status.BAD_REQUEST),
	USERCOURSE_NOT_UNIQUE(18,"User has choosed this course",Status.BAD_REQUEST),
	USERCHAPTER_NOT_UNIQUE(19,"User has choosed this chapter",Status.BAD_REQUEST),
	USERCOURSE_DOES_NOT_EXIS(20,"User does not have this course",Status.BAD_REQUEST),
	USER_DOES_NOT_PASS_CHAPTERS(21,"User does not pass all chapters",Status.BAD_REQUEST),
	USER_DOES_NOT_HAVE_FRIENDS(22,"User does not have friends",Status.BAD_REQUEST),
	USER_ALREADY_HAVE_THIS_FRIEND(23,"User have already have this friends",Status.BAD_REQUEST);
	
	public final int errorCode;
	public final String errorMessage;
	public final Status httpStatus;
	
	private Error(int errorCode, String errorMessage, Status httpStatus) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
	
	public int getErrorCode() { return errorCode; }
	
	public String getErrorMessage() { return errorMessage; }
}