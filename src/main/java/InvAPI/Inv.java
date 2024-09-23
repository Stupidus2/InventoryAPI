package InvAPI;

import InvAPI.Code;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Inv {
    public Inventory inv;
    private HashMap<ItemStack, Code> listeners = new HashMap<>();


    public Inv(String name, Integer reihen, JavaPlugin plugin) {
        if (reihen > 6) {
            reihen = 6;
        }
        inv = Bukkit.createInventory(null, reihen * 9, name);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void e(InventoryClickEvent e) {
                if (e.getClickedInventory() != null && e.getClickedInventory().equals(inv)) {
                    if (e.getCurrentItem() != null) {
                        ItemStack found = null;
                        for (ItemStack item : listeners.keySet()) {
                            if (e.getCurrentItem().equals(item)) {
                                found = item;
                                break;
                            }
                        }
                        if (found != null) {
                            listeners.get(found).onEvent(e);
                        }
                    }
                }
            }
        }, plugin);
    }
    public void openInv(Player player) {
        player.openInventory(inv);
    }

    public void setItem(Integer invzahl, ItemStack item, Code code) {
        inv.setItem(invzahl, item);

        if (code != null) {
            listeners.put(item, code);
        }
    }

    public void createItem(Material material, String name, Integer invzahl, List<String> lore, Code code) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invzahl, item);

        if (code != null) {
            listeners.put(item, code);
        }
    }
    public void createItemEnchant(Material material, String name, Integer invzahl, List<String> lore, Code code) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(Enchantment.LUCK,1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invzahl, item);

        if (code != null) {
            listeners.put(item, code);
        }
    }

    public void createSkull(String name, Integer invzahl, List<String> lore,Player player, Code code) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(name);
        meta.addEnchant(Enchantment.LUCK,1,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);
        inv.setItem(invzahl, item);

        if (code != null) {
            listeners.put(item, code);
        }
    }
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");
    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }
    public static ItemStack getPlayerHead(String name,  List<String> lore, String headURL) {
        PlayerProfile profile = getProfile(headURL);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwnerProfile(profile);
        meta.setDisplayName(name);
        meta.setLore(lore);
        head.setItemMeta(meta);
        return head;
    }
    public static List<String> createLore(String lore) {
        return Arrays.asList(lore.split("\n"));
    }
    public Inventory getInv() {
        return inv;
    }
}