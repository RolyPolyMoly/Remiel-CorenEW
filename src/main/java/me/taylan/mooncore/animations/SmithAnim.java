package me.taylan.mooncore.animations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.taylan.mooncore.MoonCore;
import me.taylan.mooncore.level.Levels;
import me.taylan.mooncore.listeners.InventoryClickListener;
import me.taylan.mooncore.listeners.JoinListener;
import me.taylan.mooncore.listeners.entitydamage.SpawnArmorStand;
import me.taylan.mooncore.utils.Painter;
import me.taylan.mooncore.utils.StatsManager;
import net.md_5.bungee.api.ChatColor;

import java.util.concurrent.ThreadLocalRandom;

public class SmithAnim {
    private MoonCore plugin;
    private StatsManager stats;
    private Levels levels;

    public SmithAnim(MoonCore plugin) {
        this.plugin = plugin;
        this.levels = plugin.getLevels();
        this.stats = plugin.getStatsManager();

    }

    public void smithAnimation(Player p, int smithTime, ItemStack result, int exp) {
        if (JoinListener.getMenu().get(p.getUniqueId()).firstEmpty() != -1) {
            int timer = stats.getProduction(p.getUniqueId());
            int timerlvl10 = stats.getProduction1(p.getUniqueId());
            if (timer > 0 && timerlvl10 > 0) {
                p.sendMessage(Painter.paint(
                        "&cZaten bir ekipman işliyorsun! 2. Bir ekipmanı işlemeye koymak için 10. seviye ve üzeri Demircilikte ustalık gerekir."));
            }
            if (timer <= 0 && timerlvl10 <= 0) {

                Block block = InventoryClickListener.getSmithBlock().get(p.getUniqueId());
                Location loc3 = block.getLocation().add(0.1, -0.2, 1.4);
                loc3.setYaw(-180);
                Location loc = block.getLocation().add(0.5, 1, 0.5);
                Location loc2 = block.getLocation().add(0.5, 1.2, 0.5);
                ArmorStand armorStand2 = SpawnArmorStand.spawnArmorStand(loc2, "<yellow>İşleniyor...");
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (p.getWorld().getName().equals("dungeonworld") || p.getWorld().getName().equals("world")) {
                        if (player2 == p) continue;
                        player2.hideEntity(plugin, armorStand2);
                    }
                }
                plugin.getIndicators2().put(armorStand2, 100);
                ArmorStand armorStand22 = SpawnArmorStand.spawnArmorStand(loc3, "");
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (p.getWorld().getName().equals("dungeonworld") || p.getWorld().getName().equals("world")) {
                        if (player2 == p) continue;
                        player2.hideEntity(plugin, armorStand22);
                    }
                }
                plugin.getIndicators2().put(armorStand22, 100);
                new SwingAnimation(armorStand22, 40, plugin);
                ArmorStand armorStand = SpawnArmorStand.spawnArmorStand(loc, "");
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (p.getWorld().getName().equals("dungeonworld") || p.getWorld().getName().equals("world")) {
                        if (player2 == p) continue;
                        player2.hideEntity(plugin, armorStand);
                    }
                }
                plugin.getIndicators2().put(armorStand, 100);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        int timer = stats.getProduction(p.getUniqueId());
                        stats.setProduction(p.getUniqueId(), 1);
                        int remain = smithTime - timer;
                        p.playSound(loc, Sound.BLOCK_ANVIL_PLACE, 2f, 1.4f);
                        ItemStack itemData = new ItemStack(Material.NETHERITE_AXE);
                        loc.getWorld().spawnParticle(Particle.ITEM_CRACK, loc, 5, 0.3, 0.5, 0.3, 0, itemData);

