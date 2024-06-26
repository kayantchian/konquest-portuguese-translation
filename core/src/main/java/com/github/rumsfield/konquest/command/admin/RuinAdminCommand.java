package com.github.rumsfield.konquest.command.admin;

import com.github.rumsfield.konquest.Konquest;
import com.github.rumsfield.konquest.command.CommandBase;
import com.github.rumsfield.konquest.model.KonPlayer;
import com.github.rumsfield.konquest.model.KonPlayer.RegionType;
import com.github.rumsfield.konquest.utility.ChatUtil;
import com.github.rumsfield.konquest.utility.MessagePath;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuinAdminCommand extends CommandBase {

	public RuinAdminCommand(Konquest konquest, CommandSender sender, String[] args) {
        super(konquest, sender, args);
    }

	@Override
	public void execute() {
		// k admin ruin create|remove|rename|reset|criticals|spawns <name> [<name>]
		Player bukkitPlayer = (Player) getSender();
		if (getArgs().length != 4 && getArgs().length != 5) {
			sendInvalidArgMessage(bukkitPlayer, AdminCommandType.RUIN);
            return;
        }
		if(getKonquest().isWorldIgnored(bukkitPlayer.getWorld())) {
			ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_INVALID_WORLD.getMessage());
			return;
		}
		if(!getKonquest().getPlayerManager().isOnlinePlayer(bukkitPlayer)) {
			ChatUtil.printDebug("Failed to find non-existent player");
			ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_INTERNAL.getMessage());
			return;
		}
		KonPlayer player = getKonquest().getPlayerManager().getPlayer(bukkitPlayer);
		String cmdMode = getArgs()[2];
		String ruinName = getArgs()[3];
		
		if(cmdMode.equalsIgnoreCase("create")) {
			if (getArgs().length != 4) {
				sendInvalidArgMessage(bukkitPlayer, AdminCommandType.RUIN);
				return;
			}
			Location playerLoc = bukkitPlayer.getLocation();
			if(getKonquest().validateName(ruinName,bukkitPlayer) != 0) {
        		return;
        	}
        	boolean pass = getKonquest().getRuinManager().addRuin(playerLoc, ruinName);
        	if(!pass) {
        		ChatUtil.sendError(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_ERROR_CREATE.getMessage(ruinName));
			} else {
        		ChatUtil.sendNotice(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_NOTICE_CREATE.getMessage(ruinName));
        	}
		} else if(cmdMode.equalsIgnoreCase("remove")) {
			// Check for valid ruin
			if(!getKonquest().getRuinManager().isRuin(ruinName)) {
				ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_UNKNOWN_NAME.getMessage(ruinName));
                return;
			}
			boolean pass = getKonquest().getRuinManager().removeRuin(ruinName);
        	if(!pass) {
        		ChatUtil.sendError(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_ERROR_REMOVE.getMessage(ruinName));
			} else {
        		ChatUtil.sendNotice(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_NOTICE_REMOVE.getMessage(ruinName));
        	}
		} else if(cmdMode.equalsIgnoreCase("rename")) {
			if (getArgs().length == 5) {
				if(!getKonquest().getRuinManager().isRuin(ruinName)) {
					ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_UNKNOWN_NAME.getMessage(ruinName));
	                return;
				}
				String newName = getArgs()[4];
				if(getKonquest().validateName(newName,bukkitPlayer) != 0) {
	        		return;
	        	}
				boolean pass = getKonquest().getRuinManager().renameRuin(ruinName,newName);
				if(!pass) {
	        		ChatUtil.sendError(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_ERROR_RENAME.getMessage(ruinName,newName));
				} else {
	        		ChatUtil.sendNotice(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_NOTICE_RENAME.getMessage(ruinName,newName));
	        	}
			} else {
				sendInvalidArgMessage(bukkitPlayer, AdminCommandType.RUIN);
			}
		} else if(cmdMode.equalsIgnoreCase("reset")) {
			// Reset ruin (restore critical blocks and respawn golems)
			boolean pass = getKonquest().getRuinManager().resetRuin(ruinName);
			if(!pass) {
				ChatUtil.sendError(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_ERROR_RESET.getMessage(ruinName));
			} else {
				ChatUtil.sendNotice(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_NOTICE_RESET.getMessage(ruinName));
			}
		} else if(cmdMode.equalsIgnoreCase("criticals")) {
        	if(player.isSettingRegion()) {
        		ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_REGION.getMessage());
                return;
        	}
        	// Check for valid ruin
			if(!getKonquest().getRuinManager().isRuin(ruinName)) {
				ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_UNKNOWN_NAME.getMessage(ruinName));
                return;
			}
			getKonquest().getRuinManager().getRuin(ruinName).clearCriticalLocations();
			player.settingRegion(RegionType.RUIN_CRITICAL);
        	ChatUtil.sendNotice(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_NOTICE_CRITICALS.getMessage(ruinName), ChatColor.LIGHT_PURPLE);
        	ChatUtil.sendNotice(bukkitPlayer, MessagePath.GENERIC_NOTICE_CLICK_AIR.getMessage());
		} else if(cmdMode.equalsIgnoreCase("spawns")) {
        	if(player.isSettingRegion()) {
        		ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_REGION.getMessage());
                return;
        	}
        	// Check for valid ruin
			if(!getKonquest().getRuinManager().isRuin(ruinName)) {
				ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_UNKNOWN_NAME.getMessage(ruinName));
                return;
			}
			getKonquest().getRuinManager().getRuin(ruinName).clearSpawnLocations();
			player.settingRegion(RegionType.RUIN_SPAWN);
        	ChatUtil.sendNotice(bukkitPlayer, MessagePath.COMMAND_ADMIN_RUIN_NOTICE_SPAWNS.getMessage(ruinName), ChatColor.LIGHT_PURPLE);
        	ChatUtil.sendNotice(bukkitPlayer, MessagePath.GENERIC_NOTICE_CLICK_AIR.getMessage());
		} else {
			sendInvalidArgMessage(bukkitPlayer, AdminCommandType.RUIN);
		}
	}

	@Override
	public List<String> tabComplete() {
		// k admin ruin create|remove|rename|criticals|spawns <name> [<name>]
		List<String> tabList = new ArrayList<>();
		final List<String> matchedTabList = new ArrayList<>();
		if(getArgs().length == 3) {
			tabList.add("create");
			tabList.add("remove");
			tabList.add("rename");
			tabList.add("reset");
			tabList.add("criticals");
			tabList.add("spawns");
			// Trim down completion options based on current input
			StringUtil.copyPartialMatches(getArgs()[2], tabList, matchedTabList);
			Collections.sort(matchedTabList);
		} else if(getArgs().length == 4) {
			String subCmd = getArgs()[2];
			if(subCmd.equalsIgnoreCase("create")) {
				tabList.add("***");
			} else if(subCmd.equalsIgnoreCase("remove") || subCmd.equalsIgnoreCase("reset") || subCmd.equalsIgnoreCase("criticals") || subCmd.equalsIgnoreCase("spawns") || subCmd.equalsIgnoreCase("rename")) {
				tabList.addAll(getKonquest().getRuinManager().getRuinNames());
			}
			// Trim down completion options based on current input
			StringUtil.copyPartialMatches(getArgs()[3], tabList, matchedTabList);
			Collections.sort(matchedTabList);
		} else if(getArgs().length == 5) {
			String subCmd = getArgs()[2];
			if(subCmd.equalsIgnoreCase("rename")) {
				tabList.add("***");
			}
			// Trim down completion options based on current input
			StringUtil.copyPartialMatches(getArgs()[4], tabList, matchedTabList);
			Collections.sort(matchedTabList);
		}
		return matchedTabList;
	}
	
}
