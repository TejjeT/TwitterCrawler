package com.tej.twittersearch;

import java.util.HashSet;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class SearchTweets {
	/**
	 * Usage: java twitter4j.examples.search.SearchTweets [query]
	 *
	 * @param args search query
	 */
	
	static Twitter twitter = new TwitterFactory().getInstance();
	static HashSet<Long> id=new HashSet<Long>();
	
	static {
		twitter.setOAuthConsumer("SChtgehUQtgrecEju546Cj8Ge", "AHfjNUa7ipMpFSQfiolQVmRKTTe5FCIcA2jyha4eJ8WLFlKJuH");
		String accessToken = "137679431-TXnSrWe3fpEfZaXc80BU1WOX3JofbPWoiwXi9HUk";
		String accessTokenSecret = "TTHv1kJP0hWP8ECTEwu2kZeHJ26KdGzxz4xrhcQa60eTi";
		AccessToken oathAccessToken = new AccessToken(accessToken, accessTokenSecret);
		twitter.setOAuthAccessToken(oathAccessToken);
		
	}
	
	
	
	public static void main(String[] args) {

		
		Query query = new Query("ICYMI: The Maryland football team have new uniforms that include the Star-Spangled Banner on their helmet and jersey");
		query.setCount(100);
		searchTweetsByQuery(query);
		//twitter.
		// getTimeLineTweets( twitter);
		
		System.out.print(id.size());
		System.exit(0);
	}

	

	//The Query from:McDonalds returns all the activities done by McDonalds but not any user.the username will be McDoanlds always
	
	
	public static void searchTweetsByQuery(Query query1)
	{
		Query query=query1;
		QueryResult result=null;
		long maxId=0;
		try {
			
			do {
			
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				System.out.println(tweets.size());
				maxId=getlastIdafterProcessTweets(tweets);
				query = result.nextQuery();
				System.out.println("query"+query.getQuery());
			}while ((query) != null);
	
		}  catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			System.out.println("Nullpointer Exception");
		}
		query1.setMaxId(maxId);
		searchTweetsByQuery(query1);
		
}


	
//Limits to last 15 retweets of each tweet
public static long getlastIdafterProcessTweets(List<Status> tweets)
{
	long maxId=0;
	for (Status tweet : tweets) {
		System.out.println(tweet.getId()+"@" + tweet.getUser().getScreenName() + " - " + tweet.getText() );
		maxId=tweet.getId();
		try {			
			if(tweet. isRetweeted() )
			{
			List<Status> retweets =twitter.getRetweets(maxId);
			getlastIdafterProcessTweets(retweets);
			}
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(id.contains(maxId))
		{
			System.out.println("Duplicate");
		}
		else
		{
			id.add(maxId);
		}
	}
	return maxId;
}


//Shows my timeline..Not usefuly anyway in the project
public static void getTimeLineTweets(Twitter twitter)
{

	List<Status> statuses = null;
	try {
		
		statuses = twitter.getHomeTimeline();
	} catch (TwitterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("Showing home timeline.");
	for (Status status : statuses) {
		System.out.println(status.getUser().getName() + ":" + status.getText());
	}
}
public static void gettweetofReplies(long inReplyToStatusId )
{
	Status replyStatus=null;;
	try {
		replyStatus = twitter.showStatus(inReplyToStatusId);
		System.out.println(replyStatus.getText());
		gettweetofReplies(replyStatus.getInReplyToStatusId());
	} catch (TwitterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

public static void getretweets(Status retweetStatus )
{
	if(retweetStatus.isRetweeted())
	{
		
	}
	
}





}