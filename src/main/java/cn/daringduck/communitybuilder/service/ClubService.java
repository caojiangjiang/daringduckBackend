package cn.daringduck.communitybuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.daringduck.communitybuilder.model.Club;
import cn.daringduck.communitybuilder.repository.ClubRepository;

@Service
public class ClubService extends GenericService<Club, Integer>{
	
	private final ClubRepository clubRepository;
	
	@Autowired
	public ClubService(ClubRepository clubRepository) {
		super(clubRepository);
		this.clubRepository = clubRepository;
	}
	
	/**
	 * Create a new club
	 * @param code
	 * @param name
	 * @param location
	 * @return
	 */
	public Club createClub(String code, String name, String location) {
		Club club = new Club(code, name, location);
		
		clubRepository.save(club);
		
		return club;
	}
	
	/**
	 * Update a club
	 * @param id
	 * @param code
	 * @param name
	 * @param location
	 * @return
	 */
	public Club updateClub(int id, String code, String name, String location) {
		Club club = clubRepository.findOne(id);

		if (code != null) {
			club.setCode(code);
		}
		
		if (name != null) {
			club.setName(name);
		}

		if (location != null) {
			club.setLocation(location);
		}
		
		clubRepository.save(club);
		
		return club;
	}
	
	/**
	 * Delete a club
	 * @param id
	 */
	public void deleteClub(int id) {
		clubRepository.delete(id);
	}
}
