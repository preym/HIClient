package com.bizconit.homeinventory;

import com.bizconit.homeinventory.model.Inventory;
import com.bizconit.homeinventory.model.Smarthub;
import com.bizconit.homeinventory.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jfaker.JFaker;
import org.jfaker.internal.Name;
import org.json.JSONObject;

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
