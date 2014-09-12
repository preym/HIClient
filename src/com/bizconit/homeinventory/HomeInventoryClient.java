package com.bizconit.homeinventory;

import com.bizconit.homeinventory.model.Inventory;
import com.bizconit.homeinventory.model.Smarthub;
import com.bizconit.homeinventory.model.User;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jfaker.JFaker;
import org.jfaker.internal.Company;
import org.jfaker.internal.Name;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
            JSONObject json = new JSONObject();
            json.put("name", user.getName());
            json.put("pin", user.getPin());

            HttpPost post = new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json.toString(), "UTF-8"));

            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse httpresponse = client.execute(post);
            HttpEntity entity = httpresponse.getEntity();
            InputStream stream = entity.getContent();
            String result = convertStreamToString(stream);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause());
        }
    }


    public List<User> getExistingUsers() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://premapp.azure-mobile.net/tables/user");
            connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            User[] users = new Gson().fromJson(stringBuffer.toString(), User[].class);
            return Arrays.asList(users);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return null;
    }

    public String convertStreamToString(InputStream is) {
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
        Name name = jFaker.Name;
        return name.firstName() + " " + name.lastName();
    }

    public void addSmarthub(String userId) {
        Smarthub smarthub = getNewFakeSmartHub();
        smarthub.setUserId(userId);
        String url = "http://premapp.azure-mobile.net/tables/smarthub";
        try {
            JSONObject json = new JSONObject();
            json.put("name", smarthub.getName());
            json.put("user_id", smarthub.getUserId());

            HttpPost post = new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json.toString(), "UTF-8"));

            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse httpresponse = client.execute(post);
            HttpEntity entity = httpresponse.getEntity();
            InputStream stream = entity.getContent();
            String result = convertStreamToString(stream);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getCause());
        }
    }

    private Smarthub getNewFakeSmartHub() {
        Smarthub smarthub = new Smarthub();
        JFaker jFaker = new JFaker();
        Company company = jFaker.Company;
        smarthub.setName(company.companyName() + " SH");
        return smarthub;
    }

    public void postData(int smarthubId, String productName, float value) {
        Inventory inventory = new Inventory();
        inventory.setSmarthubId(smarthubId);
        inventory.setProductName(productName);
        inventory.setValue(value);
    }


}
