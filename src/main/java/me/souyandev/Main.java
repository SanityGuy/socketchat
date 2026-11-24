
package me.souyandev;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("          SocketChat");
        System.out.println("================================");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty() || username.length() > 20) {
            System.out.println("Invalid username. Use 1-20 characters.");
            return;
        }

        System.out.println("\nHello, " + username + "!");

        Client client = new Client();
        client.connect(username, scanner);
        scanner.close();

        System.out.println("SocketChat closed.");
    }
}
