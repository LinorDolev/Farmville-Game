package farmground.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import farmground.logic.UserService;

@RestController
@CrossOrigin(origins="*")
public class UserUI {

	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.POST, 
			path = "/playground/users", 
			produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)

	public UserTO registerUser(@RequestBody NewUserFormTO userForm) {
		return new UserTO(userService.registerUser(userForm.toEntity()));
	}

	@RequestMapping(
			method = RequestMethod.GET, 
			path = "/playground/users/confirm/{playground}/{email}/{code}", 
			produces = MediaType.APPLICATION_JSON_VALUE)

	public UserTO confirmUser(
			@PathVariable("playground") String playground, 
			@PathVariable("email") String email,
			@PathVariable("code") String code) throws Exception {

		return new UserTO(userService.confirmUser(playground, email, code));
	}

	@RequestMapping(
			method = RequestMethod.GET, 
			path = "/playground/users/login/{playground}/{email}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO loginUser(
			@PathVariable("playground") String playground, 
			@PathVariable("email") String email)
			throws Exception {

		return new UserTO(userService.loginUser(email, playground));
	}

	@RequestMapping(
			method = RequestMethod.PUT, 
			path = "/playground/users/{playground}/{email}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(
			@PathVariable("playground") String playground, 
			@PathVariable("email") String email,
			@RequestBody UserTO updatedUser) throws Exception {

		userService.updateUser(email, updatedUser.toEntity());
		System.err.println("PUT REQUEST\nURL: /playground/users/" + playground + "/" + email);

	}
}
