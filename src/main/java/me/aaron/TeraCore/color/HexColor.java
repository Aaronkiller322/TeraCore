package me.aaron.TeraCore.color;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class HexColor {
	public static final char COLOR_CHAR = '§';
	public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
	public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-ORX]");

	private static final Map<Character, HexColor> BY_CHAR = new HashMap<>();
	private static final Map<String, HexColor> BY_NAME = new HashMap<>();

	public static final HexColor BLACK = new HexColor('0', "black", new Color(0));
	public static final HexColor DARK_BLUE = new HexColor('1', "dark_blue", new Color(170));
	public static final HexColor DARK_GREEN = new HexColor('2', "dark_green", new Color(43520));
	public static final HexColor DARK_AQUA = new HexColor('3', "dark_aqua", new Color(43690));
	public static final HexColor DARK_RED = new HexColor('4', "dark_red", new Color(11141120));
	public static final HexColor DARK_PURPLE = new HexColor('5', "dark_purple", new Color(11141290));
	public static final HexColor GOLD = new HexColor('6', "gold", new Color(16755200));
	public static final HexColor GRAY = new HexColor('7', "gray", new Color(11184810));
	public static final HexColor DARK_GRAY = new HexColor('8', "dark_gray", new Color(5592405));
	public static final HexColor BLUE = new HexColor('9', "blue", new Color(5592575));
	public static final HexColor GREEN = new HexColor('a', "green", new Color(5635925));
	public static final HexColor AQUA = new HexColor('b', "aqua", new Color(5636095));
	public static final HexColor RED = new HexColor('c', "red", new Color(16733525));
	public static final HexColor LIGHT_PURPLE = new HexColor('d', "light_purple", new Color(16733695));
	public static final HexColor YELLOW = new HexColor('e', "yellow", new Color(16777045));
	public static final HexColor WHITE = new HexColor('f', "white", new Color(16777215));
	public static final HexColor MAGIC = new HexColor('k', "obfuscated");
	public static final HexColor BOLD = new HexColor('l', "bold");
	public static final HexColor STRIKETHROUGH = new HexColor('m', "strikethrough");
	public static final HexColor UNDERLINE = new HexColor('n', "underline");
	public static final HexColor ITALIC = new HexColor('o', "italic");
	public static final HexColor RESET = new HexColor('r', "reset");

	private static int count = 0;
	private final String toString;
	private final String name;
	private final int ordinal;
	private final Color color;

	public String getName() {
		return this.name;
	}

	public Color getColor() {
		return this.color;
	}

	private HexColor(char code, String name) {
		this(code, name, (Color) null);
	}

	private HexColor(char code, String name, Color color) {
		this.name = name;
		this.toString = new String(new char[] { '§', code });
		this.ordinal = count++;
		this.color = color;

		BY_CHAR.put(Character.valueOf(code), this);
		BY_NAME.put(name.toUpperCase(Locale.ROOT), this);
	}

	private HexColor(String name, String toString, int rgb) {
		this.name = name;
		this.toString = toString;
		this.ordinal = -1;
		this.color = new Color(rgb);
	}

	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(this.toString);
		return hash;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		HexColor other = (HexColor) obj;
		return Objects.equals(this.toString, other.toString);
	}

	public String toString() {
		return this.toString;
	}

	public static String stripColor(String input) {
		if (input == null)
			return null;
		return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
				b[i] = '§';
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		return new String(b);
	}

	public static HexColor getByChar(char code) {
		return BY_CHAR.get(Character.valueOf(code));
	}

	public static HexColor of(Color color) {
		return of("#" + String.format("%08x", new Object[] { Integer.valueOf(color.getRGB()) }).substring(2));
	}

	public static HexColor of(String string) {
		Preconditions.checkArgument((string != null), "string cannot be null");
		if (string.startsWith("#") && string.length() == 7) {
			int rgb;
			try {
				rgb = Integer.parseInt(string.substring(1), 16);
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Illegal hex string " + string);
			}
			StringBuilder magic = new StringBuilder("§x");
			for (char c : string.substring(1).toCharArray()) {
				magic.append('§').append(c);
			}
			return new HexColor(string, magic.toString(), rgb);
		}
		HexColor defined = BY_NAME.get(string.toUpperCase(Locale.ROOT));
		if (defined != null)
			return defined;
		throw new IllegalArgumentException("Could not parse ChatColor " + string);
	}

	@Deprecated
	public static HexColor valueOf(String name) {
		Preconditions.checkNotNull(name, "Name is null");
		HexColor defined = BY_NAME.get(name);
		Preconditions.checkArgument((defined != null), "No enum constant " + HexColor.class.getName() + "." + name);
		return defined;
	}

	@Deprecated
	public static HexColor[] values() {
		return (HexColor[]) BY_CHAR.values().toArray((Object[]) new HexColor[BY_CHAR.values().size()]);
	}

	@Deprecated
	public String name() {
		return getName().toUpperCase(Locale.ROOT);
	}

	@Deprecated
	public int ordinal() {
		Preconditions.checkArgument((this.ordinal >= 0), "Cannot get ordinal of hex color");
		return this.ordinal;
	}
}