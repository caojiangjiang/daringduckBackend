package cn.daringduck.communitybuilder.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import cn.daringduck.communitybuilder.model.StudentsStudyStatus;
import cn.daringduck.communitybuilder.repository.StudentsStudyStatusRepository;

@Service
public class StatusService extends GenericService<StudentsStudyStatus,Long>{

	private final StudentsStudyStatusRepository studentsStudyStatusRepository;
	
	@Autowired
	public StatusService(StudentsStudyStatusRepository studentsStudyStatusRepository) {
		super(studentsStudyStatusRepository);
		this.studentsStudyStatusRepository = studentsStudyStatusRepository;
	}
	
	public StudentsStudyStatus setstudentsChapterStatus(long userId,long classId,long chapterId,int status) {
		
		StudentsStudyStatus studentsStudyStatus = new StudentsStudyStatus(userId,classId,chapterId,status);
		studentsStudyStatusRepository.save(studentsStudyStatus);
		return studentsStudyStatus;
	}
	
	public StudentsStudyStatus getstudentsChapterStatus(long userId,long classId,long chapterId) {
		StudentsStudyStatus studentsStudyStatus =studentsStudyStatusRepository.getStudentsStudyStatus(userId,classId,chapterId);
		return studentsStudyStatus;
	}
}