                        armorStand.setCustomName(
                                ChatColor.GOLD + "Ekipmanın Hazır Olmasına: " + ChatColor.WHITE + remain + " Saniye");
                        if (timer >= smithTime) {
                            levels.giveSmithLevel(p, exp);
                            if (stats.getMeslek(p.getUniqueId()).contains("Demirci")) {
                                int random = ThreadLocalRandom.current().nextInt(10);
                                        if(random <4) {
                                            levels.giveSmithLevel(p, 10);
                                            p.sendMessage(Painter.paint("Ekstra Demircilik Ustalığı Tecrübe Puanı Kazandın!"));
                                        } else {
                                            p.giveExp(50);
                                            p.sendMessage(Painter.paint("Ekstra Tecrübe Puanı Kazandın!"));

                                        }
                            }
                            armorStand.setCustomName(ChatColor.GREEN + "✔");
                            result.setAmount(1);
                            JoinListener.getMenu().get(p.getUniqueId()).addItem(result);
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    armorStand.remove();

                                }
                            }.runTaskLater(plugin, 40L);
                            armorStand2.remove();
                            armorStand22.remove();
                            stats.setProduction(p.getUniqueId(), -timer - 1);
                            InventoryClickListener.getSmithBlock().clear();
                            p.playSound(loc, Sound.ENTITY_VILLAGER_WORK_TOOLSMITH, 3f, 0.2f);

                            cancel();
                        }

                    }
                }.runTaskTimer(plugin, 0L, 20L);

            }/* else if (timerlvl10 <= 1) {
			int smithLevel = stats.getSmithLevel(p.getUniqueId());
			if (smithLevel >= 10) {
				new BukkitRunnable() {

					@Override
					public void run() {
						World world = Bukkit.getWorld("world");
						Block block = InventoryClickListener.getSmithBlock().get(p.getUniqueId());
						Location loc3 = block.getLocation().add(0.1, -0.2, 1.4);
						loc3.setYaw(-180);
						Location loc = block.getLocation().add(0.5, 1, 0.5);
						Location loc2 = block.getLocation().add(0.5, 1.2, 0.5);
						ArmorStand armorStand2 = SpawnArmorStand.spawnArmorStand(loc2, "<yellow>İşleniyor...");
						for (Player player2 : Bukkit.getOnlinePlayers()) {
							if (p.getWorld().getName().equals("dungeonworld") ||p.getWorld().getName().equals("world")) {
								if (player2 == p) continue;
								player2.hideEntity(plugin, armorStand2);
							}
						}
						ArmorStand armorStand22 = SpawnArmorStand.spawnArmorStand(loc3, "");
						for (Player player2 : Bukkit.getOnlinePlayers()) {
							if (p.getWorld().getName().equals("dungeonworld") ||p.getWorld().getName().equals("world")) {
								if (player2 == p) continue;
								player2.hideEntity(plugin, armorStand22);
							}
						}
						armorStand22.setSmall(false);
						new SwingAnimation(armorStand22, 40, plugin);
						ArmorStand armorStand = SpawnArmorStand.spawnArmorStand(loc, "");
						for (Player player2 : Bukkit.getOnlinePlayers()) {
							if (p.getWorld().getName().equals("dungeonworld") ||p.getWorld().getName().equals("world")) {
								if (player2 == p) continue;
								player2.hideEntity(plugin, armorStand);
							}
						}
						new BukkitRunnable() {

							@Override
							public void run() {
								int timerlvl10 = stats.getProduction1(p.getUniqueId());
								stats.setProduction1(p.getUniqueId(), 1);
								int remain = smithTime - timerlvl10;

								p.playSound(loc, Sound.BLOCK_ANVIL_PLACE, 2f, 1.4f);
								ItemStack itemData = new ItemStack(Material.NETHERITE_AXE);
								loc.getWorld().spawnParticle(Particle.ITEM_CRACK, loc, 5, 0.3, 0.5, 0.3, 0, itemData);

								armorStand.setCustomName(ChatColor.GOLD + "Ekipmanın Hazır Olmasına: " + ChatColor.WHITE
										+ remain + " Saniye");
								if (timerlvl10 >= smithTime) {
									levels.giveSmithLevel(p, exp);

									armorStand.setCustomName(Painter.paint("&a&l✓"));

									JoinListener.getMenu().get(p.getUniqueId()).addItem(result);
									new BukkitRunnable() {

										@Override
										public void run() {
											armorStand.remove();

										}
									}.runTaskLater(plugin, 40L);
									armorStand2.remove();
									armorStand22.remove();
									stats.setProduction(p.getUniqueId(), -timerlvl10 - 1);
									InventoryClickListener.getSmithBlock().clear();
									p.playSound(loc, Sound.ENTITY_VILLAGER_WORK_TOOLSMITH, 0.2f, 0.2f);

									cancel();
								}

							}
						}.runTaskTimer(plugin, 0L, 25L);

					}
				}.runTaskLater(plugin, smithTime * 20);

			} else {
				p.sendMessage(Painter.paint("Demircilikte ustalığın 10. seviyeden düşük!"));
			}
		}*/

        } else {
            p.sendMessage(Painter.paint("&c&lBir malzeme üretebilmek için &6&lDemirci Masası Deponu &c&lBoşalt!"));
        }
    }
}
