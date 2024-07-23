package com.Moshu.DoubleJump;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter, Listener {


    public static final ArrayList<String> empty = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {


        if (cmd.getName().equalsIgnoreCase("home")) {

            if (!(sender instanceof Player)) {

                return empty;
            }

            Player p = (Player) sender;

            ArrayList<String> completions = new ArrayList<>();

            if (args.length == 1) {

                ArrayList<String> homes = new ArrayList<>();
                homes.add("toggle");

                return StringUtil.copyPartialMatches(args[0], homes, completions);
            } else {
                return empty;
            }

        }

        return empty;

    }

}
