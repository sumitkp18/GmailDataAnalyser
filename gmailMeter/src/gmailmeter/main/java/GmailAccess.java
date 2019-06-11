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
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Float.NaN;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

public class GmailAccess {

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME
            = "Gmail API Java Quickstart";
    private static String USER;

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/gmail-java-quickstart.json");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    //private static String CLIENT_SECRET_JSON="..\\resources\\client_secret.json";
    
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();
    
    private static final String[] BANKING_WORDS_LIST={"credit","debit", "amount due","bank", "bank statement", "loan", "loan statement","emi","delayed payment",};
    private static final String[] HEALTH_WORDS_LIST={"doctor", "health", "medicine", "medical report","doctor appointment"};

    private static final String COMMON_WORDS_LIST_1 = "the\n"
            + "be\n"
            + "of\n"
            + "and\n"
            + "a\n"
            + "to\n"
            + "in\n"
            + "he\n"
            + "have\n"
            + "it\n"
            + "that\n"
            + "for\n"
            + "they\n"
            + "I\n"
            + "with\n"
            + "as\n"
            + "not\n"
            + "on\n"
            + "she\n"
            + "at\n"
            + "by\n"
            + "this\n"
            + "we\n"
            + "you\n"
            + "do\n"
            + "but\n"
            + "from\n"
            + "or\n"
            + "which\n"
            + "one\n"
            + "would\n"
            + "all\n"
            + "will\n"
            + "there\n"
            + "say\n"
            + "who\n"
            + "make\n"
            + "when\n"
            + "can\n"
            + "more\n"
            + "if\n"
            + "no\n"
            + "man\n"
            
            + "out\n"
            + "other\n"
            + "so\n"
            + "what\n"
            + "time\n"
            + "up\n"
            + "go\n"
            + "about\n"
            + "than\n"
            + "into\n"
            + "could\n"
            + "state\n"
            + "only\n"
            + "new\n"
            + "year\n"
            + "some\n"
            + "take\n"
            + "come\n"
            + "these\n"
            + "know\n"
            + "see\n"
            + "use\n"
            + "get\n"
            + "like\n"
            + "then\n"
            + "first\n"
            + "any\n"
            + "work\n"
            + "now\n"
            + "may\n"
            + "such\n"
            + "give\n"
            + "over\n"
            + "think\n"
            + "most\n"
            + "even\n"
            + "find\n"
            + "day\n"
            + "also\n"
            + "after\n"
            + "way\n"
            + "many\n"
            + "must\n"
            + "look\n"
            + "before\n"
            + "great\n"
            + "back\n"
            + "through\n"
            + "long\n"
            + "where\n"
            + "much\n"
            + "should\n"
            + "well\n"
            + "people\n"
            + "down\n"
            + "own\n"
            + "just\n"
            + "because\n"
            + "good\n"
            + "each\n"
            + "those\n"
            + "feel\n"
            + "seem\n"
            + "how\n"
            + "high\n"
            + "too\n"
            + "place\n"
            + "little\n"
            + "world\n"
            + "very\n"
            + "still\n"
            + "nation\n"
            + "hand\n"
            + "old\n"
            + "life\n"
            + "tell\n"
            + "write\n"
            + "become\n"
            + "here\n"
            + "show\n"
            + "house\n"
            + "both\n"
            + "\n"
            + "between\n"
            + "need\n"
            + "mean\n"
            + "call\n"
            + "develop\n"
            + "under\n"
            + "last\n"
            + "right\n"
            + "move\n"
            + "thing\n"
            + "general\n"
            + "school\n"
            + "never\n"
            + "same\n"
            + "another\n"
            + "begin\n"
            + "while\n"
            + "number\n"
            + "part\n"
            + "turn\n"
            + "real\n"
            + "leave\n"
            + "might\n"
            + "want\n"
            + "point\n"
            + "form\n"
            + "off\n"
            + "child\n"
            + "few\n"
            + "small\n"
            + "since\n"
            + "against\n"
            + "ask\n"
            + "late\n"
            + "home\n"
            + "interest\n"
            + "large\n"
            + "person\n"
            + "end\n"
            + "open\n"
            + "public\n"
            + "follow\n"
            + "during\n"
            + "present\n"
            + "without\n"
            + "again\n"
            + "hold\n"
            + "govern\n"
            + "around\n"
            + "possible\n"
            + "head\n"
            + "consider\n"
            + "word\n"
            + "program\n"
            + "problem\n"
            + "however\n"
            + "lead\n"
            + "system\n"
            + "set\n"
            + "order\n"
            + "eye\n"
            + "plan\n"
            + "run\n"
            + "keep\n"
            + "face\n"
            + "fact\n"
            + "group\n"
            + "play\n"
            + "stand\n"
            + "increase\n"
            + "early\n"
            + "course\n"
            + "change\n"
            + "help\n"
            + "line\n"
            + "city\n"
            + "put\n"
            + "close\n"
            + "case\n"
            + "force\n"
            + "meet\n"
            + "once\n"
            + "water\n"
            + "upon\n"
            + "war\n"
            + "build\n"
            + "hear\n"
            + "light\n"
            + "unite\n"
            + "live\n"
            + "every\n"
            + "country\n"
            + "bring\n"
            + "center\n"
            + "let\n"
            + "side\n"
            + "try\n"
            + "provide\n"
            + "continue\n"
            + "name\n"
            + "certain\n"
            + "power\n"
            + "pay\n"
            + "result\n"
            + "question\n"
            + "study\n"
            + "woman\n"
            + "member\n"
            + "until\n"
            + "far\n"
            + "night\n"
            + "always\n"
            + "service\n"
            + "away\n"
            + "report\n"
            + "something\n"
            + "company\n"
            + "week\n"
            + "church\n"
            + "toward\n"
            + "start\n"
            + "social\n"
            + "room\n"
            + "figure\n"
            + "nature\n"
            + "\n"
            + "though\n"
            + "young\n"
            + "less\n"
            + "enough\n"
            + "almost\n"
            + "read\n"
            + "include\n"
            + "president\n"
            + "nothing\n"
            + "yet\n"
            + "better\n"
            + "big\n"
            + "boy\n"
            + "cost\n"
            + "business\n"
            + "value\n"
            + "second\n"
            + "why\n"
            + "clear\n"
            + "expect\n"
            + "family\n"
            + "complete\n"
            + "act\n"
            + "sense\n"
            + "mind\n"
            + "experience\n"
            + "art\n"
            + "next\n"
            + "near\n"
            + "direct\n"
            + "car\n"
            + "law\n"
            + "industry\n"
            + "important\n"
            + "girl\n"
            + "god\n"
            + "several\n"
            + "matter\n"
            + "usual\n"
            + "rather\n"
            + "per\n"
            + "often\n"
            + "kind\n"
            + "among\n"
            + "white\n"
            + "reason\n"
            + "action\n"
            + "return\n"
            + "foot\n"
            + "care\n"
            + "simple\n"
            + "within\n"
            + "love\n"
            + "human\n"
            + "along\n"
            + "appear\n"
            + "doctor\n"
            + "believe\n"
            + "speak\n"
            + "active\n"
            + "student\n"
            + "month\n"
            + "drive\n"
            + "concern\n"
            + "best\n"
            + "door\n"
            + "hope\n"
            + "example\n"
            + "inform\n"
            + "body\n"
            + "ever\n"
            + "least\n"
            + "probable\n"
            + "understand\n"
            + "reach\n"
            + "effect\n"
            + "different\n"
            + "idea\n"
            + "whole\n"
            + "control\n"
            + "condition\n"
            + "field\n"
            + "pass\n"
            + "fall\n"
            + "note\n"
            + "special\n"
            + "talk\n"
            + "particular\n"
            + "today\n"
            + "measure\n"
            + "walk\n"
            + "teach\n"
            + "low\n"
            + "hour\n"
            + "type\n"
            + "carry\n"
            + "rate\n"
            + "remain\n"
            + "full\n"
            + "street\n"
            + "easy\n"
            + "although\n"
            + "record\n"
            + "sit\n"
            + "determine\n"
            + "level\n"
            + "local\n"
            + "sure\n"
            + "receive\n"
            + "thus\n"
            + "moment\n"
            + "spirit\n"
            + "train\n"
            + "college\n"
            + "religion\n"
            + "perhaps\n"
            + "music\n"
            + "grow\n"
            + "free\n"
            + "cause\n"
            + "serve\n"
            + "age\n"
            + "book\n"
            + "board\n"
            + "recent\n"
            + "\n"
            + "sound\n"
            + "office\n"
            + "cut\n"
            + "step\n"
            + "class\n"
            + "true\n"
            + "history\n"
            + "position\n"
            + "above\n"
            + "strong\n"
            + "friend\n"
            + "necessary\n"
            + "add\n"
            + "court\n"
            + "deal\n"
            + "tax\n"
            + "support\n"
            + "party\n"
            + "whether\n"
            + "either\n"
            + "land\n"
            + "material\n"
            + "happen\n"
            + "education\n"
            + "death\n"
            + "agree\n"
            + "arm\n"
            + "mother\n"
            + "across\n"
            + "quite\n"
            + "anything\n"
            + "town\n"
            + "past\n"
            + "view\n"
            + "society\n"
            + "manage\n"
            + "answer\n"
            + "break\n"
            + "organize\n"
            + "half\n"
            + "fire\n"
            + "lose\n"
            + "money\n"
            + "stop\n"
            + "actual\n"
            + "already\n"
            + "effort\n"
            + "wait\n"
            + "department\n"
            + "able\n"
            + "political\n"
            + "learn\n"
            + "voice\n"
            + "air\n"
            + "together\n"
            + "shall\n"
            + "cover\n"
            + "common\n"
            + "subject\n"
            + "draw\n"
            + "short\n"
            + "wife\n"
            + "treat\n"
            + "limit\n"
            + "road\n"
            + "letter\n"
            + "color\n"
            + "behind\n"
            + "produce\n"
            + "send\n"
            + "term\n"
            + "total\n"
            + "university\n"
            + "rise\n"
            + "century\n"
            + "success\n"
            + "minute\n"
            + "remember\n"
            + "purpose\n"
            + "test\n"
            + "fight\n"
            + "watch\n"
            + "situation\n"
            + "south\n"
            + "ago\n"
            + "difference\n"
            + "stage\n"
            + "father\n"
            + "table\n"
            + "rest\n"
            + "bear\n"
            + "entire\n"
            + "market\n"
            + "prepare\n"
            + "explain\n"
            + "offer\n"
            + "plant\n"
            + "charge\n"
            + "ground\n"
            + "west\n"
            + "picture\n"
            + "hard\n"
            + "front\n"
            + "lie\n"
            + "modern\n"
            + "dark\n"
            + "surface\n"
            + "rule\n"
            + "regard\n"
            + "dance\n"
            + "peace\n"
            + "observe\n"
            + "future\n"
            + "wall\n"
            + "farm\n"
            + "claim\n"
            + "firm\n"
            + "operation\n"
            + "further\n"
            + "pressure\n"
            + "property\n"
            + "morning\n"
            + "amount\n"
            + "top\n"
            + "outside";

    private static final String COMMON_WORDS_LIST_2 = "jan feb mar apr may jun jul aug sep oct nov dev mon tue wed thu fri sat sun ist forwarded test  not yes please regards ---------------------------------------------------------------  am pm the date email 	of	to	and	a	in	is\n"
            + "it	you	that	he	was	for	on\n"
            + "are	with	as	I	his	they	be\n"
            + "at	one	have	this	from	or	had\n"
            + "by	hot	but	some	what	there	we\n"
            + "can	out	other	were	all	your	when\n"
            + "up	use	word	how	said	an	each\n"
            + "she	which	do	their	time	if	will\n"
            + "way	about	many	then	them	would	write\n"
            + "like	so	these	her	long	make	thing\n"
            + "see	him	two	has	look	more	day\n"
            + "could	go	come	did	my	sound	no\n"
            + "most	number	who	over	know	water	than\n"
            + "call	first	people	may	down	side	been\n"
            + "now	find	any	new	work	part	take\n"
            + "get	place	made	live	where	after	back\n";
      /*      + "little	only	round	man	year	came	show\n"
            + "every	good	me	give	our	under	name\n"
            + "very	through	just	form	much	great	think\n"
            + "say	help	low	line	before	turn	cause\n"
            + "same	mean	differ	move	right	boy	old\n"
            + "too	does	tell	sentence	set	three	want\n"
            + "air	well	also	play	small	end	put\n"
            + "home	read	hand	port	large	spell	add\n"
            + "even	land	here	must	big	high	such\n"
            + "follow	act	why	ask	men	change	went\n"
            + "light	kind	off	need	house	picture	try\n"
            + "us	again	animal	point	mother	world	near\n"
            + "build	self	earth	father	head	stand	own\n"
            + "page	should	country	found	answer	school	grow\n"
            + "study	still	learn	plant	cover	food	sun\n"
            + "four	thought	let	keep	eye	never	last\n"
            + "door	between	city	tree	cross	since	hard\n"
            + "start	might	story	saw	far	sea	draw\n"
            + "left	late	run	dont	while	press	close\n"
            + "night	real	life	few	stop	open	seem\n"
            + "together	next	white	children	begin	got	walk\n"
            + "example	ease	paper	often	always	music	those\n"
            + "both	mark	book	letter	until	mile	river\n"
            + "car	feet	care	second	group	carry	took\n"
            + "rain	eat	room	friend	began	idea	fish\n"
            + "mountain	north	once	base	hear	horse	cut\n"
            + "sure	watch	color	face	wood	main	enough\n"
            + "plain	girl	usual	young	ready	above	ever\n"
            + "red	list	though	feel	talk	bird	soon\n"
            + "body	dog	family	direct	pose	leave	song\n"
            + "measure	state	product	black	short	numeral	class\n"
            + "wind	question	happen	complete	ship	area	half\n"
            + "rock	order	fire	south	problem	piece	told\n"
            + "knew	pass	farm	top	whole	king	size\n"
            + "heard	best	hour	better	true	during	hundred\n"
            + "am	remember	step	early	hold	west	ground\n"
            + "interest	reach	fast	five	sing	listen	six\n"
            + "table	travel	less	morning	ten	simple	several\n"
            + "vowel	toward	war	lay	against	pattern	slow\n"
            + "center	love	person	money	serve	appear	road\n"
            + "map	science	rule	govern	pull	cold	notice\n"
            + "voice	fall	power	town	fine	certain	fly\n"
            + "unit	lead	cry	dark	machine	note	wait\n"
            + "plan	figure	star	box	noun	field	rest\n"
            + "correct	able	pound	done	beauty	drive	stood\n"
            + "contain	front	teach	week	final	gave	green\n"
            + "oh	quick	develop	sleep	warm	free	minute\n"
            + "strong	special	mind	behind	clear	tail	produce\n"
            + "fact	street	inch	lot	nothing	course	stay\n"
            + "wheel	full	force	blue	object	decide	surface\n"
            + "deep	moon	island	foot	yet	busy	test\n"
            + "record	boat	common	gold	possible	plane	age\n"
            + "dry	wonder	laugh	thousand	ago	ran	check\n"
            + "game	shape	yes	hot	miss	brought	heat\n"
            + "snow	bed	bring	sit	perhaps	fill	east\n"
            + "weight	language	among";*/

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/gmail-java-quickstart.json
     */
    private static final List<String> SCOPES
            = Arrays.asList(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_MODIFY);

