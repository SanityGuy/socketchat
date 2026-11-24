package me.souyandev;

import java.util.Scanner;

public class Main {
    void main(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        while (true) {
            System.out.print("New message: ");
            String message = scanner.nextLine();

            System.out.println("["+username+"] : "+message);
        }
    }
}