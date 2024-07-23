package com.Moshu.DoubleJump;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.http.WebSocket;

public class Main extends JavaPlugin implements WebSocket.Listener {

    Utils utils = new Utils(this);

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

        getCommand("dj").setExecutor(new Commands());
        getCommand("dj").setTabCompleter(new TabCompleter());
        Bukkit.getPluginManager().registerEvents(new DoubleJump(), this);

        createFiles();

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

    File configf;
    FileConfiguration config;

    private void createFiles()
    {

        configf = new File(getDataFolder(), "config.yml");

        if(!configf.exists())
        {
         configf.getParentFile().mkdirs();
         saveResource("config.yml", false);
        }

        config = new YamlConfiguration();

        try
        {
            config.load(configf);
        }
        catch(IOException | InvalidConfigurationException ex)
        {

        }
    }

}
