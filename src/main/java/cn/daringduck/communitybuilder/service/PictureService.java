package cn.daringduck.communitybuilder.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.sun.research.ws.wadl.Resource;

import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.repository.PictureRepository;

@Service
public class PictureService extends GenericService<Picture, Long>{
	
	private final PictureRepository pictureRepository;
	
	@Autowired
	public PictureService(PictureRepository pictureRepository) {
		super(pictureRepository);
		this.pictureRepository = pictureRepository;
	}
	
	public Picture createPictureReference(String fileLocation) {
		Picture picture = new Picture(fileLocation);
		
		pictureRepository.save(picture);
		
		return picture;
	}
	
}
