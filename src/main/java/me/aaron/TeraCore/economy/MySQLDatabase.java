package me.aaron.TeraCore.economy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import me.aaron.TeraCore.util.UUIDFetcher;

public class MySQLDatabase {

    private static Connection connection;

    private static Eco_Config eco_conf = new Eco_Config();
    
    private static String table = eco_conf.config.getString("economy.data.table");
    
    public static void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            
            String url = "jdbc:mysql://";
            String adress = eco_conf.config.getString("economy.data.address");
            Integer port = eco_conf.config.getInt("economy.data.port");
            String database = eco_conf.config.getString("economy.data.database");
            String username = eco_conf.config.getString("economy.data.username");
            String password = eco_conf.config.getString("economy.data.password");
            
            connection = DriverManager.getConnection(url + adress + ":" + port + "/" + database, username, password);
            System.out.println("MySQL-Datenbankverbindung erfolgreich hergestellt.");
            createTableIfNotExists(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fehler beim Verbinden mit der MySQL-Datenbank.");
        }
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("MySQL-Datenbankverbindung geschlossen.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setMoney(UUID uuid, double money) {
        connect();
        try (PreparedStatement ps = connection.prepareStatement("REPLACE INTO " + table +" (uuid, money) VALUES (?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setDouble(2, money);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMoney(UUID uuid, double money) {
        connect();
        try (PreparedStatement ps = connection.prepareStatement("UPDATE "+ table +" SET money = money + ? WHERE uuid = ?")) {
            ps.setDouble(1, money);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeMoney(UUID uuid, double money) {
        connect();
        try (PreparedStatement ps = connection.prepareStatement("UPDATE "+ table +" SET money = money - ? WHERE uuid = ?")) {
            ps.setDouble(1, money);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getMoney(UUID uuid) {
        connect();
        double money = 0;
        try (PreparedStatement ps = connection.prepareStatement("SELECT money FROM "+ table +" WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    money = rs.getDouble("money");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return money;
    }

    public static boolean exists(UUID uuid) {
        connect();
        boolean exists = false;
        try (PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM "+ table +" WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                exists = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    public static void createTableIfNotExists(Connection conn) {
        if (conn == null) {
            System.err.println("Keine Verbindung zur Datenbank.");
            return;
        }

        String createTableSQL = "CREATE TABLE IF NOT EXISTS "+ table +" ("
                + "uuid VARCHAR(36) PRIMARY KEY, "
                + "money DOUBLE DEFAULT 0)";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("Tabelle '"+ table +"' erfolgreich erstellt oder existiert bereits.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Fehler beim Erstellen der Tabelle '"+ table +"'.");
        }
    }
    public static ArrayList<String> getAllPlayers() {
        connect(); // Stellt sicher, dass die Verbindung zur Datenbank hergestellt ist
        ArrayList<String> playerUUIDs = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT uuid FROM "+ table)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String uuid = rs.getString("uuid");
                playerUUIDs.add(UUIDFetcher.getName(UUID.fromString(uuid)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playerUUIDs;
    } 
}
