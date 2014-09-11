package com.bizconit.homeinventory;

import com.bizconit.homeinventory.model.Inventory;
import com.bizconit.homeinventory.model.Smarthub;
import com.bizconit.homeinventory.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jfaker.JFaker;
import org.jfaker.internal.Name;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 11/9/14
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class HomeInventoryClient {


    public void addUser() {
        User user = getNewFakeUser();
        String url = "http://premapp.azure-mobile.net/tables/user";
        try {

//            JSONObject json = new JSONObject();
//            json.put("name", user.getName());
//            json.put("pin", user.getPin());




            HttpParams params = new BasicHttpParams();
            params.setParameter("name", user.getName());
            params.setParameter("pin", user.getPin());

            HttpPost post = new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setParams(params);
//            post.setEntity(new StringEntity(json.toString(), "UTF-8"));
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse httpresponse = client.execute(post);
            HttpEntity entity = httpresponse.getEntity();
            InputStream stream = entity.getContent();
            String result = convertStreamToString(stream);
            System.out.println("Result: " + result);


//        URL url;
//        HttpURLConnection connection = null;
//        try {
//            System.out.println("Trying to post data....");
//
////            List<NameValuePair> params = new ArrayList<NameValuePair>();
////            params.add(new BasicNameValuePair("id", Integer.toString(user.getId())));
////            params.add(new BasicNameValuePair("name", user.getName()));
////            params.add(new BasicNameValuePair("pin", Integer.toString(user.getPin())));
//            //Create connection
//            url = new URL("http://premapp.azure-mobile.net/tables/user" + getQuery(user));
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            OutputStream os = connection.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getQuery(user));
//            writer.flush();
//            writer.close();
//            os.close();
//            connection.connect();
//            System.out.println("User Inserted successfully");

//            connection.setRequestProperty("Content-Language", "en-US");
//
//            connection.setUseCaches(false);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);

//            //Send request
//            DataOutputStream wr = new DataOutputStream(
//                    connection.getOutputStream());
//            wr.writeBytes(urlParameters);
//            wr.flush();
//            wr.close();
//
//            //Get Response
//            InputStream is = connection.getInputStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//            String line;
//            StringBuffer response = new StringBuffer();
//            while ((line = rd.readLine()) != null) {
//                response.append(line);
//                response.append('\r');
//            }
//            rd.close();
//            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause());
        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
        }
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


//    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//        for (NameValuePair pair : params) {
//            if (first)
//                first = false;
//            else
//                result.append("&");
//            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
//        }
//        return result.toString();
//    }

    private String getQuery(User user) {
        StringBuilder result = new StringBuilder();
//        result.append("?id=" + user.getId());
        result.append("?name=" + user.getName());
        result.append("&pin=" + user.getPin());
        return result.toString();
    }

    private User getNewFakeUser() {
        System.out.println("Generating Fake User....");
        User user = new User();
        user.setName(getRandomName());
        user.setPin(getRandomPin());
        System.out.println("Created Fake User: " + user.toString());
        return user;
    }

    private int getRandomPin() {
        Random r = new Random();
        int Low = 1000;
        int High = 10000;
        int randomNumber = r.nextInt(High - Low) + Low;
        return randomNumber;
    }

    private String getRandomName() {
        JFaker jFaker = new JFaker();
        ;
        Name name = jFaker.Name;
        return name.firstName() + " " + name.lastName();
    }

    public void addSmarthub(int userId) {
        Smarthub smarthub = getNewFackSmartHub();
        smarthub.setUserId(userId);

    }

    private Smarthub getNewFackSmartHub() {
        return null;
    }

    public void postData(int smarthubId, String productName, float value) {
        Inventory inventory = new Inventory();
        inventory.setSmarthubId(smarthubId);
        inventory.setProductName(productName);
        inventory.setValue(value);
    }


}
