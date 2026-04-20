package com.techken.utils;

import java.util.Scanner;

public class InputHandler {

    private final Scanner sc;

    public InputHandler() {
        this.sc = new Scanner(System.in);
    }

    public int getIntInput(int min, int max) {
        while (true) {
            try {
                String line = sc.nextLine();
                int input = Integer.parseInt(line);
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.print("Invalid range! Enter (" + min + "-" + max + "): ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a number: ");
            }
        }
    }
}