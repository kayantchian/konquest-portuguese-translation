package konquest.api.model;

import org.bukkit.Material;

import konquest.utility.MessagePath;

/**
 * An upgrade for a town.
 * Upgrades have a cost in favor, and a population requirement.
 * Upgrades can have multiple levels.
 * 
 * @author Rumsfield
 *
 */
public enum KonquestUpgrade {

	LOOT 		(3, Material.GOLD_INGOT, 				MessagePath.UPGRADE_LOOT_NAME.getMessage(), 	new String[] {MessagePath.UPGRADE_LOOT_LEVEL_1.getMessage(), MessagePath.UPGRADE_LOOT_LEVEL_2.getMessage(), MessagePath.UPGRADE_LOOT_LEVEL_3.getMessage()}),
	DROPS		(1, Material.LEATHER, 					MessagePath.UPGRADE_DROPS_NAME.getMessage(), 	new String[] {MessagePath.UPGRADE_DROPS_LEVEL_1.getMessage()}),
	FATIGUE		(1, Material.DIAMOND_PICKAXE, 			MessagePath.UPGRADE_FATIGUE_NAME.getMessage(), 	new String[] {MessagePath.UPGRADE_FATIGUE_LEVEL_1.getMessage()}),
	COUNTER		(2, Material.COMPASS, 					MessagePath.UPGRADE_COUNTER_NAME.getMessage(), 	new String[] {MessagePath.UPGRADE_COUNTER_LEVEL_1.getMessage(), MessagePath.UPGRADE_COUNTER_LEVEL_2.getMessage()}),
	HEALTH		(3, Material.ENCHANTED_GOLDEN_APPLE, 	MessagePath.UPGRADE_HEALTH_NAME.getMessage(), 	new String[] {MessagePath.UPGRADE_HEALTH_LEVEL_1.getMessage(), MessagePath.UPGRADE_HEALTH_LEVEL_2.getMessage(), MessagePath.UPGRADE_HEALTH_LEVEL_3.getMessage()}),
	DAMAGE		(2, Material.TNT, 						MessagePath.UPGRADE_DAMAGE_NAME.getMessage(), 	new String[] {MessagePath.UPGRADE_DAMAGE_LEVEL_1.getMessage(), MessagePath.UPGRADE_DAMAGE_LEVEL_2.getMessage()}),
	WATCH 		(3, Material.PLAYER_HEAD, 				MessagePath.UPGRADE_WATCH_NAME.getMessage(), 	new String[] {MessagePath.UPGRADE_WATCH_LEVEL_1.getMessage(), MessagePath.UPGRADE_WATCH_LEVEL_2.getMessage(), MessagePath.UPGRADE_WATCH_LEVEL_3.getMessage()}),
	ENCHANT		(1, Material.ENCHANTED_BOOK, 			MessagePath.UPGRADE_ENCHANT_NAME.getMessage(), 	new String[] {MessagePath.UPGRADE_ENCHANT_LEVEL_1.getMessage()});

	private final int levels;
	private final Material icon;
	private final String description;
	private final String[] levelDescriptions;
	
	KonquestUpgrade(int levels, Material icon, String description, String[] levelDescriptions) {
		this.levels = levels;
		this.icon = icon;
		this.description = description;
		this.levelDescriptions = levelDescriptions;
	}
	
	/**
	 * Gets the maximum levels for this upgrade.
	 * 
	 * @return The maximum levels
	 */
	public int getMaxLevel() {
		return levels;
	}
	
	/**
	 * Gets the material used for menu icons.
	 * 
	 * @return The material
	 */
	public Material getIcon() {
		return icon;
	}
	
	/**
	 * Gets the description of this upgrade.
	 * 
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the level description, starting at 1. 
	 * Level 0 returns empty string.
	 * Levels higher than the max level returns an empty string.
	 * 
	 * @param level The level
	 * @return The level description
	 */
	public String getLevelDescription(int level) {
		String desc = "";
		if(level > 0 && level <= levelDescriptions.length) {
			desc = levelDescriptions[level-1];
		}
		return desc;
	}
	
	/**
	 * Get the corresponding KonquestUpgrade enum that matches the given name.
	 * Returns null when the name does not match any enum.
	 * 
	 * @param name The name of the enum
	 * @return The KonquestUpgrade enum that matches name
	 */
	public static KonquestUpgrade getUpgrade(String name) {
		KonquestUpgrade result = null;
		for(KonquestUpgrade upgrade : KonquestUpgrade.values()) {
			if(upgrade.toString().equalsIgnoreCase(name)) {
				result = upgrade;
			}
		}
		return result;
	}
	
}