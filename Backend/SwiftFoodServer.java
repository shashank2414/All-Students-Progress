import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * SwiftFood Backend Server
 * A simple Java HTTP server for the SwiftFood game
 */
public class SwiftFoodServer {
    private static final int PORT = 8080;
    private static final String SERVER_NAME = "SwiftFood Server v1.0";
    
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private boolean running = false;
    
    // Game data storage (in-memory for simplicity)
    private Map<String, PlayerData> players = new ConcurrentHashMap<>();
    private Map<Integer, LevelData> levels = new HashMap<>();
    
    public SwiftFoodServer() {
        initializeLevels();
        threadPool = Executors.newFixedThreadPool(10);
    }
    
    private void initializeLevels() {
        // Level 1: Pizza Master
        LevelData level1 = new LevelData(1, "Pizza Master", "🍕");
        level1.addTask(new Task("Prepare Pizza Dough", "Mix flour, water, and yeast", 20));
        level1.addTask(new Task("Add Toppings", "Spread sauce and add toppings", 25));
        level1.addTask(new Task("Bake to Perfection", "Cook at right temperature", 30));
        levels.put(1, level1);
        
        // Level 2: Burger Builder
        LevelData level2 = new LevelData(2, "Burger Builder", "🍔");
        level2.addTask(new Task("Form the Patty", "Shape ground beef into patty", 25));
        level2.addTask(new Task("Grill the Patty", "Cook to preferred doneness", 30));
        level2.addTask(new Task("Assemble the Burger", "Layer with condiments", 35));
        levels.put(2, level2);
        
        // Level 3: Noodle Ninja
        LevelData level3 = new LevelData(3, "Noodle Ninja", "🍜");
        level3.addTask(new Task("Boil the Noodles", "Cook to al dente", 30));
        level3.addTask(new Task("Prepare the Sauce", "Create flavorful sauce", 35));
        level3.addTask(new Task("Combine and Serve", "Mix and garnish", 40));
        levels.put(3, level3);
        
        // Level 4: Dessert Wizard
        LevelData level4 = new LevelData(4, "Dessert Wizard", "🍰");
        level4.addTask(new Task("Mix the Batter", "Combine ingredients", 35));
        level4.addTask(new Task("Bake the Cake", "Bake at right temperature", 40));
        level4.addTask(new Task("Decorate with Style", "Add frosting and decorations", 45));
        levels.put(4, level4);
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;
            System.out.println(SERVER_NAME + " started on port " + PORT);
            System.out.println("Server is ready to handle requests...");
            
            while (running) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
    
    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing server: " + e.getMessage());
            }
        }
        threadPool.shutdown();
        System.out.println("Server stopped.");
    }
    
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                
                String requestLine = in.readLine();
                if (requestLine == null) return;
                
                String[] requestParts = requestLine.split(" ");
                if (requestParts.length < 2) return;
                
                String method = requestParts[0];
                String path = requestParts[1];
                
                // Read headers
                Map<String, String> headers = new HashMap<>();
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    if (line.contains(":")) {
                        String[] headerParts = line.split(":", 2);
                        headers.put(headerParts[0].trim(), headerParts[1].trim());
                    }
                }
                
                // Handle request
                String response = handleRequest(method, path, headers, in);
                
                // Send response
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println("Access-Control-Allow-Origin: *");
                out.println("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
                out.println("Access-Control-Allow-Headers: Content-Type");
                out.println("Content-Length: " + response.getBytes().length);
                out.println();
                out.println(response);
                
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
        
        private String handleRequest(String method, String path, Map<String, String> headers, BufferedReader in) {
            try {
                switch (method) {
                    case "GET":
                        return handleGetRequest(path);
                    case "POST":
                        return handlePostRequest(path, in);
                    case "PUT":
                        return handlePutRequest(path, in);
                    case "DELETE":
                        return handleDeleteRequest(path);
                    case "OPTIONS":
                        return "{}"; // CORS preflight
                    default:
                        return createErrorResponse("Method not allowed", 405);
                }
            } catch (Exception e) {
                return createErrorResponse("Internal server error: " + e.getMessage(), 500);
            }
        }
        
        private String handleGetRequest(String path) {
            if (path.equals("/api/levels")) {
                return getLevelsResponse();
            } else if (path.startsWith("/api/player/")) {
                String playerId = path.substring(12);
                return getPlayerData(playerId);
            } else if (path.equals("/api/health")) {
                return "{\"status\":\"healthy\",\"server\":\"" + SERVER_NAME + "\"}";
            } else {
                return createErrorResponse("Not found", 404);
            }
        }
        
        private String handlePostRequest(String path, BufferedReader in) {
            try {
                StringBuilder body = new StringBuilder();
                String line;
                while (in.ready() && (line = in.readLine()) != null) {
                    body.append(line);
                }
                
                if (path.equals("/api/player")) {
                    return createPlayer(body.toString());
                } else if (path.startsWith("/api/player/") && path.endsWith("/complete-task")) {
                    String playerId = path.substring(12, path.length() - 14);
                    return completeTask(playerId, body.toString());
                } else if (path.startsWith("/api/player/") && path.endsWith("/complete-level")) {
                    String playerId = path.substring(12, path.length() - 15);
                    return completeLevel(playerId, body.toString());
                } else {
                    return createErrorResponse("Not found", 404);
                }
            } catch (IOException e) {
                return createErrorResponse("Error reading request body", 400);
            }
        }
        
        private String handlePutRequest(String path, BufferedReader in) {
            try {
                StringBuilder body = new StringBuilder();
                String line;
                while (in.ready() && (line = in.readLine()) != null) {
                    body.append(line);
                }
                
                if (path.startsWith("/api/player/")) {
                    String playerId = path.substring(12);
                    return updatePlayerData(playerId, body.toString());
                } else {
                    return createErrorResponse("Not found", 404);
                }
            } catch (IOException e) {
                return createErrorResponse("Error reading request body", 400);
            }
        }
        
        private String handleDeleteRequest(String path) {
            if (path.startsWith("/api/player/")) {
                String playerId = path.substring(12);
                return deletePlayer(playerId);
            } else {
                return createErrorResponse("Not found", 404);
            }
        }
        
        private String getLevelsResponse() {
            StringBuilder response = new StringBuilder();
            response.append("{\"levels\":[");
            
            boolean first = true;
            for (LevelData level : levels.values()) {
                if (!first) response.append(",");
                response.append(level.toJson());
                first = false;
            }
            
            response.append("]}");
            return response.toString();
        }
        
        private String getPlayerData(String playerId) {
            PlayerData player = players.get(playerId);
            if (player == null) {
                return createErrorResponse("Player not found", 404);
            }
            return player.toJson();
        }
        
        private String createPlayer(String playerData) {
            // Simple player creation - in a real app, you'd parse JSON
            String playerId = "player_" + System.currentTimeMillis();
            PlayerData player = new PlayerData(playerId);
            players.put(playerId, player);
            
            return "{\"playerId\":\"" + playerId + "\",\"message\":\"Player created successfully\"}";
        }
        
        private String completeTask(String playerId, String taskData) {
            PlayerData player = players.get(playerId);
            if (player == null) {
                return createErrorResponse("Player not found", 404);
            }
            
            // Simulate task completion
            player.addXP(25);
            player.addCoins(10);
            
            return "{\"message\":\"Task completed\",\"xpGained\":25,\"coinsGained\":10}";
        }
        
        private String completeLevel(String playerId, String levelData) {
            PlayerData player = players.get(playerId);
            if (player == null) {
                return createErrorResponse("Player not found", 404);
            }
            
            // Simulate level completion
            player.addXP(50);
            player.addCoins(25);
            player.incrementLevel();
            
            return "{\"message\":\"Level completed\",\"xpGained\":50,\"coinsGained\":25}";
        }
        
        private String updatePlayerData(String playerId, String playerData) {
            PlayerData player = players.get(playerId);
            if (player == null) {
                return createErrorResponse("Player not found", 404);
            }
            
            // In a real app, you'd parse and update the player data
            return "{\"message\":\"Player data updated successfully\"}";
        }
        
        private String deletePlayer(String playerId) {
            PlayerData removed = players.remove(playerId);
            if (removed == null) {
                return createErrorResponse("Player not found", 404);
            }
            
            return "{\"message\":\"Player deleted successfully\"}";
        }
        
        private String createErrorResponse(String message, int code) {
            return "{\"error\":\"" + message + "\",\"code\":" + code + "}";
        }
    }
    
    public static void main(String[] args) {
        SwiftFoodServer server = new SwiftFoodServer();
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop();
        }));
        
        server.start();
    }
} 