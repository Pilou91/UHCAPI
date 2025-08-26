package fr.pilou.uhcapi.utils.tag;

import fr.pilou.uhcapi.API;
import fr.pilou.uhcapi.listeners.ListenerRegistry;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@ListenerRegistry()
public class TagUtils implements Listener {
    public static HashMap<UUID, ScoreboardTeam> tagMap = new HashMap<>();
    public static HashMap<ScoreboardTeam, List<UUID>> canSeeMap = new HashMap<>();
    public static HashMap<UUID, Boolean> seeHealthMap = new HashMap<>();

    /**
     * Ajoute un prefix dans le tab est au dessus de la tête du joueur.
     * @param player Le joueur à qui on met ce prefix.
     * @param prefix Le prefix voulu.
     * @param key La clé pour définir/recupérer ce prefix.
     * @param priority Sa hauteur dans le tab.
     * @see #addPrefix(Player, String, String, int, List) Version plus complète
     */
    public static void addPrefix(Player player, String prefix, String key, int priority) {
        ScoreboardTeam scoreboardTeam = new ScoreboardTeam(priority + key, prefix);
        tagMap.put(player.getUniqueId(), scoreboardTeam);
        for (Player players : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) players).getHandle().playerConnection.sendPacket(scoreboardTeam.createTeam());
            ((CraftPlayer) players).getHandle().playerConnection.sendPacket(scoreboardTeam.addOrRemovePlayer(3, player.getName()));
        }
    }

    /**
     * Ajoute un prefix dans le tab est au dessus de la tête du joueur visible par une liste de joueurs.
     * @param player Le joueur à qui on met ce prefix.
     * @param prefix Le prefix voulu.
     * @param key La clé pour définir/recupérer ce prefix.
     * @param priority Sa hauteur dans le tab.
     * @param canSee Liste des joueurs pouvant voir se prefix.
     * @see #addPrefix(Player, String, String, int) Version simplifié.
     */
    public static void addPrefix(Player player, String prefix, String key, int priority, List<UUID> canSee) {
        ScoreboardTeam scoreboardTeam = new ScoreboardTeam((priority == Integer.MAX_VALUE ? "" : priority) + key, prefix);
        tagMap.put(player.getUniqueId(), scoreboardTeam);
        canSeeMap.put(scoreboardTeam, canSee);
        for (UUID uuid : canSee) {
            Player playerCanSee = Bukkit.getPlayer(uuid);
            if (playerCanSee == null) continue;
            ((CraftPlayer) playerCanSee).getHandle().playerConnection.sendPacket(scoreboardTeam.createTeam());
            ((CraftPlayer) playerCanSee).getHandle().playerConnection.sendPacket(scoreboardTeam.addOrRemovePlayer(3, player.getName()));
        }
    }

    /**
     * Retire le prefix du au dessus de la tête du joueur, mais aussi dans le tab.
     * @param player Le joueur dont le préfix est retiré du tab
     */
    public static void removePrefix(Player player) {
        if (tagMap.get(player.getUniqueId()) != null) {
            if (canSeeMap.get(tagMap.get(player.getUniqueId())) != null) {
                for (UUID uuid : canSeeMap.get(tagMap.get(player.getUniqueId()))) {
                    Player playerCanSee = Bukkit.getPlayer(uuid);
                    if (playerCanSee == null) continue;
                    //((CraftPlayer) playerCanSee).getHandle().playerConnection.sendPacket(tagMap.get(player.getUniqueId()).removeTeam());
                    ((CraftPlayer) playerCanSee).getHandle().playerConnection.sendPacket(tagMap.get(player.getUniqueId()).addOrRemovePlayer(4, player.getName()));
                }
            } else {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    //((CraftPlayer) players).getHandle().playerConnection.sendPacket(tagMap.get(player.getUniqueId()).removeTeam());
                    ((CraftPlayer) players).getHandle().playerConnection.sendPacket(tagMap.get(player.getUniqueId()).addOrRemovePlayer(4, player.getName()));
                }
            }
            tagMap.remove(player.getUniqueId());
        }
    }

    /**
     * Défini qu'un joueur peut voir la vie des autres joueurs au-dessus de leur tête
     * @param player Le joueur auquel on confère la capacité de voir la vie
     */
    public static void setAbleToSeeHealth(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = (scoreboard.getObjective("HP") == null) ? scoreboard.registerNewObjective("HP", "health") : scoreboard.getObjective("HP");
        objective.setDisplayName(ChatColor.RED + "❤");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        player.setScoreboard(scoreboard);
        seeHealthMap.put(player.getUniqueId(), true);
    }

    /**
     * Défini qu'un joueur ne peut pas voir la vie des autres joueurs au-dessus de leur tête
     * @param player Le joueur qui ne peut plus voir la vie des autres joueurs
     */
    public static void setUnableToSeeHealth(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
        seeHealthMap.put(player.getUniqueId(), false);
    }

    /**
     *
     * @param player Le joueur qui va voir les informations sous le pseudo
     * @param obj
     * @param display Ce qui est affiché avant l'information (un ♥ par exemple)
     * @param playerValueMap La map <UUID, Integer> contenant, pour chaque joueur représenté par son UUID, le nombre qui doit êre indiqué après le display
     */
    public static void addBelowName(Player player, String obj, String display, HashMap<UUID, Integer> playerValueMap) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        if (scoreboardManager == null) return;
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = (scoreboard.getObjective(obj) == null) ? scoreboard.registerNewObjective(obj, "dummy") : scoreboard.getObjective(obj);
        objective.setDisplayName(display);
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        player.setScoreboard(scoreboard);
        Bukkit.getScheduler().runTaskTimer(API.get().getPlugin(), () -> {
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (playerValueMap != null && playerValueMap.get(offlinePlayer.getUniqueId()) != null) {
                    objective.getScore(offlinePlayer.getName()).setScore(playerValueMap.get(offlinePlayer.getUniqueId()));
                }
            }
        }, 0, 20L);
    }


    public static void stopBelowName(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard != null) {
            scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
        }
    }

    public static void addTagHologram(Player player, Supplier<String> text, Player target) {
        ArmorStand armorStand = null;
        if (target != player) {
            Location location = target.getLocation().add(0, 2.2, 0);
            armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setBasePlate(true);
            armorStand.setMarker(true);
            String newText = text.get();
            armorStand.setCustomName(newText);
        }
        ArmorStand finalArmorStand = armorStand;
        Player finalTarget = target;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (finalArmorStand != null && finalTarget != null) {
                    finalArmorStand.teleport(target.getLocation().add(0, 2.2, 0));
                    if (!player.canSee(target) || hasInvisibilityEffect(target) || target.isSneaking()) {
                        finalArmorStand.setCustomName(null);
                        finalArmorStand.setCustomNameVisible(true);
                        finalArmorStand.setCustomNameVisible(false);
                    } else {
                        String newText = text.get();
                        finalArmorStand.setCustomName(newText);
                        finalArmorStand.setCustomNameVisible(true);
                    }
                    for (Player targets : Bukkit.getOnlinePlayers()) {
                        int entityId = finalArmorStand.getEntityId();
                        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
                        if (targets != player) {
                            ((CraftPlayer) targets).getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                }
            }
        }.runTaskTimer(API.get().getPlugin(), 0L, 1L);
    }

    public static void removeTagHologram(Player player) {
        for (Entity entity : player.getWorld().getEntities()) {
            if (!(entity instanceof ArmorStand armorStand)) continue;
            if (player.getLocation().add(0, 2.2, 0).distance(armorStand.getLocation()) > 0.3) continue;
            armorStand.remove();
        }
    }

    public static boolean hasInvisibilityEffect(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (seeHealthMap.getOrDefault(player.getUniqueId(), false)) {
            setAbleToSeeHealth(player);
        }
        if (tagMap.get(player.getUniqueId()) != null) {
            if (canSeeMap.get(tagMap.get(player.getUniqueId())) == null || canSeeMap.get(tagMap.get(player.getUniqueId())).isEmpty()) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) players).getHandle().playerConnection.sendPacket(tagMap.get(player.getUniqueId()).createTeam());
                    ((CraftPlayer) players).getHandle().playerConnection.sendPacket(tagMap.get(player.getUniqueId()).addOrRemovePlayer(3, player.getName()));
                }
            } else {
                for (UUID uuid : canSeeMap.get(tagMap.get(player.getUniqueId()))) {
                    Player playerCanSee = Bukkit.getPlayer(uuid);
                    if (playerCanSee == null) continue;
                    ((CraftPlayer) playerCanSee).getHandle().playerConnection.sendPacket(tagMap.get(player.getUniqueId()).createTeam());
                    ((CraftPlayer) playerCanSee).getHandle().playerConnection.sendPacket(tagMap.get(player.getUniqueId()).addOrRemovePlayer(3, player.getName()));
                }
            }
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (tagMap.get(players.getUniqueId()) != null) {
                if (canSeeMap.get(tagMap.get(players.getUniqueId())) == null || canSeeMap.get(tagMap.get(players.getUniqueId())).isEmpty()) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(tagMap.get(players.getUniqueId()).createTeam());
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(tagMap.get(players.getUniqueId()).addOrRemovePlayer(3, players.getName()));
                } else {
                    for (UUID uuid : canSeeMap.get(tagMap.get(players.getUniqueId()))) {
                        Player playerCanSee = Bukkit.getPlayer(uuid);
                        if (playerCanSee == null) continue;
                        ((CraftPlayer) playerCanSee).getHandle().playerConnection.sendPacket(tagMap.get(players.getUniqueId()).createTeam());
                        ((CraftPlayer) playerCanSee).getHandle().playerConnection.sendPacket(tagMap.get(players.getUniqueId()).addOrRemovePlayer(3, players.getName()));
                    }
                }
            }
        }
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (Entity entity : player.getWorld().getEntities()) {
            if (entity instanceof ArmorStand) {
                if (player.getLocation().add(0, 2.2, 0).distance(entity.getLocation()) <= 0.3) {
                    entity.remove();
                }
            }
        }
    }
}