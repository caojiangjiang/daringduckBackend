package cn.daringduck.communitybuilder.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Moment;
import cn.daringduck.communitybuilder.model.MomentPart;
import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.model.Privacy;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.repository.MomentPartRepository;
import cn.daringduck.communitybuilder.repository.MomentRepository;
import cn.daringduck.communitybuilder.repository.PictureRepository;

@Service
@Transactional
public class MomentService extends GenericService<Moment, Long>{

    private final MomentRepository momentRepository;
    private final MomentPartRepository momentPartRepository;
    private final PictureRepository  pictureRepository;

	@Autowired
    public MomentService(MomentRepository momentRepository,MomentPartRepository momentPartRepository,PictureRepository  pictureRepository) {
    	super(momentRepository);
    	this.momentRepository = momentRepository;
    	this.momentPartRepository = momentPartRepository;
    	this.pictureRepository = pictureRepository;
    }
	
	/**
	 * Add a moment
	 * @param title
	 * @param user
	 * @param privacyName
	 * 			PUBLIC, PRIVATE, CLUB or FRIENDS
	 * @return
	 */
	public Moment addMoment(String title, User user, String privacyName,String eventDate){
		Privacy privacy = Privacy.valueOf(privacyName);	
		Moment moment = new Moment(title, user, privacy,eventDate);
		momentRepository.save(moment);
		return moment;
	}
	
	
	/**
	 * 
	 * get the newest Moment that published by DD01
	 * @return 
	 * 
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
		
		Picture picture = pictureRepository.getPictureById(pictureId);
		
		if(text==null)
			text = momentpart.getText();
		else
			momentpart.setText(text);
		
		if(picture==null)
			picture = momentpart.getPicture();
		else
			momentpart.setPicture(picture);

		Long moentId = momentpart.getMomentId();
		
		
		momentPartRepository.upDateMomentPart(id, text, moentId, picture);
		
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
