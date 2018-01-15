package cn.daringduck.communitybuilder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import cn.daringduck.communitybuilder.service.PictureService;
import cn.daringduck.communitybuilder.service.RoleService;
import cn.daringduck.communitybuilder.service.UserService;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {

	private AnnotationConfigApplicationContext ctx;

	/**
	 * Default constructor.
	 */
	public ContextListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent context) {
	}
	
	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent context) {
		// Load the repositories
		ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		
		// Setup the services
		UserService userService = (UserService) ctx.getBean("userService");
		PictureService pictureService = (PictureService) ctx.getBean("pictureService");
		RoleService roleService = (RoleService) ctx.getBean("roleService");
		
		// Add the services to the context
		context.getServletContext().setAttribute("userService", userService);
		context.getServletContext().setAttribute("clubService", ctx.getBean("clubService"));
		context.getServletContext().setAttribute("classService", ctx.getBean("classService"));
		context.getServletContext().setAttribute("teacherService", ctx.getBean("teacherService"));
		context.getServletContext().setAttribute("pictureService", pictureService);
		context.getServletContext().setAttribute("momentService", ctx.getBean("momentService"));
		context.getServletContext().setAttribute("courseService", ctx.getBean("courseService"));
		context.getServletContext().setAttribute("statusService", ctx.getBean("statusService"));
		context.getServletContext().setAttribute("roleService", roleService);
		

		// Add some basic data if it is not there yet
		if (userService.getPage(0).getNumberOfElements() == 0){
			roleService.addRole("user", 1);
			roleService.addRole("admin", 100);
			roleService.addRole("teacher", 20);
			roleService.addRole("member", 5);
			roleService.addRole("parent", 10);
			
			pictureService.createPictureReference("default.png");
			
			try {
				userService.addUser("admin", "Fredfred1", 0, null, null, null, null, 0, 2, 0);
			} catch (RequestException e) {
				e.printStackTrace();
			}
		}
		
	}

}
