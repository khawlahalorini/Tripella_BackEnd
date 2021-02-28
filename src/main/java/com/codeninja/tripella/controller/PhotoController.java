package com.codeninja.tripella.controller;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

import com.codeninja.tripella.service.AppProperties;

public class PhotoController {

	public String uploadProfileImage(MultipartFile file, String objectType) {
		if (file.isEmpty())
			return "";
		
		return uploadAnImage(file, objectType+"/"+System.currentTimeMillis()+((int)(Math.random()*99)));
	}

	private String uploadAnImage(MultipartFile file, String path_name) {
		

		
		var credentials = new BasicAWSCredentials(AppProperties.getAccessId(), AppProperties.getAccessSecret());
		
		String ext = file.getContentType();
		ext = "." + ext.substring(ext.lastIndexOf('/') + 1);
		String key_name = path_name + ext;

		TransferManager xfer_mgr = TransferManagerBuilder.standard()
				.withS3Client(AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.fromName(AppProperties.getBucketRegion())).build())
				.build();

		try {
			Upload xfer = xfer_mgr.upload(new PutObjectRequest(AppProperties.getBucketName(), key_name, file.getInputStream(), null)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			// loop with Transfer.isDone()
			try {
				xfer.waitForCompletion();
			} catch (AmazonServiceException e) {
				System.out.println("Amazon service error: " + e.getMessage());
			} catch (AmazonClientException e) {
				System.out.println("Amazon client error: " + e.getMessage());
			} catch (InterruptedException e) {
				System.out.println("Transfer interrupted: " + e.getMessage());
			}
		} catch (AmazonClientException | IOException e) {
			System.out.println(e.getMessage());
			xfer_mgr.shutdownNow();
			return "";
		}

		xfer_mgr.shutdownNow();
		return key_name;
	}
	
	public void updateDefaultImage(MultipartFile file, String objectType) {
		if (file.isEmpty())
			uploadAnImage(file, objectType+"Default");
	}
}
