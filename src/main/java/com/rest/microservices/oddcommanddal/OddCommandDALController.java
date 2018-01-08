package com.rest.microservices.oddcommanddal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

@RestController
public class OddCommandDALController {
	
	private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
	private final AmazonS3 client = AmazonS3Client.builder().withRegion("us-east-1").withForceGlobalBucketAccessEnabled(true).build();
	private static HashMap<String, Date> objectAccessHashMap = new HashMap<String, Date>();
	
	@GetMapping(path = "/odd-command-dal/list")
	@CrossOrigin()
	public String helloWorld() {
		ObjectListing ol = client.listObjects("odd-command");
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		for (S3ObjectSummary os: objects) {
		    System.out.println("* " + os.getKey());
		}
		String json = new Gson().toJson(objects);
		return json;
	}
	
	@GetMapping(path = "/odd-command-dal/get-object/{key:.+}")
	@CrossOrigin()
	public String getObjectMetaData(@PathVariable String key) {
		ObjectMetadata objectMetadata = client.getObjectMetadata("odd-command", key);
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(objectMetadata);
		jsonElement.getAsJsonObject().addProperty("key", key);
		jsonElement.getAsJsonObject().addProperty("shouldReTier", "false");
		Date date = new Date();
		if (objectAccessHashMap.containsKey(objectMetadata.getETag())) {
		    long timeDiff = date.getTime() - objectAccessHashMap.get(objectMetadata.getETag()).getTime();
		    objectAccessHashMap.put(objectMetadata.getETag(), date);
			if (timeDiff < 5000) {
				jsonElement.getAsJsonObject().addProperty("shouldReTier", "true");
				return gson.toJson(jsonElement);
			}
			else {
				jsonElement.getAsJsonObject().addProperty("shouldReTier", "false");
				return gson.toJson(jsonElement);
			}
		}
		else {
			objectAccessHashMap.put(objectMetadata.getETag(), date);
		}
		System.out.println("ETag: " + objectMetadata.getETag() + ", Date: " + date);
		return gson.toJson(jsonElement);
	}
}