    static {
         try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }

    }

    public static List<String> senderEmails(Gmail service, String user, List<Message> recievedMessagesList) throws IOException {
        System.out.println("\nFETCHING senderEmails -------------------------");
        
        int totalReceivedMessages=recievedMessagesList.size();
        int numberOfEmailIdsAdded = 1,step=10,dots=0;
        System.out.print("TOTAL NUMBER OF RECIEVED EMAILS: "+totalReceivedMessages+"\n PROGRESS: ");
        List<String> senderEmail = new ArrayList<String>();
        for (Message m : recievedMessagesList) {
            
             float percent=(numberOfEmailIdsAdded/(float)totalReceivedMessages)*100;
            if(percent%step<1&&percent>10)
            {
                for(int i=0;i<dots;i++)
                 System.out.print("\b");
                dots=0;
                 System.out.print((int)percent+" ");
                 step+=10;
                
            }
            else
            {   
                dots++;
                System.out.print(".");
            }
            
            //System.out.print(numberOfEmailIdsAdded+"-");
            numberOfEmailIdsAdded++;
            // System.out.println(m.getId());
            Message message=null;
            boolean fetchedMessage=false;
            while(fetchedMessage==false)
            {
                try{
                    message = service.users().messages().get(user, m.getId()).setFormat("full").execute();
                    fetchedMessage=true;
                }
                catch(Exception e)
                {
                    
                }
            }
            List<MessagePartHeader> headerList = message.getPayload().getHeaders();

            for (MessagePartHeader header : headerList) {
                if (header.getName().equals("From")) {
                    senderEmail.add(header.getValue());
                }
            }

        }
        System.out.println();
        return senderEmail;

    }

    public static List<String> recipientEmails(Gmail service, String user, List<Message> sentMessagesList) throws IOException {
         System.out.println("FETCHING RECIPIENT Emails -------------------------");
        
        int totalSentMessages=sentMessagesList.size();
        int numberOfEmailIdsAdded = 1,step=10,dots=0;
        System.out.print("TOTAL NUMBER OF RECIEVED EMAILS: "+totalSentMessages+"\n PROGRESS: ");
        List<String> recipientEmail = new ArrayList<String>();
        for (Message m : sentMessagesList) {
            
             float percent=(numberOfEmailIdsAdded/(float)totalSentMessages)*100;
            if(percent%step<1&&percent>10)
            {
                for(int i=0;i<dots;i++)
                 System.out.print("\b");
                dots=0;
                 System.out.print((int)percent+" ");
                 step+=10;
                
            }
            else
            {   
                dots++;
                System.out.print(".");
            }
            
           //System.out.print(numberOfEmailIdsAdded+"-");
            numberOfEmailIdsAdded++;
            // System.out.println(m.getId());
            Message message=null;
            boolean fetchedMessage=false;
            while(fetchedMessage==false)
            {
                try{
                    message = service.users().messages().get(user, m.getId()).setFormat("full").execute();
                    fetchedMessage=true;
                }
                catch(Exception e)
                {
                    
                }
            }
            List<MessagePartHeader> headerList = message.getPayload().getHeaders();

            for (MessagePartHeader header : headerList) {
                if (header.getName().equals("To")) {
                    recipientEmail.add(header.getValue());
                }
            }

        }
         System.out.println();
        return recipientEmail;

    }

    public static int numberOfSentMailsWithSearchString(Gmail service, String user, List<Message> sentMessagesList, String searchString) throws IOException {
        int numberOfSentMailsWithSearchString = 0;

        for (Message m : sentMessagesList) {

            Message message = service.users().messages().get(user, m.getId()).setFormat("full").execute();

            List<MessagePartHeader> headerList = message.getPayload().getHeaders();  //for getting sender or recipient email
            List<MessagePart> partsList = message.getPayload().getParts();           //for getting encoded data of email

            //printing email data containing the searchString
            for (MessagePart mp : partsList) {
                byte[] byteData = null;
                String data = null;
                // String searchString=new String("the");
                if (mp.getHeaders().get(0).getValue().equals("text/plain; charset=UTF-8")) {
                    byteData = Base64.decodeBase64(mp.getBody().getData());
                    data = new String(byteData);
                }

                String recipient = null;
                for (MessagePartHeader header : headerList) {
                    if (header.getName().equals("To")) {
                        //System.out.println("To :"+header.getValue());
                        recipient = header.getValue();
                    }
                }
                String sender = null;
                for (MessagePartHeader header : headerList) {
                    if (header.getName().equals("From")) {
                        //System.out.println("To :"+header.getValue());
                        sender = header.getValue().substring(header.getValue().indexOf("<") + 1, header.getValue().indexOf(">"));
                    }
                }

                if (data != null) {

                    int end = 0;
                    if (data.indexOf(recipient) == -1 && data.indexOf(sender) == -1) {
                        end = data.length();
                    } else if (data.indexOf(recipient) == -1) {
                        end = data.indexOf(sender);
                    } else if (data.indexOf(sender) == -1) {
                        end = data.indexOf(recipient);
                    } else {
                        end = data.indexOf(recipient) < data.indexOf(sender) ? data.indexOf(recipient) : data.indexOf(sender);
                    }

                    /* if(data.indexOf("wrote:")==-1)
                     end=data.length();
                 else end=data.indexOf("wrote:");*/
                    String filteredData = data.substring(0, end);

                    String resp = (!filteredData.toLowerCase().contains(searchString.toLowerCase()) ? "NO" : "YES");

                    if (resp.equals("YES")) {
                        numberOfSentMailsWithSearchString++;
                    }

                }

            }

        }
        return numberOfSentMailsWithSearchString;

    }

    public static int numberOfRecievedMailsWithSearchString(Gmail service, String user, List<Message> recievedMessagesList, String searchString) throws IOException, NullPointerException {
        int numberOfRecievedMailsWithSearchString = 0;

        a:
        for (Message m : recievedMessagesList) {

            Message message = service.users().messages().get(user, m.getId()).setFormat("full").execute();

            List<MessagePartHeader> headerList = message.getPayload().getHeaders();  //for getting sender or recipient email
            List<MessagePart> partsList = message.getPayload().getParts();           //for getting encoded data of email

            MessagePart mp = null;
            try {
                for (int i = 0; i < partsList.size(); i++) {
                    mp = partsList.get(i);

                    byte[] byteData = null;
                    String data = null;
                    // String searchString=new String("the");

                    if (mp.getHeaders().get(0).getValue().equals("text/plain; charset=UTF-8")) {
                        byteData = Base64.decodeBase64(mp.getBody().getData());
                        data = new String(byteData);
                    }

                    String recipient = null;
                    for (MessagePartHeader header : headerList) {
                        if (header.getName().equals("To")) {
                            // System.out.println("To :"+header.getValue());
                            recipient = header.getValue();
                            break;
                        }
                    }
                    String sender = null;
                    for (MessagePartHeader header : headerList) {
                        if (header.getName().equals("From")) {
                            //System.out.println("To :"+header.getValue());
                            sender = header.getValue().substring(header.getValue().indexOf("<") + 1, header.getValue().indexOf(">"));
                            break;
                        }
                    }

                    if (sender.equals("aadhaarauth@googlegroups.com")) {
                        System.out.println("SKIPPED!!!");
                        continue a;
                    }
                    if (data != null) {

                        int end = 0;
                        if (data.indexOf(recipient) == -1 && data.indexOf(sender) == -1) {
                            end = data.length();
                        } else if (data.indexOf(recipient) == -1) {
                            end = data.indexOf(sender);
                        } else if (data.indexOf(sender) == -1) {
                            end = data.indexOf(recipient);
                        } else {
                            end = data.indexOf(recipient) < data.indexOf(sender) ? data.indexOf(recipient) : data.indexOf(sender);
                        }

                        /* if(data.indexOf("wrote:")==-1)
                     end=data.length();
                 else end=data.indexOf("wrote:");*/
                        String filteredData = data.substring(0, end);

                        String resp = (!filteredData.toLowerCase().contains(searchString.toLowerCase()) ? "NO" : "YES");

                        if (resp.equals("YES")) {
                            numberOfRecievedMailsWithSearchString++;
                            System.out.println("From: " + sender + "   id: " + m.getId());
                        }

                    }

                }
            } catch (Exception e) {
            }// System.out.println(mp); System.out.println(m.getId());}

        }
        return numberOfRecievedMailsWithSearchString;

    }

    public static void numberOfMessagesByWeekAndHours(Gmail service, String user, List<Message> MessagesList, int days[], int hours[]) throws IOException {

        System.out.println("CALCULATING NUMBER OF MESSAGES BY WEEK AND HOURS");
        int totalMessages=MessagesList.size();
        
        System.out.println("TOTAL NUMBER OF MESSAGES; "+totalMessages);
        
        int numberOfMessagesProcessed=1,step=10,dots=0;           
        
        System.out.print("PROGRESS: ");
        for (Message m : MessagesList) {
            
            
             float percent=(numberOfMessagesProcessed/(float)totalMessages)*100;
            if(percent%step<1&&percent>10)
            {
                for(int i=0;i<dots;i++)
                 System.out.print("\b");
                dots=0;
                 System.out.print((int)percent+" ");
                 step+=10;
                
            }
            else
            {   
                dots++;
                System.out.print(".");
            }
            //System.out.print(numberOfMessagesProcessed+"-");
            numberOfMessagesProcessed++;
            
            Message message=null;
            boolean fetchedMessage=false;
            while(fetchedMessage==false)
            {
                try{
                    message = service.users().messages().get(user, m.getId()).setFormat("full").execute();
                    fetchedMessage=true;
                }
                catch(Exception e)
                {
                    
                }
            }
            List<String> labelIds = message.getLabelIds();

           // System.out.println("id: " + message.getId());
          //  System.out.println("List of labelIds: ");
            for (String label : labelIds) {
               // System.out.print(label + " ");
            }
            //System.out.println();

            List<MessagePartHeader> headerList = message.getPayload().getHeaders();

            for (MessagePartHeader header : headerList) {

                if (header.getName().equals("Date")) {
                    //For message by weekDays
                    String date = header.getValue();
                    int dayNumber = new Date(date).getDay();
                    //   int dayNumber=dayToNumber(date.substring(0, 3));
                    days[dayNumber]++;

                    //For messages by hour
                    SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
                    String time = localDateFormat.format(new Date(date));
                    int hour = Integer.parseInt(time.substring(0, 2));
                    hours[hour]++;
                }

            }

        }
        
        
         System.out.println("\nMessages by Weeks: ");
        for (int i = 0; i < 7; i++) {
            System.out.println(numberToDay(i) + ": " + days[i]);
        }

        System.out.println("\nMessages by hours: ");
        for (int i = 0; i < 24; i++) {
            System.out.println(i + "to" + (i + 1) + ": " + hours[i]);
        }

    }

    public static String numberToDay(int dayNumber) {
        String day = "";
        switch (dayNumber) {
            case 1:
                day = "Mon";
                break;
            case 2:
                day = "Tue";
                break;
            case 3:
                day = "Wed";
                break;
            case 4:
                day = "Thu";
                break;
            case 5:
                day = "Fri";
                break;
            case 6:
                day = "Sat";
                break;
            case 0:
                day = "Sun";
                break;
        }
        return day;
    }

    public static String millisToFormattedTime(long ms) {
        int s = (int) (ms / 1000);
        int y = s / (60 * 60 * 24 * 365);
        s %= (60 * 60 * 24 * 365);
        int d = s / (60 * 60 * 24);
        s %= (60 * 60 * 24);
        int h = s / (60 * 60);
        s %= (60 * 60);
        int min = s / 60;
        s %= 60;

        System.out.printf("Time: %d:%d:%d:%d:%d\n", y, d, h, min, s);

        return (y + ":" + d + ":" + h + ":" + min + ":" + s);

    }

  

    public static String[] responseTime(Gmail service, String user, List<Message> sentMessagesList) throws IOException, JSONException {
        String responseTimes[] = new String[2];
        int quickestResponseTime = -1;
        long averageResponseTime = 0;

        int selfStarted = 1;
        
        System.out.println("FETCHING RESPONSE TIME: ");
        
        int totalMessages=sentMessagesList.size();
        System.out.println("TOTAL MESSAGES: "+totalMessages);
        
        int numberOfMessagesProcessed=1;
        System.out.print("PROGRESS: ");
        for (Message m : sentMessagesList) {
            
            System.out.print(numberOfMessagesProcessed+"-");
            numberOfMessagesProcessed++;

            if (m.getId().equals(m.getThreadId())) {
               // System.out.println("You have started the mail! ID: " + m.getId());
            } else {
                selfStarted = 0;
                int s;

               // System.out.println("Sent Message id: " + m.getId());

                Message sentMessage = service.users().messages().get(user, m.getId()).execute();
                long sentMessageTime = sentMessage.getInternalDate();
                long recievedMessageTime = -1;

//-----------------------------IN-REPLY-TO----------------------------------------------------------------------           
                String inReplyTo = "";
                List<MessagePartHeader> sentMessageHeaders = sentMessage.getPayload().getHeaders();
                for (MessagePartHeader header : sentMessageHeaders) {
                    if (header.getName().equals("In-Reply-To")) {
                        inReplyTo = header.getValue();
                        break;
                    }

                }
//----------------------------------------------------------------------------------------------------------

//-----------------------------FETCHING THREADS-----------------------------------------------------------------
                List<Message> thread = service.users().threads().get(user, m.getThreadId()).execute().getMessages();

                //-----------------------FETCHING EMAILS FROM THREAD----------------------------------------------------------        
                //   System.out.println("Messages from thread");
                for (Message email : thread) {

                    //----------FETCHING MESSAGE-ID-----------------------------------------------------------                
                    String messageID = "";
                    List<MessagePartHeader> threadMessageHeaders = email.getPayload().getHeaders();
                    for (MessagePartHeader header : threadMessageHeaders) {

                        if (header.getName().equals("Message-ID")) {
                            messageID = header.getValue();
                            // System.out.println("message-ID: "+messageID);
                            break;
                        }

                    }
                    //----------------------------------------------------------------------------------------------     
                    //------------------COMPARING inReplyTo and messageId------------------------------------------------------------------------------                 
                    if (inReplyTo.equals(messageID)) {
                        recievedMessageTime = email.getInternalDate();
                        //  System.out.println("Recieved Message: \nInternal date:"+email.getInternalDate());
                       // System.out.println("recieved message ID: " + email.getId());
                        break;
                    }
                    //------------------------------------------------------------------------------------------------------------------------                 

                }

//-----------------------------------------------------------------------------------------------------------------------------------------                      
                s = (int) ((sentMessageTime - recievedMessageTime) / 1000);

                System.out.println("Response time in sec: " + s);

                averageResponseTime += s;
                if ((quickestResponseTime < 0 || quickestResponseTime > s) && s != 0) {
                    quickestResponseTime = s;
                }

                int d = s / (60 * 60 * 24);
                s %= (60 * 60 * 24);
                int h = s / (60 * 60);
                s %= (60 * 60);
                int min = s / 60;
                s %= 60;
                System.out.printf("Response Time: %d:%d:%d:%d\n", d, h, min, s);
                //  System.out.println("sentMessageID: "+sentMessage.getId()+ " recievedMessageID: "+recievedMessage.getId());

            }

        }

        System.out.println();
        if (selfStarted == 0) {

            long s = averageResponseTime / sentMessagesList.size();
            long d = (s / (60 * 60 * 24));
            s %= (60 * 60 * 24);
            long h = s / (60 * 60);
            s %= (60 * 60);
            long min = s / 60;
            s %= 60;
            System.out.println("Average response time in sec: " + averageResponseTime);
            System.out.printf("Average response Time: %d:%d:%d:%d\n", d, h, min, s);

            responseTimes[1] = d + ":" + h + ":" + min + ":" + s;

            s = (int) quickestResponseTime;
            d = s / (60 * 60 * 24);
            s %= (60 * 60 * 24);
            h = s / (60 * 60);
            s %= (60 * 60);
            min = s / 60;
            s %= 60;
            System.out.println("Quickest response time in sec: " + quickestResponseTime);
            System.out.printf("Quickest Response Time: %d:%d:%d:%d\n", d, h, min, s);

            responseTimes[0] = d + ":" + h + ":" + min + ":" + s;

        }

        return responseTimes;

    }

    public static int[] getAverage(long elapsedTimeMillis, List<Message> messageList) {

        int elapsedTimeDays = (int) (elapsedTimeMillis / (1000 * 60 * 60 * 24));
        int elapsedTimeWeeks = elapsedTimeDays / 7;
        int elapsedTimeMonths = elapsedTimeDays / 30;
        int elapsedTimeYears = elapsedTimeDays / 365;

       
        int dailyAvg = messageList.size() / elapsedTimeDays;
        System.out.println("daily avg: " + dailyAvg);

        int weeklyAvg=0;
        try{
        weeklyAvg = messageList.size() / elapsedTimeWeeks;
        System.out.println("weekly avg: " + weeklyAvg);

        }catch(Exception e)
        {
            System.out.println("weekly avg: " + weeklyAvg);
        }

         int monthlyAvg=0;
        try{
            monthlyAvg = messageList.size() / elapsedTimeMonths;
        System.out.println("monthly avg: " + monthlyAvg);
        }catch(Exception e)
        {
            System.out.println("monthly avg: " + monthlyAvg);

            
        }

        int yearlyAvg = 0;
        try {
            yearlyAvg = messageList.size() / elapsedTimeYears;
            System.out.println("yearly avg: " + yearlyAvg);

        } catch (Exception e) {
        }

        int[] average = {dailyAvg, weeklyAvg, monthlyAvg, yearlyAvg};

        return average;

    }

    public static long getAccountAge(Gmail service, String user, List<Message> inboxList, List<Message> sentList, List<Message> trashList) throws IOException {
        long presentDate = new Date().getTime();
        long inTime;
        long seTime;
        long trTime;
        String id;
        try{
        id = inboxList.get(inboxList.size() - 1).getId();
        Message firstInboxMessage = service.users().messages().get(user, id).execute();
        System.out.println("First inbox message: " + firstInboxMessage.getId() + " . Recieved on: ");
        millisToFormattedTime((presentDate) - firstInboxMessage.getInternalDate());
        inTime = presentDate - firstInboxMessage.getInternalDate();
        }
        catch(Exception e)
        {
            inTime=-1;
        }
                
        try{
        id = sentList.get(sentList.size() - 1).getId();
        Message firstSentMessage = service.users().messages().get(user, id).execute();
        System.out.println("First sent message: " + firstSentMessage.getId() + " . Recieved on: ");
        millisToFormattedTime((presentDate) - firstSentMessage.getInternalDate());
        seTime = presentDate - firstSentMessage.getInternalDate();
        }catch(Exception e)
        {
            seTime=-1;
        }

        try{
        id = trashList.get(trashList.size() - 1).getId();
        Message firstTrashMessage = service.users().messages().get(user, id).execute();
        System.out.println("First trash message: " + firstTrashMessage.getId() + " . Recieved on: ");
        millisToFormattedTime((presentDate) - firstTrashMessage.getInternalDate());
        trTime = presentDate - firstTrashMessage.getInternalDate();
        }
        catch(Exception e)
        {
            trTime=-1;
        }
       

        if (inTime > seTime && inTime > trTime) {
            System.out.print("Approx. account age: ");
            millisToFormattedTime(inTime);
            return inTime;
        } else if (seTime > trTime) {
            System.out.print("Approx. account age: ");
            millisToFormattedTime(seTime);
            return seTime;
        } else {
            System.out.print("Approx. account age: ");
            millisToFormattedTime(trTime);
            return trTime;
        }

    }

    

    /**
     * Get Message with given ID.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @param messageId ID of Message to retrieve.
     * @return Message Retrieved Message.
     * @throws IOException
     */
    public static void /*Message*/ getMessage(Gmail service, String userId, String messageId)
            throws IOException {
        Message message = service.users().messages().get(userId, messageId).execute();

        System.out.println("Message snippet: " + message.getSnippet());
        System.out.println("Message internalDate: " + message.getInternalDate());

        //  return message;
    }

    public static String gmailFormat(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

      //  System.out.println("Date in gmail format: " + sdf.format(d));
        return sdf.format(d);
    }

     public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    

    private static float compareMailBodies(String mailBody, Vector<String> mailBodies) {

        Vector<String> splittedMail = new Vector<>();
        StringTokenizer tok = new StringTokenizer(mailBody.toLowerCase(), " -,\n.;\"\':*\u0000</>+_={}[])(");
        while (tok.hasMoreTokens()) {
            splittedMail.add(tok.nextToken());

        }
       // System.out.println("Mail body: " + mailBody);

        float compareRatio = 0;

        for (String prevMail : mailBodies) {

            Vector<String> splittedMailPrev = new Vector<>();
            tok = new StringTokenizer(prevMail.toLowerCase(), " -,\n.;\"\':*\u0000</>+_={}[])(");
            while (tok.hasMoreTokens()) {
                splittedMailPrev.add(tok.nextToken());

            }

            int numberOfMatchedWords = 0;
            int totalWords = splittedMailPrev.size();
            if (totalWords != 0) {

                for (String word : splittedMail) {
                    if (prevMail.contains(word)) {
                        numberOfMatchedWords++;
                    }
                }

                compareRatio += (numberOfMatchedWords / (float) totalWords) * 100;
            }
            if (numberOfMatchedWords == 0 || totalWords == 0) {

              //  System.out.println(prevMail);
              //  System.out.println(numberOfMatchedWords + " " + totalWords);
            }
        }

        return compareRatio;

    }

    public static Map getTop10RecurringEmails(Gmail service, String user, List<Message> emailList) throws IOException 
    {
        
        
        Map<String, Vector<String>> idBodyMap = new ArrayMap<>();
        Map<String, Float> idScoreMap = new ArrayMap<>();
        
        
        int totalEmails=emailList.size();
        System.out.println("Total number of emails: "+totalEmails);

        int numberOfMailsProcessed=1,dots=0,step=10;
        System.out.print("PROGRESS: ");
        for (Message m : emailList) {
            
            
             float percent=(numberOfMailsProcessed/(float)totalEmails)*100;
            if(percent%step<1&&percent>10)
            {
                for(int i=0;i<dots;i++)
                 System.out.print("\b");
                dots=0;
                 System.out.print((int)percent+" ");
                 step+=10;
                
            }
            else
            {   
                dots++;
                System.out.print(".");
            }
            
            //System.out.print(numberOfMailsProcessed+"-");
            numberOfMailsProcessed++;
            
            
            Message mail=null;
            boolean fetchedMail=false;
            
//-----------FETCHING MAIL------------------------------------------------------------------------------------------------------------------            
        while(fetchedMail==false)
        {
            try
            {
            mail = service.users().messages().get(user, m.getId()).setFormat("full").execute();
            fetchedMail=true;
            }
            catch(Exception e)
            {
             fetchedMail=false;
                
            }
        }
//--------------------------------------------------------------------------------------------------------------------------------------         
            
            String emailId = "", mailBody = "";
            
            
//-----------FETCHING SENDERS EMAIL-ID----------------------------------------------------------------------------------------------------------            
            List<MessagePartHeader> headerList = mail.getPayload().getHeaders();

            for (MessagePartHeader header : headerList) {
                if (header.getName().equals("From")) {
                    emailId = header.getValue();
                }
            }
//------------------------------------------------------------------------------------------------------------------------------------------------


//-------------FETCHING MAIL BODY-----------------------------------------------------------------------------------------------------------------

            List<MessagePart> partsList = mail.getPayload().getParts();           //for getting encoded data of email
            try {
                for (MessagePart mp : partsList) {
                    byte[] byteData = null;
                    String data = null;
                    // String searchString=new String("the");
                    if (mp.getHeaders().get(0).getValue().equals("text/plain; charset=UTF-8")) {
                        byteData = Base64.decodeBase64(mp.getBody().getData());
                        data = new String(byteData);
                    }

                    if (data != null) {
                        int end;
                        if (data.indexOf("wrote:") == -1) {
                            end = data.length();
                        } else {
                            end = data.indexOf("wrote:");
                        }
                        mailBody = data.substring(0, end);

                    }
                }
            } catch (Exception e) {
                continue;
                /*
                String encodedBody = mail.getPayload().getBody().getData();
                byte[] byteData = Base64.decodeBase64(encodedBody);
                String data = new String(byteData);

                if (data != null) {
                    int end;
                    if (data.indexOf("wrote:") == -1) {
                        end = data.length();
                    } else {
                        end = data.indexOf("wrote:");
                    }
                    mailBody = data.substring(0, end);

                }*/

            }
//--------------------------------------------------------------------------------------------------------------------------------------------


//------------CALCULATING COMPARE SCORE------------------------------------------------------------------------------------------------------
            

            //CALCULATING COMPARE-SCORE OF MAIL OF EXISTING EMAIL-ID IN THE idBody MAP 
            if (idBodyMap.containsKey(emailId)) {

                Vector<String> bodies = idBodyMap.get(emailId);

                float compareScore = compareMailBodies(mailBody, bodies);

                //System.out.print("email-id: " + emailId + "\ncompare score: " + compareScore);
                //System.out.println("id:" + m.getId());
                bodies.add(mailBody);

                idBodyMap.put(emailId, bodies);

                float prevScore;
                if (idScoreMap.containsKey(emailId)) {
                    prevScore = idScoreMap.get(emailId);
                } else {
                    prevScore = 0;
                }

                idScoreMap.put(emailId, compareScore + prevScore);

            } 
            
            
            // ADDING NEW EMAIL-ID and BODY TO THE idBody MAP
            else {
                Vector<String> bodies = new Vector<String>();
                bodies.add(mailBody);
                idBodyMap.put(emailId, bodies);
            }

        }

        System.out.println("");
//-----------------------------------------------------------------------------------------------------------------------------        
        
        return idScoreMap;

    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
            new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    public static String[] getTop10Words(Gmail service, String user, String q,List<String> labelIds) throws IOException {
        int[] frequency = new int[10];
        String[] top10Words = new String[10];

        Map<String, Integer> wordMap = new ArrayMap<>();
        List<String> commonWords = Arrays.asList(COMMON_WORDS_LIST_1.toLowerCase().split("\n"));

        ListMessagesResponse response = service.users().messages().list(user).setLabelIds(labelIds).setQ(q).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(user).setLabelIds(labelIds)
                        .setQ(q).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        
        int totalMsgs=messages.size();
        System.out.println("TOTAL MESSAGES: "+totalMsgs);
        int msgsFetched=1,step=10;
        System.out.println("PROGRESS: ");
        
        int dots=0;
        for (Message m : messages) {
            
            float percent=(msgsFetched/(float)totalMsgs)*100;
            if(percent%step<1&&percent>10)
            {
                for(int i=0;i<dots;i++)
                 System.out.print("\b");
                dots=0;
                 System.out.print((int)percent+" ");
                 step+=10;
                
            }
            else
            {   
                dots++;
                System.out.print(".");
            }
           
           
            msgsFetched++;

            Message message=null;
            boolean fetchedMessage=false;
            while(fetchedMessage==false)
            {
                try{
                    message=service.users().messages().get(user, m.getId()).setFormat("full").execute();
                    fetchedMessage=true;
                }
                catch(Exception ex)
                {
                    fetchedMessage=false;
                }
            }

            // List<MessagePartHeader> headerList = message.getPayload().getHeaders();  //for getting sender or recipient email
            List<MessagePart> partsList = message.getPayload().getParts();           //for getting encoded data of email

            String filteredData = "";
            //printing email data containing the searchString
            try {
                for (MessagePart mp : partsList) {
                    byte[] byteData = null;
                    String data = null;
                    // String searchString=new String("the");
                    if (mp.getHeaders().get(0).getValue().equals("text/plain; charset=UTF-8")) {
                        byteData = Base64.decodeBase64(mp.getBody().getData());
                        data = new String(byteData);
                    }

                    if (data != null) {
                        int end;
                        if (data.indexOf("wrote:") == -1) {
                            end = data.length();
                        } else {
                            end = data.indexOf("wrote:");
                        }
                        filteredData = data.substring(0, end);

                        if (filteredData.contains("apigee")) {
//                            System.out.println("Found APIGEE");
//                            System.out.println("Message id: "+message.getId());
//                            System.out.println("Filtered data: "+ filteredData);
                        }
                    }
                }
            } catch (Exception e) {
                continue;
               /* String encodedBody = message.getPayload().getBody().getData();
                byte[] byteData = Base64.decodeBase64(encodedBody);
                String data = new String(byteData);

                if (data != null) {
                    int end;
                    if (data.indexOf("wrote:") == -1) {
                        end = data.length();
                    } else {
                        end = data.indexOf("wrote:");
                    }
                    filteredData = data.substring(0, end);

                }*/
            }

//            System.out.println("\n\n\n---------------------------------------");
//            System.out.println("message id: "+message.getId() +"\nmail body:\n" + filteredData);

            // String[] words = filteredData.split(" ");
            Vector<String> words = new Vector<>();
            StringTokenizer tok = new StringTokenizer(filteredData.toLowerCase(),"\r\t\n \u200b\u0009(){}[]<>");// " -,\n.;\"\':*\u0000\u0009</>+_={}[])("+"\r");
            
            
            while (tok.hasMoreTokens()) {
                String token=tok.nextToken();
               // System.out.println("token: "+token);
                if(!(token.contains("http")||token.contains("?") ||( token.contains("@")&&token.contains("."))))
                {
                    if(token.endsWith(",")||token.endsWith(".")||token.endsWith(":"))
                    {
                        words.add(token.substring(0,token.length()-1));
                       // System.out.println("token added: "+token);
                    }
                    else
                    {
                       // System.out.println("token added: "+token);
                        words.add(token);
                    }
                }
                

            }
            //StringTokenizer tok2=new StringTokenizer(filteredData," -,\n.;\"\'");
            List<String> wordList = new ArrayList<>();

            for (String s : words) {
                //String s=tok1.nextToken();
                int c = 0;  //s.charAt(0) > 122 && s.charAt(0) < 97
                if (!( s.length()<2||USER.contains(s)||wordList.contains(s) || COMMON_WORDS_LIST_2.contains(s) || isNumeric(s.trim()))) {
                    wordList.add(s);
                    for (String w : words) {
                        //String w=tok2.nextToken();
                        if (s.equals(w)) {
                            c++; 
                        }
                    }
                    if (wordMap.containsKey(s)) {
                        wordMap.put(s, wordMap.get(s) + c);
                    } else {
                        wordMap.put(s, c);

                    }

                }

            }

        }
        System.out.println("");

        for (String key : wordMap.keySet()) {
            if (key.length() == 1 && key.charAt(0)>90&& key.charAt(0)<65&& key.charAt(0)<97&& key.charAt(0)>122) {
               // System.out.println("_______________________________________");
               // System.out.println((int) key.charAt(0));
              //  System.out.println("_______________________________________");
            } else {
               System.out.println(key + ": " + wordMap.get(key));
               
               /* for(char c:key.toCharArray())
                {
                    System.out.print((int)c+" ");
                }
                System.out.println();*/
            }

            if (key.charAt(0) != 13) {
                for (int i = 9; i >= 0; i--) {
                    if (wordMap.get(key) > frequency[i]) {
                        if (i == 0 || wordMap.get(key) <= frequency[i - 1]) {
                            frequency[i] = wordMap.get(key);
                            top10Words[i] = key;

                        } else if (wordMap.get(key) > frequency[i - 1]) {
                            continue;
                        }
                    } else {
                        break;
                    }
                }
            }

        }
        
        wordMap = sortByValue( wordMap );

        // System.out.println("the unknown character:"+((int)top10Words[0].charAt(0)));
        
        /*
        System.out.println("----------------------\n     TOP 10 words\n----------------------");
        for (int i = 0; i < 10 && frequency[i] != 0; i++) {
           // System.out.print((int)top10Words[i].charAt(0)+" ");
            System.out.println(top10Words[i] + " : " + frequency[i]);
        }*/
        
        gap();
                System.out.println("\n SORTED WORD MAP:");

        
        for(Map.Entry<String, Integer> entry : wordMap.entrySet())
        {
            
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        
        gap();
        
        int i=0;
        for(Map.Entry<String, Integer> entry : wordMap.entrySet())
        {
            top10Words[i]=entry.getKey();
            i++;
            if(i==10)break;
        }

        //Map<String[],int[]> ans=new ArrayMap<>();
        //ans.put(top10Words, frequency);
        return top10Words;

    }
    
    public static String[] getTop10WordsBody(Gmail service, String user, String q,List<String> labelIds) throws IOException {
        int[] frequency = new int[10];
        String[] top10Words = new String[10];

        Map<String, Integer> wordMap = new ArrayMap<>();
        List<String> commonWords = Arrays.asList(COMMON_WORDS_LIST_1.toLowerCase().split("\n"));

        ListMessagesResponse response = service.users().messages().list(user).setLabelIds(labelIds).setQ(q).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(user).setLabelIds(labelIds)
                        .setQ(q).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        
        int totalMsgs=messages.size();
        System.out.println("TOTAL MESSAGES: "+totalMsgs);
        int msgsFetched=1,step=10;
        System.out.println("PROGRESS: ");
        
        int dots=0;
        for (Message m : messages) {
            
            float percent=(msgsFetched/(float)totalMsgs)*100;
            if(percent%step<1&&percent>10)
            {
                for(int i=0;i<dots;i++)
                 System.out.print("\b");
                dots=0;
                 System.out.print((int)percent+" ");
                 step+=10;
                
            }
            else
            {   
                dots++;
                System.out.print(".");
            }
           
           
            msgsFetched++;

            Message message=null;
            boolean fetchedMessage=false;
            while(fetchedMessage==false)
            {
                try{
                    message=service.users().messages().get(user, m.getId()).setFormat("full").execute();
                    fetchedMessage=true;
                }
                catch(Exception ex)
                {
                    fetchedMessage=false;
                }
            }

            // List<MessagePartHeader> headerList = message.getPayload().getHeaders();  //for getting sender or recipient email
            List<MessagePart> partsList = message.getPayload().getParts();           //for getting encoded data of email

            String filteredData = "";
            //printing email data containing the searchString
            try {
                for (MessagePart mp : partsList) {
                    byte[] byteData = null;
                    String data = null;
                    // String searchString=new String("the");
                    if (mp.getHeaders().get(0).getValue().equals("text/plain; charset=UTF-8")) {
                        byteData = Base64.decodeBase64(mp.getBody().getData());
                        data = new String(byteData);
                    }

                    if (data != null) {
                        int end;
                        if (data.indexOf("wrote:") == -1) {
                            end = data.length();
                        } else {
                            end = data.indexOf("wrote:");
                        }
                        filteredData = data.substring(0, end);

                        if (filteredData.contains("apigee")) {
//                            System.out.println("Found APIGEE");
//                            System.out.println("Message id: "+message.getId());
//                            System.out.println("Filtered data: "+ filteredData);
                        }
                    }
                }
            } catch (Exception e) {
                continue;
               /* String encodedBody = message.getPayload().getBody().getData();
                byte[] byteData = Base64.decodeBase64(encodedBody);
                String data = new String(byteData);

                if (data != null) {
                    int end;
                    if (data.indexOf("wrote:") == -1) {
                        end = data.length();
                    } else {
                        end = data.indexOf("wrote:");
                    }
                    filteredData = data.substring(0, end);

                }*/
            }

//            System.out.println("\n\n\n---------------------------------------");
//            System.out.println("message id: "+message.getId() +"\nmail body:\n" + filteredData);

            // String[] words = filteredData.split(" ");
            Vector<String> words = new Vector<>();
            StringTokenizer tok = new StringTokenizer(filteredData.toLowerCase(),"\r\t\n \u200b\u0009(){}[]<>");// " -,\n.;\"\':*\u0000\u0009</>+_={}[])("+"\r");
            
            
            while (tok.hasMoreTokens()) {
                String token=tok.nextToken();
               // System.out.println("token: "+token);
                if(!(token.contains("http")||token.contains("?") ||( token.contains("@")&&token.contains("."))))
                {
                    if(token.endsWith(",")||token.endsWith(".")||token.endsWith(":"))
                    {
                        words.add(token.substring(0,token.length()-1));
                       // System.out.println("token added: "+token);
                    }
                    else
                    {
                       // System.out.println("token added: "+token);
                        words.add(token);
                    }
                }
                

            }
            //StringTokenizer tok2=new StringTokenizer(filteredData," -,\n.;\"\'");
            List<String> wordList = new ArrayList<>();

            for (String s : words) {
                //String s=tok1.nextToken();
                int c = 0;  //s.charAt(0) > 122 && s.charAt(0) < 97
                if (!( s.length()<2||USER.contains(s)||wordList.contains(s) || COMMON_WORDS_LIST_2.contains(s) || isNumeric(s.trim()))) {
                    wordList.add(s);
                    for (String w : words) {
                        //String w=tok2.nextToken();
                        if (s.equals(w)) {
                            c++; 
                        }
                    }
                    if (wordMap.containsKey(s)) {
                        wordMap.put(s, wordMap.get(s) + c);
                    } else {
                        wordMap.put(s, c);

                    }

                }

            }

        }
        System.out.println("");

        for (String key : wordMap.keySet()) {
            if (key.length() == 1 && key.charAt(0)>90&& key.charAt(0)<65&& key.charAt(0)<97&& key.charAt(0)>122) {
               // System.out.println("_______________________________________");
               // System.out.println((int) key.charAt(0));
              //  System.out.println("_______________________________________");
            } else {
               System.out.println(key + ": " + wordMap.get(key));
               
               /* for(char c:key.toCharArray())
                {
                    System.out.print((int)c+" ");
                }
                System.out.println();*/
            }

            if (key.charAt(0) != 13) {
                for (int i = 9; i >= 0; i--) {
                    if (wordMap.get(key) > frequency[i]) {
                        if (i == 0 || wordMap.get(key) <= frequency[i - 1]) {
                            frequency[i] = wordMap.get(key);
                            top10Words[i] = key;

                        } else if (wordMap.get(key) > frequency[i - 1]) {
                            continue;
                        }
                    } else {
                        break;
                    }
                }
            }

        }
        
        wordMap = sortByValue( wordMap );

        // System.out.println("the unknown character:"+((int)top10Words[0].charAt(0)));
        
        /*
        System.out.println("----------------------\n     TOP 10 words\n----------------------");
        for (int i = 0; i < 10 && frequency[i] != 0; i++) {
           // System.out.print((int)top10Words[i].charAt(0)+" ");
            System.out.println(top10Words[i] + " : " + frequency[i]);
        }*/
        
        gap();
                System.out.println("\n SORTED WORD MAP:");

        
        for(Map.Entry<String, Integer> entry : wordMap.entrySet())
        {
            
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        
        gap();
        
        int i=0;
        for(Map.Entry<String, Integer> entry : wordMap.entrySet())
        {
            top10Words[i]=entry.getKey();
            i++;
            if(i==10)break;
        }

        //Map<String[],int[]> ans=new ArrayMap<>();
        //ans.put(top10Words, frequency);
        return top10Words;

    }

    
    public static String[] getTop10WordsSubject(Gmail service, String user, String q,List<String> labelIds) throws IOException {
        int[] frequency = new int[10];
        String[] top10Words = new String[10];

        Map<String, Integer> wordMap = new ArrayMap<>();
        List<String> commonWords = Arrays.asList(COMMON_WORDS_LIST_1.toLowerCase().split("\n"));

        ListMessagesResponse response = service.users().messages().list(user).setLabelIds(labelIds).setQ(q).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(user).setLabelIds(labelIds)
                        .setQ(q).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        
        int totalMsgs=messages.size();
        System.out.println("TOTAL MESSAGES: "+totalMsgs);
        int msgsFetched=1,step=10;
        System.out.println("PROGRESS: ");
        
        int dots=0;
        for (Message m : messages) {
            
            float percent=(msgsFetched/(float)totalMsgs)*100;
            if(percent%step<1&&percent>10)
            {
                for(int i=0;i<dots;i++)
                 System.out.print("\b");
                dots=0;
                 System.out.print((int)percent+" ");
                 step+=10;
                
            }
            else
            {   
                dots++;
                System.out.print(".");
            }
           
           
            msgsFetched++;

            Message message=null;
            boolean fetchedMessage=false;
            while(fetchedMessage==false)
            {
                try{
                    message=service.users().messages().get(user, m.getId()).setFormat("full").execute();
                    fetchedMessage=true;
                }
                catch(Exception ex)
                {
                    fetchedMessage=false;
                }
            }

            // List<MessagePartHeader> headerList = message.getPayload().getHeaders();  //for getting sender or recipient email
            List<MessagePartHeader> headersList = message.getPayload().getHeaders();           //for getting encoded data of email

            String filteredData = "";
            //printing email data containing the searchString
            try {
                for (MessagePartHeader header : headersList) {
                   
                    if (header.get("name").equals("Subject")) {
                        filteredData=header.get("value").toString();
                    }

                }
            } catch (Exception e) {
                continue;
              
            }

//            System.out.println("\n\n\n---------------------------------------");
//            System.out.println("message id: "+message.getId() +"\nmail body:\n" + filteredData);

            // String[] words = filteredData.split(" ");
            Vector<String> words = new Vector<>();
            StringTokenizer tok = new StringTokenizer(filteredData.toLowerCase(),"\r\t\n \u200b\u0009(){}[]<>");// " -,\n.;\"\':*\u0000\u0009</>+_={}[])("+"\r");
            
            
            while (tok.hasMoreTokens()) {
                String token=tok.nextToken();
               // System.out.println("token: "+token);
                if(!(token.contains("http")||token.contains("?") ||( token.contains("@")&&token.contains("."))))
                {
                    if(token.endsWith(",")||token.endsWith(".")||token.endsWith(":"))
                    {
                        words.add(token.substring(0,token.length()-1));
                       // System.out.println("token added: "+token);
                    }
                    else
                    {
                       // System.out.println("token added: "+token);
                        words.add(token);
                    }
                }
                

            }
            //StringTokenizer tok2=new StringTokenizer(filteredData," -,\n.;\"\'");
            List<String> wordList = new ArrayList<>();

            for (String s : words) {
                //String s=tok1.nextToken();
                int c = 0;  //s.charAt(0) > 122 && s.charAt(0) < 97
                if (!( s.length()<2||USER.contains(s)||wordList.contains(s) || COMMON_WORDS_LIST_2.contains(s) || isNumeric(s.trim()))) {
                    wordList.add(s);
                    for (String w : words) {
                        //String w=tok2.nextToken();
                        if (s.equals(w)) {
                            c++; 
                        }
                    }
                    if (wordMap.containsKey(s)) {
                        wordMap.put(s, wordMap.get(s) + c);
                    } else {
                        wordMap.put(s, c);

                    }

                }

            }

        }
        System.out.println("");

        for (String key : wordMap.keySet()) {
            if (key.length() == 1 && key.charAt(0)>90&& key.charAt(0)<65&& key.charAt(0)<97&& key.charAt(0)>122) {
               // System.out.println("_______________________________________");
               // System.out.println((int) key.charAt(0));
              //  System.out.println("_______________________________________");
            } else {
               System.out.println(key + ": " + wordMap.get(key));
               
               /* for(char c:key.toCharArray())
                {
                    System.out.print((int)c+" ");
                }
                System.out.println();*/
            }

            if (key.charAt(0) != 13) {
                for (int i = 9; i >= 0; i--) {
                    if (wordMap.get(key) > frequency[i]) {
                        if (i == 0 || wordMap.get(key) <= frequency[i - 1]) {
                            frequency[i] = wordMap.get(key);
                            top10Words[i] = key;

                        } else if (wordMap.get(key) > frequency[i - 1]) {
                            continue;
                        }
                    } else {
                        break;
                    }
                }
            }

        }
        
        wordMap = sortByValue( wordMap );

        // System.out.println("the unknown character:"+((int)top10Words[0].charAt(0)));
        
        /*
        System.out.println("----------------------\n     TOP 10 words\n----------------------");
        for (int i = 0; i < 10 && frequency[i] != 0; i++) {
           // System.out.print((int)top10Words[i].charAt(0)+" ");
            System.out.println(top10Words[i] + " : " + frequency[i]);
        }*/
        
        gap();
                System.out.println("\n SORTED WORD MAP:");

        
        for(Map.Entry<String, Integer> entry : wordMap.entrySet())
        {
            
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        
        gap();
        
        int i=0;
        for(Map.Entry<String, Integer> entry : wordMap.entrySet())
        {
            top10Words[i]=entry.getKey();
            i++;
            if(i==10)break;
        }

        //Map<String[],int[]> ans=new ArrayMap<>();
        //ans.put(top10Words, frequency);
        return top10Words;

    }

    
    public static String[] getTop5EmailsTest(Gmail service, String user, Set<String> emailList, String q) throws IOException {
        
        int[] n = new int[5];
        String[] e = new String[5];

        String[] top5 = new String[5];
        Map<String, Integer> emailFrequencyMap = new HashMap<>();

        int totalEmails=emailList.size();
        System.out.println("TOTAL NUMBER OF EMAIL-IDS: "+totalEmails);
        
        int numberOfEmailIdsProcessed=1;
        System.out.print("PROGRESS: ");
        
        for (String email : emailList) 
        {
            System.out.print(numberOfEmailIdsProcessed+"-");
            numberOfEmailIdsProcessed++;
            if (email.contains(USER)) {
                continue;
            }
            ListMessagesResponse response=null;
            boolean fetchedResponse=false;
            while(fetchedResponse==false)
            {
                try{
                    response= service.users().messages().list(user).setQ(q + email).execute();
                    fetchedResponse=true;
                }
                catch(Exception ex)
                {
                    fetchedResponse=false;
                }
            }

            List<Message> messages = new ArrayList<>();
            while (response.getMessages() != null) {
                messages.addAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(user)
                            .setQ("from: " + email).setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }

            int numberOfMails = messages.size();
            
            emailFrequencyMap.put(email,numberOfMails);

                      
         
        }
        
        emailFrequencyMap=sortByValue(emailFrequencyMap);
         gap();
                System.out.println("\n SORTED EMAIL-FREQUENCY MAP:");

        
        for(Map.Entry<String, Integer> entry : emailFrequencyMap.entrySet())
        {
            
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        
        gap();
        
        int i=0;
        for(Map.Entry<String, Integer> entry : emailFrequencyMap.entrySet())
        {
            top5[i]=entry.getKey();
            i++;
            if(i==5)break;
        }
        
        System.out.println("\b");


        return top5;

    }
    
    
    public static Map getTop5SocialEmails(Gmail service, String user, Set<String> emailList, String q) throws IOException {
        
        int[] n = new int[5];
        String[] e = new String[5];

        Map<String, Integer> top5Map = new HashMap<>();
        Map<String, Integer> emailFrequencyMap = new HashMap<>();

        int totalEmails=emailList.size();
        System.out.println("TOTAL NUMBER OF EMAIL-IDS: "+totalEmails);
        
        int numberOfEmailIdsProcessed=1;
        System.out.print("PROGRESS: ");
        
        for (String email : emailList) 
        {
            System.out.print(numberOfEmailIdsProcessed+"-");
            numberOfEmailIdsProcessed++;
            if (email.contains(USER)) {
                continue;
            }
            ListMessagesResponse response=null;
            boolean fetchedResponse=false;
            while(fetchedResponse==false)
            {
                try{
                    response= service.users().messages().list(user).setQ(q + email).execute();
                    fetchedResponse=true;
                }
                catch(Exception ex)
                {
                    fetchedResponse=false;
                }
            }

            List<Message> messages = new ArrayList<>();
            while (response.getMessages() != null) {
                messages.addAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(user)
                            .setQ("from: " + email).setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }

            int numberOfMails = messages.size();
            
            emailFrequencyMap.put(email,numberOfMails);

                      
         
        }
        
        emailFrequencyMap=sortByValue(emailFrequencyMap);
         gap();
                System.out.println("\n SORTED EMAIL-FREQUENCY MAP:");

        
        for(Map.Entry<String, Integer> entry : emailFrequencyMap.entrySet())
        {
            
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        
        gap();
        
        int i=0;
        for(Map.Entry<String, Integer> entry : emailFrequencyMap.entrySet())
        {
            top5Map.put(entry.getKey(),entry.getValue());
            i++;
            if(i==5)break;
        }
        
        System.out.println("\b");


        return top5Map;

    }

  
    /**
     * List all Messages of the user's mailbox with labelIds applied.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @param labelIds Only return Messages with these labelIds applied.
     * @throws IOException
     */
    public static List<Message> listMessagesWithLabels(Date after, Date before, Gmail service, String userId, List<String> labelIds) throws IOException {

        if(labelIds.size()==2)
        System.out.println("Fetching Message list of : "+ labelIds.get(1));
        else
            System.out.println("Fetching Message list of : "+ labelIds.get(0));
        
        
        ListMessagesResponse response = service.users().messages().list(userId)
                .setLabelIds(labelIds).setQ("after:" + gmailFormat(after) + " "
                + "before:" + gmailFormat(before)).execute();

        List<Message> messages = new ArrayList<Message>();
        
        
        
        int c=1;
        System.out.print("page: ");
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            System.out.print(c++ + "-");
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setLabelIds(labelIds)
                        .setQ("after:" + gmailFormat(after) + " "
                                + "before:" + gmailFormat(before)).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        System.out.println("\b");
        return messages;
    }

    
    private static List getDraftDetails(Gmail service, String user, List<Message> draftList) throws IOException, JSONException
    {
        
        List<JSONObject> draftDetails=new ArrayList<>();
        
        for(Message d : draftList){
            
            Message draft = service.users().messages().get(user, d.getId()).setFormat("full").execute();
            
            List<MessagePartHeader> headers=draft.getPayload().getHeaders();
                    
            
            String subject="NA";
            String to="NA";
            String date="NA";
            
            for(MessagePartHeader h: headers)
            {
                if(h.get("name").equals("Subject"))
                    subject=h.get("value").toString();
                if(h.get("name").equals("To"))
                    to=h.get("value").toString();
                if(h.get("name").equals("Date"))
                    date=h.get("value").toString();
            }
            
            
            JSONObject draftDetail=new JSONObject();
            draftDetail.put("Subject", subject);
            draftDetail.put("Recipient", to);
            draftDetail.put("Date", date);
            
            draftDetails.add(draftDetail);
            
            
        }
        
        
        return draftDetails;
    }
        
    
    
    private static void gap()
    {
        System.out.println("--------------------------------------------------------------------------------------------------------\n\n");
    }
    private static void deleteCredentials()
    {
        File storedCredentials=new File("C:\\Users\\Sumit\\.credentials\\gmail-java-quickstart.json\\StoredCredential");
        // if( storedCredentials.renameTo(new File("C:\\Users\\Sumit\\.credentials\\gmail-java-quickstart.json\\StoredCredential4")))
         if(storedCredentials.delete())
           System.out.println("Deleted");
           else 
               System.out.println("Error");
    }
    
    private static void setUserEmailId(Gmail service) throws IOException
    {
         String email = service.users().getProfile("me").execute().getEmailAddress();
        
        USER = email;
        
    }
    private static void listLabelIds(Gmail service) throws IOException
    {
         ListLabelsResponse listResponse
                = service.users().labels().list(USER).execute();

        List<Label> labels = listResponse.getLabels();

        if (labels.size() == 0) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
    }
    
    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in
                = GmailAccess.class.getResourceAsStream("..\\resources\\client_secret.json");
        GoogleClientSecrets clientSecrets
                = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     *
     * @return an authorized Gmail client service
     * @throws IOException
     */
    public static Gmail getGmailService() throws IOException {
        Credential credential = authorize();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    
     /**
     * Fetches all the info of user's mailbox and stores in a JSON
     *
     * @param  storedCredentialsPath Path of the user credentials file: StoredCredentials
     * @return gmailMeterObject A JSONObject containing all the info of the user's Gmail account mailbox
     * @throws IOException, NullPointerException, JSONException 
     */
    public JSONObject getGmailObject(File storedCredentialsPath) throws IOException, NullPointerException, JSONException {


//      DATA_STORE_DIR=storedCredentialsPath;   
//        try {
//            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
//        } catch (Throwable t) {
//            t.printStackTrace();
//            System.exit(1);
//        }


            deleteCredentials();

        // Build a new authorized API client service.
        Gmail service = getGmailService();
        String user = "me";

        setUserEmailId(service);
        System.out.println("email: " + USER);
        gap();
        
        listLabelIds(service);
        gap();
               

//////////////////ADDING LABEL ID'S/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////       

        List<String> primaryMailsLabel = new ArrayList<>();
        List<String> socialMailsLabel = new ArrayList<>();
        List<String> promotionsMailsLabel = new ArrayList<>();
        List<String> forumsMailsLabel = new ArrayList<>();
        List<String> updatesMailsLabel = new ArrayList<>();
        List<String> sentLabels = new ArrayList<>();
        List<String> inboxLabel = new ArrayList<>();
        List<String> trashLabel = new ArrayList<>();
        List<String> draftLabel = new ArrayList<>();
        
        primaryMailsLabel.add("INBOX");
        primaryMailsLabel.add("CATEGORY_PERSONAL");
        
        socialMailsLabel.add("INBOX");
        socialMailsLabel.add("CATEGORY_SOCIAL");
        
        promotionsMailsLabel.add("INBOX");
        promotionsMailsLabel.add("CATEGORY_PROMOTIONS");
        
        forumsMailsLabel.add("INBOX");
        forumsMailsLabel.add("CATEGORY_FORUMS");
        
        updatesMailsLabel.add("INBOX");
        updatesMailsLabel.add("CATEGORY_UPDATES");
        
        sentLabels.add("SENT");
        
        inboxLabel.add("INBOX");
        
        trashLabel.add("TRASH");
        
        draftLabel.add("DRAFT");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




//////////////// SETTING INTERVAL/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, -60);
        Date startDate = cal2.getTime();
        System.out.println("Time  period:");
        System.out.println(startDate + " to  " + new Date());
        gap();

        Date currentDate = new Date();

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.YEAR, -10);
        Date creationDate = cal1.getTime();
        

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        


//        
//          String[] top10WordPrimary = getTop10WordsSubject(service, user, "after:" + gmailFormat(startDate) + " "
//                + "before:" + gmailFormat(currentDate),primaryMailsLabel);
//          String[] top10WordSent = getTop10WordsSubject(service, user, "after:" + gmailFormat(startDate) + " "
//                + "before:" + gmailFormat(currentDate),sentLabels);
//
//         System.exit(0);



/////////////////FETCHING MESSAGE LISTS AND DISPLAYING SIZE///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        List<Message> inboxPrimaryList = listMessagesWithLabels(creationDate, currentDate, service, user, primaryMailsLabel);
        List<Message> primaryList = listMessagesWithLabels(startDate, currentDate, service, user, primaryMailsLabel);//inboxLabel);
        List<Message> socialList = listMessagesWithLabels(startDate, currentDate, service, user, socialMailsLabel);
        List<Message> promotionsList = listMessagesWithLabels(startDate, currentDate, service, user, promotionsMailsLabel);
        List<Message> updatesList = listMessagesWithLabels(startDate, currentDate, service, user, updatesMailsLabel);
        List<Message> forumsList = listMessagesWithLabels(startDate, currentDate, service, user, forumsMailsLabel);
        List<Message> draftList = listMessagesWithLabels(startDate, currentDate, service, user, draftLabel);
        List<Message> sentList = listMessagesWithLabels(creationDate, currentDate, service, user, sentLabels);
        List<Message> trashList = listMessagesWithLabels(creationDate, currentDate, service, user, trashLabel);

        List<Message> recievedMessagesList = listMessagesWithLabels(startDate, currentDate, service, user, primaryMailsLabel);
        List<Message> sentMessagesList = listMessagesWithLabels(startDate, currentDate, service, user, sentLabels);
        
      //  System.out.println("inboxList size: "+ inboxList.size());
        System.out.println("primaryList size: "+ primaryList.size());
        System.out.println("socialList size: "+ socialList.size());
        System.out.println("promotionsList size: "+ promotionsList.size());
        System.out.println("forumsList size: "+ forumsList.size());
        System.out.println("sentList size: "+ sentList.size());
        System.out.println("trashList size: "+ trashList.size());
        System.out.println("draft size: "+ draftList.size());
        System.out.println("recievedMessageList size: "+ recievedMessagesList.size());
        System.out.println("sentMessagesList size: "+ sentMessagesList.size());
        
        gap();
        
  //--------------------------------------------------------------------------------------------------------------------------------------------------------------      
        
  //------------------------DRAFT DETAILS-------------------------------------------------------------------------------------------------------------------------
  
           
                System.out.println("Number of drafts: "+draftList.size());
                
                System.out.println("FETCHING DRAFT DETAILS: ");
                List<JSONObject> draftDetails=getDraftDetails(service, user, draftList);
                System.out.println("DONE");
  
  
  
  //---------------------------------------------------------------------------------------------------------------------------------------------------------------
  
        
 //--------------------------TEST CODE      -----------------------------------------------------------------------------------------------------------------------
//        
//           List<Integer> bankWordsCount = new ArrayList();
//        Map<String,Integer> bankWordCountMap=new ArrayMap<>();
//        
//        for(int i=0; i<BANKING_WORDS_LIST.length; i++)
//        {
//            int count=numberOfRecievedMailsWithSearchString(service,user,recievedMessagesList,BANKING_WORDS_LIST[i]);
//            bankWordsCount.add(count);
//            bankWordCountMap.put(BANKING_WORDS_LIST[i],count);
//            
//        }
//        
//        bankWordCountMap=sortByValue(bankWordCountMap);
//        
//        Map<String,Integer> healthWordCountMap=new ArrayMap<>();
//        
//        
//        for(int i=0; i<HEALTH_WORDS_LIST.length; i++)
//        {
//            int count=numberOfRecievedMailsWithSearchString(service,user,recievedMessagesList,HEALTH_WORDS_LIST[i]);
//            healthWordCountMap.put(HEALTH_WORDS_LIST[i],count);
//            
//        }
//        
//        healthWordCountMap=sortByValue(healthWordCountMap);
//        System.out.println(healthWordCountMap);
//        System.out.print(bankWordCountMap);
//        System.exit(0);
        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        /**
         * FETCHING ACCOUNT AGE
         * (i) FETCHING ACCOUNT AGE IN millis FROM getAccountAge()
         * (ii)CONVERTING  millis TO FORMATTED TIME STRING : Y:D:H:M:S
         */
        long accountAgeMillis = getAccountAge(service, user, inboxPrimaryList, sentList, trashList);
        String accountAge = millisToFormattedTime(accountAgeMillis);
        gap();
        
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
 
 
 //////////////FETCHING AVERAGE MAILS////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
 
        long intervalTimeMillis=currentDate.getTime()-startDate.getTime();
 
        
        System.out.println("Primary mail's average:");
        int primaryAvg[] = getAverage(intervalTimeMillis, primaryList);
        System.out.println("Social mail's average:");
        int socialAvg[] = getAverage(intervalTimeMillis, socialList);
        System.out.println("Promotion mail's average:");
        int promotionsAvg[] = getAverage(intervalTimeMillis, promotionsList);
        System.out.println("Update mail's average:");
        int updatesAvg[] = getAverage(intervalTimeMillis, updatesList);
        System.out.println("Forum mail's average:");
        int forumsAvg[] = getAverage(intervalTimeMillis, forumsList);

       // System.out.println("Incoming mail's average:");
       // int inboxAvg[] = getAverage(accountAgeMillis, inboxPrimaryList);
        System.out.println("Outgoing mail's average:");
        int sentAvg[] = getAverage(intervalTimeMillis, sentMessagesList);
        
        
        gap();
 
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////FETCHING TOP 10 WORDS/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        String[] top10WordsInbox = getTop10Words(service, user,  "after:" + gmailFormat(startDate) + " "
                + "before:" + gmailFormat(currentDate),primaryMailsLabel);
        String[] top10WordsSent = getTop10Words(service, user,  "after:" + gmailFormat(startDate) + " "
                + "before:" + gmailFormat(currentDate),sentLabels);
        
        gap();

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



/////////////////FETCHING TOP 10 RECURRING EMAILS//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("FETCHING TOP 10 RECURRING EMAILS: \n");
    //    System.out.println("emailids with compared scores:");
        Map<String, Float> recurringEmailsMap = getTop10RecurringEmails(service, user, recievedMessagesList);

      /*  for (String emailId : recurringEmailsMap.keySet()) {
            System.out.println(emailId + ": " + recurringEmailsMap.get(emailId));
        }
        */

        String[] recurringEmails = recurringEmailsMap.keySet().toArray(new String[recurringEmailsMap.keySet().size()]);

        for (int i = 0; i < recurringEmails.length - 1; i++) {
            for (int j = i + 1; j < recurringEmails.length; j++) {
                if ((float) recurringEmailsMap.get(recurringEmails[i]) < (float) recurringEmailsMap.get(recurringEmails[j])) {
                    String t = recurringEmails[i];
                    recurringEmails[i] = recurringEmails[j];
                    recurringEmails[j] = t;

                }
            }
        }
        
        System.out.println("RECURRING EMAILS: "+recurringEmails.length);
        String[] top10RecurringEmails; 
        if(recurringEmails.length>10) top10RecurringEmails = new String[10];
        else top10RecurringEmails = new String[recurringEmails.length];
        
        System.out.println("---------------------------------------");
        System.out.println("TOP 10 RECURRING EMAILS:");
        System.out.println("---------------------------------------");
        for (int i = 0; i < top10RecurringEmails.length; i++) {
            top10RecurringEmails[i] = recurringEmails[i];
            System.out.println(recurringEmails[i]);
        }
        
        
        gap();
        

  
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
 
 
//---------------------------SENDER AND RECIPIENT EMAILS---------------------------------------------------------
        /**
         * (i)Getting e-mails of senders and recipients by using
         * recievedMessagesList and sentMessagesList Storing it in List<String>
         * senderEmailsList and recipientEmailsList
         *
         * (ii) Iterating through the lists and splitting the recipient's
         * e-mails in case the mail was sent to multiple recipient
         *
         * (iii) Adding the list to a Set<String> for de-duplication
         *
         * (iv) Displaying the total number of senders and recipients as the
         * size() of the Set.
         *
         */
        List<String> senderEmailsList = new ArrayList<>();
        List<String> recipientEmailsList = new ArrayList<>();

        senderEmailsList = senderEmails(service, user, recievedMessagesList);

        recipientEmailsList = recipientEmails(service, user, sentMessagesList);  

        for (int j = 0; j < senderEmailsList.size();) {
            if (senderEmailsList.get(j).contains(">,")) {
                String[] emails = senderEmailsList.get(j).split(",");

                for (int i = 0; i < emails.length; i++) {
                    senderEmailsList.add(emails[i]);
                }
                String removed = senderEmailsList.remove(j);
            } else {
                j++;
            }
        }

        for (int j = 0; j < recipientEmailsList.size();) {
            if (recipientEmailsList.get(j).contains(">,")) {
                String[] emails = recipientEmailsList.get(j).split(",");

                for (String email1 : emails) {
                    recipientEmailsList.add(email1.trim());
                }

                String removed = recipientEmailsList.remove(j);
            } else {
                j++;
            }
        }

        Set<String> senderEmails = new HashSet<>(senderEmailsList);

        Set<String> recipientEmails = new HashSet<>(recipientEmailsList);
       

        System.out.println("Number of recipients: " + recipientEmails.size());
        System.out.println("Number of senders: " + senderEmails.size());
        
        gap();

   
        
        System.out.println("Top 5 senders:");
     
        String[] top5SendersTest=getTop5EmailsTest(service, user, senderEmails, "after:" + gmailFormat(startDate) + " "
                + "before:" + gmailFormat(currentDate)+" from: ");
        
        for(String s: top5SendersTest)
        {
            System.out.println(s);
        }
        
        gap();
        
        

        System.out.println("Top 5 Recipients:");
             
       String[] top5RecipientsTest=getTop5EmailsTest(service, user, recipientEmails, "after:" + gmailFormat(startDate) + " "
                + "before:" + gmailFormat(currentDate)+" to: ");
       
        for(String s: top5RecipientsTest)
        {
            System.out.println(s);
        }

        gap();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//---------------TOP 5 SOCIAL EMAILS------------------------------------------------------------------------------------------------------------------------------------

       List<String> socialMailsList = senderEmails(service, user, socialList);

     
        for (int j = 0; j < socialMailsList.size();) {
            if (socialMailsList.get(j).contains(">,")) {
                String[] emails = socialMailsList.get(j).split(",");

                for (int i = 0; i < emails.length; i++) {
                    socialMailsList.add(emails[i].substring(emails[i].indexOf("<")+1,emails[i].indexOf(">")));
                }
                socialMailsList.remove(j);
               
            } else if(socialMailsList.get(j).contains(">")) {
                
                 socialMailsList.add(socialMailsList.get(j).substring(socialMailsList.get(j).indexOf("<")+1,socialMailsList.get(j).indexOf(">")));
                 socialMailsList.remove(j);
               
            }
            
            else{
                j++;
            }
        }
        
        
          Set<String> socialEmails = new HashSet<>(socialMailsList);
          
          
          Map<String,Integer> top5Socials=getTop5SocialEmails(service, user, socialEmails, "after:" + gmailFormat(startDate) + " "
                + "before:" + gmailFormat(currentDate)+" from: ");
       
          System.out.println("TOP 5 Social: ");
        for(Map.Entry<String, Integer> entry: top5Socials.entrySet())
        {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        

        gap();


///////////////////////NUMBER OF MESSAGE BY WEEK DAYS AND HOURS//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////       
        int daysRecieved[] = new int[7];
        int hoursRecieved[] = new int[24];
        int daysSent[] = new int[7];
        int hoursSent[] = new int[24];

        System.out.println("\n\nNumber of recieved messages :");
        numberOfMessagesByWeekAndHours(service, user, recievedMessagesList, daysRecieved, hoursRecieved);
        System.out.println("\n\nNumber of sent messages :");
        numberOfMessagesByWeekAndHours(service, user, sentMessagesList, daysSent, hoursSent);
        gap();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////        



//PRINTING TOTAL NUMBER OF SENT AND RECIEVED MESSAGES----------------------------------------------------      
        System.out.println("\nTotal recieved messages: " + recievedMessagesList.size());
        System.out.println("Total sent messages: " + sentMessagesList.size());
        
        gap();
//----------------------------------------------------------------------------------------------------------



//---------------------------RESPONSE TIME------------------------------------------------------------------
        /**
         * Calculating the response time of each sent mail and then, calculating
         * the quickest response time and the average response time.
         *
         * FINALLY PRINTING THE QUICKEST AND AVERAGE RESPONSE TIME
         */
        String[] responseTimes = responseTime(service, user, sentMessagesList);
        gap();
//-------------------------------------------------------------------------------------------------------------






//----------------------Added By DAX -------------------------------------------------------------------------------------------------------------------------------
        
//----------------------BANK_WORDS AND HEALTH_WORDS COUNT MAP-------------------------------------------------------------------------------------------------------------------------------
      
        
        System.out.println("BANK_WORDS_COUNT: ");
        List<Integer> bankWordsCount = new ArrayList();
        Map<String,Integer> bankWordCountMap=new ArrayMap<>();
        
        for(int i=0; i<BANKING_WORDS_LIST.length; i++)
        {
            int count=numberOfRecievedMailsWithSearchString(service,user,recievedMessagesList,BANKING_WORDS_LIST[i]);
            bankWordsCount.add(count);
            bankWordCountMap.put(BANKING_WORDS_LIST[i],count);
            System.out.println(BANKING_WORDS_LIST[i]+" : "+count);
        }
        
        bankWordCountMap=sortByValue(bankWordCountMap);
        
        Map<String,Integer> healthWordCountMap=new ArrayMap<>();
        
        
        for(int i=0; i<HEALTH_WORDS_LIST.length; i++)
        {
            int count=numberOfRecievedMailsWithSearchString(service,user,recievedMessagesList,HEALTH_WORDS_LIST[i]);
            healthWordCountMap.put(HEALTH_WORDS_LIST[i],count);
            
            System.out.println(HEALTH_WORDS_LIST[i]+" : "+count);

            
        }
        
        healthWordCountMap=sortByValue(healthWordCountMap);
        
        
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------


        
        /*  //Storing data in a JSON file-------------------------------------------
        
        JSONObject data=new JSONObject();
        
        JSONArray messagesSentByWeeks=new JSONArray(daysSent);
        JSONArray messagesRecievedByWeeks=new JSONArray(daysRecieved);
        JSONArray messagesSentByHours=new JSONArray(hoursSent);
        JSONArray messagesRecievedByHours=new JSONArray(hoursRecieved);
        
        data.put("messagesSentByWeeks",messagesSentByWeeks);
        data.put("messagesRecievedByWeeks",messagesRecievedByWeeks);
        data.put("messagesSentByHours",messagesSentByHours);
        data.put("messagesRecievedByHours",messagesRecievedByHours);
        
        
        
        try {                         // Writing to a file     
            File file=new File(".\\gmailMeterData.json");           
            file.createNewFile();             
            FileWriter fileWriter = new FileWriter(file);             
            System.out.println("Writing JSON object to file");             
            System.out.println("-----------------------");             
            System.out.println(data);
            String stringData="mailData=\'"+data.toString()+"\';";
            fileWriter.write(stringData);             
            fileWriter.flush();             
            fileWriter.close();         
        } catch (IOException e) 
        {             
            e.printStackTrace();         
        }
      
        
         return data;

         */
        //Storing data in a JSON file-------------------------------------------
        JSONObject byHour = new JSONObject();
        JSONObject byWeek = new JSONObject();

        JSONArray messagesSentByWeeks = new JSONArray(daysSent);
        JSONArray messagesRecievedByWeeks = new JSONArray(daysRecieved);

        byWeek.put("sent", messagesSentByWeeks);
        byWeek.put("recieved", messagesRecievedByWeeks);

        JSONArray messagesSentByHours = new JSONArray(hoursSent);
        JSONArray messagesRecievedByHours = new JSONArray(hoursRecieved);

        byHour.put("sent", messagesSentByHours);
        byHour.put("recieved", messagesRecievedByHours);

        JSONObject numberOfMessages = new JSONObject();
        numberOfMessages.put("sent", sentMessagesList.size());
        numberOfMessages.put("received", recievedMessagesList.size());
        numberOfMessages.put("byHour", byHour);
        numberOfMessages.put("byWeek", byWeek);

        JSONObject responseTime = new JSONObject();
        responseTime.put("quickest", responseTimes[0]);
        responseTime.put("average", responseTimes[1]);

        JSONObject interval = new JSONObject();
        interval.put("from", startDate);
        interval.put("to", currentDate);

        JSONObject senders = new JSONObject();
        senders.put("total", senderEmails.size());
        senders.put("top5", top5SendersTest);

        JSONObject recipients = new JSONObject();
        recipients.put("total", recipientEmails.size());
        recipients.put("top5", top5RecipientsTest);

        //  JSONObject words=new JSONObject();
        // words.put("top10Words",top10Words);
        JSONObject AverageMessages = new JSONObject();
        JSONObject incomingAverage = new JSONObject();
        JSONObject outgoingAverage = new JSONObject();
        outgoingAverage.put("daily", sentAvg[0]);
        outgoingAverage.put("weekly", sentAvg[1]);
        outgoingAverage.put("monthly", sentAvg[2]);
        outgoingAverage.put("yearly", sentAvg[3]);

//        incomingAverage.put("daily", inboxAvg[0]);
//        incomingAverage.put("weekly", inboxAvg[1]);
//        incomingAverage.put("monthly", inboxAvg[2]);
//        incomingAverage.put("yearly", inboxAvg[3]);

        JSONObject primaryAverage = new JSONObject();
        primaryAverage.put("daily", primaryAvg[0]);
        primaryAverage.put("weekly", primaryAvg[1]);
        primaryAverage.put("monthly", primaryAvg[2]);
        primaryAverage.put("yearly", primaryAvg[3]);

        JSONObject socialAverage = new JSONObject();
        socialAverage.put("daily", socialAvg[0]);
        socialAverage.put("weekly", socialAvg[1]);
        socialAverage.put("monthly", socialAvg[2]);
        socialAverage.put("yearly", socialAvg[3]);

        JSONObject updatesAverage = new JSONObject();
        updatesAverage.put("daily", updatesAvg[0]);
        updatesAverage.put("weekly", updatesAvg[1]);
        updatesAverage.put("monthly", updatesAvg[2]);
        updatesAverage.put("yearly", updatesAvg[3]);

        JSONObject promotionsAverage = new JSONObject();
        promotionsAverage.put("daily", promotionsAvg[0]);
        promotionsAverage.put("weekly", promotionsAvg[1]);
        promotionsAverage.put("monthly", promotionsAvg[2]);
        promotionsAverage.put("yearly", promotionsAvg[3]);

        JSONObject forumsAverage = new JSONObject();
        forumsAverage.put("daily", forumsAvg[0]);
        forumsAverage.put("weekly", forumsAvg[1]);
        forumsAverage.put("monthly", forumsAvg[2]);
        forumsAverage.put("yearly", forumsAvg[3]);

        incomingAverage.put("primary", primaryAverage);
        incomingAverage.put("socials", socialAverage);
        incomingAverage.put("updates", updatesAverage);
        incomingAverage.put("forums", forumsAverage);
        incomingAverage.put("promotions", promotionsAverage);

        AverageMessages.put("received", incomingAverage);
        AverageMessages.put("sent", outgoingAverage);
        JSONObject gmailMeterObject = new JSONObject();
        
        JSONObject top10Words=new JSONObject();
        top10Words.put("inbox",top10WordsInbox);
        top10Words.put("sent",top10WordsSent);
        
        JSONObject draftJson=new JSONObject();
        draftJson.put("total",draftList.size());
        draftJson.put("list",draftDetails);
        
        gmailMeterObject.put("emailId", USER);
        gmailMeterObject.put("accountAge", accountAge);
        gmailMeterObject.put("interval", interval);
        gmailMeterObject.put("senders", senders);
        gmailMeterObject.put("recipients", recipients);
        gmailMeterObject.put("averageMessages", AverageMessages);
        gmailMeterObject.put("numberOfMessages", numberOfMessages);
        gmailMeterObject.put("top10Words", top10Words);
        gmailMeterObject.put("top10Recurringemails", top10RecurringEmails);
        gmailMeterObject.put("responseTime", responseTime);
        gmailMeterObject.put("bankWordCount", bankWordCountMap);
        gmailMeterObject.put("healthWordCount", healthWordCountMap);
        gmailMeterObject.put("top5Socials", top5Socials);
        gmailMeterObject.put("drafts",draftJson);
        

        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        gmailMeterObject.put("timestamp", timestamp);

        try {                         // Writing to a file     
            File file = new File(".\\gmailMeterData.json");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            gap();
            System.out.println("Writing JSON object to file");
            System.out.println("-----------------------");
            System.out.println(gmailMeterObject);
            String stringData = "gmailAnalysisObject=\'" + gmailMeterObject.toString() + "\';";
            fileWriter.write(stringData);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        
        
        

        return gmailMeterObject;

        //-------------------------------------------------------------------------
        /*   for(String email:senderEmails)
      {
          for(String emailFromList:senderEmailsList)
          {
              if(email.equals(emailFromList))
                  senderFrequency[x++];
          }
          
      }*/
//----------------------------------------------------------------------------------------------------------      
        //   gd.setSenderEmails(senderEmailsList);
        // gd.setRecipientEmails(recipientEmailsList);
        /*   System.out.println("\n\nSender emails:\n");
     for(String email:senderEmails) //for(int i=0;i<senderEmails.size();i++)
      {
          System.out.println(email);
          //String email=senderEmails.get(i);
        //  for(int j=0;)
      }
      
      System.out.println("\n\nRecipient emails:\n");
      for(String email:recipientEmails) //for(int i=0;i<senderEmails.size();i++)
      {
          System.out.println(email);
          //String email=senderEmails.get(i);
        //  for(int j=0;)
      }*/
 /*  System.out.println("\n\nSender emails with duplicates:\n");
     for(String email:senderEmailsList) //for(int i=0;i<senderEmails.size();i++)
      {
          System.out.println(email);
          //String email=senderEmails.get(i);
        //  for(int j=0;)
      }
      
      System.out.println("\n\nRecipient emails with duplicates:\n");
      for(String email:recipientEmailsList) //for(int i=0;i<senderEmails.size();i++)
      {
          System.out.println(email);
          //String email=senderEmails.get(i);
        //  for(int j=0;)
      }*/
//---------------------NUMBER OF SENT AND RECEIVED EMAILS WITH THE SEARCH STRING-------------------------------------------------------

        



        /* String searchString=new String("regards");
      int numberOfSentMailsWithSearchString=numberOfSentMailsWithSearchString(service,user,sentMessagesList,searchString);
            System.out.println("numberOfSentMailsWithSearchString:"+numberOfSentMailsWithSearchString);

      
      int numberOfRecievedMailsWithSearchString=numberOfRecievedMailsWithSearchString(service,user,recievedMessagesList,searchString);
      
      System.out.println("numberOfRecievedMailsWithSearchString:"+numberOfRecievedMailsWithSearchString);
      
        gd.setNumberOfSentMailsWithSearchString(numberOfSentMailsWithSearchString);
        gd.setNumberOfRecievedMailsWithSearchString(numberOfRecievedMailsWithSearchString);*/
//-----------------------------------------------------------------------------------------------------------------------------------------      
    }

    public void base64DecoderTest() {
        //String encoded1 = "DQoNCg0KDQpKb3RGb3JtIERhaWx5IERpZ2VzdA0KDQogICAgaHRtbCB7d2lkdGg6MTAwJX0NCiAgICBib2R5IHtiYWNrZ3JvdW5kLWNvbG9yOiNlNmU2ZTY7bWFyZ2luOjA7fQ0KICAgIC5SZWFkTXNnQm9keSB7d2lkdGg6MTAwJTt9DQogICAgLkV4dGVybmFsQ2xhc3Mge3dpZHRoOjEwMCU7fQ0KICAgIC5FeHRlcm5hbENsYXNzLCAuRXh0ZXJuYWxDbGFzcyBwLCAuRXh0ZXJuYWxDbGFzcyBzcGFuLCAuRXh0ZXJuYWxDbGFzcyBmb250LCAuRXh0ZXJuYWxDbGFzcyB0ZCwgLkV4dGVybmFsQ2xhc3MgZGl2IHttc28tbGluZS1oZWlnaHQtcnVsZTpleGFjdGx5OyBsaW5lLWhlaWdodDogMTAwJX0gDQogICAgdGFibGV7Ym9yZGVyLWNvbGxhcHNlOmNvbGxhcHNlO30NCiAgICBpbWcge2Rpc3BsYXk6YmxvY2s7fQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiANCg0KDQoNCg0KDQogICAgDQogICAgICAgIA0KDQogICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgRlNXIEFzc2Vzc21lbnQgd2l0aCBTcG91c2UgRGF0YSB3aXRoIFN0YXRlIGluZm9ybWF0aW9uIGZvciBJbmRpYSAtIEdvb2dsZSAgNCANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBKdW4gMjgsIDIwMTYgMTE6MzBhbQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUZpcnN0IG5hbWU6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEthcnRpawkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlMYXN0IE5hbWUgOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBCYWJhcml5YQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlFbWFpbAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBrYXJ0aWsud2Vic0B5YWhvby5jb20JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUGhvbmUgbnVtYmVyCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDg4NjYxNTY2NDkJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTmF0aW9uYWxpdHk6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEluZGlhCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU1hcml0YWwtU3RhdHVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFVubWFycmllZAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlXaGF0IGlzIHlvdXIgY3VycmVudCBjb3VudHJ5IHJlc2lkZW5jZToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgSW5kaWEJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRG8geW91IGhhdmUgYW55IGRlcGVuZGVudCBDaGlsZGVybjoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVdoYXQgaXMgeW91ciBQcmVmZXJyZWQgRGVzdGluYXRpb24gaW4gQ2FuYWRhOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBPbnRhcmlvCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCTEyIFllYXJzIGFuZCBZb3VuZ2VycyAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCTEzIHRvIDIxIFllYXJzOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJMjIgWWVhciBhbmQgT2xkZXI6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlOZXQgV29ydGg6IGluIFVTRAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAwIC0gNTAwMAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlFbmdsaXNoOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBTcGVhazsgUmVhZDsgV3JpdGU7IExpc3RlbgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlGcmVuY2g6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXZlIHlvdSBjb21wbGV0ZWQgaGlnaCBzY2hvb2wgKHNlY29uZGFyeSBzY2hvb2wpPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBZZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGF2ZSB5b3UgcmVjZWl2ZWQgYW55IGVkdWNhdGlvbiBvciB0cmFpbmluZyBvdGhlciB0aGFuIGhpZ2ggc2Nob29sIChzZWNvbmRhcnkgc2Nob29sKT8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgWWVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhpZ2hlc3QgRWR1Y2F0aW9uIGxldmVsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE1hc3RlcnMgUHJvZ3JhbQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQcm9ncmFtIER1cmF0aW9uOiAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTGVzcyB0aGFuIDMgeWVhcnMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRmllbGQgb2YgU3R1ZHk6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNjaWVuY2UJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTG9jYXRpb246ICggY291bnRyeSApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEluZGlhCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhhdmUgeW91IGV2ZXIgd29ya2VkIGluIENhbmFkYSBpbiBhIHNraWxsZWQgb2NjdXBhdGlvbiBmb3IgYXQgbGVhc3Qgb25lIHllYXI_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5vCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhhdmUgeW91IGRvbmUgYW55IHBhaWQgd29yayBhbnkgd2hlcmUgaW4gdGhlIHdvcmxkIGR1cmluZyB0aGUgbGFzdCAxMCB5ZWFycz8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTm8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJT2NjdXBhdGlvbjoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVdvcmsgRXhwZXJpZW5jZToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVR5cGUgb2YgSm9iOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRG8geW91IGhhdmUgYSB3cml0dGVuIGpvYiBvZmZlciBmcm9tIGEgQ2FuYWRpYW4gZW1wbG95ZXI_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlPY2N1cGF0aW9uOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUHJvdmluY2U6IAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSXMgdGhpcyBqb2Igb2ZmZXIgcmVsYXRlZCB0byB5b3VyIG1vc3QgcmVjZW50IGZpZWxkIG9mIHN0dWR5PyAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCURvIHlvdSBoYXZlIGFueSBmYW1pbHkgbWVtYmVycywgcmVsYXRpdmVzLCBvciBjbG9zZSBmcmllbmRzIGxpdmluZyBpbiBDYW5hZGE_ICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTm8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUGxlYXNlIHVzZSB0aGlzIHNwYWNlIHRvIHNlbmQgYW55IHNwZWNpZmljIHF1ZXN0aW9ucywgY29tbWVudHMgb3IgYWRkaXRpb25hbCBpbmZvcm1hdGlvbiB0aGF0IG1heSBiZSByZWxldmFudCB0byB5b3VyIGlucXVpcnkuCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQbGVhc2UgY2hlY2sgaGVyZToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxvY2F0aW9uICggY2l0eSApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQbGVhc2UgdXBsb2FkIHRoZSBsYXRlc3QgY29weSBvZiB5b3VyIFJlc3VtZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBodHRwOi8vd3d3LmpvdGZvcm0uY29tL3VwbG9hZHMva2FiaXJraGFubmEvNDEyMDE1MDc0ODE5NDkvMzQyOTM3MDAzMzIyNzA4NDQ1L0tCIENWLmRvY3gJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJQWdlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDI3CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxvY2F0aW9uICggY2l0eSAgKQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBTdXJhdAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlEaWQgeW91IGNvbXBsZXRlIHRoaXMgcHJvZ3JhbT8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgWWVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxvY2F0aW9uICggY291bnRyeSApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlTcG91c2UgTmFtZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgLCAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGFzIHNwb3VzZSBjb21wbGV0ZWQgSGlnaCBzY2hvb2wgPyAoIHNlY29uZGFyeSBzY2hvb2wgKQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGlnaGVzdCBFZHVjYXRpb24gbGV2ZWwgb2Ygc3BvdXNlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQcm9ncmFtIGR1cmF0aW9uCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlGaWVsZCBvZiBzdHVkeQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJQWdlIG9mIHlvdXIgc3BvdXNlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlDdXJyZW50IFN0YXRlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEd1amFyYXQJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJQ2hvb3NlIHRoZSBTa2lsbGVkIHdvcmtlciBvY2N1cGF0aW9uIHRoYXQgeW91IHdpc2ggdG8gYXBwbHkgdW5kZXIJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTXkgc2tpbGxzIGFyZSBub3QgbGlzdGVkIGhlcmUJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRW50ZXIgeW91ciBza2lsbHMgaGVyZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBQaGFybWEgTWFya2V0aW5nLCBQb2xpc2ggRGlhbW9uZCBHcmFkaW5nIGFuZCBhc3NvcnRpbmcJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRW50ZXIgeW91ciBjdXJyZW50IGpvYiBkZXNjcmlwdGlvbiBpbiB5b3VyIG93biAgd29yZHMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgaSBhbSBkb2luZyBhIERpYW1vbmQgQXNzb3J0aW5nIGluIGEgbGVhZGluZyBkaWFtb25kIGNvbXBhbnkgaW4gc3VyYXQuCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUN1cnJlbnQgY2l0eSBvZiByZXNpZGVuY2UJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgU3VyYXQJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGF2ZSB5b3UgdGFrZW4gdGhlIElFTFRTIGV4YW0gPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBObwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlXaGVuIGRvIHlvdSBwbGFuIHRvIHRha2UgeW91ciBJRUxUUyBleGFtID8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTm90IGRlY2lkZWQgeWV0CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVdoYXQgd2FzIHlvdXIgSUVMVFMgc2NvcmUgPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSG93IGRpZCB5b3UgaGVhciBhYm91dCB1cyA_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEVtYWlsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBKdW4gMjgsIDIwMTYgMTA6MDFhbQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUZpcnN0IG5hbWU6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFJpZHdhbiAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTGFzdCBOYW1lIDoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgU2hpdHUJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRW1haWwJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgcmlkd2Fuc2hpdHVAZ21haWwuY29tCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVBob25lIG51bWJlcgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICArOTcxNTU2MDI3MDQwCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU5hdGlvbmFsaXR5OgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBOaWdlcmlhCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU1hcml0YWwtU3RhdHVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFVubWFycmllZAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlXaGF0IGlzIHlvdXIgY3VycmVudCBjb3VudHJ5IHJlc2lkZW5jZToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgVW5pdGVkIEFyYWIgRW1pcmF0ZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRG8geW91IGhhdmUgYW55IGRlcGVuZGVudCBDaGlsZGVybjoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVdoYXQgaXMgeW91ciBQcmVmZXJyZWQgRGVzdGluYXRpb24gaW4gQ2FuYWRhOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBPbnRhcmlvCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCTEyIFllYXJzIGFuZCBZb3VuZ2VycyAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCTEzIHRvIDIxIFllYXJzOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJMjIgWWVhciBhbmQgT2xkZXI6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlOZXQgV29ydGg6IGluIFVTRAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAwIC0gNTAwMAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlFbmdsaXNoOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBSZWFkOyBXcml0ZTsgTGlzdGVuCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUZyZW5jaDoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhhdmUgeW91IGNvbXBsZXRlZCBoaWdoIHNjaG9vbCAoc2Vjb25kYXJ5IHNjaG9vbCk_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFllcwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXZlIHlvdSByZWNlaXZlZCBhbnkgZWR1Y2F0aW9uIG9yIHRyYWluaW5nIG90aGVyIHRoYW4gaGlnaCBzY2hvb2wgKHNlY29uZGFyeSBzY2hvb2wpPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBZZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGlnaGVzdCBFZHVjYXRpb24gbGV2ZWwJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQmFjaGVsb3JzIFByb2dyYW0JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUHJvZ3JhbSBEdXJhdGlvbjogCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDUgeWVhcnMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRmllbGQgb2YgU3R1ZHk6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIENoZW1pY2FsIEVuZ2luZWVyaW5nIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlMb2NhdGlvbjogKCBjb3VudHJ5ICkJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTmlnZXJpYQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXZlIHlvdSBldmVyIHdvcmtlZCBpbiBDYW5hZGEgaW4gYSBza2lsbGVkIG9jY3VwYXRpb24gZm9yIGF0IGxlYXN0IG9uZSB5ZWFyPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBObwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXZlIHlvdSBkb25lIGFueSBwYWlkIHdvcmsgYW55IHdoZXJlIGluIHRoZSB3b3JsZCBkdXJpbmcgdGhlIGxhc3QgMTAgeWVhcnM_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFllcwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlPY2N1cGF0aW9uOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBIZWFsdGggYW5kIFNhZmV0eSBPZmZpY2VyIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlXb3JrIEV4cGVyaWVuY2U6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDMgeWVhcnMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJVHlwZSBvZiBKb2I6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZ1bGwgdGltZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlEbyB5b3UgaGF2ZSBhIHdyaXR0ZW4gam9iIG9mZmVyIGZyb20gYSBDYW5hZGlhbiBlbXBsb3llcj8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU9jY3VwYXRpb246CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQcm92aW5jZTogCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlJcyB0aGlzIGpvYiBvZmZlciByZWxhdGVkIHRvIHlvdXIgbW9zdCByZWNlbnQgZmllbGQgb2Ygc3R1ZHk_IAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRG8geW91IGhhdmUgYW55IGZhbWlseSBtZW1iZXJzLCByZWxhdGl2ZXMsIG9yIGNsb3NlIGZyaWVuZHMgbGl2aW5nIGluIENhbmFkYT8gIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBZZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUGxlYXNlIHVzZSB0aGlzIHNwYWNlIHRvIHNlbmQgYW55IHNwZWNpZmljIHF1ZXN0aW9ucywgY29tbWVudHMgb3IgYWRkaXRpb25hbCBpbmZvcm1hdGlvbiB0aGF0IG1heSBiZSByZWxldmFudCB0byB5b3VyIGlucXVpcnkuCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQbGVhc2UgY2hlY2sgaGVyZToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxvY2F0aW9uICggY2l0eSApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIER1YmFpIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQbGVhc2UgdXBsb2FkIHRoZSBsYXRlc3QgY29weSBvZiB5b3VyIFJlc3VtZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBodHRwOi8vd3d3LmpvdGZvcm0uY29tL3VwbG9hZHMva2FiaXJraGFubmEvNDEyMDE1MDc0ODE5NDkvMzQyOTMxNjg5ODQyNzU3Mzk0L1JpZHdhbkNWLmRvYwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlBZ2UJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgMzAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTG9jYXRpb24gKCBjaXR5ICApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE9nYm9tb3NvIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlEaWQgeW91IGNvbXBsZXRlIHRoaXMgcHJvZ3JhbT8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgWWVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxvY2F0aW9uICggY291bnRyeSApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFVuaXRlZCBBcmFiIEVtaXJhdGVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVNwb3VzZSBOYW1lCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAsIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXMgc3BvdXNlIGNvbXBsZXRlZCBIaWdoIHNjaG9vbCA_ICggc2Vjb25kYXJ5IHNjaG9vbCApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIaWdoZXN0IEVkdWNhdGlvbiBsZXZlbCBvZiBzcG91c2UJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVByb2dyYW0gZHVyYXRpb24JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUZpZWxkIG9mIHN0dWR5CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlBZ2Ugb2YgeW91ciBzcG91c2UJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUN1cnJlbnQgU3RhdGUJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUNob29zZSB0aGUgU2tpbGxlZCB3b3JrZXIgb2NjdXBhdGlvbiB0aGF0IHlvdSB3aXNoIHRvIGFwcGx5IHVuZGVyCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE15IHNraWxscyBhcmUgbm90IGxpc3RlZCBoZXJlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUVudGVyIHlvdXIgc2tpbGxzIGhlcmUJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgT2NjdXBhdGlvbmFsIEhlYWx0aCBhbmQgU2FmZXR5IE9mZmljZXIgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUVudGVyIHlvdXIgY3VycmVudCBqb2IgZGVzY3JpcHRpb24gaW4geW91ciBvd24gIHdvcmRzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlDdXJyZW50IGNpdHkgb2YgcmVzaWRlbmNlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIER1YmFpIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXZlIHlvdSB0YWtlbiB0aGUgSUVMVFMgZXhhbSA_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5vCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVdoZW4gZG8geW91IHBsYW4gdG8gdGFrZSB5b3VyIElFTFRTIGV4YW0gPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAyIG1vbnRocyBmcm9tIG5vdwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlXaGF0IHdhcyB5b3VyIElFTFRTIHNjb3JlID8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhvdyBkaWQgeW91IGhlYXIgYWJvdXQgdXMgPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBGYWNlYm9vawkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICANCg0KDQogICAgICAgICAgICANCiAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgSnVuIDI4LCAyMDE2IDg6NTBhbQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUZpcnN0IG5hbWU6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFudXAgS3VtYXIJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTGFzdCBOYW1lIDoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgVGFtYW5nCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUVtYWlsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGFudXBrdW1hcnRhbWFuZ0BnbWFpbC5jb20JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUGhvbmUgbnVtYmVyCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDk4NDkwODI0NjAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTmF0aW9uYWxpdHk6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5lcGFsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU1hcml0YWwtU3RhdHVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE1hcnJpZWQJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJV2hhdCBpcyB5b3VyIGN1cnJlbnQgY291bnRyeSByZXNpZGVuY2U6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5lcGFsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCURvIHlvdSBoYXZlIGFueSBkZXBlbmRlbnQgQ2hpbGRlcm46CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5vCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVdoYXQgaXMgeW91ciBQcmVmZXJyZWQgRGVzdGluYXRpb24gaW4gQ2FuYWRhOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBPbnRhcmlvCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCTEyIFllYXJzIGFuZCBZb3VuZ2VycyAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCTEzIHRvIDIxIFllYXJzOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJMjIgWWVhciBhbmQgT2xkZXI6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlOZXQgV29ydGg6IGluIFVTRAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA1MDAwIC0gMTAwMDAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRW5nbGlzaDoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgU3BlYWs7IFJlYWQ7IFdyaXRlOyBMaXN0ZW4JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRnJlbmNoOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGF2ZSB5b3UgY29tcGxldGVkIGhpZ2ggc2Nob29sIChzZWNvbmRhcnkgc2Nob29sKT8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgWWVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhhdmUgeW91IHJlY2VpdmVkIGFueSBlZHVjYXRpb24gb3IgdHJhaW5pbmcgb3RoZXIgdGhhbiBoaWdoIHNjaG9vbCAoc2Vjb25kYXJ5IHNjaG9vbCk_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFllcwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIaWdoZXN0IEVkdWNhdGlvbiBsZXZlbAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBCYWNoZWxvcnMgUHJvZ3JhbQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQcm9ncmFtIER1cmF0aW9uOiAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgNCB5ZWFycwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlGaWVsZCBvZiBTdHVkeToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgY29tcHV0ZXIgc2NpZW5jZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlMb2NhdGlvbjogKCBjb3VudHJ5ICkJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTmVwYWwJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGF2ZSB5b3UgZXZlciB3b3JrZWQgaW4gQ2FuYWRhIGluIGEgc2tpbGxlZCBvY2N1cGF0aW9uIGZvciBhdCBsZWFzdCBvbmUgeWVhcj8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTm8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGF2ZSB5b3UgZG9uZSBhbnkgcGFpZCB3b3JrIGFueSB3aGVyZSBpbiB0aGUgd29ybGQgZHVyaW5nIHRoZSBsYXN0IDEwIHllYXJzPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBZZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJT2NjdXBhdGlvbjoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgd2ViIGRldmVsb3BlcgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlXb3JrIEV4cGVyaWVuY2U6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDIgeWVhcnMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJVHlwZSBvZiBKb2I6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZ1bGwgdGltZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlEbyB5b3UgaGF2ZSBhIHdyaXR0ZW4gam9iIG9mZmVyIGZyb20gYSBDYW5hZGlhbiBlbXBsb3llcj8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU9jY3VwYXRpb246CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQcm92aW5jZTogCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlJcyB0aGlzIGpvYiBvZmZlciByZWxhdGVkIHRvIHlvdXIgbW9zdCByZWNlbnQgZmllbGQgb2Ygc3R1ZHk_IAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRG8geW91IGhhdmUgYW55IGZhbWlseSBtZW1iZXJzLCByZWxhdGl2ZXMsIG9yIGNsb3NlIGZyaWVuZHMgbGl2aW5nIGluIENhbmFkYT8gIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBZZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUGxlYXNlIHVzZSB0aGlzIHNwYWNlIHRvIHNlbmQgYW55IHNwZWNpZmljIHF1ZXN0aW9ucywgY29tbWVudHMgb3IgYWRkaXRpb25hbCBpbmZvcm1hdGlvbiB0aGF0IG1heSBiZSByZWxldmFudCB0byB5b3VyIGlucXVpcnkuCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQbGVhc2UgY2hlY2sgaGVyZToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxvY2F0aW9uICggY2l0eSApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGthdGhtYW5kdQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQbGVhc2UgdXBsb2FkIHRoZSBsYXRlc3QgY29weSBvZiB5b3VyIFJlc3VtZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJQWdlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDI0CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxvY2F0aW9uICggY2l0eSAgKQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBrYXRobWFuZHUJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRGlkIHlvdSBjb21wbGV0ZSB0aGlzIHByb2dyYW0_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFllcwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlMb2NhdGlvbiAoIGNvdW50cnkgKQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBOZXBhbAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlTcG91c2UgTmFtZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBQcmF0aWtzaHlhICBCaGFuZGFyaQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXMgc3BvdXNlIGNvbXBsZXRlZCBIaWdoIHNjaG9vbCA_ICggc2Vjb25kYXJ5IHNjaG9vbCApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFllcwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIaWdoZXN0IEVkdWNhdGlvbiBsZXZlbCBvZiBzcG91c2UJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgSGlnaCBTY2hvb2wJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUHJvZ3JhbSBkdXJhdGlvbgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBMZXNzIHRoYW4gMyB5ZWFycwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlGaWVsZCBvZiBzdHVkeQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBtYW5hZ2VtZW50CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUFnZSBvZiB5b3VyIHNwb3VzZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAyNAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlDdXJyZW50IFN0YXRlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlDaG9vc2UgdGhlIFNraWxsZWQgd29ya2VyIG9jY3VwYXRpb24gdGhhdCB5b3Ugd2lzaCB0byBhcHBseSB1bmRlcgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQ29tcHV0ZXIgcHJvZ3JhbW1lcnMgYW5kIGludGVyYWN0aXZlIG1lZGlhIGRldmVsb3BlcnMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRW50ZXIgeW91ciBza2lsbHMgaGVyZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRW50ZXIgeW91ciBjdXJyZW50IGpvYiBkZXNjcmlwdGlvbiBpbiB5b3VyIG93biAgd29yZHMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgZnJvbnRlbmQgd2ViIGRldmVsb3BlcgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlDdXJyZW50IGNpdHkgb2YgcmVzaWRlbmNlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIExhbGl0cHVyCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhhdmUgeW91IHRha2VuIHRoZSBJRUxUUyBleGFtID8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgWWVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVdoZW4gZG8geW91IHBsYW4gdG8gdGFrZSB5b3VyIElFTFRTIGV4YW0gPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJV2hhdCB3YXMgeW91ciBJRUxUUyBzY29yZSA_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDcJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSG93IGRpZCB5b3UgaGVhciBhYm91dCB1cyA_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZhY2Vib29rCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBKdW4gMjgsIDIwMTYgNjoyNmFtCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRmlyc3QgbmFtZToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUHVuYW0gCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxhc3QgTmFtZSA6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEd1cnVuZyBCaGFyYXRpCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUVtYWlsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIHB1bmFtZ3JnNzc3N0BnbWFpbC5jb20JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUGhvbmUgbnVtYmVyCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDk4NDYwMzgwMTYJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTmF0aW9uYWxpdHk6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5lcGFsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU1hcml0YWwtU3RhdHVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE1hcnJpZWQJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJV2hhdCBpcyB5b3VyIGN1cnJlbnQgY291bnRyeSByZXNpZGVuY2U6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5lcGFsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCURvIHlvdSBoYXZlIGFueSBkZXBlbmRlbnQgQ2hpbGRlcm46CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFllcwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlXaGF0IGlzIHlvdXIgUHJlZmVycmVkIERlc3RpbmF0aW9uIGluIENhbmFkYToJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQWxiZXJ0YQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkxMiBZZWFycyBhbmQgWW91bmdlcnMgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDIJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJMTMgdG8gMjEgWWVhcnM6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkyMiBZZWFyIGFuZCBPbGRlcjoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU5ldCBXb3J0aDogaW4gVVNECQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDAgLSA1MDAwCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUVuZ2xpc2g6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNwZWFrOyBSZWFkOyBXcml0ZTsgTGlzdGVuCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUZyZW5jaDoJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhhdmUgeW91IGNvbXBsZXRlZCBoaWdoIHNjaG9vbCAoc2Vjb25kYXJ5IHNjaG9vbCk_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFllcwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXZlIHlvdSByZWNlaXZlZCBhbnkgZWR1Y2F0aW9uIG9yIHRyYWluaW5nIG90aGVyIHRoYW4gaGlnaCBzY2hvb2wgKHNlY29uZGFyeSBzY2hvb2wpPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBZZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGlnaGVzdCBFZHVjYXRpb24gbGV2ZWwJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQmFjaGVsb3JzIFByb2dyYW0JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUHJvZ3JhbSBEdXJhdGlvbjogCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDQgeWVhcnMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRmllbGQgb2YgU3R1ZHk6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE1hbmFnZW1lbnQJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTG9jYXRpb246ICggY291bnRyeSApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5lcGFsCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhhdmUgeW91IGV2ZXIgd29ya2VkIGluIENhbmFkYSBpbiBhIHNraWxsZWQgb2NjdXBhdGlvbiBmb3IgYXQgbGVhc3Qgb25lIHllYXI_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIE5vCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUhhdmUgeW91IGRvbmUgYW55IHBhaWQgd29yayBhbnkgd2hlcmUgaW4gdGhlIHdvcmxkIGR1cmluZyB0aGUgbGFzdCAxMCB5ZWFycz8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgWWVzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU9jY3VwYXRpb246CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFkbWluIGFuZCBGaW5hbmNlIE9mZmljZXIJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJV29yayBFeHBlcmllbmNlOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAxIHllYXIJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJVHlwZSBvZiBKb2I6CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZ1bGwgdGltZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlEbyB5b3UgaGF2ZSBhIHdyaXR0ZW4gam9iIG9mZmVyIGZyb20gYSBDYW5hZGlhbiBlbXBsb3llcj8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCU9jY3VwYXRpb246CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQcm92aW5jZTogCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlJcyB0aGlzIGpvYiBvZmZlciByZWxhdGVkIHRvIHlvdXIgbW9zdCByZWNlbnQgZmllbGQgb2Ygc3R1ZHk_IAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRG8geW91IGhhdmUgYW55IGZhbWlseSBtZW1iZXJzLCByZWxhdGl2ZXMsIG9yIGNsb3NlIGZyaWVuZHMgbGl2aW5nIGluIENhbmFkYT8gIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBObwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQbGVhc2UgdXNlIHRoaXMgc3BhY2UgdG8gc2VuZCBhbnkgc3BlY2lmaWMgcXVlc3Rpb25zLCBjb21tZW50cyBvciBhZGRpdGlvbmFsIGluZm9ybWF0aW9uIHRoYXQgbWF5IGJlIHJlbGV2YW50IHRvIHlvdXIgaW5xdWlyeS4JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCVBsZWFzZSBjaGVjayBoZXJlOgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTG9jYXRpb24gKCBjaXR5ICkJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUG9raGFyYQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlQbGVhc2UgdXBsb2FkIHRoZSBsYXRlc3QgY29weSBvZiB5b3VyIFJlc3VtZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJQWdlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDMxCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUxvY2F0aW9uICggY2l0eSAgKQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBQb2toYXJhCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCURpZCB5b3UgY29tcGxldGUgdGhpcyBwcm9ncmFtPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBZZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJTG9jYXRpb24gKCBjb3VudHJ5ICkJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgTmVwYWwJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJU3BvdXNlIE5hbWUJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgUHJha2FzaCAgQmhhcmF0aQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIYXMgc3BvdXNlIGNvbXBsZXRlZCBIaWdoIHNjaG9vbCA_ICggc2Vjb25kYXJ5IHNjaG9vbCApCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFllcwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIaWdoZXN0IEVkdWNhdGlvbiBsZXZlbCBvZiBzcG91c2UJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgRGlwbG9tYSAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJUHJvZ3JhbSBkdXJhdGlvbgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAzIHllYXJzCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCUZpZWxkIG9mIHN0dWR5CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEp1bmlvciBFbmdpbmVlcgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlBZ2Ugb2YgeW91ciBzcG91c2UJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgMzEJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJQ3VycmVudCBTdGF0ZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJQ2hvb3NlIHRoZSBTa2lsbGVkIHdvcmtlciBvY2N1cGF0aW9uIHRoYXQgeW91IHdpc2ggdG8gYXBwbHkgdW5kZXIJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEZpbmFuY2lhbCBhdWRpdG9ycyBhbmQgYWNjb3VudGFudHMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRW50ZXIgeW91ciBza2lsbHMgaGVyZQkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJRW50ZXIgeW91ciBjdXJyZW50IGpvYiBkZXNjcmlwdGlvbiBpbiB5b3VyIG93biAgd29yZHMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQWRtaW4gYW5kIEZpbmFuY2UgT2ZmaWNlcgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlDdXJyZW50IGNpdHkgb2YgcmVzaWRlbmNlCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFBva2hhcmEJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJSGF2ZSB5b3UgdGFrZW4gdGhlIElFTFRTIGV4YW0gPwkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBZZXMJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJV2hlbiBkbyB5b3UgcGxhbiB0byB0YWtlIHlvdXIgSUVMVFMgZXhhbSA_CQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlXaGF0IHdhcyB5b3VyIElFTFRTIHNjb3JlID8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgNgkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQlIb3cgZGlkIHlvdSBoZWFyIGFib3V0IHVzID8JCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgRmFjZWJvb2sJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgDQoNCg0KICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgCQkNCiAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBFZGl0IFlvdXIgRGFpbHkgRGlnZXN0IFNldHRpbmdzDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBJZiB5b3Ugbm8gbG9uZ2VyIHdhbnQgdG8gcmVjZWl2ZSBlbWFpbHMgZnJvbSBKb3RGb3JtLCBwbGVhc2UgdW5zdWJzY3JpYmUuDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAkJDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgDQoNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgICAgICAgICAgICAgICAgIA0KDQoNCiAgICAgICAgICAgICAgICAgICAgICAgIA0KICAgICAgICAgICAgICAgICAgICANCiAgICAgICAgICAgICAgICAJCQ0KICAgICAgICAgICAgDQoNCiAgICAgICAgICAgIA0KICAgICAgICANCiAgICANCg0KDQoNCg==";
        String encoded2 = "DQpSZWZlcmVuY2VzOiA8RTFhbk5POS0wMDAxeE8tVUJAdHJpbml0eS5zdXBlcmRuc3NpdGUuY29tPg0KIDw1NzBCNDNEQy41MDIwNEBlbWJpb25pY3MuY29tPg0KIDxDQUZBLWQxMTM9SjBkTnprWUJKbXN3emJ5U3lDdmpNX1FIZTVIRlRzZEZualYtRFRmVGdAbWFpbC5nbWFpbC5jb20-";
        String encoded3 = "DQogPDU3MEYyN0FCLjMwNzAwMDhAZW1iaW9uaWNzLmNvbT4NCiA8Q0FGQS1kMTI1aVZqM0ZZVzVpMS0tVlZPYkdaRmRIb0hWZkJ4aTJRQy0weDNacjRCN0pnQG1haWwuZ21haWwuY29tPg0KRnJvbTogQ2hhaXRhbm55YSBNYWhhdG1lIDxjYXJlZXJAZW1iaW9uaWNzLmNvbT4NCk1lc3NhZ2UtSUQ6IDw1NzEwOEVGMy45MDAwNTAxQGVtYmlvbmljcy5jb20-";
        String encoded4 = "DQpEYXRlOiBGcmksIDE1IEFwciAyMDE2IDEyOjE5OjIzICswNTMwDQpVc2VyLUFnZW50OiBNb3ppbGxhLzUuMCAoWDExOyBMaW51eCB4ODZfNjQ7IHJ2OjM4LjApIEdlY2tvLzIwMTAwMTAxDQogVGh1bmRlcmJpcmQvMzguMS4wDQpNSU1FLVZlcnNpb246IDEuMA0KSW4tUmVwbHktVG86IDxDQUZBLWQxMjVpVmozRllXNWkxLS1WVk9iR1pGZEhvSFZmQnhpMlFDLTB4M1pyNEI3SmdAbWFpbC5nbWFpbC5jb20-";
        String encoded5 = "U2lyLA0KSSAgaGF2ZSBhbHJlYWR5IG1haWxlZCB5b3UgdGhlIHJlcXVpcmVkIGRvY3VtZW50cy4NCg0KVGhhbmtzDQoNCk9uIFNhdCwgTWF5IDcsIDIwMTYgYXQgMToyMyBQTSwgS2FiaXIgS2hhbm5hIDxra2hhbm5hQHNpbXBsaWxlbmQuY29tPiB3cm90ZToNCg0KPiBEZWFyIENhbmRpZGF0ZSwNCj4gVGhpcyBpcyBhIHJlbWluZGVyIG1lc3NhZ2UgdGhhdCB5b3UgbmVlZCB0byBzdWJtaXQgdGhlIGZvbGxvd2luZyBkb2N1bWVudHMNCj4gaW4gc29mdCBjb3B5IGJ5IHRoZSAxMHRoIG9mIE1heS4gIElmIHlvdSBkb250IGhhdmUgc29tZSBvZiB0aGVzZSB0aGVzZSB3aXRoDQo-";
        String encoded6 = "SGVsbG8gc2lyLA0KVGhhbmtzIGZvciB0aGUgYWJvdmUgaW5mb3JtYXRpb24gcmVnYXJkaW5nIGpvaW5pbmcgZGV0YWlscyBhbmQNCmFjY29tbW9kYXRpb24uIEkgYW0gb3B0aW5nIGZvciB0aGUqIGNvbXBhbnkgYXNzaWduZWQgYWNjb21tb2RhdGlvbiouDQoNClRoYW5raW5nIHlvdS4NCg0KU3VtaXQgS3VtYXIgUHJhZGhhbg0KDQpPbiBTYXQsIE1heSA3LCAyMDE2IGF0IDE6MDIgUE0sIEthYmlyIEtoYW5uYSA8a2toYW5uYUBzaW1wbGlsZW5kLmNvbT4gd3JvdGU6DQoNCj4gRGVhciBDYW5kaWRhdGUsDQo-";
        String encoded7 = "SGVsbG8gU3VtaXQsDQoNClRoZSByZXN1bHQgSSBhbSBnZXR0aW5nIGlzIDoNCi9MMToxLCAyLCAzLCA0LCA1LCA3LCA4LCA5LCAxMCwgMTEvLw0KLy9MMjo1LCAxMiwgMTMsIDE0LCAxNSwgMTYsIDE3LCAxOCwgMTksIDIwLCA3LCA4LCA5LCAxMCwgMTEvLw0KLy9TbyBoZXJlIGludGVyc2VjdGluZyBub2RlIGlzIDcgLiAvLw0KLy9NZXJnZWQgbGlzdDoxLCAyLCAzLCA0LCA1LCA1LCA3LCA4LCA5LCAxMCwgMTEsIDEyLCAxMywgMTQsIDE1LCAxNiwgMTcsIA0KMTgsIDE5LCAyMC8vDQovDQoNCiAxLiBZb3Ugc2hvdWxkIGZpcnN0IGFzayB1c2VyIGhvdyBtYW55IG51bWJlcnMgaGUgd2FudHMgdG8gZW50ZXIsIHBsZWFzZQ0KICAgIHJlbW92ZSBoYXJkY29kZWQgMTAgbnVtYmVycy4NCiAyLiBJbnRlcnNlY3Rpb24gbm9kZSBpcyBpbmNvcnJlY3QuDQogMy4gSW4gdGhlIG1lcmdlZCBsaXN0IHRoZSBpbnRlcnNlY3Rpb24gbm9kZSBpcyByZXBlYXRlZC4gVGhlIG1lYXJnZWQNCiAgICBsaXN0IHNob3VsZCBjb250YWluIG9ubHkgb25lIGluc3RhbmNlIG9mIGNvbW1vbiBub2Rlcy4NCg0KUGxlYXNlIG1hbnkgdGhlc2UgY2hhbmdlcyAmIHRoZW4gd2UgYXJlIGdvb2QgdG8gZ28uDQoNClJlZ2FyZHMsDQoNCkNoYWl0YW5ueWEgTWFoYXRtZQ0KDQpFbWJpb25pbmNzIFRlY2hub2xvZ2llcyBQdnQgTHRkDQpQaCBubyAJOiAJOTIyNTUwMjM0MA0KV2ViIHNpdGUgCTogCWh0dHA6Ly9lbWJpb25pY3MuY29tIDxodHRwOi8vZW1iaW9uaWNzLmNvbT4NCg0KDQpPbiBGcmlkYXkgMTUgQXByaWwgMjAxNiAxMDoyMyBBTSwgU3VtaXQgUHJhZGhhbiB3cm90ZToNCj4gSGVsbG8gU2lyLA0KPiBJIGFtIGV4dHJlbWVseSBzb3JyeSBmb3IgdGhlIGRlbGF5IGluIHRoZSBzdWJtaXNzaW9uIG9mIHRoZSANCj4gYXNzaWdubWVudC4gRHVlIHRvIHdlYWsgaW50ZXJuZXQgY29ubmVjdGlvbiB5ZXN0ZXJkYXkgYXQgb3V0IGhvc3RlbCBJIA0KPiBjb3VsZG4ndCBtYWlsIHlvdSB0aGUgYXNzaWdubWVudCB3aXRoaW4gdGhlIGRlYWRsaW5lIGFmdGVyIHRoZSANCj4gZmFyZXdlbGwgcHJvZ3JhbSBlbmRlZC4gSSBhbSBzdWJtaXR0aW5nIHRoZSBjb2RlIGFzIHdlbGwgYXMgdGhlIA0KPiBkb2N1bWVudGF0aW9uIG5vdyBhcyBhbiBhdHRhY2htZW50ICB3aXRoIHRoaXMgbWFpbC4NCj4NCj4gUGxlYXNlIHNpciwgZG8gY29uc2lkZXIgbXkgYXNzaWdubWVudCBmb3IgZXZhbHVhdGlvbi4gSSBhc3N1cmUgeW91IA0KPiB0aGF0IHRoZXJlIHdvdWxkbid0IGJlIGFueSBzdWNoIGRlbGF5IGFnYWluLg0KPg0KPiBUaGFua2luZyB5b3UuDQo-";

//        byte[] byteData1 = Base64.decodeBase64(encoded1);
        byte[] byteData2 = Base64.decodeBase64(encoded2);
        byte[] byteData3 = Base64.decodeBase64(encoded3);
        byte[] byteData4 = Base64.decodeBase64(encoded4);
        byte[] byteData5 = Base64.decodeBase64(encoded5);
        byte[] byteData6 = Base64.decodeBase64(encoded6);
        byte[] byteData7 = Base64.decodeBase64(encoded7);
  //      String decoded1 = new String(byteData1);
        String decoded2 = new String(byteData2);
        String decoded3 = new String(byteData3);
        String decoded4 = new String(byteData4);
        String decoded5 = new String(byteData5);
        String decoded6 = new String(byteData6);
        String decoded7 = new String(byteData7);

        String filtered = decoded7.substring(0, decoded7.indexOf("wrote:"));
        System.out.println("filtered string: " + filtered);

    //    System.out.println("decoded String:" + decoded1);
        System.out.println("\n\n-------------------------------------------\n\n");
        System.out.println("decoded String:" + decoded2);
        System.out.println("\n\n-------------------------------------------\n\n");
        System.out.println("decoded String:" + decoded3);
        System.out.println("\n\n-------------------------------------------\n\n");
        System.out.println("decoded String:" + decoded4);
        System.out.println("\n\n-------------------------------------------\n\n");
        System.out.println("decoded String:" + decoded5);
        System.out.println("\n\n-------------------------------------------\n\n");
        System.out.println("decoded String:" + decoded6);
        System.out.println("\n\n-------------------------------------------\n\n");
        System.out.println("decoded String:" + decoded7);

        System.exit(0);
    }

}
