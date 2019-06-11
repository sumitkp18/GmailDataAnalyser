/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmailmeter.main.java;

/**
 *
 * @author Sumit
 */
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;
//import gmailmeter.main.java.GmailAccess.gmailFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


public class GmailData {
    
    
   
    private List<Label> labels=null;
    private List<String> senderEmails=null;
    private List<String> recipientEmails=null;
    private int numberOfSentEmails=0;
    private int numberOfRecievedEmails=0;    
    private int numberOfRecievedMailsWithSearchString=0;
    private int numberOfSentMailsWithSearchString=0;
    private List<String> mailData=null;
    
   
    
     public GmailData()
     {
         
     }
    
    public List<Label> getLabels()
    {
        return labels;
    }
    
    public void setLabels( List<Label> labels) throws IOException
    {
      
         this.labels = labels;
        
    
    }
    
    public int getNumberOfRecievedEmails()
    {
        return numberOfRecievedEmails;
    }
     public void setNumberOfRecievedEmails(int numberOfRecievedEmails) throws IOException
     {
    this.numberOfRecievedEmails=numberOfRecievedEmails;    
    }
      public  int getNumberOfSentEmails()
    {
        return  numberOfSentEmails;
       
    }
       public void setNumberOfSentEmails(int numberOfSentEmails) throws IOException
     {
    this.numberOfSentEmails=numberOfSentEmails;    
    }
       
        public List<String> getRecipientEmails()
    {
        return  recipientEmails;
    }
     public void setRecipientEmails(List<String> recipientEmails) throws IOException
     {
    this.recipientEmails=recipientEmails;    
    }
      public  List<String> getSenderEmails()
    {
        return  senderEmails;
       
    }
      
        public void setSenderEmails(List<String> senderEmails) throws IOException
     {
             this.senderEmails=senderEmails;    
    }
      
    
         public int getNumberOfSentMailsWithSearchString()
    {
        return numberOfSentMailsWithSearchString;
    }
       
        public void setNumberOfSentMailsWithSearchString(int numberOfSentMailsWithSearchString)
    {
        this.numberOfSentMailsWithSearchString=numberOfSentMailsWithSearchString;
    }
        
         public int getNumberOfRecievedMailsWithSearchString()
    {
        return numberOfRecievedMailsWithSearchString;
    }
       
        public void setNumberOfRecievedMailsWithSearchString(int numberOfRecievedMailsWithSearchString)
    {
        this.numberOfRecievedMailsWithSearchString=numberOfRecievedMailsWithSearchString;
    }
        
     public List<String> getMailData()
     {
         return mailData;
     }
     
     public void setMailData(List<String> mailData)
     {
         this.mailData=mailData;
     }
       
}

