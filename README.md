# TeraCore

**TeraCore** ist ein leistungsstarker Essentials-Ersatz für Minecraft-Server mit Fokus auf Modularität, Benutzerfreundlichkeit und modernen Features.  
Es erlaubt das Aktivieren/Deaktivieren aller Funktionen wie Commands, Events, Messages und Permissions.  
Außerdem bietet TeraCore:

- Einen **flexiblen Prefix-Manager**
- **HexColor-Support** (Hex-Farben in Texten)
- Eigene **Placeholder** zur individuellen Gestaltung von Nachrichten

---

## ✅ Beispielcode

```java
public void Test(Player player) {

    String text = "%teracore_prefix% &eTestnachricht mit Info: ";
    String klick = "&6[Klick]";
    String hover = "#FF4511 Coole Hexfarbe \n &cKlicke, um den Text zu kopieren";
    String copy = "Coole Nachricht, die kopiert wurde";

    TeraText teraText = new TeraText(text);
    TeraHoverText teraHoverText = new TeraHoverText(klick);
    teraHoverText.setHoverText(hover);
    teraHoverText.setCopyText(copy);

    teraText.addHoverText(teraHoverText);
    teraText.addText("&e!");

    teraText.sendMessage(player);
}
```

---

## 🎯 Placeholder-Tabelle

| Placeholder         | Beschreibung                                     |
|-----------------------|----------------------------------------------------|
| `%teracore_prefix%`   | Gibt den Prefix mit Spliter zurück (z. B. `Tera ›`) |
| `%teracore_name%`     | Gibt den Prefix ohne Spliter zurück (z. B. `Tera`) |
| `%teracore_money%`    | Gibt das aktuelle Geld des Spielers aus           |
| `%teracore_money_round%`       | Gibt das gerundete Geld des Spielers aus          |

---

## 💡 Command-Tabelle

| Command     | Permission                | Beschreibung                                 |
|--------------|-----------------------------|-------------------------------------------------|
| `gamemode`   | `teracore.gamemode`         | Ändert den Spielmodus (Alias: `gm`)            |
| `fly`        | `teracore.fly`              | Aktiviert oder deaktiviert Flugmodus           |
| `time`       | `teracore.time`             | Setzt die Weltzeit manuell                     |
| `day`        | `teracore.time.day`         | Setzt die Zeit auf Tag                         |
| `night`      | `teracore.time.night`       | Setzt die Zeit auf Nacht                       |
| `weather`    | `teracore.weather`          | Ändert das Wetter                              |
| `sun`        | `teracore.weather.sun`      | Setzt das Wetter auf Sonnig                    |
| `rain`       | `teracore.weather.rain`     | Setzt das Wetter auf Regen                     |
| `thunder`    | `teracore.weather.thunder`  | Setzt das Wetter auf Gewitter                  |
| `tphere`     | `teracore.tphere`           | Teleportiert Spieler zu dir                    |
| `teleport`   | `teracore.teleport`         | Teleportiert dich zu einem Spieler (Alias: `tp`)|
| `speed`      | `teracore.speed`            | Ändert Flug- oder Laufgeschwindigkeit          |
| `eat`        | `teracore.eat`              | Füllt den Hungerbalken (Alias: `food`)         |
| `heal`       | `teracore.heal`             | Heilt den Spieler vollständig                  |
| `money`      | `teracore.money`            | Zeigt den aktuellen Kontostand an              |
| `addmoney`   | `teracore.money.add`        | Fügt einem Spieler Geld hinzu                  |
| `removemoney` | `teracore.money.remove`     | Zieht einem Spieler Geld ab                    |
| `setmoney`   | `teracore.money.set`        | Setzt den Kontostand eines Spielers            |
| `pay`        | `teracore.money.pay`        | Sendet einem anderen Spieler Geld              |
| `skull`      | `teracore.skull`            | Gibt einen Spielerkopf                         |
| `motd`       | `teracore.motd`             | Zeigt die Nachricht des Tages an               |
| `home`       | `teracore.home`             | Teleportiert dich zu deinem Zuhause            |
| `sethome`    | `teracore.home.set`         | Setzt deinen Zuhause-Punkt                     |
| `delhome`    | `teracore.home.delete`      | Löscht dein Zuhause                            |
| `movehome`   | `teracore.home.move`        | Bewegt dein Zuhause zur aktuellen Position     |
| `warp`       | `teracore.warp`             | Teleportiert zu einem Warp-Punkt               |
| `setwarp`    | `teracore.warp.set`         | Erstellt einen neuen Warp                      |
| `delwarp`    | `teracore.warp.delete`      | Löscht einen Warp                              |
| `kick`       | `teracore.kick`             | Kickt einen Spieler                            |
| `ban`        | `teracore.ban`              | Bannt einen Spieler dauerhaft                  |
| `unban`      | `teracore.unban`            | Entbannt einen Spieler                         |
| `banlist`    | `teracore.banlist`          | Zeigt alle gebannten Spieler                   |
| `tempban`    | `teracore.tempban`          | Bannt einen Spieler temporär                   |
| `seed`       | `teracore.seed`             | Zeigt den Seed der Welt                        |

---

🎉 **TeraCore – Deine zentrale Schaltstelle für alles Wichtige auf dem Server.**  
Modular, leistungsfähig und modern.