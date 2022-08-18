package com.Moshu.DoubleJump;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    public void onEnable()
    {

        CommandSender s = Bukkit.getConsoleSender();
        long start = (int) System.currentTimeMillis();
        PluginDescriptionFile pdfFile = getDescription();

        s.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lDoubleJump &f---------------------"));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAuthor: &cMoshu"));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fVersion: &c" + pdfFile.getVersion()));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fPermissions: &c" + pdfFile.getPermissions().size()));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));

        Bukkit.getPluginManager().registerEvents(new DoubleJump(), this);

        DoubleJump.checkDoubleJump();

        long stop = (int) System.currentTimeMillis();

        long time = stop - start;

        s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lStartup: &fTook &c" + time + "ms &fto enable."));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lmc.b-zone.ro &f--------------------"));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));

    }


    public void onDisable()
    {}

}
