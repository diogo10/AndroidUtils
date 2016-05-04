package diogo.democon.util;

import com.rollbar.android.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.net.ssl.*;

/**
 * My own  generic HttpURLConnection
 * <p/>
 * Created by diogo on 18/02/16.
 */
public class ServerUtil {

    private static final String BASE_URL = "http://shrouded-woodland-9458.herokuapp.com/";

    /**
     * GET
     *
     * @param method - server method
     * @return a result string
     * @throws Exception
     */
    public static String get(String method) throws Exception {

        InputStream is = null;
        String contentAsString = "";

        try {
            URL url = new URL(BASE_URL + method);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100);
            conn.setConnectTimeout(100);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            contentAsString = readIt(is);
            return contentAsString;

        } catch (IOException io) {
            Rollbar.reportException(io, "critical", "GET - an error occurs while opening the connection");
            throw io;
        } catch (Exception e) {
            System.out.println("get error = " + e.getMessage());
            Rollbar.reportException(e, "critical", "GET - general exception");
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            return contentAsString;
        }
    }


    /**
     * DELETE
     *
     * @param method - server method
     * @return - a result string
     * @throws Exception
     */
    public static String delete(String method) throws Exception {

        InputStream is = null;
        String contentAsString = null;

        try {
            URL url = new URL(BASE_URL + method);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000);
            conn.setConnectTimeout(1500);
            conn.setRequestMethod("DELETE");
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            contentAsString = readIt(is);

        } catch (Exception e) {
            Rollbar.reportException(e, "critical", "DELETE - an error occurs while opening the connection");
            System.out.println("delete error = " + e.getMessage());
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            return contentAsString;
        }
    }


    /**
     * POST
     *
     * @param method - server method
     * @param params - params
     * @return - a result string
     * @throws Exception
     */
    public static String post(String method, HashMap<String, String> params) throws Exception {

        URL url;
        String response = "";
        try {
            url = new URL(BASE_URL + method);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setReadTimeout(1500);
            conn.setConnectTimeout(1500);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = ""; //wrong post - requestcode - 422

            }

        } catch (IOException io) {
            Rollbar.reportException(io, "critical", "POST - an error occurs while opening the connection");
            throw io;
        } catch (Exception e) {
            System.out.println("get error = " + e.getMessage());
            Rollbar.reportException(e, "critical", "POST - general exception");
            throw e;
        }

        return response;
    }


    /**
     * Handle Content-Type - application/x-www-form-urlencoded way
     * <p/>
     * Example
     * <p/>
     * event[name]  =     Test
     * event[start] =     2010-10-12 10:45:00
     * event[end]   =     2010-10-11 10:45:00
     *
     * @param params - HashMap
     * @return HashMap in string format
     * @throws UnsupportedEncodingException
     */
    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    // Reads an InputStream and converts it to a String.
    private static String readIt(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream));) {

            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return sb.toString();

    }
}
