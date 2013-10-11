-- This is the base support schema, it is meant to store support data
-- like error reporting, users, roles and permissions
-- We don't make any assumptions on the user, we only store
-- authentiction data.

CREATE OR REPLACE FUNCTION Update_Last_Updated_Column()
RETURNS TRIGGER AS '
BEGIN
NEW.last_updated = now();
RETURN NEW;
END; ' language 'plpgsql';

CREATE TABLE IF NOT EXISTS Users (
    username VARCHAR(512) NOT NULL,
        CONSTRAINT Lowercase_Username
        CHECK (LOWER(username) = username),
    password VARCHAR(512) NOT NULL,
    active BOOLEAN DEFAULT true NOT NULL,
    date_created TIMESTAMP(2)
        DEFAULT CURRENT_TIMESTAMP
        NOT NULL,
    last_updated TIMESTAMP(2)
        DEFAULT CURRENT_TIMESTAMP
        NOT NULL,
    PRIMARY KEY (username)
);

DROP TRIGGER IF EXISTS Update_Users_Timestamp on Users;
CREATE TRIGGER Update_Users_Timestamp BEFORE UPDATE ON Users
    FOR EACH ROW EXECUTE PROCEDURE
    Update_Last_Updated_Column();

CREATE TABLE IF NOT EXISTS Roles (
    -- The role code should be a working name
    -- for each role
    role_code VARCHAR(64) NOT NULL,
    description VARCHAR(1024) NOT NULL,
    date_created TIMESTAMP(2)
        DEFAULT CURRENT_TIMESTAMP
        NOT NULL,
    last_updated TIMESTAMP(2)
        DEFAULT CURRENT_TIMESTAMP
        NOT NULL,
    PRIMARY KEY (role_code)
);

DROP TRIGGER IF EXISTS Update_Roles_Timestamp on Roles;
CREATE TRIGGER Update_Roles_Timestamp BEFORE UPDATE ON Roles
    FOR EACH ROW EXECUTE PROCEDURE
    Update_Last_Updated_Column();

CREATE TABLE IF NOT EXISTS Role_Assignments (
    username VARCHAR(512) NOT NULL
        REFERENCES Users (username)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    role_code VARCHAR(64) NOT NULL
        REFERENCES Roles (role_code)
        ON UPDATE CASCADE,
    date_created TIMESTAMP(2)
        DEFAULT CURRENT_TIMESTAMP
        NOT NULL,
    last_updated TIMESTAMP(2)
        DEFAULT CURRENT_TIMESTAMP
        NOT NULL,
    PRIMARY KEY (username, role_code)
);

DROP TRIGGER IF EXISTS Update_Role_Assignments_Timestamp on Role_Assignments;
CREATE TRIGGER Update_Role_Assignments_Timestamp BEFORE UPDATE ON Role_Assignments
    FOR EACH ROW EXECUTE PROCEDURE
    Update_Last_Updated_Column();

CREATE TABLE IF NOT EXISTS Tunnels (
    binding_port SMALLINT UNIQUE NOT NULL,
    host VARCHAR(1024) NOT NULL,
    host_port SMALLINT NOT NULL,
    active boolean DEFAULT true NOT NULL,
    date_created TIMESTAMP(2)
        DEFAULT CURRENT_TIMESTAMP
        NOT NULL,
    last_updated TIMESTAMP(2)
        DEFAULT CURRENT_TIMESTAMP
        NOT NULL,
    PRIMARY KEY (binding_port, host, host_port)
);

DROP TRIGGER IF EXISTS Update_Tunnels_Timestamp on Tunnels;
CREATE TRIGGER Update_Tunnels_Timestamp BEFORE UPDATE ON Tunnels
    FOR EACH ROW EXECUTE PROCEDURE
    Update_Last_Updated_Column();
