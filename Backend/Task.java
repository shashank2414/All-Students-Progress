/**
 * Task class represents a task within a level in the SwiftFood game
 */
public class Task {
    private String title;
    private String description;
    private String icon;
    private int xpReward;
    private boolean completed;
    
    public Task(String title, String description, int xpReward) {
        this.title = title;
        this.description = description;
        this.xpReward = xpReward;
        this.completed = false;
        this.icon = "🍽️"; // Default icon
    }
    
    public Task(String title, String description, String icon, int xpReward) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.xpReward = xpReward;
        this.completed = false;
    }
    
    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public int getXpReward() { return xpReward; }
    public boolean isCompleted() { return completed; }
    
    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setIcon(String icon) { this.icon = icon; }
    public void setXpReward(int xpReward) { this.xpReward = xpReward; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public void complete() {
        this.completed = true;
    }
    
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"title\":\"").append(title).append("\",");
        json.append("\"description\":\"").append(description).append("\",");
        json.append("\"icon\":\"").append(icon).append("\",");
        json.append("\"xpReward\":").append(xpReward).append(",");
        json.append("\"completed\":").append(completed);
        json.append("}");
        return json.toString();
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", xpReward=" + xpReward +
                ", completed=" + completed +
                '}';
    }
} 