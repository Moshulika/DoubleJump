package com.Moshu.DoubleJump;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class DoubleJump implements Listener {

    private static Map<UUID, Integer> cooldown = new HashMap<>();
    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("DoubleJump");

    public boolean canJump(Player p)
    {
        return !hasJumped(p) || cooldown.get(p.getUniqueId()) < getMaxJumps();
    }

    public int getMaxJumps()
    {
        return plugin.getConfig().getInt("max_jumps");
    }

    public int getCooldown()
    {
        return plugin.getConfig().getInt("cooldown");
    }

    public Particle getParticle()
    {
        return Particle.valueOf(plugin.getConfig().getString("particle"));
    }

    public Sound getSound()
    {
        return Sound.valueOf(plugin.getConfig().getString("sound"));
    }

    public boolean hasJumped(Player p)
    {
        return cooldown.containsKey(p.getUniqueId());
    }

    public int getJumps(Player p)
    {
        return cooldown.get(p.getUniqueId());
    }

    public void addJump(Player p)
    {

        if(hasJumped(p))
        {
            cooldown.put(p.getUniqueId(), getJumps(p) + 1);
        }
        else
        {
            cooldown.put(p.getUniqueId(), 1);
        }

    }

    public void removeJump(Player p)
    {
        cooldown.put(p.getUniqueId(), getJumps(p) - 1);
    }

    /**
     * Untested
     * @param e event
     */
    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {

            if (p.getGameMode() == GameMode.SURVIVAL) {

                if (p.isInvulnerable()) return;
                if (!p.hasPermission("doublejump.use")) return;

                    if(!cooldown.containsKey(p.getUniqueId()) || getJumps(p) == 0) {

                        if (p.getVelocity().getY() < -0.08) p.setAllowFlight(false);
                    }

            }
        }
    }

    private static boolean blacklistedRegion(Player p)
    {

        for(String s : plugin.getConfig().getStringList("blacklisted_regions"))
        {
            if(Locations.isInRegion(p.getLocation(), s))
            {
                return true;
            }
        }

        return false;
    }// jump toggle

    private static boolean enabledWorld(Player p)
    {
        return plugin.getConfig().getStringList("enabled_worlds").contains(p.getLocation().getWorld().getName());
    }

    @EventHandler(ignoreCancelled = true)
    public void onToggleFlight(PlayerToggleFlightEvent e) {

        Player p = e.getPlayer();

        if (p.isFlying()) return;
        if (!p.hasPermission("doublejump.use")) return;

        e.setCancelled(true);

        if (p.isSwimming() || p.isSneaking()) return;
        if(p.getGameMode() != GameMode.SURVIVAL) return;

        if (!enabledWorld(p)) return;
        if (blacklistedRegion(p)) return;

        if (!canJump(p)) {
            Utils.sendParsed(p, Utils.getLang("maxjumps").replace("%jumps%", Integer.toString(getJumps(p))).replace("%maxjumps%", Integer.toString(getMaxJumps())));
            return;
        }

        addJump(p);
        Utils.sendParsed(p, Utils.getLang("jump").replace("%jumps%", Integer.toString(getJumps(p))).replace("%maxjumps%", Integer.toString(getMaxJumps())));

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
        {
            removeJump(p);
        }, getCooldown() * 20L);


        try {
            p.getWorld().spawnParticle(getParticle(), Utils.getParticleLocation(p.getLocation()), 1);
            p.getWorld().spawnParticle(getParticle(), Utils.getParticleLocation(p.getLocation()), 1);
            p.getWorld().spawnParticle(getParticle(), Utils.getParticleLocation(p.getLocation()), 1);
        } catch (NullPointerException ex) {
            plugin.getLogger().log(Level.SEVERE, "Incorrect particle name! Check your config!");
        }

        p.setVelocity(p.getLocation().getDirection().multiply(1).setY(1));


        try {
            Utils.sendSound(p, getSound());
        } catch (NullPointerException ex) {
            plugin.getLogger().log(Level.SEVERE, "Incorrect sound name! Check your config!");
        }


    }

    public static void checkDoubleJump()
    {

        BukkitRunnable run = new BukkitRunnable() {

            @Override
            public void run() {

                for(Player p : Bukkit.getOnlinePlayers()) {


                    if (p.getGameMode() != GameMode.SURVIVAL) continue;
                    if (p.getAllowFlight()) continue;
                    if (!p.hasPermission("doublejump.use")) continue;
                    if (!enabledWorld(p)) continue;
                    if (blacklistedRegion(p)) continue;

                    Bukkit.getScheduler().runTask(plugin, () ->
                    {

                        p.setAllowFlight(true);

                    });


                }

            }
        };

        run.runTaskTimerAsynchronously(plugin, 0, 20);

    }

}
