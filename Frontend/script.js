 // Game State
let gameState = {
    currentLevel: 1,
    playerLevel: 1,
    playerXP: 0,
    playerCoins: 100,
    levelProgress: 0,
    completedTasks: 0,
    totalTasks: 3,
    achievements: {
        'First Steps': false,
        'Pizza Pro': false,
        'Speed Chef': false
    }
};

// Level Data
const levelData = {
    1: {
        title: "Pizza Master",
        icon: "🍕",
        tasks: [
            {
                title: "Prepare Pizza Dough",
                description: "Mix flour, water, and yeast to create the perfect pizza base",
                icon: "🍕",
                xpReward: 20
            },
            {
                title: "Add Toppings",
                description: "Spread sauce and add your favorite toppings",
                icon: "🧀",
                xpReward: 25
            },
            {
                title: "Bake to Perfection",
                description: "Cook the pizza at the right temperature for the perfect crust",
                icon: "🔥",
                xpReward: 30
            }
        ]
    },
    2: {
        title: "Burger Builder",
        icon: "🍔",
        tasks: [
            {
                title: "Form the Patty",
                description: "Shape ground beef into a perfect burger patty",
                icon: "🥩",
                xpReward: 25
            },
            {
                title: "Grill the Patty",
                description: "Cook the patty to your preferred doneness",
                icon: "🔥",
                xpReward: 30
            },
            {
                title: "Assemble the Burger",
                description: "Layer lettuce, tomato, cheese, and condiments",
                icon: "🍔",
                xpReward: 35
            }
        ]
    },
    3: {
        title: "Noodle Ninja",
        icon: "🍜",
        tasks: [
            {
                title: "Boil the Noodles",
                description: "Cook noodles to al dente perfection",
                icon: "🍜",
                xpReward: 30
            },
            {
                title: "Prepare the Sauce",
                description: "Create a flavorful sauce with vegetables and spices",
                icon: "🥬",
                xpReward: 35
            },
            {
                title: "Combine and Serve",
                description: "Mix noodles with sauce and garnish with herbs",
                icon: "🌿",
                xpReward: 40
            }
        ]
    },
    4: {
        title: "Dessert Wizard",
        icon: "🍰",
        tasks: [
            {
                title: "Mix the Batter",
                description: "Combine flour, sugar, eggs, and butter for the perfect cake",
                icon: "🥚",
                xpReward: 35
            },
            {
                title: "Bake the Cake",
                description: "Bake at the right temperature for a fluffy texture",
                icon: "🔥",
                xpReward: 40
            },
            {
                title: "Decorate with Style",
                description: "Add frosting, sprinkles, and creative decorations",
                icon: "🎨",
                xpReward: 45
            }
        ]
    }
};

// Initialize the game
document.addEventListener('DOMContentLoaded', function() {
    // Check if user is logged in
    const isLoggedIn = localStorage.getItem('swiftFoodLoggedIn');
    if (isLoggedIn !== 'true') {
        // Redirect to login page if not logged in
        window.location.href = 'login.html';
        return;
    }
    
    initializeGame();
    setupEventListeners();
    updateUI();
});

function initializeGame() {
    // Always start fresh - no saved game state
    // Clear any existing game state
    localStorage.removeItem('swiftFoodGameState');
    
    // Reset game state to defaults
    gameState = {
        currentLevel: 1,
        playerLevel: 1,
        playerXP: 0,
        playerCoins: 100,
        levelProgress: 0,
        completedTasks: 0,
        totalTasks: 3,
        achievements: {
            'First Steps': false,
            'Pizza Pro': false,
            'Speed Chef': false
        }
    };
    
    // Update UI with fresh state
    updatePlayerStats();
    updateLevelCards();
}

function setupEventListeners() {
    // Level card click events
    const levelCards = document.querySelectorAll('.level-card');
    levelCards.forEach(card => {
        card.addEventListener('click', function() {
            const level = parseInt(this.dataset.level);
            if (level <= gameState.currentLevel) {
                startLevel(level);
            }
        });
    });
}

function updateUI() {
    updatePlayerStats();
    updateLevelCards();
    updateProgressBar();
}

function updatePlayerStats() {
    document.getElementById('player-level').textContent = gameState.playerLevel;
    document.getElementById('player-xp').textContent = gameState.playerXP;
    document.getElementById('player-coins').textContent = gameState.playerCoins;
}

function updateLevelCards() {
    const levelCards = document.querySelectorAll('.level-card');
    levelCards.forEach(card => {
        const level = parseInt(card.dataset.level);
        const status = card.querySelector('.level-status');
        
        if (level <= gameState.currentLevel) {
            status.textContent = 'UNLOCKED';
            status.className = 'level-status unlocked';
            card.classList.add('unlocked');
        } else {
            status.textContent = 'LOCKED';
            status.className = 'level-status locked';
            card.classList.remove('unlocked');
        }
    });
}

function startLevel(level) {
    gameState.currentLevel = level;
    gameState.completedTasks = 0;
    gameState.levelProgress = 0;
    
    const levelData = getLevelData(level);
    if (!levelData) return;
    
    // Update UI
    document.getElementById('level-selection').style.display = 'none';
    document.getElementById('active-level').style.display = 'block';
    document.getElementById('current-level-title').textContent = `Level ${level}: ${levelData.title}`;
    
    // Load first task
    loadTask(0);
    updateProgressBar();
}

