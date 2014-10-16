package com.bizconit.homeinventory;

import com.bizconit.homeinventory.model.Sensor;
import com.bizconit.homeinventory.model.Smarthub;
import com.bizconit.homeinventory.model.User;

import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 11/9/14
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class HomeInventory {

  static HomeInventoryClient client = new HomeInventoryClient();

  public static void main(String... args) {
    System.out.println("####### Welcome to HomeInventoryClient #######");
    postData();


//    for (int i = 0; i < 100; i++) {
//      postData();
//    }


//    int option;
//    do {
//      promtUser();
//      Scanner scanner = new Scanner(System.in);
//      option = scanner.nextInt();
//      switch (option) {
//        case 1:
//          addUser();
//          break;
//        case 2:
//          registerSmartHub();
//          break;
//        case 3:
//          postData();
//          break;
//        case 4:
//          associateUsers();
//          break;
//        case 5:
//          addSensor();
//          break;
//        case 6:
//          System.out.println(" Have a nice day, Bye");
//          System.exit(0);
//          break;
//        default:
//          System.out.println(" Invalid Choice");
//      }
//    }
//    while (option != 4);
  }

  private static void addSensor() {
    System.out.println("You are about to register new Sensor");
    Sensor sensor = new Sensor();
    System.out.println("Registering Product with sensor...");
    String[] randomProduct = client.getRandomProduct();
    sensor.setProduct_type(randomProduct[1]);
    sensor.setProduct_name(randomProduct[0]);
    System.out.println("ProductName: " + sensor.getProduct_name());
    System.out.println("Selecting existing smarthub...");
    List<Smarthub> smarthubs = client.getExistingSmarthubs();
    int randomNumber = getRandomNumber(smarthubs.size() + 1);
//    Smarthub smarthub = client.getExistingSmarthub("2D71CACD-1473-4D54-910C-EA10799AB2F2");
    sensor.setSmarthub_id(smarthubs.get(randomNumber).getId());

    boolean isExist = false;


    List<Sensor> sensors = client.getExistingSensors(smarthubs.get(randomNumber).getId());
    for (Sensor exiSensor : sensors) {
      if (exiSensor.getProduct_name().equalsIgnoreCase(sensor.getProduct_name())) {
        System.out.println("Sensor already Exist");
        isExist = true;
      }
    }

    if (isExist == false) {
      System.out.println("Sensor Not Exist");
      client.addSensor(sensor);
    }
  }

  private static void associateUsers() {
    System.out.println(" Your are about to Associate Users");
    System.out.println(" Please Wait...");
    List<User> users = client.getExistingUsers();
    User parent = users.get(getRandomNumber(users.size()));
    System.out.println(" Parent User: " + parent);
    User child = users.get(getRandomNumber(users.size()));
    while (parent.getId().equals(child.getId())) {
      child = users.get(getRandomNumber(users.size() + 1));
    }
    System.out.println(" Child User: " + child);
    String familyMembers = parent.getFamily_members();
    System.out.println("familyMembers:" + familyMembers);
    if (familyMembers != null) {
      boolean isExist = false;
      String[] familyList = familyMembers.split(",");
      for (int index = 0; index < familyList.length; index++) {
        System.out.println("familyMembersList:" + familyList[index]);

        if (familyList[index].equals(child.getId())) {
          isExist = true;
          break;
        }
      }
      if (!isExist) {
        familyMembers = familyMembers + "," + child.getId();
        System.out.println("familyMembers:" + familyMembers);
      }
    } else {
      familyMembers = child.getId();
    }
    parent.setFamily_members(familyMembers);
    client.updateUser(parent);
  }

  private static void postData() {
    System.out.println(" You are about to post Data");
    List<Sensor> sensors = client.getExistingSensors();
//    Sensor sensor = sensors.get(getRandomNumber(sensors.size()+1));
//    Sensor sensor = client.getExistingSensor("");
    for (int i = 0; i < sensors.size(); i++)
      client.postData(sensors.get(i));
  }

  private static void registerSmartHub() {
    System.out.println(" You are about to Register SmartHub");
//    System.out.println(" Getting Existing Users...");
//    List<User> users = client.getExistingUsers();
//    int randomNumber = getRandomNumber(users.size());
//    User user = users.get(randomNumber);
//    System.out.println(" Random User: " + user);
    client.addSmarthub("F5938FA6-6E7A-4CC6-8052-4D516136CD97");
  }

  public static int getRandomNumber(int range) {
    Random random = new Random();
    return random.nextInt(Math.max((range - 1), 0));
  }

  private static void addUser() {
    System.out.println(" Please wait.....");
    client.addUser();
  }

  private static void promtUser() {
    System.out.println("\t 1 -----> Insert New User");
    System.out.println("\t 2 -----> Register New Smarthub to Existing User");
    System.out.println("\t 3 -----> Post Data");
    System.out.println("\t 4 -----> Associate User");
    System.out.println("\t 5 -----> Add Sensor");
    System.out.println("\t 6 -----> Quit");
    System.out.println(" Please Choose Your option:");
  }


}
