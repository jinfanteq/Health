CREATE DATABASE IF NOT EXISTS health;
USE health;

-- ======================
--  Tabla Usuario (PADRE)
-- ======================
CREATE TABLE Usuario (
    id BIGINT(255) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    rol ENUM('paciente', 'medico', 'admin') NOT NULL
);
-- ======================
-- CLASES HIJAS        -- 
-- ======================
--  Tabla Paciente 
-- ======================
CREATE TABLE Paciente (
    id BIGINT(255) PRIMARY KEY,
    tipoSangre VARCHAR(10),
    altura INT,
    peso FLOAT,
    edad INT,
    alergias TEXT,
    FOREIGN KEY (id) REFERENCES Usuario(id) ON DELETE CASCADE
);

-- ======================
--  Tabla Medico
-- ======================
CREATE TABLE Medico (
	id BIGINT(255) PRIMARY KEY,
    especialidad VARCHAR(100),
    FOREIGN KEY (id) REFERENCES Usuario(id) ON DELETE CASCADE
);

-- ====================--
--  Tabla Admin (HIJA) -- 
-- ====================--
CREATE TABLE Admin (
     id BIGINT(255) PRIMARY KEY,
     nombre VARCHAR(150),
    FOREIGN KEY (id) REFERENCES Usuario(id) 
);

-- ======================
--  Tabla Cita Médica
-- ======================
CREATE TABLE CitaMedica (
    idCita INT AUTO_INCREMENT PRIMARY KEY,
    idPaciente BIGINT(255) NOT NULL,
    idMedico BIGINT(255) NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    motivoConsulta VARCHAR(255),
    codigoCitaSistema VARCHAR(50),

    FOREIGN KEY (idPaciente) REFERENCES Paciente(id) ON DELETE CASCADE,
    FOREIGN KEY (idMedico) REFERENCES Medico(id) ON DELETE CASCADE
);
