package cn.daringduck.communitybuilder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.daringduck.communitybuilder.Error;
import cn.daringduck.communitybuilder.PasswordSecurity;
import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.AuthToken;
import cn.daringduck.communitybuilder.model.Chapter;
import cn.daringduck.communitybuilder.model.Club;
import cn.daringduck.communitybuilder.model.Course;
import cn.daringduck.communitybuilder.model.CourseChapter;
import cn.daringduck.communitybuilder.model.Moment;
import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.model.Privacy;
import cn.daringduck.communitybuilder.model.Role;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.model.UserChapter;
import cn.daringduck.communitybuilder.model.UserCourse;
import cn.daringduck.communitybuilder.repository.AuthTokenRepository;
import cn.daringduck.communitybuilder.repository.ChapterRepository;
import cn.daringduck.communitybuilder.repository.ClubRepository;
import cn.daringduck.communitybuilder.repository.CourseChapterRepository;
import cn.daringduck.communitybuilder.repository.CourseRepository;
import cn.daringduck.communitybuilder.repository.MomentRepository;
import cn.daringduck.communitybuilder.repository.PictureRepository;
import cn.daringduck.communitybuilder.repository.RoleRepository;
import cn.daringduck.communitybuilder.repository.UserChapterRepository;
import cn.daringduck.communitybuilder.repository.UserCourseRepository;
import cn.daringduck.communitybuilder.repository.UserRepository;

