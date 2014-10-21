package com.tej.twittersearch;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.json.DataObjectFactory;

public class GetRetweets {



	/**
	 * Shows up to 100 of the first retweets of a given tweet.
	 *
	 * @author Yusuke Yamamoto - yusuke at mac.com
	 */

	private static int last_searchId;
	//private static String row_count;
	private static int counter;
	private static boolean init=false;
	/**
	 * Usage: java twitter4j.examples.tweets.GetRetweets [status id]
	 *
	 * @param args message
	 */
	public static void main(String[] args) {

		ResultSet resultset=null;
		/* if (args.length < 1) {
	            System.out.println("Usage: java twitter4j.examples.tweets.GetRetweets [status id]");
	            System.exit(-1);
	        }*/
		//    System.out.println("Showing up to 100 of the first retweets of the status id - [" + args[0] + "].");
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer("SChtgehUQtgrecEju546Cj8Ge", "AHfjNUa7ipMpFSQfiolQVmRKTTe5FCIcA2jyha4eJ8WLFlKJuH");
			String accessToken = "137679431-TXnSrWe3fpEfZaXc80BU1WOX3JofbPWoiwXi9HUk";
			String accessTokenSecret = "TTHv1kJP0hWP8ECTEwu2kZeHJ26KdGzxz4xrhcQa60eTi";
			AccessToken oathAccessToken = new AccessToken(accessToken, accessTokenSecret);
			twitter.setOAuthAccessToken(oathAccessToken);
			Statement statement = MySQLConnFactory.getsqlstatementforExec();
			String sql = "  select distinct t1.searchid,text,records,tweetid,t1.rtstatus from tweet_data t1,search_master t2 where t1.searchid=t2.searchid and t1.rtstatus ='N' and retweetcount>50 and favouritecount>10";
//String sql="select distinct t1.searchid,text,records,tweetid,t1.rtstatus from tweet_data t1,search_master t2 where  t1.searchid=t2.searchid and t2.searchid=240 and t1.retweetedid=-1 and t1.tweetid not in (select retweetedid from tweet_data where retweetedid!=-1 and searchid=240) ";
		resultset=statement.executeQuery(sql);

			while (resultset.next())
			{
				List<Status> statuses=null;
				try
				{
				 statuses = twitter.getRetweets(new Double (resultset.getString(4)).longValue());
				 insertTweets(statuses, resultset.getInt(3), resultset.getInt(1));
				 updatecurrentRecord(resultset.getDouble(4),false);
				}
				catch(Exception e)
				{
					System.out.println("Exception"+e.getMessage());
					 updatecurrentRecord(resultset.getDouble(4),true);
				}
				
				
				 //counter++;
					

				 /*
				  * This will get 100 * 20 tweets at a given time.
				  */

				 //if (counter == 1) {
					
					 try {
						Thread.sleep(2 * 60 * 800);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				
						
				//	}
				/*query=new Query(resultset.getString(2));
				fetchTweets( query,  twitter, 0,0,resultset.getInt(1));*/
			}
			
			}
			System.out.println("done.");
			System.exit(0);
		}  catch (SQLException e)		{
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
		finally
		{
			try {
				if(resultset!=null&&!resultset.isClosed())
					resultset.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MySQLConnFactory.closeAll();
		}
	}





	private static void updatecurrentRecord(double tweetid,boolean iserror) {
		
			int rowCount=0;
			
			String  sql =null;
			if (!iserror)
				sql="update tweet_data set rtStatus='Y' where tweetid ="+tweetid+";";
			else
				sql="update tweet_data set rtStatus='E' where tweetid ="+tweetid+";";
			Statement statement = MySQLConnFactory.getsqlstatementforExec();

			try {
				rowCount=statement.executeUpdate(sql); 

			} catch (SQLException ex) {
				//ex.printStackTrace();
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
			
			
			if(rowCount!=1)
			{
				System.out.println("rtStatus update failed:"+tweetid);
			}
			
		
		
	}





	public static long insertTweets(List<Status> tweets ,int rowcount, int searchid)
	{
		String sql=null;
		long  lastTweetId=0;
		Statement statement=MySQLConnFactory.getsqlstatementforExec();
		for (Status tweet : tweets) {

			try
			{

				//if (printCounter != 1) {
					/*
					 * sql = "select max(searchid) as s from search_master";
					 * resultset = statement.executeQuery(sql); if
					 * (statement.execute(sql)) { resultset =
					 * statement.getResultSet(); } while (resultset.next())
					 * { last_searchId = resultset.getInt("s"); }
					 */

					try{
						
						
						if(tweet.getGeoLocation()!=null)
						sql = "insert into tweet_data values (" + searchid
								+ "," + tweet.getId() + "," + retweetId(tweet)
								+ ",'" + isReply(tweet) + "','"
								+ isFavourite(tweet) + "','" + isStatus(tweet)
								+ "','" + textCleaning(tweet) + "','"
								+ tweet.getCreatedAt() + "','"
								+ tweet.getUser().getScreenName() + "',"
								+ tweet.getRetweetCount() + ","
								+ tweet.getUser().getFollowersCount() + ","
								+ tweet.getInReplyToStatusId() + ",'"
								+ locationCleaning(tweet) + "',"     //13
								+ tweet.getFavoriteCount()+ ",'"
								+ r_isReply(tweet) + "','"
								+ r_isFavourite(tweet) + "','"
								+ r_isStatus(tweet) + "','"
								+ r_textCleaning(tweet) + "','"
								+ r_getCreatedAt(tweet) + "','"
								+ r_getUserName(tweet) + "',"
								+ r_getRetweetCount(tweet) + ","
								+ r_getFollowerCount(tweet) + ","
								+ r_InReply(tweet) + ",'"
								+ r_locationCleaning(tweet) +"',"
								+ r_getFavoriteCount(tweet) + 
								",'Y',"+tweet.getGeoLocation().getLatitude()+","+tweet.getGeoLocation().getLongitude()+")";
						else
							sql = "insert into tweet_data values (" + searchid
							+ "," + tweet.getId() + "," + retweetId(tweet)
							+ ",'" + isReply(tweet) + "','"
							+ isFavourite(tweet) + "','" + isStatus(tweet)
							+ "','" + textCleaning(tweet) + "','"
							+ tweet.getCreatedAt() + "','"
							+ tweet.getUser().getScreenName() + "',"
							+ tweet.getRetweetCount() + ","
							+ tweet.getUser().getFollowersCount() + ","
							+ tweet.getInReplyToStatusId() + ",'"
							+ locationCleaning(tweet) + "',"     //13
							+ tweet.getFavoriteCount()+ ",'"
							+ r_isReply(tweet) + "','"
							+ r_isFavourite(tweet) + "','"
							+ r_isStatus(tweet) + "','"
							+ r_textCleaning(tweet) + "','"
							+ r_getCreatedAt(tweet) + "','"
							+ r_getUserName(tweet) + "',"
							+ r_getRetweetCount(tweet) + ","
							+ r_getFollowerCount(tweet) + ","
							+ r_InReply(tweet) + ",'"
							+ r_locationCleaning(tweet) +"',"
							+ r_getFavoriteCount(tweet) + 
							",'Y',0.0,0.0)";
						System.out.println(sql);
						Integer cnt = statement.executeUpdate(sql);
						rowcount = rowcount + cnt;
					}
					catch(Exception e){
						System.out.println("Error while recording entry:"+e.getMessage()+" for the tweetid " );
						//System.out.println(sql);
					}
					lastTweetId = tweet.getId();

				/*}
				else{
					System.out.println("The Printer count is one");
				}*/
				/*if (printCounter == 0) {

				} else {
					printCounter++;
				}*/
			}
			catch(Exception e)
			{
				System.out.println("Error:"+e.getLocalizedMessage());
			}
		}


		rowcount=updateRowCount(  searchid);
		return lastTweetId;
	}

	public static int updateRowCount(int searchid)
	{
		int rowCount=0;
		System.out.println("updaating row count");
		String  sql = "select count(*) C from tweet_data where searchid="+searchid;
		Statement statement = MySQLConnFactory.getsqlstatementforExec();
		ResultSet resultset=null;

		try {
			resultset=statement.executeQuery(sql);
			if (resultset.next()) {
				rowCount=resultset.getInt(1);
			}

			sql = "update search_master set records = " + rowCount + " where searchid = " + searchid;

			statement.executeUpdate(sql); 

		} catch (SQLException ex) {
			//ex.printStackTrace();
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally
		{
			try {
				resultset.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rowCount;


	}


	public static int getSearchMasterid(String query)
	{
		int returnValue=0;
		String  sql = "insert into search_master(searchstring, starttime) values ('" + query + "', now())";
		Statement statement = MySQLConnFactory.getsqlstatementforExec();
		ResultSet resultset=null;

		try {
			statement.executeUpdate(sql);
			System.out.println("first record");
			sql = "select max(searchid) as s from search_master";

			resultset = statement.executeQuery(sql);
			if (statement.execute(sql)) {
				resultset = statement.getResultSet();
			}
			while (resultset.next()) {
				returnValue = resultset.getInt("s");
			}
		} catch (SQLException ex) {
			//ex.printStackTrace();
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally
		{
			try {
				resultset.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return returnValue;

	}





	private static void fetchTweets(Query query, Twitter twitter, long tweetId,
			Integer printCnt,Integer searchid) {
		long lastTweetId = 0;
		try {
			if (tweetId == 0) {

			} 
			else {
				/*
				 * In case twitter disconnects you after a certain number of
				 * tweets and you still want to check the subsequent tweets if
				 * they exist, They we use these two lines. Use just replace the
				 * id with the last tweet id that was fetched and then the
				 * program tries to fetch all the tweets that available after
				 * that particular tweet.
				 */
				//long id = 356871024215719936L;
				//query.setMaxId(id);
				query.setMaxId(tweetId);
			}
			query.setCount(100);
			Integer printCounter = printCnt;
			QueryResult result;
			if (tweetId == 0 && searchid==0) {
				last_searchId=getSearchMasterid(queryCleaning(query) );

			}
			else if (searchid!=0)
				last_searchId=searchid;
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				lastTweetId=   insertTweets(tweets, 0, searchid);


				/*
				 * TO make sure we do not hit the rate limit
				 */

				 counter++;
				

				 /*
				  * This will get 100 * 20 tweets at a given time.
				  */

				 if (counter == 20) {
					 break;
				 }
			} while ((query = result.nextQuery()) != null);

		} catch (TwitterException te) {
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		} catch (NullPointerException e) {

			/*
			 * This is so that if we reach the end of the tweets we can exit
			 * with a null pointer exception.
			 */
			//e.printStackTrace();
			System.out.println("End of all tweets in stream");
			return ;
			//System.exit(0);
			/*
			 * try { Thread.sleep(5 * 60 * 1000); fetchTweets(query, twitter,
			 * lastTweetId - 10, 1); } catch (InterruptedException ie) {
			 * System.out.println(ie.getMessage()); }
			 */
		}  catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			Thread.sleep(2 * 60 * 1000);
			fetchTweets(query, twitter, lastTweetId, 1,searchid);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * Function used to remove unnecessary line breaks and punctuation marks in
	 * the tweet text.
	 */
	public static String textCleaning(Status tweet) {
		return (tweet.getText().replace("\n", " %n%")).replace(",", "%c%").replace(
				"'", "%a%").replace("\\", "%b%");
	}

	public static String queryCleaning(Query query) {
		return (query.getQuery().replace("\n", " %n%")).replace(",", "%c%").replace(
				"'", "%a%").replace("\\", "%b%");
	}

	public static String locationCleaning(Status tweet) {
		return (tweet.getUser().getLocation().replace("\n", " %n%")).replace(",", "%c%").replace(
				"'", "%a%").replace("\\", "%b%");
	}

	/*
	 * Function used to get the original id if the tweet is a retweet.
	 */
	public static long retweetId(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return tweet.getRetweetedStatus().getId();
		} else {
			return -1;
		}
	}

	public static String isReply(Status tweet) {
		if (tweet.getInReplyToStatusId() == -1) {
			return ("N");
		} else {
			return ("Y");
		}
	}

	public static String isFavourite(Status tweet) {
	/*	String json = DataObjectFactory.getRawJSON(tweet);
		String fav = json.substring(json.lastIndexOf("favorite_count") + 16, json.lastIndexOf("favorite_count") + 17);
		if (Integer.valueOf(fav) > 0 && retweetId(tweet) == -1) {
			return ("Y");
		} else {
			return ("N");
		}*/
		return "-1";
	}
	private static long r_getFavoriteCount(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return (tweet.getRetweetedStatus().getFavoriteCount());
		} else {
			return -1;
		}
	}
	public static String isStatus(Status tweet) {
		if (tweet.getInReplyToStatusId() == -1 || tweet.isFavorited()) {
			return ("Y");
		} else {
			return ("N");
		}
	}

	public static String r_textCleaning(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return (tweet.getRetweetedStatus().getText().replace("\n", " %n%")).replace(",", "%c%").replace(
					"'", "%a%").replace("\\", "%b%");
		} else {
			return null;
		}
	}

	public static String r_locationCleaning(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return (tweet.getRetweetedStatus().getUser().getLocation().replace("\n", " %n%")).replace(",", "%c%").replace(
					"'", "%a%").replace("\\", "%b%");
		} else {
			return null;
		}
	}

	public static String r_isReply(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			if (tweet.getRetweetedStatus().getInReplyToStatusId() == -1) {
				return ("N");
			} else {
				return ("Y");
			}
		} else {
			return "-1";
		}
	}

	public static String r_isFavourite(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			if (tweet.getRetweetedStatus().isFavorited()) {
				return ("Y");
			} else {
				return ("N");
			}
		} else {
			return "-1";
		}
	}

	public static String r_isStatus(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			if (tweet.getRetweetedStatus().getInReplyToStatusId() == -1 || tweet.isFavorited()) {
				return ("Y");
			} else {
				return ("N");
			}
		} else {
			return "-1";
		}
	}

	public static Date r_getCreatedAt(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return (tweet.getRetweetedStatus().getCreatedAt());
		} else {
			return null;
		}
	}

	public static String r_getUserName(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return (tweet.getRetweetedStatus().getUser().getScreenName());
		} else {
			return null;
		}
	}

	public static long r_getRetweetCount(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return (tweet.getRetweetedStatus().getRetweetCount());
		} else {
			return -1;
		}
	}

	public static long r_getFollowerCount(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return (tweet.getRetweetedStatus().getUser().getFollowersCount());
		} else {
			return -1;
		}
	}

	public static long r_InReply(Status tweet) {
		if (tweet.getRetweetedStatus() != null) {
			return (tweet.getRetweetedStatus().getInReplyToStatusId());
		} else {
			return -1;
		}
	}












}


