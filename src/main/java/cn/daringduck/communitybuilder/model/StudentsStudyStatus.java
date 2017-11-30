package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "students_study_status")
public class StudentsStudyStatus {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private long userId;
	
	@NotNull
	private long classId;
	
	@NotNull
	private long chapterId;
	
	@NotNull
	private int status;
	
	public StudentsStudyStatus()
	{
		
	}
	
	public StudentsStudyStatus(long userId, long classId, long chapterId,int status) {
		this.userId = userId;
		this.classId = classId;
		this.chapterId = chapterId;
		this.status = status;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getUserId() {
		return this.userId;
	}
	
	public void setClassId(long classId) {
		this.classId = classId;
	}
	
	public long getClassId() {
		return this.classId;
	}
	
	public void setChapterId(long chapterId) {
		this.chapterId = chapterId;
	}
	
	public long getChapterId() {
		return this.chapterId;
	}	
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	
}