@Service
@Transactional
public class UserService extends GenericService<User, Long> {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private AuthTokenRepository authRepository;
	private PasswordSecurity passwordSecurity;
	private MomentRepository momentRepository;
	private ClubRepository clubRepository;
	private PictureRepository pictureRepository;
    private ChapterRepository chapterRepository;
    private CourseRepository courseRepository;
    private UserChapterRepository userChapterRepository;
    private UserCourseRepository userCourseRepository;
    private CourseChapterRepository courseChapterRepository;
	
	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository, AuthTokenRepository authRepository,
			MomentRepository momentRepository, ClubRepository clubRepository,PictureRepository pictureRepository,
			ChapterRepository chapterRepository,CourseRepository courseRepository,UserChapterRepository userChapterRepository,
			UserCourseRepository userCourseRepository,CourseChapterRepository courseChapterRepository) {
		super(userRepository);
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.authRepository = authRepository;
		this.momentRepository = momentRepository;
		this.pictureRepository = pictureRepository;
		this.clubRepository = clubRepository;
		this.chapterRepository = chapterRepository;
		this.courseRepository = courseRepository;
		this.userChapterRepository = userChapterRepository;
		this.userCourseRepository = userCourseRepository;
		this.courseChapterRepository = courseChapterRepository;
		this.passwordSecurity = new PasswordSecurity();
	}

	/**
	 * Check for an AuthToken in the system. If the AuthToken is found then the
	 * User that is linked with this token will be returned
	 * 
	 * @param tokenString
	 *            The token
	 * @return User
	 */
	public User findUserByAuthToken(String authToken) {

		if (authToken == null) {
			return null;
		}

		AuthToken token = authRepository.findOne(authToken);

		if (token == null) {
			return null;
		}

		return token.getUser();
	}

	public List<User> getUserByRoleId(int roleId){ 
		return userRepository.getByRoleId(roleId); 
	} 
	  
	/**
	 * Adds a new user
	 * @param username
	 * @param password
	 * @param gender
	 * @param nickname
	 * @param phone
	 * @param wechat
	 * @param email
	 * @param picture
	 * @param roleId
	 * @param clubId
	 * @return
	 * @throws RequestException
	 */
	public User addUser(String username, String password, int gender, String nickname, String phone, String wechat,
			String email,long pictureId, int roleId, int clubId) throws RequestException {
		
		// Check if the passed parameters are correct
		checkValues(username, password, nickname, gender, phone, wechat, email, false);
		
		Role role = roleRepository.findOne(roleId);

		Club club = null;
		if (clubId != 0 && clubId != 1) {
			club = clubRepository.findOne(clubId);
		}
		
		Picture picture =null;
		if(pictureId!=0 && pictureId!=-1) {
			picture = pictureRepository.getPictureById(pictureId);
		}
		
		User user = new User(username, "", gender == 2, nickname, phone, wechat, email, role, picture, club, null);	

		String passwordHash = passwordSecurity.hash(password.toCharArray());

		user.setPassword(passwordHash);

		userRepository.save(user);

		return user;
	}

	/**
	 * Check if a username & password combination is correct. If that is the
	 * case then return the User object
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User authenticate(String username, String password) {

		User user = userRepository.findByUsernameIgnoreCase(username);

		if (user == null || !passwordSecurity.authenticate(password.toCharArray(), user.getPassword())) {
			return null;
		}

		return user;

	}

	/**
	 * Update a user
	 * @param id
	 * @param username
	 * @param password
	 * @param nickname
	 * @param gender
	 * @param phone
	 * @param wechat
	 * @param email
	 * @param disabled
	 * @param pictured
	 * @param roleId
	 * @param clubId
	 * @return
	 * @throws RequestException
	 */
	public User updateUserWithId(long id, String username, String password, String nickname, int gender, String phone,
			String wechat, String email, int disabled,long pictureId, int roleId, int clubId) throws RequestException {
		
		// Check if the passed parameters are correct
		checkValuesEdit(username, password, nickname, gender, phone, wechat, email, true);

		User user = userRepository.findOne(id);

		if (username != null) {
			user.setUsername(username);
		}

		// Edit the data
		if (password != null) {
			String passwordHash = passwordSecurity.hash(password.toCharArray());
			user.setPassword(passwordHash);
		}

		if (nickname != null) {
			user.setNickname(nickname);
		}

		if (gender == 1 || gender == 2) {
			// 1 = male, 2 = female
			user.setGender(gender == 2);
		}

		if (phone != null) {
			user.setPhone(phone);
		}

		if (wechat != null) {
			user.setWechat(wechat);
		}

		if (email != null) {
			user.setEmail(email);
		}

		if (disabled == 1 || disabled == 2) {
			// 1 = enabled, 2 = disabled
			user.setDisabled(disabled == 2);
		}

		if (roleId != 0) {
			Role role = roleRepository.findOne(roleId);
			user.setRole(role);
		}
		
		if (clubId != 0 && clubId != -1) {
			Club club = clubRepository.findOne(clubId);
			user.setClub(club);
		}
		
		if (clubId == -1) {
			user.setClub(null);
		}

		if(pictureId!=0) {
			Picture picture = pictureRepository.getPictureById(pictureId);
			user.setPicture(picture);
		}
		
		userRepository.save(user);

		return user;
	}

	/**
	 * Generate a token for a user
	 * 
	 * @param user
	 *            The user who is getting the token
	 * @return the token
	 */
	public AuthToken generateAuthToken(User user) {
		// Generate a random string.
		String tokenString = UUID.randomUUID().toString();

		// Create the token object
		AuthToken token = new AuthToken(tokenString, user);

		authRepository.save(token);

		return token;
	}

	public Page<Moment> getUserMoments(long userId, int page) throws RequestException {
		User user = get(userId);

		if (user == null) {
			throw new RequestException(Error.USER_DOES_NOT_EXIST);
		}

		return momentRepository.findByUser(user, new PageRequest(page, PAGE_SIZE));
	}

	public Moment addUserMoment(long userId, String title, String privacyName,String eventDate) throws RequestException {
		User user = get(userId);

		if (user == null) {
			throw new RequestException(Error.USER_DOES_NOT_EXIST);
		}
		
		Privacy privacy = Privacy.valueOf(privacyName);
		
		Moment moment = new Moment(title, user, privacy,eventDate);
		
		momentRepository.save(moment);
		
		return moment;
	}
	
	public Moment getUserMoment(long userId, long momentId) throws RequestException {		
		Moment moment = momentRepository.findOne(momentId);
		
		return moment;
	}
	
	/**
	 * add a chapter to a user
	 * @author 曹将将
	 * 
	 * @param userId
	 * @param chapterId
	 * @param teacherId
	 * @param score
	 * @param passedOrNot
	 * 
	 * @return UserChapter
	 * @throws RequestException 
	 * 
	 * */
	public boolean addUserChapter(long userId,long chapterId,long teacherId,int score,boolean passedOrNot,String comment) throws RequestException {
		UserChapter userChapter1 = userChapterRepository.findByUserIdAndChapterId(userId,chapterId);
		
		if(userChapter1!=null) {
			throw new RequestException(Error.USERCHAPTER_NOT_UNIQUE);
		}
		else {
			boolean result = false;
			
			User user = userRepository.findOne(userId);
			
			if(user == null) {
				throw new RequestException(Error.USER_DOES_NOT_EXIST);
			}
			
			Chapter chapter = chapterRepository.findOne(chapterId);
			
			if(chapter == null) {
				throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST);
			}
			
			User teacher = userRepository.findOne(teacherId);
			
//			if(teacher == null) {
//				throw new RequestException(Error.USER_DOES_NOT_EXIST);
//			}
			
			
			long date = 0;
			
			UserChapter userChapter = new UserChapter(user,chapter,teacher,date,passedOrNot,score,comment);
			
			if(userChapterRepository.save(userChapter)!=null)
				result = true;
			
			return result;
		}
	}
	
	/**
	 * add a course to a user
	 * @author 曹将将
	 * 
	 * @param userId
	 * @param courseId
	 * @param teacherId
	 * @param passedOrNot
	 * 
	 * @return UserChapter
	 * @throws RequestException 
	 * 
	 * */
	public boolean addUserCourse(long userId,int courseId,long teacherId,boolean passedOrNot,long date) throws RequestException {
		
		UserCourse userCourse1 = userCourseRepository.findByUserIdAndCourseId(userId,courseId);
		
		if(userCourse1!=null) {
			throw new RequestException(Error.USERCOURSE_NOT_UNIQUE);
		}
		else {
			boolean result = false;
			
			User user = userRepository.findOne(userId);
			
			if(user == null) {
				throw new RequestException(Error.USER_DOES_NOT_EXIST);
			}
			
			Course course = courseRepository.findOne(courseId);
			
			if(course == null) {
				throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
			}
			
			User teacher = userRepository.findOne(teacherId);
			
			if(teacher==null) {
				throw new RequestException(Error.USER_DOES_NOT_EXIST);
			}
			
			UserCourse userCourse = new UserCourse(user,course,teacher,date,passedOrNot);
			
			if(userCourseRepository.save(userCourse)!=null)
				result = true;
			
		    List<CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId); 
		       
		    for(int i=0;i<courseChapters.size();i++) { 
		      addUserChapter(userId,courseChapters.get(i).getChapter().getId(),0,0,false,null); 
		    } 
			
			return result;
		}
	}
	
	
	/**
	 * change a user's course status
	 * 
	 * @param userId
	 * @param courseId
	 * @param teacherId
	 * @param status
	 * @author 曹将将
	 * 
	 * @return UserCourse
	 * @throws RequestException 
	 * */
	public boolean changeUserCourse(long userId,int courseId,long teacherId,String status,long date) throws RequestException {
		//use it to show the result of changeUserCourse
		boolean result = false;
		
		//use it to show the user's chapter whether passed or not
		boolean chapterPassOrNot = true;
		
		//get userCourse
		UserCourse userCourse = userCourseRepository.findByUserIdAndCourseId(userId,courseId);
		
		if(userCourse == null) {
			throw new RequestException(Error.USERCOURSE_DOES_NOT_EXIS);
		}
		
		//get course chapter
		List<CourseChapter> courseChapters = new ArrayList<>();
		courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId);
		
		//get userChapter and if the chapter is required but the user does not pass it so course can not be passed
		for(int i=0;i<courseChapters.size();i++) {
			//get userChapter by userId and chapterId
			UserChapter userChapter = userChapterRepository.findByUserIdAndChapterId(userId, courseChapters.get(i).getChapter().getId());
			
			if(courseChapters.get(i).getChapter().isRequiredOrNot()==true&&userChapter.getPassOrNot()==false)
				chapterPassOrNot = false;
			
		}
		
		//get the teacher
		User teacher = userRepository.findOne(teacherId);
		if(teacher!=null) {
			userCourse.setTeacher(teacher);
		}
		
		//when the teacher or the admin want to let the user pass and the user pass the chapter he needed, he pass the course
		if(status.equalsIgnoreCase("true")&&chapterPassOrNot) {
			userCourse.setPassedOrNot(true);
		}
		
		//when teacher want to let a user passed but the user dose not pass the chapters, system will give the teacher a prompt
		if(status.equalsIgnoreCase("true")&&chapterPassOrNot==false) {
			throw new RequestException(Error.USER_DOES_NOT_PASS_CHAPTERS);
		}
		
		//if the user does not pass the course, the date is 0
		if(status.equalsIgnoreCase("false")) {
			userCourse.setPassedOrNot(false);
			date = 0;
		}
		
		userCourse.setDate(date);
		
		if(userCourseRepository.save(userCourse)!=null)
			result = true;
		
		return result;
	}
	
	
	/**
	 * change a user's chapter status
	 * 
	 * @param userId
	 * @param courseId
	 * @param teacherId
	 * @param status
	 * @author 曹将将
	 * 
	 * @return UserCourse
	 * */
	public boolean changeUserChapter(long userId,long chapterId,long teacherId,int score,String status,long date,String comment) {
		boolean result = false;
		
		UserChapter userChapter = userChapterRepository.findByUserIdAndChapterId(userId,chapterId);
		
		User teacher = userRepository.findOne(teacherId);
		
		if(teacher!=null) {
			userChapter.setTeacher(teacher);
		}
		
		if(status.equalsIgnoreCase("true")) {
			userChapter.setPassOrNot(true);
		}
		
		userChapter.setDate(date);
		
		
		if(status.equalsIgnoreCase("false")) {
			userChapter.setPassOrNot(false);
		}
		
		if(score>0) {
			userChapter.setScore(score);
		}
		
		if(!comment.equalsIgnoreCase(""))
			userChapter.setComment(comment);
		
		if(userChapterRepository.save(userChapter)!=null)
			result = true;
		
		return result;
	}
	
	/**
	 * get a user's courses 
	 * */
	public String getUserCourse(long userId) {
		
		List<UserCourse> userCourses = userCourseRepository.findByUserId(userId);
		
		JSONObject jsonObject1 = new JSONObject();
	    for(int i =0;i<userCourses.size();i++) { 
	        UserCourse userCourse = userCourses.get(i); 
	        
	        Course course = userCourses.get(i).getCourse(); 
			
			JSONObject jsonObject2 = new JSONObject();
		    

			


			

		     
			
			jsonObject2.put("courseId", course.getId());
			jsonObject2.put("name", course.getName()); 
			jsonObject2.put("date", userCourse.getDate());
			
			if(course.getPicture()!=null) {
				jsonObject2.put("pictureId",course.getPicture().getId());
				jsonObject2.put("picturePosition",course.getPicture().getFileLocation());
			}
			else {
				jsonObject2.put("pictureId","");
				jsonObject2.put("picturePosition","");
			}
			
		      if(userCourse.getTeacher()==null) {
			    	 
			      jsonObject2.put("teacherName", ""); 
			      jsonObject2.put("teacherId", ""); 
		      }
		      else {
		    	   
			      jsonObject2.put("teacherName", userCourse.getTeacher().getNickname()); 
			      jsonObject2.put("teacherId", userCourse.getTeacher().getId());
		      }
		      
				jsonObject2.put("passOrNot", userCourse.isPassedOrNot());
			jsonObject1.put(i+"", jsonObject2);
		}
		
		return jsonObject1.toString();
	}
	
	public boolean deleteUserCourse(long userId,int courseId) {
		userCourseRepository.deleteUserCourseByUserIdAndCourseId(userId, courseId);
		return true;
	}
	
	
	  /** 
	   * get a user's chapters  
	   * @throws RequestException  
	   * */ 
	  public String getUserChapter(long userId,int courseId) throws RequestException {   
	     
	    List<CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId); 
	     
	    if(courseChapters == null) { 
	      throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST); 
	    } 
	    
	    List<Chapter> chapters = new ArrayList<>(); 
	     
	    for(int i=0;i<courseChapters.size();i++) { 
	      chapters.add(courseChapters.get(i).getChapter()); 
	    } 
	     
	    if(chapters == null) { 
		     throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST); 
		} 
	    
	    JSONObject jsonObject1 = new JSONObject(); 
	    
	    for(int k=0;k<chapters.size();k++) { 
	      UserChapter userChapter = userChapterRepository.findByUserIdAndChapterId(userId,chapters.get(k).getId()); 
	       
	      JSONObject jsonObject = new JSONObject(); 
	       
	      if(userChapter.getTeacher() ==null) { 
	          jsonObject.put("teacherName", ""); 
	          jsonObject.put("teacherId", ""); 
	        } 
	      else { 
	          jsonObject.put("teacherName", userChapter.getTeacher().getNickname()); 
	          jsonObject.put("teacherId", userChapter.getTeacher().getId()); 
	        } 
	         
	        jsonObject.put("requiredOrNot", userChapter.getChapter().isRequiredOrNot());
	        jsonObject.put("score", userChapter.getScore());
	        jsonObject.put("passOrNot", userChapter.getPassOrNot()); 
	        jsonObject.put("date", userChapter.getDate());
	        
	        jsonObject.put("chapterTitle", userChapter.getChapter().getTitle()); 
	        jsonObject.put("chapterId", userChapter.getChapter().getId()); 
	        jsonObject1.put(k+"", jsonObject); 
	      } 
	       
	      return jsonObject1.toString(); 
	  }
	// Helper methods
	
	/**
	 * Pattern for safe password
	 */
	private static final Pattern[] passwordRegexes = new Pattern[3];
	{
		passwordRegexes[0] = Pattern.compile(".*[A-Z].*");
		passwordRegexes[1] = Pattern.compile(".*[a-z].*");
		passwordRegexes[2] = Pattern.compile(".*\\d.*");
		// passwordRegexes[3] = Pattern.compile(".*[~!].*");
	}

	/**
	 * Check if password contains number, small and upercase letter and another
	 * character
	 */
	private boolean isLegalPassword(String pass) {

		if (pass.length() < 8) {
			return false;
		}

		for (int i = 0; i < passwordRegexes.length; i++) {
			if (!passwordRegexes[i].matcher(pass).matches()) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Check values if they are valid
	 * 
	 * @return
	 * @throws RequestException 
	 */
	private void checkValues(String username, String password, String nickname, int gender, String phone,
			String wechat, String email, boolean edit) throws RequestException {

		if ((username == null || password == null) && !edit) {
			throw new RequestException(Error.NO_USERNAME_OR_PASSWORD);
		}
		
		if (username != null && username.length() < 4) {
			throw new RequestException(Error.USERNAME_SHORT);
		}
		
		if (username != null && userRepository.findByUsernameIgnoreCase(username) != null){
			throw new RequestException(Error.USERNAME_NOT_UNIQUE);
		}

		if (password != null && !isLegalPassword(password)) {
			throw new RequestException(Error.UNSAFE_PASSWORD);
		}

		if (gender < 0 || gender > 2) {
			throw new RequestException(Error.GENDER_INVALID);
		}
	}
	
	
	/**
	 * Check values if they are valid
	 * 
	 * @return
	 * @throws RequestException 
	 */
	private void checkValuesEdit(String username, String password, String nickname, int gender, String phone,
			String wechat, String email, boolean edit) throws RequestException {

		if ((username == null || password == null) && !edit) {
			throw new RequestException(Error.NO_USERNAME_OR_PASSWORD);
		}
		
		if (username != null && username.length() < 4) {
			throw new RequestException(Error.USERNAME_SHORT);
		}

		if (password != null && !isLegalPassword(password)) {
			throw new RequestException(Error.UNSAFE_PASSWORD);
		}

		if (gender < 0 || gender > 2) {
			throw new RequestException(Error.GENDER_INVALID);
		}
	}
	
	

	
}
