package cn.daringduck.communitybuilder.controller;

import javax.servlet.ServletContext;

import cn.daringduck.communitybuilder.Error;
import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.service.UserService;

public abstract class GenericController {

	private UserService userService;

	public GenericController(ServletContext context) {
		this.userService = (UserService) context.getAttribute("userService");
	}

	/**
	 * Get the user doing the request
	 * 
	 * @param token
	 * @return
	 */
	protected User getUser(String token) {
		return userService.findUserByAuthToken(token);
	}

	/**
	 * Limit the call to a specific role or specific roles
	 * 
	 * @param roleName
	 * @throws AccessException 
	 */
	protected void secure(String token, String... roles) throws RequestException {
		if (token == null) {
			throw new RequestException(Error.NO_TOKEN_FOUND);
		}
		
		User user = userService.findUserByAuthToken(token);
		
		if (user == null) {
			throw new RequestException(Error.NO_VALID_TOKEN);
		}

		// All logged in users
		if (roles[0].equals("*")){
			return;
		}
		
		for(String role : roles) {
			if (user.getRole().getName().equalsIgnoreCase(role)) {
				return;
			}
		}
	
		throw new RequestException(Error.NO_PERMISSION);
	}

}
