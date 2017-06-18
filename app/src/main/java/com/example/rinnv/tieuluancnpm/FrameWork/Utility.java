package com.example.rinnv.tieuluancnpm.FrameWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by rinnv on 4/15/2017.
 */

public class Utility {
    public static List<String> CheckWord(String original) {
        List<String> messages = new LinkedList<>();
        HttpURLConnection con = null;
        try {
            // Check if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();

            // Build RESTful query for Google API
            String q = URLEncoder.encode(original, "UTF-8");
            URL url = new URL(
                    "http://google.com/complete/search?output=toolbar&q="
                            + q);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setRequestMethod("GET");
           /* con.addRequestProperty("Referer",
                    "https://en.oxforddictionaries.com/definition");*/
            con.setDoInput(true);

            // Start the query
            con.connect();

            // Check if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();

            // Read results from the query
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(con.getInputStream(), null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (eventType == XmlPullParser.START_TAG
                        && name.equalsIgnoreCase("suggestion")) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if (parser.getAttributeName(i).equalsIgnoreCase("data")) {
                            String sentence = parser.getAttributeValue(i);
                            String[] wordSplit = sentence.split(" ");
                            String wordExpect = wordSplit[0];
                            if (messages.contains(wordExpect))
                                continue;
                            else
                                messages.add(wordExpect);
                        }
                    }
                }
                eventType = parser.next();
            }

            // Check if task has been interrupted
            if (Thread.interrupted())
                throw new InterruptedException();

        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XmlPullParserException", e);

        } catch (Exception e) {
            Log.d(TAG, "InterruptedException", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        // All done
        return messages;
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net != null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}

