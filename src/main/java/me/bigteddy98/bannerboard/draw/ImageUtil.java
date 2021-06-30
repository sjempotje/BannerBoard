package me.bigteddy98.bannerboard.draw;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil {

    public static Map<String, BufferedImage> loadCache(File imageMap) {
        Map<String, BufferedImage> tmp = new HashMap<>();

        Bukkit.getConsoleSender().sendMessage("[INFO] [BannerBoard] Loading images...");

        loadFolder(tmp, "", imageMap);

        Bukkit.getConsoleSender().sendMessage("[INFO] [BannerBoard] Succesfully loaded " + tmp.size() + " image(s).");

        return tmp;
    }

    private static void loadFolder(Map<String, BufferedImage> tmp, String prefix, File imageMap) {
        for (File file : imageMap.listFiles()) {

            if (file.isDirectory()) {
                loadFolder(tmp, prefix + file.getName() + "/", file);
                continue;
            }

            String name = file.getName();
            if (name.endsWith(".png")) {
                try {
                    tmp.put(prefix + name, ImageIO.read(file));
                    Bukkit.getConsoleSender().sendMessage("[INFO] [BannerBoard] Succesfully loaded image " + (prefix + name) + ".");
                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[WARNING] [BannerBoard] Could not load image " + file.getName() + ". " + e.getMessage());
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[WARNING] [BannerBoard] Could not load image " + file.getName() + ". Bannerboard only supports png images.");
            }
        }
    }
}
