package com.pncint.downloader;

import com.pncint.downloader.service.JiraDownloaderService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class JiraAttachmentDownloaderApplication {

	@Autowired
	private JiraDownloaderService jiraDownloaderService;

	public static void main(String[] args) {
		SpringApplication.run(JiraAttachmentDownloaderApplication.class, args);

//		String jql = ""; //add jql query here
//		String downloadPath = "C:\\Users\\{username}\\Documents\\JiraAttachments"; //change username to pnc PL#
//		jiraDownloaderService.downloadAttachments(jql, downloadPath);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

//	public void downloadJiraAttachments() throws JSONException, IOException {
//		String jql = ""; //add jql query here
//		String downloadPath = "C:\\Users\\{username}\\Documents\\JiraAttachments"; //change username to pnc PL#
//		jiraDownloaderService.downloadAttachments(jql, downloadPath);
//	}

}


