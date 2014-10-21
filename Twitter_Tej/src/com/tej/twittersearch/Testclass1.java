package com.tej.twittersearch;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Testclass1 {
	static Twitter twitter = new TwitterFactory().getInstance();
	static HashSet<Long> id=new HashSet<Long>();
	private static BufferedReader reader;
	
	static {
		twitter.setOAuthConsumer("SChtgehUQtgrecEju546Cj8Ge", "AHfjNUa7ipMpFSQfiolQVmRKTTe5FCIcA2jyha4eJ8WLFlKJuH");
		String accessToken = "137679431-TXnSrWe3fpEfZaXc80BU1WOX3JofbPWoiwXi9HUk";
		String accessTokenSecret = "TTHv1kJP0hWP8ECTEwu2kZeHJ26KdGzxz4xrhcQa60eTi";
		AccessToken oathAccessToken = new AccessToken(accessToken, accessTokenSecret);
		twitter.setOAuthAccessToken(oathAccessToken);
		
	}
	 public static void main(String[] args) {
		 String line=null;
		  try {
			  
			  reader = new BufferedReader(new FileReader("C:\\Users\\Tejeshwar\\Desktop\\BrandID.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			while((line=reader.readLine())!=null)
				{
				 try {
		          	 ResponseList<twitter4j.User> users = twitter.lookupUsers(line.split(","));
		            for (twitter4j.User user : users) {
		                if (user.getStatus() != null) {
		                    System.out.println("@" + user.getScreenName() );
		                } else {
		                    // the user is protected
		                    System.out.println("@" + user.getScreenName());
		                }
		            }
		           // System.out.println("Successfully looked up users [" + args[0] + "].");
		           // System.exit(0);
		        } catch (TwitterException te) {
		            te.printStackTrace();
		            System.out.println("Failed to lookup users: " + te.getMessage());
		          //  System.exit(-1);
		        }
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
	       
	    }

}
