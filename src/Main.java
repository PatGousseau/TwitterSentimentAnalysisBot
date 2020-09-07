import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UploadedMedia;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import com.vader.sentiment.analyzer.SentimentAnalyzer;

import twitter4j.StatusUpdate;


   public class Main  {
	   
	   private static final String FILE_NAME = "pastUsers.txt";
 	   private static Twitter twitter = TwitterFactory.getSingleton();
 	   private static ArrayList<Status> allStatuses = new ArrayList<Status>(); // All tweets of user
 	   private static ArrayList<Status> allMentionsOfMe; // All mentions of me
 	   private static ArrayList<String> exTweets = new ArrayList<String>(); // Example tweets



 	  /** 
 	   * This method writes all tweets and retweets from a given user to .txt file and calls Python script to analyze them
 	   */ 
	   public static void getUserAnalysis(String user) throws IOException {

				// Get all their tweets
		    	  try {
					allStatuses = getTweets(user);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
		    	  
		    	  // Write all tweets to .txt file
		    	  writeTweetsToFile(allStatuses,"allTweets.txt"); 
		    	  Runtime rt_2 = Runtime.getRuntime();
				  Process pr_2 = rt_2.exec("python C:\\Users\\pcgou\\eclipse-workspace\\Twitter_Bot\\get_sentiment.py");
				  try {
					pr_2.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				  getResults("allTweets.txt");
	   }
	   
 	  /** 
 	   * This method runs a Python script to create sentiment chart. Chart is then read back in and tweeted to the user.
 	   */
	   public static void createGraphic(String statusUser,String analyzedUser, long inReplyToStatusId) throws IOException, TwitterException, InterruptedException {

		   Runtime rt = Runtime.getRuntime();
		   Process pr = rt.exec("python C:\\Users\\pcgou\\eclipse-workspace\\Twitter_Bot\\create_graphic.py " + analyzedUser);
		   pr.waitFor();
		   File file = new File("PieChart.png");
		   UploadedMedia media = twitter.uploadMedia(file);
		   long  mediaId = media.getMediaId();
		   String replyMessage =  "Sentiment analysis complete, " + "@" + statusUser +". " +  "\n \nThis process is automated. If you have any questions please contact me at pcgousseau@gmail.com";

		   StatusUpdate statusUpdate = new StatusUpdate(replyMessage);
		   statusUpdate.setInReplyToStatusId(inReplyToStatusId);
		   statusUpdate.setMediaIds(mediaId);
		   Status status = twitter.updateStatus(statusUpdate);
	   }
	  
 	  /** 
 	   * This method adds the tweet to the tweet ID database containing all previous mentions of "@yourStatsBot"
 	   */
	  public static void  addTweetToList(String tweetId) {
		  
		  ArrayList<String> allStatusesOfMe = new ArrayList<String>(); // All past users
		
		  try{
				BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
				String line = br.readLine(); // Read the first line
				  
				// If this is the very first user to use the system 
				if(line == null) {
					allStatusesOfMe.add(tweetId);
				}
					
				else {
					int index = 0;
					// Add all statuses in .txt to arraylist
					while(line != null){
						allStatusesOfMe.add(line);
						index++;
						line = br.readLine();
					}

					// Add new user to end of arraylist
					if(!alreadyAnalysed(tweetId)) {
						allStatusesOfMe.add(tweetId);
					}
				}
				
				// Write full list of users back to the tweet ID database
				PrintWriter writer = null;
				FileWriter fw = new FileWriter(FILE_NAME, false);
				writer = new PrintWriter(fw);
				int newSize = allStatusesOfMe.size();
							
				for(int i = 0; i < newSize; i ++) {
					writer.println(allStatusesOfMe.get(i));
				}
				writer.close();
			}
			catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}			
	}

 	  /** 
 	   * This method parses through the tweet ID database and determines if a given tweet has previously been analyzed
 	   */
	  private static boolean alreadyAnalysed(String tweetId) {
		 
		  boolean alreadyAnalysed = false;
		  try{
				BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
				String line = br.readLine(); // Read the first line
	
					int index = 0;
					// Check if tweet ID is already in the tweet ID database
					while(line != null){

						if(line.equals(tweetId)) {
							alreadyAnalysed = true;
						}
						index++;
						line = br.readLine();
					}
		  }
			catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}
		  return alreadyAnalysed;
	}

  /** 
   * This method retrieves and returns all tweets and retweets from a given user
   */ 
    public static ArrayList getTweets(String user) throws TwitterException {
  	  
  	  	Paging page = new Paging(1,200);
  	  	int p = 1;
  	  	page.setPage(p);
	  	ArrayList<Status> testStatuses =(ArrayList<Status>)(twitter.getUserTimeline(user,page));
	  	while(p<= 10) {
  		  p++;
  		  page.setPage(p);
  		  testStatuses.addAll(twitter.getUserTimeline(user,page));
	  	}
  	 
	  	return testStatuses;
    }
    
    /** 
     * This method write Tweets To File
     */
    public static void writeTweetsToFile(ArrayList<Status> allTweets, String fileName) {

    	PrintWriter writer = null;
		FileWriter fw;
		try {
			fw = new FileWriter(fileName, false);
		
		writer = new PrintWriter(fw);
		int size = allTweets.size();
					
		for(int i = 0; i < size-1; i ++) {
			writer.println(allTweets.get(i).getText().replaceAll("\r", "").replaceAll("\n", ""));
		}
		
		writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    /** 
     * This method retrieves the results from the Python sentiment analysis
     */
    public static void getResults(String fileName) {
    	String[][] results = new String[2000][2];
    
    	try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));

				int index = 0;
				String score = null;
				String tweet = null;
				double max = 0.0;
				double min = 0.0;
				String maxEx = null;
				String minEx = null;
				
				// Reads all statuses in sentiment analysis database into arraylist
				score = br.readLine();
				while(score != null && index < 2000){
					tweet = br.readLine();
					if(Double.parseDouble(score) > max && ((tweet.charAt(0) != 'R') && (tweet.charAt(1) != 'T'))){
						max = Double.parseDouble(score);
						maxEx = tweet;
					}
					else if(Double.parseDouble(score) < min && ((tweet.charAt(0) != 'R') && (tweet.charAt(1) != 'T'))) {
						min = Double.parseDouble(score);
						minEx = tweet;
					}
					score = br.readLine();
					index++;
				}
				
				PrintWriter writer = null;
				FileWriter fw;
				ArrayList<String> examples = new ArrayList<String>();
				examples.add(maxEx);
				examples.add(minEx);
				
				try {
					fw = new FileWriter("tweetExamples.txt", false);
					writer = new PrintWriter(fw);
					int size = examples.size();
							
					try {
						for(int i = 0; i < size; i ++) {
							writer.println(examples.get(i).replaceAll("\r", "").replaceAll("\n", ""));
						}
					}
					catch(java.lang.NullPointerException e) {
						
					}
					
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    	}
    	
    	catch (IOException ioe) {}

			
    }
    
    

    // EXAMPLE ACCOUNTS: "benshapiro","elonmusk","RichardDawkins","BretWeinstein","PeterAttiaMD","djblitzwpg","KingJames","JoelEmbiid","SHAQ","Money23Green"
      public static void main (String []args) throws TwitterException, IOException {

    	  long delay = 120000; // Time between each checking of mentions (milliseconds)
    	  final Timer timer = new Timer();
    	  timer.schedule(new TimerTask(){
    		 
    		  public void run() {
    			
    		    	// Get all mentions of me
    				try {
						allMentionsOfMe = (ArrayList)twitter.getMentionsTimeline();
					} catch (TwitterException e1) {
						e1.printStackTrace();
					}

    				// Remove all elements that have already been analyzed
					ArrayList<Status> notAnalyzed = new ArrayList<Status>();

	    			for (int i = 0; i < allMentionsOfMe.size(); i++) {

	    				if(!(alreadyAnalysed(String.valueOf(allMentionsOfMe.get(i).getId())))) {
	    					notAnalyzed.add(allMentionsOfMe.get(i));
	    				}
					}

	    			System.out.println("QUEUE:");
  	    			for (int j = 0; j < notAnalyzed.size(); j++) {
  						System.out.println(j + ": " + notAnalyzed.get(j).getUser().getName());
  					}

	    			if(notAnalyzed.size()>0) {
    							// Add this mention of me to the tweet ID database
    							addTweetToList(String.valueOf(notAnalyzed.get(0).getId()));

    							// Get user to be analyzed
    							String analyzedUser = "";
    							String statusUser = notAnalyzed.get(0).getUser().getScreenName();
    							String tweet = notAnalyzed.get(0).getText(); // Get text from tweet
    							String[] tweetArray = tweet.split(" "); // Split tweet into array by space 
    							ArrayList<String> twoAccounts = new ArrayList<String>(); // Put mentions (items that start with @) in an ArrayList of two 
    					
    							try {
	    							for (int i = 0; i < tweetArray.length; i++) {
										if(tweetArray[i].charAt(0) == '@') {
											twoAccounts.add(tweetArray[i]);
										}
	    							}
    							
	    							if(twoAccounts.size() == 2) {
	    							// Analyze first mention that is not "@yourStatsBot"
		    							for (int j = 0; j < twoAccounts.size(); j++) {
											if(!(twoAccounts.get(j).equals("@yourStatsBot"))){
												analyzedUser = twoAccounts.get(j).substring(1);
											}
										}
	    							}
	    							
	    							else {
	    								analyzedUser = statusUser;
	    							}
    							}
    							catch (java.lang.StringIndexOutOfBoundsException e) {
    								
    							}
    							
    							// Get user analytics
    							try {
									getUserAnalysis(analyzedUser);
								} catch (IOException e1) {
									e1.printStackTrace();
								}

    							try {	 
    								createGraphic(statusUser,analyzedUser, notAnalyzed.get(0).getId());	 
    								
    							} catch (IOException e) {
    								e.printStackTrace();
    							} catch (TwitterException e) {
    								e.printStackTrace();
    							} catch (InterruptedException e) {
    								e.printStackTrace();
    							}
    							
    							// Empty all ArrayLists used for the next user
    							allStatuses.clear();
    							exTweets.clear();			
	    			}
  	    			notAnalyzed.clear();
    					
    			  
//    			  **FOR TESTING**
//    			  
//    			  String statusUser = "lenadunham";
//    			  try {
//					getUserAnalysis(statusUser);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    			  try {
//					createPie(statusUser);
//				} catch (IOException | TwitterException | InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//    			  System.out.println(index);
//    			  
//    			  if(index + 1 < size) {
//    				index++;
//    			  }
    		  }  
    	  },0,delay);
    	  
    	  
    	  //timer.cancel();

    	  }
      }
