/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gmailmeter.main.java;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.MIN_PRIORITY;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Sumit
 */
public class GmailMeter implements Runnable{

    
   
    private JSONObject gmailDataJSON=new JSONObject();
    private static boolean dataFetched=false;
    
    private void sendMail(String gmailData)
    {
        // Recipient's email ID needs to be mentioned.
      String to = "spradhan@kentrus.com";//change accordingly
      String cc1="kkhanna@kentrus.com";
      String cc2="kkhanna@simplilend.com";
      
      // Sender's email ID needs to be mentioned
      String from = "sumit.cooldude00@gmail.com";//change accordingly
      final String username = "sumit.cooldude00@gmail.com";//change accordingly
      final String password = "";//change accordingly

      // Assuming you are sending email through relay.jangosmtp.net
      String host = "smtp.gmail.com";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");

      // Get the Session object.
      Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
         }
      });

      try {
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
         InternetAddress.parse(to));
         
         //Address[] cc =  {new InternetAddress(cc1),new InternetAddress(cc2),new InternetAddress(from)};
         //message.addRecipients(Message.RecipientType.CC, cc);

         // Set Subject: header field
         message.setSubject("Your Gmail report!");

         // Now set the actual message
         message.setText(gmailData);

         // Send message
         Transport.send(message);

         System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
            throw new RuntimeException(e);
      }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws IOException, NullPointerException, JSONException, InterruptedException {
        // TODO code application logic here
        
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        
        GmailMeter gmail=new GmailMeter();
        Thread fetch=new Thread(gmail,"Fetch Gmail Data");
        Thread main=Thread.currentThread(); 
        main.setPriority(MIN_PRIORITY);
        fetch.setPriority(MAX_PRIORITY);
        fetch.start();
        
        
        
        while(true)
        {
           
            if(dataFetched==true)
             {
                 
                System.out.println("data fetched! do u want to continue or exit? \n enter 1 to exit \n else anything to continue!");
               // String input=br.readLine();
               // if(input.equals("1"))
                        System.exit(0);
                
                
                
            }
        }
    

    }

    @Override
    public void run() {
        
        try {
           
             File DATA_STORE_DIR=new File("E:/gmailCredentials");
           gmailDataJSON= new GmailAccess().getGmailObject(DATA_STORE_DIR);
           System.out.println(gmailDataJSON);
           System.out.println("______________________________ DATA FETCHING COMPLETE _______________________________");
          // sendMail(gmailDataJSON.toString());
           dataFetched=true;
           System.out.println("ending  thread fetch!");
        
        } catch (IOException ex) {
            Logger.getLogger(GmailMeter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(GmailMeter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(GmailMeter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