function getLevelData(level) {
    return levelData[level] || null;
}

function loadTask(taskIndex) {
    const levelData = getLevelData(gameState.currentLevel);
    if (!levelData || taskIndex >= levelData.tasks.length) return;
    
    const task = levelData.tasks[taskIndex];
    document.getElementById('task-title').textContent = task.title;
    document.getElementById('task-description').textContent = task.description;
    document.querySelector('.task-icon').textContent = task.icon;
}

function completeTask() {
    const levelData = getLevelData(gameState.currentLevel);
    if (!levelData) return;
    
    const currentTask = levelData.tasks[gameState.completedTasks];
    
    // Add rewards
    gameState.playerXP += currentTask.xpReward;
    gameState.playerCoins += 10;
    gameState.completedTasks++;
    
    // Check for level up
    checkLevelUp();
    
    // Update progress
    gameState.levelProgress = (gameState.completedTasks / levelData.tasks.length) * 100;
    
    // Check if level is complete
    if (gameState.completedTasks >= levelData.tasks.length) {
        completeLevel();
    } else {
        // Load next task
        loadTask(gameState.completedTasks);
        updateProgressBar();
        updatePlayerStats();
        
        // Add completion animation
        const taskCard = document.getElementById('current-task');
        taskCard.classList.add('bounce');
        setTimeout(() => taskCard.classList.remove('bounce'), 1000);
    }
}

function completeLevel() {
    // Show level complete modal
    const modal = document.getElementById('level-complete-modal');
    modal.style.display = 'block';
    
    // Add level completion rewards
    gameState.playerXP += 50;
    gameState.playerCoins += 25;
    
    // Unlock next level
    if (gameState.currentLevel < Object.keys(levelData).length) {
        gameState.currentLevel++;
    }
    
    // Check for achievements
    checkAchievements();
    
    // Save game state
    saveGameState();
    
    updatePlayerStats();
}

function nextLevel() {
    // Hide modal
    document.getElementById('level-complete-modal').style.display = 'none';
    
    // Go back to level selection
    showLevelSelection();
}

function showLevelSelection() {
    document.getElementById('active-level').style.display = 'none';
    document.getElementById('level-selection').style.display = 'block';
    updateUI();
}

function updateProgressBar() {
    const progressFill = document.getElementById('level-progress');
    progressFill.style.width = `${gameState.levelProgress}%`;
}

function checkLevelUp() {
    const xpNeeded = gameState.playerLevel * 100;
    if (gameState.playerXP >= xpNeeded) {
        gameState.playerLevel++;
        gameState.playerXP -= xpNeeded;
        
        // Show level up animation
        const levelElement = document.getElementById('player-level');
        levelElement.classList.add('bounce');
        setTimeout(() => levelElement.classList.remove('bounce'), 1000);
        
        // Show level up notification
        showNotification(`🎉 Level Up! You are now Level ${gameState.playerLevel}!`);
    }
}

function checkAchievements() {
    const achievements = {
        'First Steps': gameState.playerLevel >= 1,
        'Pizza Pro': gameState.currentLevel >= 2,
        'Speed Chef': gameState.playerXP >= 200
    };
    
    Object.keys(achievements).forEach(achievement => {
        if (achievements[achievement] && !gameState.achievements[achievement]) {
            gameState.achievements[achievement] = true;
            showNotification(`🏆 Achievement Unlocked: ${achievement}!`);
        }
    });
    
    updateAchievements();
}

function updateAchievements() {
    const achievementList = document.getElementById('achievement-list');
    achievementList.innerHTML = '';
    
    Object.keys(gameState.achievements).forEach(achievement => {
        const isUnlocked = gameState.achievements[achievement];
        const achievementElement = document.createElement('div');
        achievementElement.className = `achievement ${isUnlocked ? 'unlocked' : 'locked'}`;
        achievementElement.innerHTML = `
            <span class="achievement-icon">${isUnlocked ? '✅' : '🔒'}</span>
            <span class="achievement-text">${achievement}</span>
        `;
        achievementList.appendChild(achievementElement);
    });
}

function showNotification(message) {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: linear-gradient(45deg, #4ecdc4, #44a08d);
        color: white;
        padding: 1rem 2rem;
        border-radius: 10px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.3);
        z-index: 1001;
        animation: slideInRight 0.3s ease;
    `;
    
    document.body.appendChild(notification);
    
    // Remove notification after 3 seconds
    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

function saveGameState() {
    localStorage.setItem('swiftFoodGameState', JSON.stringify(gameState));
}

// Add CSS animations for notifications
const style = document.createElement('style');
style.textContent = `
    @keyframes slideInRight {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes slideOutRight {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
`;
document.head.appendChild(style);

// Auto-save game state periodically
setInterval(saveGameState, 30000); // Save every 30 seconds

// Logout function
function logout() {
    // Clear all game data
    localStorage.removeItem('swiftFoodGameState');
    localStorage.removeItem('swiftFoodLoggedIn');
    localStorage.removeItem('swiftFoodUser');
    
    // Redirect to login page
    window.location.href = 'login.html';
}