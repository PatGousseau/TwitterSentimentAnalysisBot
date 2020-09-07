import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.TwitterException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Twitterer{
   
      private static Twitter twitter;
      private static long statusId;
      private static String name;
      
      public Twitterer(PrintStream console){
    	  twitter = TwitterFactory.getSingleton(); 
      }

      /** 
 	   * This method uses Stanford Core NLP to analyze the given text
 	   */ 
//      public int sentimentAnalysis(String text) {
//    	  
//	    	String sentiment = null;
//	    	int emotion = 0;
//	      	StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
//			
//			CoreDocument coreDocument = new CoreDocument(text);
//			
//			stanfordCoreNLP.annotate(coreDocument);
//			
//			List<CoreSentence> sentences = coreDocument.sentences();
//			
//			for(CoreSentence sentence : sentences) {
//				sentiment = sentence.sentiment();
//			}
//			
//			if(sentiment.equals("Very negative")) {
//				emotion = 1;
//			}
//			else if(sentiment.equals("Negative")) {
//				emotion = 2;
//			}
//			else if(sentiment.equals("Neutral")) {
//				emotion = 3;
//			}
//			else if(sentiment.equals("Positive")) {
//				emotion = 4;
//			}
//			else if(sentiment.equals("Very positive")) {
//				emotion = 5;
//			}
//			
//			return emotion;
//		
//      }
//
//      /** 
// 	   * This method retrieves and returns all tweets and retweets from a given user
// 	   */ 
//      public static ArrayList getTweets(User user) throws TwitterException {
//    	  
//    	  Paging page = new Paging(1,200);
//    	  int p = 1;
//    	  page.setPage(p);
//    	  ArrayList<Status> testStatuses =(ArrayList<Status>)(twitter.getUserTimeline(user.getId(),page));
//    	  while(p<= 100) {
//    		  p++;
//    		  page.setPage(p);
//    		  System.out.println(twitter.getUserTimeline(user.getId(), page));
//    		  testStatuses.addAll(twitter.getUserTimeline(user.getId(),page));
//    	}
//    	  System.out.println(testStatuses.size());
//    	  return testStatuses;
//      }
//      
//   
//      public static long getStatusId() {
//    	  return statusId;
//      }
//      public static String getStatusUser() {
//    	  return name;
//      }

   
   }  
