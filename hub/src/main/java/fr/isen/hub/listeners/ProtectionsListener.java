package fr.isen.hub.listeners;

import fr.isen.hub.managers.ProtectionsManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class ProtectionsListener implements Listener {

    private final ProtectionsManager manager;

    private static final Set<Material> INTERACTIVE_BLOCKS = Set.of(
            // Conteneurs
            Material.CHEST, Material.TRAPPED_CHEST, Material.ENDER_CHEST,
            Material.BARREL, Material.DROPPER, Material.DISPENSER, Material.HOPPER,
            Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER,
            // Shulker boxes (toutes couleurs)
            Material.SHULKER_BOX,
            Material.WHITE_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX,
            // Tables de crafting / utilitaires
            Material.CRAFTING_TABLE, Material.ENCHANTING_TABLE,
            Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL,
            Material.GRINDSTONE, Material.STONECUTTER, Material.LOOM,
            Material.CARTOGRAPHY_TABLE, Material.LECTERN,
            Material.COMPOSTER, Material.NOTE_BLOCK, Material.JUKEBOX,
            Material.BEEHIVE, Material.BEE_NEST,
            // Portes bois
            Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR,
            Material.CRIMSON_DOOR, Material.WARPED_DOOR, Material.MANGROVE_DOOR,
            Material.CHERRY_DOOR, Material.BAMBOO_DOOR,
            // Portes fer / cuivre (1.21)
            Material.IRON_DOOR,
            Material.COPPER_DOOR, Material.EXPOSED_COPPER_DOOR,
            Material.WEATHERED_COPPER_DOOR, Material.OXIDIZED_COPPER_DOOR,
            Material.WAXED_COPPER_DOOR, Material.WAXED_EXPOSED_COPPER_DOOR,
            Material.WAXED_WEATHERED_COPPER_DOOR, Material.WAXED_OXIDIZED_COPPER_DOOR,
            // Trappes bois
            Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.BIRCH_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR,
            Material.CRIMSON_TRAPDOOR, Material.WARPED_TRAPDOOR, Material.MANGROVE_TRAPDOOR,
            Material.CHERRY_TRAPDOOR, Material.BAMBOO_TRAPDOOR,
            // Trappes fer / cuivre (1.21)
            Material.IRON_TRAPDOOR,
            Material.COPPER_TRAPDOOR, Material.EXPOSED_COPPER_TRAPDOOR,
            Material.WEATHERED_COPPER_TRAPDOOR, Material.OXIDIZED_COPPER_TRAPDOOR,
            Material.WAXED_COPPER_TRAPDOOR, Material.WAXED_EXPOSED_COPPER_TRAPDOOR,
            Material.WAXED_WEATHERED_COPPER_TRAPDOOR, Material.WAXED_OXIDIZED_COPPER_TRAPDOOR,
            // Barrières
            Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.DARK_OAK_FENCE_GATE,
            Material.CRIMSON_FENCE_GATE, Material.WARPED_FENCE_GATE, Material.MANGROVE_FENCE_GATE,
            Material.CHERRY_FENCE_GATE, Material.BAMBOO_FENCE_GATE,
            // Interrupteurs
            Material.LEVER,
            Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON,
            Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON,
            Material.CRIMSON_BUTTON, Material.WARPED_BUTTON, Material.STONE_BUTTON,
            Material.POLISHED_BLACKSTONE_BUTTON
    );

    public ProtectionsListener(ProtectionsManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!manager.isBypass(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!manager.isBypass(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!manager.isBypass(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!INTERACTIVE_BLOCKS.contains(block.getType())) return;

        Player player = event.getPlayer();
        if (manager.isBypass(player)) return;

        event.setCancelled(true);
    }
}
