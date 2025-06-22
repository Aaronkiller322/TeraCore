package me.aaron.TeraCore.color;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.aaron.TeraCore.Component.TextComponent;
import me.aaron.TeraCore.economy.Eco_Config;
import me.aaron.TeraCore.economy.Eco_Manager;
import me.aaron.TeraCore.main.TeraMain;
import me.aaron.TeraCore.util.UUIDFetcher;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class TeraHolder extends PlaceholderExpansion {


	Eco_Config eco_conf = new Eco_Config();
	    @Override
	    public String getAuthor() {
	        return "Aaronkiller322";
	    }
	    
	    @Override
	    public String getIdentifier() {
	        return "TeraCore";
	    }

	    @Override
	    public String getVersion() {
	        return "2.5.3";
	    }
	    
	    @Override
	    public String getRequiredPlugin() {
	        return "TeraCore";
	    }
	    
	    @Override
	    public boolean canRegister() {
	        return true;
	    }
	    @Override
	    public boolean persist() {
	        return true; 
	    }
	    @Override
	    public String onRequest(OfflinePlayer player, String params) {
	        if (params.contains("<gradient") || params.contains("<color") || params.contains("<hexcolor")) {
	        	try {
	        	String color = parseColorTags(params);
	            return color;
	        	}catch (Exception e) {
				}
	        }

	    	if(params.startsWith("hexcolor")) {
	    		//teracore_hexcolor_FF3030_Text ist cool_FF743D
	    		String[] split = params.split("_");
	    		String hex_1 = split[1];
	    		String text = split[2];
	    		String hex_2 = split[3];
	    		String color = HexColorTextGenerator.converHexcolorText(text, "#" + hex_1, "#" + hex_2);
	    		
				return color;
	    	}
	    	if(params.equalsIgnoreCase("prefix")) {
	    		return TeraMain.getPlugin().prefix;
	    	}
	    	if(params.equalsIgnoreCase("name")) {
	    		return TeraMain.getPlugin().name;
	    	}

	    	if(params.equalsIgnoreCase("money")) {
				if(new Eco_Config().enabled()) {
					try {
						return String.valueOf(new Eco_Manager(player.getUniqueId()).getMoney());
					} catch (Exception e) {
						return "Player not found";
					}
				}
	    	}
	    	if(params.equalsIgnoreCase("money_round")) {
				if(new Eco_Config().enabled()) {
					try {
						Eco_Manager manager = new Eco_Manager(player.getUniqueId());
						return manager.roundMoney(manager.getMoney());

					} catch (Exception e) {
						return "Player not found";
					}
				}
	    	}
	        return null;
	    
	}
	    
	    
	    private String parseColorTags(String params) {
	        StringBuilder result = new StringBuilder();
	        int index = 0;
	        while (index < params.length()) {
	            if (params.startsWith("<gradient:", index)) {
	                try {
	                    int start = params.indexOf("<gradient:", index) + 10;
	                    int end = params.indexOf(">", start);
	                    if (end == -1) {
	                        result.append("[Ungültiges Gradient-Tag]");
	                        break;
	                    }

	                    String gradientDefinition = params.substring(start, end);
	                    String[] gradientColors = gradientDefinition.split(":");
	                    if (gradientColors.length != 2) {
	                        result.append("[Ungültige Gradient-Farb-Syntax]");
	                        break;
	                    }

	                    String hexStart = "#" +gradientColors[0];
	                    String hexEnd = "#" +gradientColors[1];
	                    int textStart = end + 1;
	                    int textEnd = params.indexOf("</gradient>", textStart);
	                    if (textEnd == -1) {
	                        result.append("[Ungültiges Gradient-Tag]");
	                        break;
	                    }

	                    String text = params.substring(textStart, textEnd);
	                    String gradientText = HexColorTextGenerator.converHexcolorText(text, hexStart, hexEnd);
	                    result.append(gradientText);
	                    index = textEnd + 11;
	                } catch (Exception e) {
	                    result.append("[Fehler bei Gradient]");
	                    index++;
	                }
	            } else if (params.startsWith("<color:", index)) {
	                try {
	                    int start = params.indexOf("<color:", index) + 7;
	                    int end = params.indexOf(">", start);
	                    if (end == -1) {
	                        break;
	                    }
	                    String colorCode = params.substring(start, end);
	                    int textStart = end + 1;
	                    int textEnd = params.indexOf("</color>", textStart);
	                    if (textEnd == -1) {
	                        break;
	                    }

	                    String text = params.substring(textStart, textEnd);
	                    HexColor color = HexColor.of(colorCode);
						TextComponent message = new TextComponent(text);
						message.setColor(color);
	                    result.append(message.toLegacyText());
	                    index = textEnd + 8;
	                } catch (Exception e) {
	                    result.append("[Ungültige Color-Syntax]");
	                    index++;
	                }
	            } else if (params.startsWith("<hexcolor:", index)) {
	                try {
	                    int start = params.indexOf("<hexcolor:", index) + 10;
	                    int end = params.indexOf(">", start);
	                    if (end == -1) {
	                        break;
	                    }
	                    String colorCode = "#" + params.substring(start, end);

	                    int textStart = end + 1;
	                    int textEnd = params.indexOf("</hexcolor>", textStart);
	                    if (textEnd == -1) {
	                        break;
	                    }
	                    String text = params.substring(textStart, textEnd);
	                    String coloredText = HexColorTextGenerator.converHexcolorText(text, colorCode, colorCode);
	                    result.append(coloredText);
	                    index = textEnd + 11;
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    result.append("[Ungültige Color-Syntax]");
	                    index++;
	                }
		            }else {
	                result.append(params.charAt(index));
	                index++;
	            }
	        }
	        return result.toString();
	    }
}