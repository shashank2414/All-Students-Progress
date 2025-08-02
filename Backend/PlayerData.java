import java.util.*;

/**
 * PlayerData class represents a player in the SwiftFood game
 */
public class PlayerData {
    private String playerId;
    private String playerName;
    private int level;
    private int xp;
    private int coins;
    private int currentLevel;
    private int completedTasks;
    private Map<String, Boolean> achievements;
    private Date createdAt;
    private Date lastPlayed;
    
    public PlayerData(String playerId) {
        this.playerId = playerId;
        this.playerName = "Player_" + playerId.substring(7); // Remove "player_" prefix
        this.level = 1;
        this.xp = 0;
        this.coins = 100;
        this.currentLevel = 1;
        this.completedTasks = 0;
        this.achievements = new HashMap<>();
        this.createdAt = new Date();
        this.lastPlayed = new Date();
        
        // Initialize achievements
        achievements.put("First Steps", false);
        achievements.put("Pizza Pro", false);
        achievements.put("Speed Chef", false);
        achievements.put("Burger Master", false);
        achievements.put("Noodle Ninja", false);
        achievements.put("Dessert Wizard", false);
    }
    
    // Getters
    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public int getCoins() { return coins; }
    public int getCurrentLevel() { return currentLevel; }
    public int getCompletedTasks() { return completedTasks; }
    public Map<String, Boolean> getAchievements() { return achievements; }
    public Date getCreatedAt() { return createdAt; }
    public Date getLastPlayed() { return lastPlayed; }
    
    // Setters
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setLevel(int level) { this.level = level; }
    public void setXp(int xp) { this.xp = xp; }
    public void setCoins(int coins) { this.coins = coins; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
    public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }
    
    // Game methods
    public void addXP(int xpGained) {
        this.xp += xpGained;
        checkLevelUp();
        updateLastPlayed();
    }
    
    public void addCoins(int coinsGained) {
        this.coins += coinsGained;
        updateLastPlayed();
    }
    
    public void incrementLevel() {
        this.currentLevel++;
        updateLastPlayed();
    }
    
    public void completeTask() {
        this.completedTasks++;
        updateLastPlayed();
    }
    
    public void unlockAchievement(String achievementName) {
        if (achievements.containsKey(achievementName)) {
            achievements.put(achievementName, true);
        }
    }
    
    private void checkLevelUp() {
        int xpNeeded = level * 100;
        while (xp >= xpNeeded) {
            level++;
            xp -= xpNeeded;
            xpNeeded = level * 100;
        }
    }
    
    private void updateLastPlayed() {
        this.lastPlayed = new Date();
    }
    
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"playerId\":\"").append(playerId).append("\",");
        json.append("\"playerName\":\"").append(playerName).append("\",");
        json.append("\"level\":").append(level).append(",");
        json.append("\"xp\":").append(xp).append(",");
        json.append("\"coins\":").append(coins).append(",");
        json.append("\"currentLevel\":").append(currentLevel).append(",");
        json.append("\"completedTasks\":").append(completedTasks).append(",");
        json.append("\"createdAt\":\"").append(createdAt).append("\",");
        json.append("\"lastPlayed\":\"").append(lastPlayed).append("\",");
        
        // Add achievements
        json.append("\"achievements\":{");
        boolean first = true;
        for (Map.Entry<String, Boolean> entry : achievements.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
            first = false;
        }
        json.append("}");
        
        json.append("}");
        return json.toString();
    }
    
    @Override
    public String toString() {
        return "PlayerData{" +
                "playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", level=" + level +
                ", xp=" + xp +
                ", coins=" + coins +
                ", currentLevel=" + currentLevel +
                ", completedTasks=" + completedTasks +
                '}';
    }
} 