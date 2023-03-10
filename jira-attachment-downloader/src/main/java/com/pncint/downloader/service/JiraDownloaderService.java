package com.pncint.downloader.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class JiraDownloaderService {

    @Autowired
    private RestTemplate restTemplate;

    // These values are being sourced from applications.yml file
    @Value("${jira.baseUrl}")
    private  String jiraBaseUrl;

    @Value("${jira.username}")
    private String jiraUserName;

    @Value("${jira.token}")
    private String jiraToken;

    public static void main(String[] args) throws JSONException, IOException {
        String jql = ""; //add jql query here
        String downloadPath = "C:\\Users\\{username}\\Documents\\JiraAttachments"; //change username to pnc PL#
        JiraDownloaderService service = new JiraDownloaderService();
        service.downloadAttachments(jql, downloadPath);
    }

    public void downloadAttachments(String jql, String downloadPath) throws JSONException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jiraToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = jiraBaseUrl + "/rest/api/3/search?jql=" + jql; //will need to edit this to PNC jira

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        // get stories
        JSONArray issues = jsonObject.getJSONArray("issues");
        // loop through jira stories, get story key and create folder named after story
        for (int i = 0; i < issues.length(); i++) {
            JSONObject issue = issues.getJSONObject(i);
            String issueKey = issue.getString("key");

            File directory = new File(downloadPath + "/" + issueKey);
            if (!directory.exists()) {
               directory.mkdir();
            }
            // get attachments
            JSONObject fields = issue.getJSONObject("fields");
            JSONArray attachments = fields.getJSONArray("attachment");
            // loop through each attachment and get contents
            for (int j = 0; j < attachments.length(); j++) {
                JSONObject attachment = attachments.getJSONObject(j);
                String attachmentUrl = attachment.getString("content");

                ResponseEntity<byte[]> attachmentResponse = restTemplate.exchange(attachmentUrl, HttpMethod.GET, entity, byte[].class);

                String fileName = attachment.getString("filename");

                FileOutputStream outputStream = new FileOutputStream(downloadPath + "/" + issueKey + "/" + fileName);
                outputStream.write(attachmentResponse.getBody());
                outputStream.close();
            }
        }
    }

}
