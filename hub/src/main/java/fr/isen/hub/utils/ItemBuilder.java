package fr.isen.hub.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder name(String name) {
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(name)
                .decoration(TextDecoration.ITALIC, false);
        this.meta.displayName(component);
        return this;
    }

    public ItemBuilder name(Component name) {
        this.meta.displayName(name.decoration(TextDecoration.ITALIC, false));
        return this;
    }

    public ItemBuilder lore(String... lines) {
        List<Component> lore = this.meta.hasLore() ? this.meta.lore() : new ArrayList<>();

        for (String line : lines) {
            lore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(line)
                    .decoration(TextDecoration.ITALIC, false));
        }

        this.meta.lore(lore);
        return this;
    }

    public ItemBuilder lore(List<String> lines) {
        return lore(lines.toArray(new String[0]));
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder durability(int damage) {
        if (this.meta instanceof Damageable damageable) {
            damageable.setDamage(damage);
        }
        return this;
    }

    public ItemBuilder leatherColor(Color color) {
        if (this.meta instanceof LeatherArmorMeta leatherMeta) {
            leatherMeta.setColor(color);
        }
        return this;
    }

    public ItemBuilder modelData(int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        this.meta.addEnchant(enchant, level, true);
        return this;
    }

    public ItemBuilder storedEnchant(Enchantment enchant, int level) {
        if (this.meta instanceof EnchantmentStorageMeta storageMeta) {
            storageMeta.addStoredEnchant(enchant, level, true);
        }
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder hideAllAttributes() {
        this.meta.addItemFlags(ItemFlag.values());
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder glow() {
        this.meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder skullOwner(OfflinePlayer player) {
        if (this.meta instanceof SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(player);
        }
        return this;
    }

    public ItemBuilder storeString(JavaPlugin plugin, String key, String value) {
        NamespacedKey nsKey = new NamespacedKey(plugin, key);
        meta.getPersistentDataContainer().set(nsKey, PersistentDataType.STRING, value);
        return this;
    }

    public <T extends ItemMeta> ItemBuilder applyMeta(Class<T> metaClass, Consumer<T> consumer) {
        if (metaClass.isInstance(this.meta)) {
            consumer.accept(metaClass.cast(this.meta));
        }
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }
}