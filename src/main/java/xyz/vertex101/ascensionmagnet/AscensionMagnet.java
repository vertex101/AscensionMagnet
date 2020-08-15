package xyz.vertex101.ascensionmagnet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.vertex101.ascensionmagnet.commands.CommandMagnet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class AscensionMagnet extends JavaPlugin {
    private boolean invFull;
    private double maxDistance = 8.0D;
    public final List<Player> magnetPlayers = new CopyOnWriteArrayList<Player>();

    public static AscensionMagnet getPlugin() {
        return JavaPlugin.getPlugin(AscensionMagnet.class);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.maxDistance = getConfig().getInt("max-distance");
        this.invFull = getConfig().getBoolean("inventory-full");
        this.getCommand("magnet").setExecutor(new CommandMagnet());
        ItemSearch itemSearch = new ItemSearch();
        getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, itemSearch, 5L, 5L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private class ItemSearch implements Runnable {
        private ItemSearch() {}

        public void run() {
            for (World world : AscensionMagnet.this.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (!(entity instanceof Item))
                        continue;
                    Item item = (Item)entity;
                    ItemStack stack = item.getItemStack();
                    Location location = item.getLocation();
                    if (stack.getAmount() <= 0 || item.isDead() || item.getPickupDelay() > item.getTicksLived())
                        continue;
                    Player closestPlayer = null;
                    double distanceSmall = AscensionMagnet.this.maxDistance;
                    for (Player player : AscensionMagnet.this.magnetPlayers) {
                        if (player != null)
                            if (player.getWorld().getName().equals(world.getName())) {
                                double playerDistance = player.getLocation().distance(location);
                                if (playerDistance < distanceSmall) {
                                    distanceSmall = playerDistance;
                                    closestPlayer = player;
                                }
                            }
                    }
                    if (closestPlayer == null)
                        continue;
                    if(invFull)
                        continue;
                    item.setVelocity(closestPlayer.getLocation().toVector().subtract(item.getLocation().toVector()).normalize());
                }
            }
        }
    }
}
