package com.techken.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class SaveManager {

    private static final String PROFILE_FILE = "profile.properties";
    private static final String SETTINGS_FILE = "settings.properties";
    private static final String MATCH_HISTORY_FILE = "match_history.properties";

    public static boolean saveProfileData(String playerName, int wins, int losses) {
        Properties properties = new Properties();
        properties.setProperty("name", playerName);
        properties.setProperty("wins", String.valueOf(wins));
        properties.setProperty("losses", String.valueOf(losses));

        try (FileOutputStream fos = new FileOutputStream(PROFILE_FILE)) {
            properties.store(fos, "Tech-Ken Profile Data");
            return true;
        } catch (IOException e) {
            System.err.println("Error saving profile data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Properties loadProfileData() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROFILE_FILE)) {
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            System.err.println("Error loading profile data: " + e.getMessage());
            properties.setProperty("playerName", "Player1");
            properties.setProperty("wins", "0");
            properties.setProperty("losses", "0");
            return properties;
        }
    }

    public static boolean saveSettings(int musicVolume, boolean fullscreen, String resolution) {
        Properties properties = new Properties();
        properties.setProperty("musicVolume", String.valueOf(musicVolume));
        properties.setProperty("fullscreen", String.valueOf(fullscreen));
        properties.setProperty("resolution", resolution);

        try (FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(fos, "Tech-Ken Settings");
            return true;
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Properties loadSettings() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(SETTINGS_FILE)) {
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            properties.setProperty("musicVolume", "80");
            properties.setProperty("fullscreen", "false");
            properties.setProperty("resolution", "1280 x 720");
            return properties;
        }
    }

    public static boolean saveMatchResult(String playerCharacter, String cpuCharacter, String winner) {
        Properties history = new Properties();
        try (FileInputStream fis = new FileInputStream(MATCH_HISTORY_FILE)) {
            history.load(fis);
        } catch (IOException e) {
        }
        int matchCount = 0;
        for (String key : history.stringPropertyNames()) {
            if (key.startsWith("match")) {
                matchCount++;
            }
        }
        
        // Format: playerChar|cpuChar|winner|timestamp (Will be updating this soon for a better logs)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String matchData = playerCharacter + "|" + cpuCharacter + "|" + winner + "|" + timestamp;
        history.setProperty("match" + matchCount, matchData);
        
        try (FileOutputStream fos = new FileOutputStream(MATCH_HISTORY_FILE)) {
            history.store(fos, "Tech-Ken Match History");
            return true;
        } catch (IOException e) {
            System.err.println("Error saving match history: " + e.getMessage());
            return false;
        }
    }
}