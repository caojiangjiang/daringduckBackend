package cn.daringduck.communitybuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.daringduck.communitybuilder.model.StudentsStudyStatus;

public interface StudentsStudyStatusRepository  extends JpaRepository<StudentsStudyStatus,Long> {

	@Query(value = ("select studentsStudyStatus from StudentsStudyStatus studentsStudyStatus where studentsStudyStatus.userId=?1 and studentsStudyStatus.classId = ?1 and studentsStudyStatus.chapterId=?3"))
	StudentsStudyStatus getStudentsStudyStatus(long userId, long classId, long chapterId);
}
