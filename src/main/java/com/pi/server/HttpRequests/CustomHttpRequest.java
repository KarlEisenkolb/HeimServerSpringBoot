package com.pi.server.HttpRequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class CustomHttpRequest {

    private final String LOG_TAG = CustomHttpRequest.class.getSimpleName();
    private String stringUrl;

    public static String startRequestInstance(String stringUrl){
        CustomHttpRequest customHttpRequest = new CustomHttpRequest(stringUrl);
        return customHttpRequest.startRequestAndGetResponseString();
    }

    private CustomHttpRequest(String stringUrl){
        this.stringUrl = stringUrl;
    }

    private String startRequestAndGetResponseString(){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            System.out.println(LOG_TAG + " Error with creating URL " + e.getMessage());
        }

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            System.out.println(LOG_TAG + " Error closing input stream " + e.getMessage());
        }

        return jsonResponse;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                System.out.println(LOG_TAG + " Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            System.out.println(LOG_TAG + " Problem retrieving JSON results. " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
