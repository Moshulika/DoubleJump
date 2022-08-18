package com.Moshu.DoubleJump;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoubleJump implements Listener {

    private static Map<UUID, Integer> cooldown = new HashMap<>();
    public static ArrayList<UUID> justjump = new ArrayList<>();
    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("Engine");

    public boolean canJump(Player p)
    {
        return !hasJumped(p) || cooldown.get(p.getUniqueId()) <= 3;
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

            if (p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {

                if (p.hasPermission("engine.fly")) return;
                if (p.isInvulnerable()) return;

                if (Data.getSkilltree(p).equals("utility") || justjump.contains(p.getUniqueId())) {

                    Location loc = p.getLocation();

                    if (Locations.isInRegion(loc) && !Locations.isInRegion(loc, "arena_pvp") && !Locations.isInRegion(loc, "arena") && !Locations.isInRegion(loc, "clanwar")) return;

                    if (p.getVelocity().getY() < -0.08) p.setAllowFlight(false);

                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onToggleFlight(PlayerToggleFlightEvent e) {

        Player p = e.getPlayer();

        if(Data.getSkilltree(p).equals("utility") || justjump.contains(p.getUniqueId())) {


            if(!justjump.contains(p.getUniqueId())) {
                if (Fly.hasFlyAccess(p) || p.hasPermission("engine.gamemode") || p.isOp()) return;
                if (p.getGameMode() == GameMode.CREATIVE) return;
            }

            if(p.isFlying()) return;

            e.setCancelled(true);

            if(p.isSwimming() || p.isSneaking()) return;
            if(Locations.isInRegion(p.getLocation())) return;

            if(!canJump(p))
            {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.errorSymbol() + "Ai obosit, ai grija sa nu mai sari! (4/4)"));
                return;
            }

            addJump(p);

            if(!justjump.contains(p.getUniqueId()))
            {

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&b✈&8) &fWoosh! (" + getJumps(p) + "/4)"));

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                {
                    removeJump(p);
                }, 1800);

            }
            else
            {

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                {
                    removeJump(p);
                }, 300);

            }

            p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, Grave.getParticleLocation(p.getLocation()), 1);
            p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, Grave.getParticleLocation(p.getLocation()), 1);
            p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, Grave.getParticleLocation(p.getLocation()), 1);
            p.setVelocity(p.getLocation().getDirection().multiply(1).setY(1));
            Utils.sendSound(p, Sound.ENTITY_ENDER_DRAGON_FLAP);

        }

    }

    public static void checkDoubleJump()
    {

        BukkitRunnable run = new BukkitRunnable() {

            @Override
            public void run() {

                for(Player p : Bukkit.getOnlinePlayers())
                {

                    if(Data.getSkilltree(p).equals("utility") || justjump.contains(p.getUniqueId()))
                    {

                        if(!justjump.contains(p.getUniqueId()))
                        {
                            if (p.hasPermission("engine.fly")) continue;
                            if (p.getGameMode() != GameMode.SURVIVAL) continue;
                        }

                        if(Locations.isInRegion(p.getLocation())) continue;
                        if(p.getAllowFlight()) continue;

                        Bukkit.getScheduler().runTask(plugin, () ->
                        {

                            if(!Fly.hasFlyAccess(p))
                            {
                                p.setFlying(false);
                            }

                            if (p.getVelocity().getY() < -0.08)
                            {
                                p.setAllowFlight(false);
                                return;
                            }

                            p.setAllowFlight(true);

                        });

                    }
                    else
                    {

                        if(!Fly.hasFlyAccess(p))
                        {
                            p.setFlying(false);
                            p.setAllowFlight(false);
                        }

                    }

                }

            }
        };

        run.runTaskTimerAsynchronously(plugin, 0, 20);

    }

}
