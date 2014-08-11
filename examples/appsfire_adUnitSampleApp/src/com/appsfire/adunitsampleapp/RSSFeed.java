// Simple RSS feed downloader and parser using SAX and XMLReader, used to display content around Sashimi ad views
// Inspired by: http://stackoverflow.com/questions/4827344/how-to-parse-xml-using-the-sax-parser

package com.appsfire.adunitsampleapp;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;

public class RSSFeed {
	// Tag for logging messages
	private static final String CLASS_TAG = "RSSFeed";
	
	// One RSS item
    public class Item {
        private String title;
        private String description;
        private String link;

        public Item() {
            setTitle(null);
            setDescription(null);
            setLink(null);
        }
        
        public Item(String title, String description, String link) {
        	setTitle (title);
        	setDescription (description);
        	setLink (link);
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
        }
        
        @Override
        public String toString() {
        	StringBuilder result = new StringBuilder();
            String NEW_LINE = System.getProperty("line.separator");

            result.append(this.getClass().getName() + " Object {" + NEW_LINE);
            result.append(" Title: " + title + NEW_LINE);
            result.append(" Description: " + description + NEW_LINE);
            result.append(" Link: " + link + NEW_LINE );
            result.append("}");

            return result.toString();
        }
    }
     
    // List of RSS items currently being parsed
    private ArrayList<Item> items = new ArrayList<Item>();
    
    // List of RSS items available for the app to display around Sashimis; starts with default content, and updated once
    // an RSS feed has successfully been downloaded and parsed
    private ArrayList<Item> publicItems = new ArrayList<Item>();
    
    // Current RSS item being parsed
    private Item item;
    
    // true if currently parsing an item, false if not
    private boolean inItem = false;
    
    // Current content being built for an item
    private StringBuilder content;
    
    /**
     * Constructor
     */
    public RSSFeed() {
    	synchronized (publicItems) {
    		// Insert default items
    		publicItems = new ArrayList<Item>();
	    	publicItems.add(new Item ("How do cryptocurrencies work?", "Not so long ago the thought of reaching into your pocket for a digital currency might have seemed too far-fetched.", "http://rss.cnn.com/~r/rss/edition_business/~3/f3Vny2RFogA/index.html"));
	    	publicItems.add(new Item ("Is your hotel chic? Check the label", "These days, no fashion house portfolio is complete without a hotel -- or at the very least, a luxuriously designed suite.", "http://rss.cnn.com/~r/rss/edition_business/~3/MqQfpA4work/index.html"));
	    	publicItems.add(new Item ("Can airplane seats keep the peace?", "There is no shortage of adjectives one can apply to airline seats: uncomfortable, bulky, cramped, outdated and -- from an airline's point of view -- overpriced. It's no wonder then that many carriers are looking to make a change.", "http://rss.cnn.com/~r/rss/edition_business/~3/PbW-NsM9gss/index.html"));
	    	publicItems.add(new Item ("The future of supersonic flight", "Concorde is a thing of the past, but a number of companies are racing to release the first supersonic business jet.", "http://rss.cnn.com/~r/rss/edition_business/~3/_GNOjRF_BzY/index.html"));
	    	publicItems.add(new Item ("Bobbi Brown's billion dollar idea", "When Bobbi Brown introduced her eponymous lipstick line to Bergdorf Goodman back in 1991, she never expected all 10 pinky-brown shades to fly off the shelves in just one day.", "http://rss.cnn.com/~r/rss/edition_business/~3/ADCywkcvf1E/index.html"));
	    	publicItems.add(new Item ("The dress that launched an empire", "When Diane von Furstenberg first unveiled her wrap dress back in 1974, she never expected the DVF brand and dress to reach such iconic status in the world of fashion.", "http://rss.cnn.com/~r/rss/edition_business/~3/hyeC-xMR3rM/index.html"));
	    	publicItems.add(new Item ("Name your price for Paris hotel stay", "Several hotels in the French capital are conducting a potentially risky experiment, asking guests to decide what they're willing to pay.", "http://rss.cnn.com/~r/rss/edition_business/~3/JulEx2QcEPI/index.html"));
	    	publicItems.add(new Item ("World's coolest bookstores ", "From Maastricht to Melbourne, these itineraries make bookish travelers look stylish.", "http://rss.cnn.com/~r/rss/edition_business/~3/d-jv_r_dVmI/index.html"));
	    	publicItems.add(new Item ("World-class motor museums", "With factory tours and collections of stunning vintage prototypes, southern Germany is petrolhead paradise.", "http://rss.cnn.com/~r/rss/edition_business/~3/5WCSi9K5NHw/index.html"));
	    	publicItems.add(new Item ("New air safety task force duties", "The director general of IATA says a new task force will help countries share air safety information.", "http://rss.cnn.com/~r/rss/edition_business/~3/w38Sd3qdLAk/qmb-iata-task-force-better-info.cnn.html"));
	    	publicItems.add(new Item ("Don't let the auto industry kill you", "Shanin Specter says we need to strengthen laws that punish auto companies for selling defective cars that kill people.", "http://rss.cnn.com/~r/rss/edition_business/~3/azL30Mn_ves/index.html"));
	    	publicItems.add(new Item ("Meet Airbus' newest jet", "Takeoff on one of Airbus' new A350WXB test planes is a strangely quiet experience.", "http://rss.cnn.com/~r/rss/edition_business/~3/cQmhHh2gXWQ/index.html"));
    	}
    }
    
