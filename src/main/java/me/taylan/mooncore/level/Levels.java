package me.taylan.mooncore.level;

import com.manya.pdc.DataTypes;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay.AdvancementFrame;
import eu.endercentral.crazy_advancements.advancement.ToastNotification;
import fr.mrmicky.fastboard.FastBoard;
import me.taylan.mooncore.MoonCore;
import me.taylan.mooncore.commands.SeviyeCommand;
import me.taylan.mooncore.eco.Ekonomi;
import me.taylan.mooncore.utils.ItemHandler;
import me.taylan.mooncore.utils.StatsManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Levels {

    private MoonCore plugin;
    private SeviyeCommand seviyeCommand;
    private StatsManager stats;
    private Ekonomi ekonomi;
    private ItemHandler itemHandler;
    private final PersistentDataType<?, HashMap<String, Integer>> Enchants = DataTypes
            .hashMap(PersistentDataType.STRING, PersistentDataType.INTEGER);
    private BukkitRunnable r;
    private DecimalFormat df = new DecimalFormat("#,###.##");

    public Levels(MoonCore plugin) {
        this.seviyeCommand = plugin.getSeviyeCommand();
        this.plugin = plugin;
        this.itemHandler = plugin.getItemHandler();
        this.stats = plugin.getStatsManager();
        this.ekonomi = plugin.getEkonomi();

        r = new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date(System.currentTimeMillis());
                    FastBoard board = new FastBoard(player);

                    double para = ekonomi.getBalance(player);
                    board.updateTitle(ChatColor.AQUA + "Moon Network");

                    board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "     Remiel", "    ",
                            ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para),
                            "", ChatColor.GRAY + player.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                            ChatColor.AQUA + "mc.moonnw.xyz"

                    );
                }
            }
        };
        r.runTaskTimerAsynchronously(plugin, 0, 100L);

    }

    public void giveWorkLevel(Player p, int exp2) {
        UUID uuid = p.getUniqueId();
        int smithLevel = stats.getWorkLevel(uuid);
        int smithExp = stats.getWorkExp(uuid);
        int smithRequiredExp = stats.getWorkRequiredExp(uuid);
        FastBoard board = new FastBoard(p);

        board.updateTitle(ChatColor.AQUA + "Moon Network");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());

        ToastNotification notification = new ToastNotification(Material.SMITHING_TABLE,
                ChatColor.YELLOW + "El Sanatlar??nda ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);


        stats.setWorkExp(uuid, exp2);
        stats.setExp(uuid, exp2 - 3);

        int prog = 100 * smithExp / smithRequiredExp;

        double para = ekonomi.getBalance(p);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "        Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "El Sanatlar?? " + ChatColor.WHITE + "Ustal??k " + smithLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + smithExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + smithRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + p.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );
        if (smithExp >= smithRequiredExp) {
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            int Level = stats.getLevel(uuid);
            stats.setExp(uuid, smithLevel * 2);


            smithLevel++;
            stats.setWorkLevel(uuid, 1);
            stats.setWorkRequiredExp(uuid, smithLevel * 55);
            stats.setWorkExp(uuid, -smithExp);
            notification.send(p);

        }

    }

    public void giveSmithLevel(Player p, int exp) {
        UUID uuid = p.getUniqueId();
        int smithLevel = stats.getSmithLevel(uuid);
        int smithExp = stats.getSmithExp(uuid);
        int smithRequiredExp = stats.getSmithRequiredExp(uuid);
        FastBoard board = new FastBoard(p);

        board.updateTitle(ChatColor.AQUA + "Moon Network");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());

        ToastNotification notification = new ToastNotification(Material.SMITHING_TABLE,
                ChatColor.YELLOW + "Demircilikte ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);

        if (stats.getHepsi(p.getUniqueId()).get(1).hasItemMeta() && stats.getHepsi(p.getUniqueId()).get(1).getItemMeta().getDisplayName().contains("Demirci")) {
            int newexp = exp * 20 / 100;
            stats.setSmithExp(uuid, exp + newexp);
        } else {
            stats.setSmithExp(uuid, exp);
        }
        stats.setExp(uuid, exp - 4);

        int prog = 100 * smithExp / smithRequiredExp;
        double para = ekonomi.getBalance(p);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "        Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "Demircilik " + ChatColor.WHITE + "Ustal??k " + smithLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + smithExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + smithRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + p.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );
        if (smithExp >= smithRequiredExp) {
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            int Level = stats.getLevel(uuid);
            stats.setExp(uuid, smithLevel * 2);


            smithLevel++;
            stats.setSmithLevel(uuid, 1);
            stats.setSmithRequiredExp(uuid, smithLevel * 55);
            stats.setSmithExp(uuid, -smithExp);
            notification.send(p);

        }

    }

    public void giveCookLevel(Player p, ItemStack item) {
        UUID uuid = p.getUniqueId();
        int cookLevel = stats.getCookLevel(uuid);
        int cookExp = stats.getCookExp(uuid);
        int cookRequiredExp = stats.getCookRequiredExp(uuid);
        FastBoard board = new FastBoard(p);

        board.updateTitle(ChatColor.AQUA + "Moon Network");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());

        ToastNotification notification = new ToastNotification(Material.BREAD,
                ChatColor.YELLOW + "A??????l??kta ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);

        if (ExpList.getExpMap().containsKey(item.getType().toString())) {
            int exp2 = ExpList.getExpMap().get(item.getType().toString());
            stats.setCookExp(uuid, exp2);
            stats.setExp(uuid, exp2 - 1);
        }

        int prog = 100 * cookExp / cookRequiredExp;

        double para = ekonomi.getBalance(p);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "        Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "A??????l??k " + ChatColor.WHITE + "Ustal??k " + cookLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + cookExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + cookRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY + ")",
                "     ", ChatColor.GRAY + p.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );
        if (cookExp >= cookRequiredExp) {
            stats.setCan(p, p.getUniqueId(), 1);
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            stats.setExp(uuid, cookLevel * 2);


            cookLevel++;
            stats.setCookLevel(uuid, 1);
            stats.setCookRequiredExp(uuid, cookLevel * 70);
            stats.setCookExp(uuid, -cookExp);
            notification.send(p);

        }

    }

    public void giveCombatEXP(Player player, int exp, LivingEntity Entity) {
        ToastNotification notification = new ToastNotification(Material.IRON_SWORD,
                ChatColor.YELLOW + "Avc??l??kta ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);
        UUID uuid = player.getUniqueId();
        int combatLevel = stats.getCombatLevel(uuid);
        int combatExp = stats.getCombatExp(uuid);
        int combatRequiredExp = stats.getCombatRequiredExp(uuid);

        FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.AQUA + "Moon Network");
        stats.setCombatExp(uuid, exp);
        if (ExpList.getExpMap().containsKey(Entity.getType().toString())) {

            int exep = ExpList.getExpMap().get(Entity.getType().toString());
            stats.setExp(uuid, exep);
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                NamespacedKey key = new NamespacedKey(plugin, "Enchants");
                if (meta.getPersistentDataContainer() != null) {

                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if (container.has(key, Enchants)) {
                        if (container.get(key, Enchants).containsKey("ustal??k")) {
                            int level = container.get(key, Enchants).get("ustal??k");
                            stats.setMadencilikExp(player.getUniqueId(), level);
                        }
                    }
                }
            }
        }

        int prog = 100 * combatExp / combatRequiredExp;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());

        double para = ekonomi.getBalance(player);

        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "        Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "Avc??l??k " + ChatColor.WHITE + "Ustal??k " + combatLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + combatExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + combatRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + player.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );

        if (combatExp >= combatRequiredExp) {
            stats.setKritikHasari(player.getUniqueId(), 1);
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            stats.setExp(uuid, combatLevel * 2);

            combatLevel++;
            stats.setCombatLevel(uuid, 1);
            stats.setCombatRequiredExp(uuid, combatLevel * 60);
            stats.setCombatExp(uuid, -combatExp);
            notification.send(player);

        }

    }

    public void giveOduncuExp(Player player, Block block) {
        ToastNotification notification = new ToastNotification(Material.IRON_AXE,
                ChatColor.YELLOW + "Odunculukta ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);
        UUID uuid = player.getUniqueId();
        int odunculukLevel = stats.getOdunculukLevel(uuid);
        int odunculukExp = stats.getOdunculukExp(uuid);
        int odunculukRequiredExp = stats.getOdunculukRequiredExp(uuid);
        FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.AQUA + "Moon Network");
        if (ExpList.getExpMap().containsKey(block.getType().toString())) {

            int exp = ExpList.getExpMap().get(block.getType().toString());
            stats.setOdunculukExp(uuid, exp);
            stats.setExp(uuid, exp - 1);
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                NamespacedKey key = new NamespacedKey(plugin, "Enchants");
                if (meta.getPersistentDataContainer() != null) {

                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if (container.has(key, Enchants)) {
                        if (container.get(key, Enchants).containsKey("ustal??k")) {
                            int level = container.get(key, Enchants).get("ustal??k");
                            stats.setMadencilikExp(player.getUniqueId(), level);
                        }
                    }
                }
            }
        }

        int prog = 100 * odunculukExp / odunculukRequiredExp;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        double para = ekonomi.getBalance(player);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "         Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "Odunculuk " + ChatColor.WHITE + "Ustal??k " + odunculukLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + odunculukExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + odunculukRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + player.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );
        if (odunculukExp >= odunculukRequiredExp) {
            stats.setGuc(player.getUniqueId(), 1);
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            int Level = stats.getLevel(uuid);
            stats.setExp(uuid, odunculukLevel * 2);

            odunculukLevel++;

            stats.setOdunculukLevel(uuid, 1);
            stats.setOdunculukRequiredExp(uuid, odunculukLevel * 60);
            stats.setOdunculukExp(uuid, -odunculukExp);
            notification.send(player);
        }

    }
    public void giveFarmingExp(Player player, ItemStack item) {
        ToastNotification notification = new ToastNotification(Material.WHEAT,
                ChatColor.YELLOW + "??ift??ilikte ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);
        UUID uuid = player.getUniqueId();
        int farmingLevel = stats.getFarmingLevel(uuid);
        int farmingExp = stats.getFarmingExp(uuid);
        int farmingRequiredExp = stats.getFarmingRequiredExp(uuid);
        FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.AQUA + "Moon Network");
        if (ExpList.getExpMap().containsKey(item.getType().toString())) {

            int exp = ExpList.getExpMap().get(item.getType().toString());
            stats.setFarmingExp(uuid, exp);
            stats.setExp(uuid, exp - 1);
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                NamespacedKey key = new NamespacedKey(plugin, "Enchants");
                if (meta.getPersistentDataContainer() != null) {

                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if (container.has(key, Enchants)) {
                        if (container.get(key, Enchants).containsKey("ustal??k")) {
                            int level = container.get(key, Enchants).get("ustal??k");
                            stats.setMadencilikExp(player.getUniqueId(), level);
                        }
                    }
                }
            }
        }

        int prog = 100 * farmingExp / farmingRequiredExp;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        double para = ekonomi.getBalance(player);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "         Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "??ift??ilik " + ChatColor.WHITE + "Ustal??k " + farmingLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + farmingExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + farmingRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + player.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );
        if (farmingExp >= farmingRequiredExp) {
            stats.setDoygunluk(player, player.getUniqueId(), 1);
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            int Level = stats.getLevel(uuid);
            stats.setExp(uuid, farmingLevel * 2);
            farmingLevel++;
            stats.setFarmingLevel(uuid, 1);
            stats.setFarmingRequiredExp(uuid, farmingLevel * 60);
            stats.setFarmingExp(uuid, -farmingExp);
            notification.send(player);
        }

    }
    public void giveFarmingExp(Player player, Block block) {
        ToastNotification notification = new ToastNotification(Material.WHEAT,
                ChatColor.YELLOW + "??ift??ilikte ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);
        UUID uuid = player.getUniqueId();
        int farmingLevel = stats.getFarmingLevel(uuid);
        int farmingExp = stats.getFarmingExp(uuid);
        int farmingRequiredExp = stats.getFarmingRequiredExp(uuid);
        FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.AQUA + "Moon Network");
        if (ExpList.getExpMap().containsKey(block.getType().toString())) {

            int exp = ExpList.getExpMap().get(block.getType().toString());
            stats.setFarmingExp(uuid, exp);
            stats.setExp(uuid, exp - 1);
        }

        int prog = 100 * farmingExp / farmingRequiredExp;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        double para = ekonomi.getBalance(player);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "         Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "??ift??ilik " + ChatColor.WHITE + "Ustal??k " + farmingLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + farmingExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + farmingRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + player.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );
        if (farmingExp >= farmingRequiredExp) {
            stats.setDoygunluk(player, player.getUniqueId(), 1);
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            int Level = stats.getLevel(uuid);
            stats.setExp(uuid, farmingLevel * 2);
            farmingLevel++;
            stats.setFarmingLevel(uuid, 1);
            stats.setFarmingRequiredExp(uuid, farmingLevel * 60);
            stats.setFarmingExp(uuid, -farmingExp);
            notification.send(player);
        }

    }

    public void giveEnchantExp(Player player, int aga) {
        ToastNotification notification = new ToastNotification(Material.ENCHANTING_TABLE,
                ChatColor.YELLOW + "B??y??c??l??kte ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);
        UUID uuid = player.getUniqueId();
        int miningLevel = stats.getEnchLevel(uuid);
        int miningExp = stats.getEnchExp(uuid);
        int miningRequiredExp = stats.getEnchRequiredExp(uuid);
        FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.AQUA + "Moon Network");


            stats.setEnchExp(uuid, aga*10);
            stats.setExp(uuid, aga*10 - 5*aga);


        int prog = 100 * miningExp / miningRequiredExp;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        double para = ekonomi.getBalance(player);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "        Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "B??y??c??l??k " + ChatColor.WHITE + "Ustal??k " + miningLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + miningExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + miningRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + player.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );
        if (miningExp >= miningRequiredExp) {
            stats.set??lham(player.getUniqueId(), 1);
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            int Level = stats.getLevel(uuid);
            stats.setExp(uuid, miningLevel * 2);

            miningLevel++;
            stats.setMadencilikLevel(uuid, 1);
            stats.setMadencilikRequiredExp(uuid, miningLevel * 60);
            stats.setMadencilikExp(uuid, -miningExp);
            notification.send(player);
        }

    }

    public void giveMiningExp(Player player, Block block) {
        ToastNotification notification = new ToastNotification(Material.IRON_PICKAXE,
                ChatColor.YELLOW + "Madencilikte ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);
        UUID uuid = player.getUniqueId();
        int miningLevel = stats.getMadencilikLevel(uuid);
        int miningExp = stats.getMadencilikExp(uuid);
        int miningRequiredExp = stats.getMadencilikRequiredExp(uuid);
        FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.AQUA + "Moon Network");
        if (ExpList.getExpMap().containsKey(block.getType().toString())) {
            int exp = ExpList.getExpMap().get(block.getType().toString());
            stats.setMadencilikExp(uuid, exp);
            stats.setExp(uuid, exp - 1);
            if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                NamespacedKey key = new NamespacedKey(plugin, "Enchants");
                if (meta.getPersistentDataContainer() != null) {

                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if (container.has(key, Enchants)) {
                        if (container.get(key, Enchants).containsKey("ustal??k")) {
                            int level = container.get(key, Enchants).get("ustal??k");
                            stats.setMadencilikExp(player.getUniqueId(), level);
                        }
                    }
                }
            }
        }



        int prog = 100 * miningExp / miningRequiredExp;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        double para = ekonomi.getBalance(player);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "        Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "Madencilik " + ChatColor.WHITE + "Ustal??k " + miningLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + miningExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + miningRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + player.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );
        if (miningExp >= miningRequiredExp) {
            stats.setDirenc(player.getUniqueId(), 1);
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            int Level = stats.getLevel(uuid);
            stats.setExp(uuid, miningLevel * 2);

            miningLevel++;
            stats.setMadencilikLevel(uuid, 1);
            stats.setMadencilikRequiredExp(uuid, miningLevel * 60);
            stats.setMadencilikExp(uuid, -miningExp);
            notification.send(player);
        }

    }

    public void giveBrewEXP(Player player, Material mat) {
        ToastNotification notification = new ToastNotification(Material.BREWING_STAND,
                ChatColor.YELLOW + "Simyac??l??kta ustal??????n y??kseldi!", AdvancementFrame.CHALLENGE);
        UUID uuid = player.getUniqueId();
        int alchemyLevel = stats.getAlchemyLevel(uuid);
        int alchemyExp = stats.getAlchemyExp(uuid);
        int alchemyRequiredExp = stats.getAlchemyRequiredExp(uuid);
        FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.AQUA + "Moon Network");
        if (ExpList.getExpMap().containsKey(mat.toString())) {
            int exp = ExpList.getExpMap().get(mat.toString());
            stats.setAlchemyExp(uuid, exp);
            stats.setExp(uuid, exp - 1);
        }

        int prog = 100 * alchemyExp / alchemyRequiredExp;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());

        double para = ekonomi.getBalance(player);
        board.updateLines(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "        Remiel", " ",
                ChatColor.GOLD + "Dinar ???" + ChatColor.WHITE + df.format(para), "   ",

                ChatColor.YELLOW + "Simyac??l??k " + ChatColor.WHITE + "Ustal??k " + alchemyLevel,
                ChatColor.GREEN + "EXP " + ChatColor.WHITE + alchemyExp + ChatColor.GRAY + "/" + ChatColor.WHITE
                        + alchemyRequiredExp + ChatColor.GRAY + " (" + ChatColor.GREEN + "%" + prog + ChatColor.GRAY
                        + ")",
                "     ", ChatColor.GRAY + player.getName(), ChatColor.DARK_GRAY + formatter.format(date),
                ChatColor.AQUA + "mc.moonnw.xyz"

        );

        if (alchemyExp >= alchemyRequiredExp) {
            int Exp = stats.getExp(uuid);
            int RequiredExp = stats.getRequiredExp(uuid);
            int Level = stats.getLevel(uuid);
            stats.setExp(uuid, alchemyLevel * 2);

            alchemyLevel++;
            stats.setAlchemyLevel(uuid, 1);
            stats.setAlchemyRequiredExp(uuid, alchemyLevel * 60);
            stats.setAlchemyExp(uuid, -alchemyExp);
            notification.send(player);

        }

    }

}
