package cn.daringduck.communitybuilder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.daringduck.communitybuilder.Error;
import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Friends;
import cn.daringduck.communitybuilder.model.Moment;
import cn.daringduck.communitybuilder.model.MomentPart;
import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.model.Privacy;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.repository.FriendsRepository;
import cn.daringduck.communitybuilder.repository.MomentPartRepository;
import cn.daringduck.communitybuilder.repository.MomentRepository;
import cn.daringduck.communitybuilder.repository.PictureRepository;
import cn.daringduck.communitybuilder.repository.UserRepository;

@Service
@Transactional
public class MomentService extends GenericService<Moment, Long>{

    private final MomentRepository momentRepository;
    private final MomentPartRepository momentPartRepository;
    private final PictureRepository  pictureRepository;
    private final FriendsRepository friendsRepository;
    private final UserRepository userRespository;

	@Autowired
    public MomentService(MomentRepository momentRepository,MomentPartRepository momentPartRepository,
    		PictureRepository  pictureRepository,FriendsRepository friendsRepository,UserRepository userRespository) {
    	super(momentRepository);
    	this.momentRepository = momentRepository;
    	this.momentPartRepository = momentPartRepository;
    	this.pictureRepository = pictureRepository;
    	this.friendsRepository =friendsRepository;
    	this.userRespository = userRespository;
    }
	
	
	public Page<Moment> findAllOrderByDesc(int page){
		
		Sort sort = new Sort(Direction.DESC, "id");
		
		Page<Moment> moments = momentRepository.findAll(new PageRequest(page, PAGE_SIZE,sort));
		
		return moments;
	}
	
	/**
	 * Add a moment
	 * @param title
	 * @param user
	 * @param privacyName
	 * 			PUBLIC, PRIVATE, CLUB or FRIENDS
	 * @return
	 */
	public Page<Moment> getUserMoments(User user, int page) throws RequestException {

		if (user == null) {
			throw new RequestException(Error.USER_DOES_NOT_EXIST);
		}

		return momentRepository.findByUser(user, new PageRequest(page, PAGE_SIZE));
	}

	public Moment addUserMoment(User user, String title, String privacyName, String eventDate) throws RequestException {

		if (user == null) {
			throw new RequestException(Error.USER_DOES_NOT_EXIST);
		}
		
		Privacy privacy = Privacy.valueOf(privacyName);
		
		//posted time stamp
		
		long posted = System.currentTimeMillis();
		
		long modifiedTime = posted;
		
		System.out.println(modifiedTime);
		
		Moment moment = new Moment(title, user, privacy,posted,modifiedTime,eventDate);
		
		momentRepository.save(moment);
		
		return moment;
	}
	
	
	public Moment changeUserMoment(long momentId, String title, String privacyName, String eventDate) throws RequestException {
		
		Moment moment =get(momentId);
		
		if (moment == null) {
			throw new RequestException(Error.MOMENT_DOES_NOT_BELONG_TO_USER);
		}
		
		if(title!=null) {
			moment.setTitle(title);
		}
		
		Privacy privacy = Privacy.valueOf(privacyName);
		
		if(privacy!=null) {
			moment.setPrivacy(privacy);
		}
		
		moment.setModifiedTime();
		
		if(eventDate!=null) {
			moment.setEventDate(eventDate);
		}
		
		momentRepository.save(moment);
		
		return moment;
	}
	
	/**
	 * get the newest Moment that published by DD01
	 * @return SS
	 */
	public Moment getDD01NewestMoment() {
		return momentRepository.getDD01NewestMoment();
	}
	
	
	/**
	 * Add a momentPart
	 * @param part
	 * @param text
	 * @param moment_id
	 * @param photo_id
	 * 	
	 * @return
	 * @throws RequestException 
	 */
	public MomentPart addMomentPart(int part,String text,long moment_id,long pictureId) throws RequestException {
		
		Picture picture = pictureRepository.getPictureById(pictureId);	
		MomentPart momentPart = new MomentPart(part, text, picture, moment_id);
		momentPartRepository.save(momentPart);
		return momentPart;
	}
	
