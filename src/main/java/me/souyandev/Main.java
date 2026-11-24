package me.souyandev;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (username.isEmpty()) {
            System.out.println("Username cannot be empty!");
            return;
        }

        System.out.println("\nWelcome to SocketChat, "+username+"!");
        System.out.println("Type /quit to exit.");

        while (true) {
            System.out.print("New message: ");
            String message = scanner.nextLine();

            if (message.equalsIgnoreCase("/quit")) {
                System.out.println("Goodbye, "+username+"!");
                break;
            }

            if (message.isBlank()){
                continue;
            }

            System.out.println("["+username+"] : "+message);
        }
    }
}