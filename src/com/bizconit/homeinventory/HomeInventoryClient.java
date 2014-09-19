package com.bizconit.homeinventory;

import com.bizconit.homeinventory.model.Inventory;
import com.bizconit.homeinventory.model.Smarthub;
import com.bizconit.homeinventory.model.User;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;
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
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 11/9/14
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class HomeInventoryClient {

  String[] products = {"Wheat", "Rice", "Sugar", "Maida", "Dalda", "Oil", "Tea Powder",
      "Coffee Powder", "Milk", "Jeera", "Papad", "Washing Powder", "Utencils Cleaning",
      "Moong Dal", "Ground Nuts", "Matki", "Murmure", "Kismis", "Eggs", "Bread", "Diaper",
      "Wipes", "Kaju"};
  Map<String, String[]> productsMap = new HashMap<>();
  String[] smartHubLocations = {"office", "home", "car"};
  private String BASE_URL = "https://aesop.azure-mobile.net/tables/";

  public HomeInventoryClient() {
    String[] unitProducts = {"Eggs", "Optimum Nutrition", "Melissa and Doug", "Labrada Nutrition", ""};
    String[] weightProducts = {"Wheat", "Rice", "Sugar", "Maida", "Dalda", "Oil", "Tea Powder", "Coffee Powder", "Milk", "Jeera", "Papad", "Washing Powder", "Utencils Cleaning",
        "Moong Dal", "Ground Nuts", "Matki", "Murmure", "Kismis", "Kaju"};

    productsMap.put("unit", unitProducts);
    productsMap.put("weight", weightProducts);
    System.out.println(productsMap);
  }

  public void addUser() {
    User user = getNewFakeUser();
    String url = BASE_URL + "user";
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
      URL url = new URL(BASE_URL + "user");
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
    System.out.println(" Generating Fake User....");
    User user = new User();
    user.setName(getRandomName());
    user.setPin(getRandomPin());
    System.out.println(" Created Fake User: " + user.toString());
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
    String url = BASE_URL + "smarthub";
    try {
      JSONObject json = new JSONObject();
      json.put("name", smarthub.getName());
      json.put("user_id", smarthub.getUserId());
      json.put("location", smartHubLocations[HomeInventory.getRandomNumber(smartHubLocations.length)]);

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

//  public void addStatic() {
//
//    String url = BASE_URL + "product_type";
//
//    try {
//      JSONObject json = new JSONObject();
//      json.put("product_type", "unit");
//      json.put("threshold_value", "10");
//      HttpPost post = new HttpPost(url);
//      post.setHeader("Accept", "application/json");
//      post.setHeader("Content-Type", "application/json");
//      post.setEntity(new StringEntity(json.toString(), "UTF-8"));
//
//      DefaultHttpClient client = new DefaultHttpClient();
//      HttpResponse httpresponse = client.execute(post);
//      HttpEntity entity = httpresponse.getEntity();
//      InputStream stream = entity.getContent();
//      String result = convertStreamToString(stream);
//      System.out.println("Result: " + result);
//    } catch (Exception e) {
//
//    }
//
//  }

  private Smarthub getNewFakeSmartHub() {
    Smarthub smarthub = new Smarthub();
    JFaker jFaker = new JFaker();
    Company company = jFaker.Company;
    smarthub.setName(company.companyName() + " SH");
    return smarthub;
  }

  public void postData(String smarthubId) {
    Inventory inventory = getRandomData();
    inventory.setSmarthubId(smarthubId);

    String url = BASE_URL + "inventory";
    try {
      JSONObject json = new JSONObject();
      json.put("product_name", inventory.getProductName());
      json.put("value", inventory.getValue());
      json.put("smarthub_id", inventory.getSmarthubId());
      json.put("product_type", inventory.getProductType());

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

  private Inventory getRandomData() {
    Inventory inventory = new Inventory();
    String[] product = getRandomProduct();
    inventory.setProductName(product[0]);
    inventory.setValue(HomeInventory.getRandomNumber(100));
    inventory.setProductType(product[1]);
    return inventory;
  }

  private String[] getRandomProduct() {
    int randomNumber = new Random().nextInt(productsMap.size());
    ArrayList<String> keys = new ArrayList<>(productsMap.keySet());
    String productType = keys.get(randomNumber);
    String products[] = productsMap.get(productType);
    String result[] = new String[2];
    result[0] = products[HomeInventory.getRandomNumber(products.length)];
    result[1] = productType;
    return result;
  }

  public List<Smarthub> getExistingSmarthubs() {
    System.out.println(" Getting Existing Smarthubs...");
    HttpURLConnection connection = null;
    try {
      URL url = new URL(BASE_URL + "smarthub");
      connection = (HttpURLConnection) url.openConnection();
      InputStream inputStream = connection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      Smarthub[] users = new Gson().fromJson(stringBuffer.toString(), Smarthub[].class);
      return Arrays.asList(users);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null)
        connection.disconnect();
    }
    return null;
  }

  public void updateUser(User user) {

    String url = BASE_URL + "user/" + user.getId();
    try {
      JSONObject json = new JSONObject();
      json.put("family_members", user.getFamilyMembers());
      HttpPatch post = new HttpPatch(url);
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
}