	/**
	 * get my friends' moments
	 * @param user
	 * 
	 * @return
	 * @throws RequestException 
	 * **/
	public List<Moment> getMyFriendsMoments(User user,int page) throws RequestException{
		//find user's friends
		List<Friends> friends = friendsRepository.findByUser(user);
		
		if(friends.size() == 0) {
			throw new RequestException(Error.USER_DOES_NOT_HAVE_FRIENDS);
		}
		
		//be used to selected right users' moments
		List<Long> friensValue = new ArrayList<>();
		
		//combine all friends
		for(int i=0;i<friends.size();i++) {
			
			friensValue.add(friends.get(i).getFriend().getId());
		}
		
		//get top N moments from all friends' moments sort by time 
		//List<Moment> moments = momentRepository.getMyFriendsMoments();
		
		Pageable pageable = new PageRequest(page, PAGE_SIZE);
		
		List<Moment> moments = momentRepository.findByUserIdInOrderByModifiedTimeDesc(friensValue, pageable).getContent();
		
		return moments;
	}
	
	
	/**
	 * get my friends' moments
	 * @param user
	 * 
	 * @return
	 * @throws RequestException 
	 * **/
	public List<Moment> getClubMoments(User user,int page) throws RequestException{
		
		if(user.getClub()==null) {
			throw new RequestException(Error.USER_DOES_NOT_HAVE_CLUB);
		}
		
		//get clubId
		int clubId = user.getClub().getId();
		
		//get the user in the club
		List<User> users = userRespository.findByClubId(clubId);
		
		//create a new list to store userId
		List<Long> clubValue = new ArrayList<>();
		
		for(int i=0;i<users.size();i++) {
			//add user into club
			clubValue.add(users.get(i).getId());
		}
		
		Pageable pageable = new PageRequest(0, PAGE_SIZE);
		
		List<Moment> moments = momentRepository.findByUserIdInOrderByModifiedTimeDesc(clubValue, pageable).getContent();
		
		return moments;
	}
	
	/**
	 * delete a momentPart
	 * @param moment_id
	 * 	
	 * @return
	 * @throws RequestException 
	public void deleteMomentPart(long moment_id) {
		momentPartRepository.delete(moment_id);
	}
	
	
	/**
	 * change a momentPart
	 * @param part
	 * @param text
	 * @param moment_id
	 * @param photo_id
	 * 	
	 * @return
	 * @throws RequestException 
	 */

	public MomentPart updateMomentPartWithId(long id,String text,long pictureId)throws RequestException{
		
		MomentPart momentpart = momentPartRepository.getOne(id);
		
		if(momentpart == null) {
			throw new RequestException(Error.MOMENTPART_DOES_NOT_EXIST);
		}
		
		Picture picture = pictureRepository.getPictureById(pictureId);
		
		if(text==null)
			text = momentpart.getText();
		else
			momentpart.setText(text);
		
		if(picture==null)
			picture = momentpart.getPicture();
		else
			momentpart.setPicture(picture);

		Long momentId = momentpart.getMomentId();
		
		
		//when momentPart change, change moment's modifiedDate
		Moment moment =get(momentId);
		
		if(moment == null) {
			throw new RequestException(Error.MOMENT_DOES_NOT_EXIST);
		}
		
		moment.setModifiedTime();
		
		momentRepository.save(moment);
		momentPartRepository.upDateMomentPart(id, text, momentId, picture);
		
		return momentpart;
	}
	
	
	/**
	 * get momentParts
	 * @param id
	 * 	
	 * @return
	 * @throws RequestException 
	 */
	public List<MomentPart> getMomentPartWithId(long id){
		return momentPartRepository.getMomentPart(id);
	}
	
}
