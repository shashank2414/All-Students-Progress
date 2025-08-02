import java.util.*;

/**
 * LevelData class represents a level in the SwiftFood game
 */
public class LevelData {
    private int levelId;
    private String title;
    private String icon;
    private List<Task> tasks;
    private boolean unlocked;
    private int requiredLevel;
    
    public LevelData(int levelId, String title, String icon) {
        this.levelId = levelId;
        this.title = title;
        this.icon = icon;
        this.tasks = new ArrayList<>();
        this.unlocked = levelId == 1; // First level is always unlocked
        this.requiredLevel = levelId;
    }
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public void addTask(String title, String description, int xpReward) {
        tasks.add(new Task(title, description, xpReward));
    }
    
    // Getters
    public int getLevelId() { return levelId; }
    public String getTitle() { return title; }
    public String getIcon() { return icon; }
    public List<Task> getTasks() { return tasks; }
    public boolean isUnlocked() { return unlocked; }
    public int getRequiredLevel() { return requiredLevel; }
    
    // Setters
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    public void setRequiredLevel(int requiredLevel) { this.requiredLevel = requiredLevel; }
    
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"levelId\":").append(levelId).append(",");
        json.append("\"title\":\"").append(title).append("\",");
        json.append("\"icon\":\"").append(icon).append("\",");
        json.append("\"unlocked\":").append(unlocked).append(",");
        json.append("\"requiredLevel\":").append(requiredLevel).append(",");
        
        // Add tasks
        json.append("\"tasks\":[");
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) json.append(",");
            json.append(tasks.get(i).toJson());
        }
        json.append("]");
        
        json.append("}");
        return json.toString();
    }
    
    @Override
    public String toString() {
        return "LevelData{" +
                "levelId=" + levelId +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", tasks=" + tasks +
                ", unlocked=" + unlocked +
                '}';
    }
} 