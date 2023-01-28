package com.github.rumsfield.konquest.utility;

import com.github.rumsfield.konquest.Konquest;
import com.github.rumsfield.konquest.model.KonPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatUtil {

	private static final ChatColor broadcastColor = ChatColor.LIGHT_PURPLE;
	private static final ChatColor noticeColor = ChatColor.GRAY;
	private static final ChatColor errorColor = ChatColor.RED;
	private static final ChatColor alertColor = ChatColor.GOLD;

	/**
	 * Formats hex color codes, written by user zwrumpy
	 * <a href="https://www.spigotmc.org/threads/hex-color-code-translate.449748/#post-4270781">...</a>
	 * @param message The message to format
	 * @return The formatted message
	 */
	public static String parseHex(String message) {
		Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
           
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }
           
            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	/**
	 * Parse a string color code into usable color
	 * @param input - The code to be parsed
	 * @return ChatColor enum code, null if invalid code
	 */
	public static ChatColor parseColorCode(String input) {
		ChatColor result = null;
		try {
			result = ChatColor.valueOf(input);
		} catch (Exception ignored) {}
		return result;
	}
	
	/**
	 * Search base string and replace
	 * 		%PREFIX% with prefix arg
	 * 		%SUFFIX% with suffix arg
	 * 		%KINGDOM% with kingdom arg
	 * 		%TITLE% with title arg
	 * 		%NAME% with name arg
	 * @param base Base format that may or may not contain %PREFIX%, %SUFFIX%, %KINGDOM%, %TITLE% or %NAME%.
	 * @param kingdom Kingdom name to replace %KINGDOM% with
	 * @param title Title to replace %TITLE% with
	 * @param name Name to replace %NAME% with
	 * @return Formatted string
	 */
	public static String parseFormat(String base, String prefix, String suffix, String kingdom, String title, String name, ChatColor teamColor, ChatColor titleColor, boolean formatName, boolean formatKingdom) {
		String message = base;
		if(prefix.equals("")) {
			message = message.replace("%PREFIX% ", "");
			message = message.replace("%PREFIX%", "");
		} else {
			message = message.replace("%PREFIX%", prefix);
		}
		if(suffix.equals("")) {
			message = message.replace("%SUFFIX% ", "");
			message = message.replace("%SUFFIX%", "");
		} else {
			message = message.replace("%SUFFIX%", suffix);
		}
		if(kingdom.equals("")) {
			message = message.replace("%KINGDOM% ", "");
			message = message.replace("%KINGDOM%", "");
		} else {
			if(formatKingdom) {
				message = message.replace("%KINGDOM%", teamColor+kingdom);
			} else {
				message = message.replace("%KINGDOM%", kingdom);
			}
		}
		if(title.equals("")) {
			message = message.replace("%TITLE% ", "");
			message = message.replace("%TITLE%", "");
		} else {
			message = message.replace("%TITLE%", titleColor+title);
		}
		if(name.equals("")) {
			message = message.replace("%NAME% ", "");
			message = message.replace("%NAME%", "");
		} else {
			if(formatName) {
				message = message.replace("%NAME%", teamColor+name);
			} else {
				message = message.replace("%NAME%", name);
			}
		}
		return message;
	}
	
	public static Color lookupColor(ChatColor reference) {
		Color result = Color.WHITE;
		switch(reference) {
			case BLACK:
				result = Color.fromRGB(0x000000);
				break;
			case DARK_BLUE:
				result = Color.fromRGB(0x0000AA);
				break;
			case DARK_GREEN:
				result = Color.fromRGB(0x00AA00);
				break;
			case DARK_AQUA:
				result = Color.fromRGB(0x00AAAA);
				break;
			case DARK_RED:
				result = Color.fromRGB(0xAA0000);
				break;
			case DARK_PURPLE:
				result = Color.fromRGB(0xAA00AA);
				break;
			case GOLD:
				result = Color.fromRGB(0xFFAA00);
				break;
			case GRAY:
				result = Color.fromRGB(0xAAAAAA);
				break;
			case DARK_GRAY:
				result = Color.fromRGB(0x555555);
				break;
			case BLUE:
				result = Color.fromRGB(0x5555FF);
				break;
			case GREEN:
				result = Color.fromRGB(0x55FF55);
				break;
			case AQUA:
				result = Color.fromRGB(0x55FFFF);
				break;
			case RED:
				result = Color.fromRGB(0xFF5555);
				break;
			case LIGHT_PURPLE:
				result = Color.fromRGB(0xFF55FF);
				break;
			case YELLOW:
				result = Color.fromRGB(0xFFFF55);
				break;
			case WHITE:
				result = Color.fromRGB(0xFFFFFF);
				break;
			default:
				break;
		}
		return result;
	}
	
	public static BarColor mapBarColor(ChatColor reference) {
		BarColor result = BarColor.WHITE;
		switch(reference) {
			case YELLOW:
			case GOLD:
				result = BarColor.YELLOW;
				break;
			case RED:
			case DARK_RED:
				result = BarColor.RED;
				break;
			case BLUE:
			case DARK_BLUE:
			case DARK_AQUA:
			case AQUA:
				result = BarColor.BLUE;
				break;
			case GREEN:
			case DARK_GREEN:
				result = BarColor.GREEN;
				break;
			case BLACK:
			case DARK_PURPLE:
				result = BarColor.PURPLE;
				break;
			case LIGHT_PURPLE:
				result = BarColor.PINK;
				break;
			default:
				break;
		}
		return result;
	}
	
	public static void printDebug(String message) {
		if(Konquest.getInstance().getCore().getBoolean(CorePath.DEBUG.getPath())) {
        	Bukkit.getServer().getConsoleSender().sendMessage("[Konquest DEBUG] " + message);
        }
	}
	
	public static void printConsole(String message) {
		Bukkit.getServer().getConsoleSender().sendMessage("[Konquest] " + message);
	}
	
	public static void printConsoleAlert(String message) {
		String alert = alertColor + "[Konquest] " + message;
		Bukkit.getServer().getConsoleSender().sendMessage(alert);
	}
	
	public static void printConsoleError(String message) {
		String error = errorColor + "[Konquest ERROR] " + message;
		Bukkit.getServer().getConsoleSender().sendMessage(error);
	}

	public static void sendNotice(Player player, String message) {
		String notice = Konquest.getChatTag() + noticeColor + message;
		player.sendMessage(notice);
	}
	
	public static void sendNotice(Player player, String message, ChatColor color) {
		String notice = Konquest.getChatTag() + color + message;
		player.sendMessage(notice);
	}
	
	public static void sendMessage(Player player, String message) {
		player.sendMessage(message);
	}
	
	public static void sendMessage(Player player, String message, ChatColor color) {
		String notice = color + message;
		player.sendMessage(notice);
	}

	public static void sendError(Player player, String message) {
		String error = Konquest.getChatTag() + errorColor + message;
		player.sendMessage(error);
	}
	
	public static void sendBroadcast(String message) {
		String notice = Konquest.getChatTag() + broadcastColor + message;
		Bukkit.broadcastMessage(notice);
	}
	
	public static void sendAdminBroadcast(String message) {
		String notice = Konquest.getChatTag() + broadcastColor + message;
		Bukkit.broadcast(notice,"konquest.command.admin");
	}
	
	public static void sendKonTitle(KonPlayer player, String title, String subtitle) {
		if(title.equals("")) {
			title = " ";
		}
		if(!player.isAdminBypassActive() && !player.isPriorityTitleDisplay()) {
			player.getBukkitPlayer().sendTitle(title, subtitle, 10, 40, 10);
		}
	}
	
	public static void sendKonTitle(KonPlayer player, String title, String subtitle, int duration) {
		if(title.equals("")) {
			title = " ";
		}
		if(!player.isAdminBypassActive() && !player.isPriorityTitleDisplay()) {
			player.getBukkitPlayer().sendTitle(title, subtitle, 10, duration, 10);
		}
	}
	
	public static void sendKonPriorityTitle(KonPlayer player, String title, String subtitle, int durationTicks, int fadeInTicks, int fadeOutTicks) {
		if(title.equals("")) {
			title = " ";
		}
		if(!player.isAdminBypassActive() && !player.isPriorityTitleDisplay()) {
			int totalDuration = (durationTicks+fadeInTicks+fadeOutTicks)/20;
			if(totalDuration < 1) {
				totalDuration = 1;
			}
			player.setIsPriorityTitleDisplay(true);
			Timer priorityTitleDisplayTimer = player.getPriorityTitleDisplayTimer();
			priorityTitleDisplayTimer.stopTimer();
			priorityTitleDisplayTimer.setTime(totalDuration);
			priorityTitleDisplayTimer.startTimer();
			player.getBukkitPlayer().sendTitle(title, subtitle, fadeInTicks, durationTicks, fadeOutTicks);
		}
	}

	public static void sendConstantTitle(Player player, String title, String subtitle) {
		if(title.equals("")) {
			title = " ";
		}
		player.sendTitle(title, subtitle, 1, 9999999, 1);
	}
	
	public static void resetTitle(Player player) {
		player.resetTitle();
		player.sendTitle("", "", 1, 1, 1);
	}
	
	
}