package com.Moshu.DoubleJump;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.UUID;

public class Commands implements CommandExecutor {

    private static final ArrayList<UUID> a = new ArrayList<>();

    public static boolean exempt(Player p)
    {

        return a.contains(p.getUniqueId());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if(cmd.getName().equalsIgnoreCase("dj"))
        {

            if(!(sender instanceof Player))
            {
                Utils.sendNotPlayer();
                return true;
            }

            Player p = (Player) sender;

            if(!p.hasPermission("doublejump.use"))
            {
                Utils.sendNoAccess(p);
                return true;
            }

            if(args.length == 0)
            {

                PluginDescriptionFile pdf = Bukkit.getPluginManager().getPlugin("DoubleJump").getDescription();

                sender.sendMessage(" ");
                sender.sendMessage(Utils.format(" #80C7FD&lDOUBLE JUMP"));
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7Created by &fMoshu"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &f" + pdf.getCommands().size() + "&7 commands and &f" + pdf.getPermissions().size() + "&7 permissions loaded."));
                sender.sendMessage(Utils.format("  &7Version " + pdf.getVersion()));
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7Use mc.b-zone.ro/discord for help."));
                sender.sendMessage(" ");

            }
            else if(args.length == 1)
            {

                if(args[0].equalsIgnoreCase("toggle"))
                {

                    if(exempt(p))
                    {
                        a.remove(p.getUniqueId());
                        Utils.sendParsed(p, Utils.getLang("toggle"));
                    }
                    else
                    {
                        a.add(p.getUniqueId());
                        Utils.sendParsed(p, Utils.getLang("toggle"));
                    }


                }

            }
            else
            {
                Utils.sendParsed(p, Utils.getLang("wrong-cmd"));
            }

        }

        return true;
    }

}
