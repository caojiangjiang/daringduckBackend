package cn.daringduck.communitybuilder;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Settings for Jersey
 * 
 * @author Jochem Ligtenberg
 */
@ApplicationPath("api")
public class CommunityBuilder extends ResourceConfig {
	
	public CommunityBuilder() {
		register(MultiPartFeature.class);
		packages("cn.daringduck.communitybuilder");
		packages("cn.daringduck.communitybuilder.controller");

		for (Class<?> c : this.getClasses()) {
			System.out.println(c.getName());
		}
	}
	
}
