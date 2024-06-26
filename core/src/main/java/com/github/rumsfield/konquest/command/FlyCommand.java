package com.github.rumsfield.konquest.command;

import com.github.rumsfield.konquest.Konquest;
import com.github.rumsfield.konquest.model.KonPlayer;
import com.github.rumsfield.konquest.model.KonTerritory;
import com.github.rumsfield.konquest.utility.ChatUtil;
import com.github.rumsfield.konquest.utility.MessagePath;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FlyCommand extends CommandBase {

	public FlyCommand(Konquest konquest, CommandSender sender, String[] args) {
        super(konquest, sender, args);
    }
	
	public void execute() {
		// k fly
		Player bukkitPlayer = (Player) getSender();
		if (getArgs().length != 1) {
			sendInvalidArgMessage(bukkitPlayer,CommandType.FLY);
		} else {
        	if(!getKonquest().getPlayerManager().isOnlinePlayer(bukkitPlayer)) {
    			ChatUtil.printDebug("Failed to find non-existent player");
    			ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_INTERNAL.getMessage());
    			return;
    		}
        	KonPlayer player = getKonquest().getPlayerManager().getPlayer(bukkitPlayer);
        	
        	if(bukkitPlayer.getGameMode().equals(GameMode.SURVIVAL)) {
        		if(player.isFlyEnabled()) {
        			player.setIsFlyEnabled(false);
        			ChatUtil.sendNotice(bukkitPlayer, MessagePath.GENERIC_NOTICE_DISABLE_AUTO.getMessage());
        		} else {
        			// Verify player is in friendly territory
        			boolean isFriendly = false;
        			if(getKonquest().getTerritoryManager().isChunkClaimed(bukkitPlayer.getLocation())) {
        				KonTerritory territory = getKonquest().getTerritoryManager().getChunkTerritory(bukkitPlayer.getLocation());
        				if(territory.getKingdom().equals(player.getKingdom())) {
        					isFriendly = true;
        				}
        			}
        			if(isFriendly) {
        				player.setIsFlyEnabled(true);
        				player.setFlyDisableWarmup(false);
            			ChatUtil.sendNotice(bukkitPlayer, MessagePath.GENERIC_NOTICE_ENABLE_AUTO.getMessage());
        			} else {
        				ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_NO_ALLOW.getMessage());
        			}
        		}
        	} else {
        		ChatUtil.sendError(bukkitPlayer, MessagePath.GENERIC_ERROR_NO_ALLOW.getMessage());
        	}
        }
		
	}
	
	@Override
	public List<String> tabComplete() {
		// No arguments to complete
		return Collections.emptyList();
	}
	
}
