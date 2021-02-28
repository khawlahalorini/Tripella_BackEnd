package com.codeninja.tripella.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class PhotoService {
	private AmazonS3 amazonS3;
	
	@Value("${aws.s3.bucketURL}")
	private String bucketURL;
	
	@Value("${aws.s3.bucketName}")
	private String bucketName;
	
	@Value("${aws.s3.accessId}")
	private String accessId;
	
	@Value("${aws.s3.accessSecret}")
	private String accessSecret;
	
	@Value("${aws.s3.region}")
	private String region;

	@PostConstruct
	private void initPhotoService() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessId, this.accessSecret);
		this.amazonS3 = AmazonS3ClientBuilder.standard().withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

	public String upload(MultipartFile photoFile, String objectName) throws Exception {
		
		String ext = photoFile.getContentType();
		ext = "." + ext.substring(ext.lastIndexOf('/') + 1);
		String relativePath = objectName + "/"+System.currentTimeMillis()+((int)(Math.random()*99)) + ext;
		
		amazonS3.putObject(new PutObjectRequest(bucketName, relativePath, photoFile.getInputStream(), null)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		
		return relativePath;
	}

	public boolean delete(String photoPath) {
		try {
			amazonS3.deleteObject(bucketName, photoPath);
		return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public List<String> getPhotosList(String objectName, int id) {
		List<String> list = new ArrayList<String>();
		for (S3ObjectSummary os : amazonS3.listObjectsV2(bucketName, objectName+"/"+id).getObjectSummaries()) {
			list.add(bucketURL + os.getKey());
		}
			
		return list;
	}
	
	public List<String> getPostPhotos(int id){
		return getPhotosList("post/", id);
	}
}