    /**
     * Download and parse RSS feed
     * 
     * @param url URL of rss feed to download and parse
     * 
     * @note downloads asynchronously, and updates the publicly viewable RSS items when done
     */
    public void loadFeed (String url) {
    	final String finalUrl = url;
    	
    	content = new StringBuilder();
    	Thread rssThread = new Thread() {
    		@Override
    		public void run() {
    			// Download and parse in new thread
		        try {
		            URL rssUrl = new URL(finalUrl);
		            SAXParserFactory factory = SAXParserFactory.newInstance();
		            SAXParser saxParser = factory.newSAXParser();
		            XMLReader xmlReader = saxParser.getXMLReader();
		            RSSHandler rssHandler = new RSSHandler();
		            xmlReader.setContentHandler(rssHandler);
		            InputSource inputSource = new InputSource(rssUrl.openStream());
		            xmlReader.parse(inputSource);		
		        } catch (Exception e) {
		        	Log.d (CLASS_TAG, "exception reading RSS feed: " + e.toString ());
		        }
    		}
    	};
    	rssThread.start ();
    }
    
    /**
     * Get default or downloaded items
     * 
     * @return items
     */
    public ArrayList<Item> getItems() {    	
    	synchronized (publicItems) {
    		return publicItems;
    	}
    }
    
    // Handler for xml content
    
    private class RSSHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        	content = new StringBuilder();
            if(localName.equalsIgnoreCase("item")) {
                inItem = true;
                item = new Item();
            }
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        	if(localName.equalsIgnoreCase("title")) {
                if(inItem) {
                    item.setTitle(content.toString());
                }
            } else if(localName.equalsIgnoreCase("link")) {
                if(inItem) {
                    item.setLink(content.toString());
                }
            } else if(localName.equalsIgnoreCase("description")) {
                if(inItem) {
                    item.setDescription(strip(content.toString()));
                }
            } else if(localName.equalsIgnoreCase("item")) {
                inItem = false;
                items.add(item);
            } else if(localName.equalsIgnoreCase("channel")) {
                // Channel tag ended, all content retrieved; transfer items to publicly viewable list
            	synchronized (publicItems) {
            		if (items.size() >= 8) {
            			// Enough items to have content around a Sashimi
            			publicItems = items;
            		}
            	}
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
        	content.append(ch, start, length);
        }        
        
        // Strip html tags, used to remove <img> and other tags from the description
        private String strip (String str) {
        	Pattern p = Pattern.compile("<(.|\n)*?>",
                    Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        	return p.matcher(str).replaceAll("");
        }
    }
}
