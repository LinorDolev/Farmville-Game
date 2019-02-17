package farmground.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import farmground.logic.ActivityEntity;
import farmground.logic.UserEntity;
import farmground.logic.UserService;
import farmground.logic.plugins.UserRole;

@Component
@Aspect
public class PermissionsAspect {
	
	private UserService userService;
	
	@Autowired
	public PermissionsAspect(UserService userService) {
		setUserService(userService);
	}
		
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	private void validateActivityUserType(String type, JoinPoint jp) {
		String targetClassName = jp.getTarget().getClass().getSimpleName();
		String methodName = jp.getSignature().getName();
		
		if(jp.getArgs().length < 1 || jp.getArgs()[0].getClass() != ActivityEntity.class) {
			throw new IllegalArgumentException("Cannot use a " + type + " only service " + targetClassName + "."+ methodName +"() without ActivityEntity argument" );
		}
		ActivityEntity activity = (ActivityEntity) jp.getArgs()[0];
		
		UserEntity user = userService.getUser(activity.getPlayerEmail());
		if(!user.isConfirmed() || !user.getRole().equals(type)) {
			throw new IllegalArgumentException("User " + user.getUsername() + " is not authorized to use: " + targetClassName + "."+ methodName +"()");
		}
	}
	
	@Before("@annotation(farmground.aop.Manager)")
	public void checkIfUserIsAManager(JoinPoint jp) {
		validateActivityUserType(UserRole.manager.name(), jp);
	}
		


	@Before("@annotation(farmground.aop.Player)")
	public void checkIfUserIsAPlayer(JoinPoint jp) {
		validateActivityUserType(UserRole.player.name(), jp);
	}

	@Before("@annotation(farmground.aop.Guest)")
	public void checkIfUserIsAGuest(JoinPoint jp) {
		validateActivityUserType(UserRole.guest.name(), jp);
	}

}
