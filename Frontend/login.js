// Login page functionality
document.addEventListener('DOMContentLoaded', function() {
    // Check if user is already logged in
    const isLoggedIn = localStorage.getItem('swiftFoodLoggedIn');
    if (isLoggedIn === 'true') {
        // Redirect to main game
        window.location.href = 'index.html';
        return;
    }

    // Setup form event listeners
    setupFormListeners();
});

function setupFormListeners() {
    // Login form
    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', handleLogin);

    // Register form
    const registerForm = document.getElementById('registerForm');
    registerForm.addEventListener('submit', handleRegister);
}

function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const rememberMe = document.getElementById('rememberMe').checked;

    // Simple validation
    if (!username || !password) {
        showNotification('Please fill in all fields', 'error');
        return;
    }

    // Simulate login process
    showLoadingState();
    
    setTimeout(() => {
        // For demo purposes, accept any login
        // In a real app, you'd validate against a backend
        const userData = {
            username: username,
            loginTime: new Date().toISOString(),
            rememberMe: rememberMe
        };

        // Store login state
        localStorage.setItem('swiftFoodLoggedIn', 'true');
        localStorage.setItem('swiftFoodUser', JSON.stringify(userData));
        
        // Clear any existing game state to start fresh
        localStorage.removeItem('swiftFoodGameState');
        
        showNotification('Login successful! Redirecting...', 'success');
        
        // Redirect to main game
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1000);
    }, 1500);
}

function handleRegister(event) {
    event.preventDefault();
    
    const username = document.getElementById('regUsername').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    // Validation
    if (!username || !email || !password || !confirmPassword) {
        showNotification('Please fill in all fields', 'error');
        return;
    }

    if (password !== confirmPassword) {
        showNotification('Passwords do not match', 'error');
        return;
    }

    if (password.length < 6) {
        showNotification('Password must be at least 6 characters', 'error');
        return;
    }

    // Simulate registration process
    showLoadingState();
    
    setTimeout(() => {
        // For demo purposes, accept any registration
        const userData = {
            username: username,
            email: email,
            registrationTime: new Date().toISOString()
        };

        // Store user data
        localStorage.setItem('swiftFoodUser', JSON.stringify(userData));
        localStorage.setItem('swiftFoodLoggedIn', 'true');
        
        // Clear any existing game state to start fresh
        localStorage.removeItem('swiftFoodGameState');
        
        showNotification('Registration successful! Redirecting...', 'success');
        
        // Redirect to main game
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1000);
    }, 1500);
}

function loginAsGuest() {
    showLoadingState();
    
    setTimeout(() => {
        // Create guest user
        const guestData = {
            username: 'Guest_' + Math.floor(Math.random() * 10000),
            isGuest: true,
            loginTime: new Date().toISOString()
        };

        localStorage.setItem('swiftFoodUser', JSON.stringify(guestData));
        localStorage.setItem('swiftFoodLoggedIn', 'true');
        
        // Clear any existing game state to start fresh
        localStorage.removeItem('swiftFoodGameState');
        
        showNotification('Welcome Guest! Starting fresh game...', 'success');
        
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1000);
    }, 1000);
}

function showRegisterForm() {
    document.querySelector('.login-card').style.display = 'none';
    document.getElementById('registerCard').style.display = 'block';
}

function showLoginForm() {
    document.getElementById('registerCard').style.display = 'none';
    document.querySelector('.login-card').style.display = 'block';
}

function showLoadingState() {
    // Disable all buttons
    const buttons = document.querySelectorAll('button');
    buttons.forEach(button => {
        button.disabled = true;
        button.style.opacity = '0.6';
    });

    // Show loading indicator
    const loadingDiv = document.createElement('div');
    loadingDiv.className = 'loading-indicator';
    loadingDiv.innerHTML = `
        <div class="spinner"></div>
        <p>Processing...</p>
    `;
    loadingDiv.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: rgba(255, 255, 255, 0.95);
        padding: 2rem;
        border-radius: 15px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        z-index: 1000;
        text-align: center;
    `;
    document.body.appendChild(loadingDiv);

    // Add spinner CSS
    const style = document.createElement('style');
    style.textContent = `
        .spinner {
            width: 40px;
            height: 40px;
            border: 4px solid #f3f3f3;
            border-top: 4px solid #4ecdc4;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin: 0 auto 1rem;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    `;
    document.head.appendChild(style);
}

function showNotification(message, type = 'info') {
    // Remove existing notifications
    const existingNotifications = document.querySelectorAll('.notification');
    existingNotifications.forEach(notification => notification.remove());

    // Create notification
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    // Style based on type
    const colors = {
        success: '#4ecdc4',
        error: '#e74c3c',
        info: '#3498db'
    };
    
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${colors[type] || colors.info};
        color: white;
        padding: 1rem 2rem;
        border-radius: 10px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.3);
        z-index: 1001;
        animation: slideInRight 0.3s ease;
        font-weight: 600;
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

// Add CSS animations for notifications
const notificationStyle = document.createElement('style');
notificationStyle.textContent = `
    @keyframes slideInRight {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes slideOutRight {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
`;
document.head.appendChild(notificationStyle); 