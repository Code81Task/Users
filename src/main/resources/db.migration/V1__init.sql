CREATE TABLE system_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,  -- Hashed with bcrypt
    email VARCHAR(255) UNIQUE,
    role_id INTEGER REFERENCES roles(id) ON DELETE RESTRICT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Members table (borrowers)
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(50),
    address TEXT,
    membership_date DATE DEFAULT CURRENT_DATE
);

-- Activity Logs table (for user actions)
CREATE TABLE activity_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INTEGER REFERENCES system_users(id) ON DELETE SET NULL,  -- System user who performed action
    action VARCHAR(255) NOT NULL,  -- e.g., 'Login', 'Book Added'
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);