package com.bizconit.homeinventory;

import com.bizconit.homeinventory.model.Inventory;
import com.bizconit.homeinventory.model.Sensor;
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
import java.sql.Timestamp;
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
  String[] smartHubLocations = {"office", "home"};
  private String BASE_URL = "https://aesop.azure-mobile.net/tables/";
  private int temperatures[] = {25, 30};
  private String hexValues[] = {"0x05e0", "0x019d", ""};

  public HomeInventoryClient() {
    String[] unitProducts = {"Eggs", "Optimum Nutrition", "Labrada Nutrition", "Coca-Cola Can", "Benadryl", "Vicodin"};
    String[] weightProducts = {"Wheat", "Rice", "Sugar", "Oil", "Tea Powder", "Coffee Powder", "Milk", "Washing Powder", "Utencils Cleaning",
        "Moong Dal", "Ground Nuts", "Wipes", "Kaju"};

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
//      json.put("location", smartHubLocations[HomeInventory.getRandomNumber(smartHubLocations.length)]);
      json.put("location", "Office");

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

  public void postData(Sensor sensor) {
    Inventory lastInventory = getLastInsertedInventory(sensor.getId());
    int lastInsertedValue = 0;
    Timestamp lastTimestamp = null;
    if (lastInventory != null) {
      lastInsertedValue = Integer.parseInt(lastInventory.getWeight(), 16);
      lastTimestamp = lastInventory.getInserted_at();
    }

    System.out.println("Last InsertedAt: " + lastTimestamp);
    System.out.println("lastInsertedValue:" + lastInsertedValue);
    int randomNumber = new Random().nextInt(3000 - 100) + 100;
    System.out.println("randomValue:" + randomNumber);
    if (lastInsertedValue != 0 && randomNumber >= lastInsertedValue) {
      randomNumber = new Random().nextInt(lastInsertedValue - 100) + 100;
      System.out.println("regeneratedValue:" + randomNumber);
    }
    Timestamp timestamp = getRandomTimestamp("2014-08-01 00:00:00");
    System.out.println("Random Timestamp: " + timestamp);
    if (lastTimestamp != null && lastTimestamp.getTime() >= timestamp.getTime()) {
      timestamp = getRandomTimestamp(lastTimestamp.toString());
      System.out.println("Re generated Random Timestamp: " + timestamp);
    }

    Inventory inventory = new Inventory();
    inventory.setSmarthub_id(sensor.getSmarthub_id());
    inventory.setSensor_id(sensor.getId());
    inventory.setInserted_at(timestamp);
    inventory.setWeight(Integer.toHexString(randomNumber));

    inventory.setTemperature(temperatures[HomeInventory.getRandomNumber(3)]);
    System.out.println("Temperature: " + inventory.getTemperature());
    String url = BASE_URL + "inventory";
    try {
      JSONObject json = new JSONObject();
      json.put("sensor_id", inventory.getSensor_id());
      json.put("smarthub_id", inventory.getSmarthub_id());
      json.put("inserted_at", inventory.getInserted_at());
      json.put("weight", inventory.getWeight());
      json.put("temperature", inventory.getTemperature());

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

  private Timestamp getRandomTimestamp(String startTimestamp) {
    long offset = Timestamp.valueOf(startTimestamp).getTime();
    long end = new Timestamp(new Date().getTime()).getTime();
    long diff = end - offset + 1;
    Timestamp rand = new Timestamp(offset + (long) (Math.random() * diff));
    return rand;
  }

  private Inventory getLastInsertedInventory(String sensorId) {
    HttpURLConnection connection = null;
    try {
      URL url = new URL(BASE_URL +
          "inventory?$filter=(sensor_id+eq+'" + sensorId + "')&__systemProperties=updatedAt&$orderby=__updatedAt%20desc");
      connection = (HttpURLConnection) url.openConnection();
      InputStream inputStream = connection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      Inventory[] inventories = new Gson().fromJson(stringBuffer.toString(), Inventory[].class);
      if (inventories != null && inventories.length != 0) {
        return inventories[0];
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null)
        connection.disconnect();
    }
    return null;
  }

  private float getLastInsertedValue(String sensorId) {

    HttpURLConnection connection = null;
    try {
      URL url = new URL(BASE_URL +
          "inventory?$filter=(sensor_id+eq+'" + sensorId + "')&__systemProperties=updatedAt&$orderby=__updatedAt%20desc");
      connection = (HttpURLConnection) url.openConnection();
      InputStream inputStream = connection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      Inventory[] inventories = new Gson().fromJson(stringBuffer.toString(), Inventory[].class);
      if (inventories != null && inventories.length != 0) {
        return inventories[0].getValue();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null)
        connection.disconnect();
    }
    return 0;
  }

  public String[] getRandomProduct() {
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
      json.put("family_members", user.getFamily_members());
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

  public void addSensor(Sensor sensor) {
    System.out.println("Inserting Sensor...");
    String url = BASE_URL + "sensor";
    try {
      JSONObject json = new JSONObject();
      json.put("product_name", sensor.getProduct_name());
      json.put("product_type", sensor.getProduct_type());
      json.put("smarthub_id", sensor.getSmarthub_id());

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

  public List<Sensor> getExistingSensors() {
    System.out.println(" Getting Existing Sensors...");
    HttpURLConnection connection = null;
    try {
      URL url = new URL(BASE_URL + "sensor");
      connection = (HttpURLConnection) url.openConnection();
      InputStream inputStream = connection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      Sensor[] sensors = new Gson().fromJson(stringBuffer.toString(), Sensor[].class);
      System.out.println("" + sensors.length);
      return Arrays.asList(sensors);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null)
        connection.disconnect();
    }
    return null;
  }

  public List<Sensor> getExistingSensors(String smarthubId) {
    System.out.println(" Getting Existing Sensors...");
    HttpURLConnection connection = null;
    try {
      URL url = new URL(BASE_URL + "sensor?$filter=(smarthub_id+eq+'" + smarthubId + "')");
      connection = (HttpURLConnection) url.openConnection();
      InputStream inputStream = connection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      Sensor[] sensors = new Gson().fromJson(stringBuffer.toString(), Sensor[].class);
      System.out.println("" + sensors.length);
      return Arrays.asList(sensors);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null)
        connection.disconnect();
    }
    return null;
  }

  public Sensor getExistingSensor(String id) {
    System.out.println(" Getting Existing Sensors...");
    HttpURLConnection connection = null;
    try {
      URL url = new URL(BASE_URL + "sensor?$filter=(id+eq+" + id + ")");
      connection = (HttpURLConnection) url.openConnection();
      InputStream inputStream = connection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      Sensor sensor = new Gson().fromJson(stringBuffer.toString(), Sensor.class);
      return sensor;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null)
        connection.disconnect();
    }
    return null;
  }

  public Smarthub getExistingSmarthub(String id) {

    System.out.println(" Getting Existing Smarthubs...");
    HttpURLConnection connection = null;
    try {
      URL url = new URL(BASE_URL + "smarthub?$filter=(id+eq+'" + id + "')");
      connection = (HttpURLConnection) url.openConnection();
      InputStream inputStream = connection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      Smarthub[] smarthub = new Gson().fromJson(stringBuffer.toString(), Smarthub[].class);
      return smarthub[0];
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (connection != null)
        connection.disconnect();
    }
    return null;


  }
}

