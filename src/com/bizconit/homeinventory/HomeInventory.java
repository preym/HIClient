package com.bizconit.homeinventory;

import com.bizconit.homeinventory.model.Smarthub;
import com.bizconit.homeinventory.model.User;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
        int option;
        do {
            promtUser();
            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    addUser();
                    break;
                case 2:
                    registerSmartHub();
                    break;
                case 3:
                    postData();
                    break;
                case 4:
                    System.out.println("Have a nice day, Bye");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }
        while (option != 4);
    }

    private static void postData() {
        System.out.println("You are about to post Data");
        List<Smarthub> smarthubs = client.getExistingSmarthubs();
        Smarthub smarthub = smarthubs.get(getRandomNumber(smarthubs.size()));
        client.postData(smarthub.getId());
    }

    private static void registerSmartHub() {
        System.out.println("you are about to Register SmartHub");
        System.out.println("Getting Existing Users...");
        List<User> users = client.getExistingUsers();
        int randomNumber = getRandomNumber(users.size());
        User user = users.get(randomNumber);
        System.out.println("Random User: " + user);
        client.addSmarthub(user.getId());
    }

    public static int getRandomNumber(int range) {
        Random random = new Random();
        return random.nextInt(range - 1);
    }

    private static void addUser() {
        System.out.println("Please wait.....");
        client.addUser();
    }

    private static void promtUser() {
        System.out.println("\t\t 1 -----> Insert New User");
        System.out.println("\t\t 2 -----> Register New Smarthub to Existing User");
        System.out.println("\t\t 3 -----> Post Data");
        System.out.println("\t\t 4 -----> Quit");
        System.out.println("\tPlease Choose Your option:");
    }


}
