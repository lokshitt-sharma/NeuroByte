CREATE TABLE app_user (
  id BIGSERIAL PRIMARY KEY, 
  username VARCHAR(100) UNIQUE NOT NULL, 
  email VARCHAR(255) UNIQUE NOT NULL, 
  password_hash VARCHAR(255) NOT NULL, 
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE problem (
  id BIGSERIAL PRIMARY KEY, 
  title VARCHAR(255) NOT NULL, 
  description TEXT NOT NULL, 
  difficulty VARCHAR(20) NOT NULL, 
  starter_code TEXT, 
  time_limit_seconds INT DEFAULT 60, 
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE test_case (
  id BIGSERIAL PRIMARY KEY, 
  problem_id BIGINT REFERENCES problem(id) ON DELETE CASCADE, 
  input TEXT, 
  expected_output TEXT, 
  is_hidden BOOLEAN DEFAULT false
);

CREATE TABLE submission (
  id BIGSERIAL PRIMARY KEY, 
  user_id BIGINT REFERENCES app_user(id), 
  problem_id BIGINT REFERENCES problem(id) ON DELETE CASCADE, 
  language VARCHAR(50), 
  source_code TEXT, 
  verdict VARCHAR(50), 
  runtime_ms INT, 
  memory_kb INT, 
  score INT, 
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
