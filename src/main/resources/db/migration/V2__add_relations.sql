ALTER TABLE permissions
    ADD CONSTRAINT fk_permissions_user
        FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE permissions
    ADD CONSTRAINT fk_permissions_resource
        FOREIGN KEY (resource_id) REFERENCES resources (id);

CREATE INDEX idx_permissions_user_id
    ON permissions (user_id);

CREATE INDEX idx_permissions_resource_id
    ON permissions (resource_id);

CREATE UNIQUE INDEX uk_permissions_user_resource_action
    ON permissions (user_id, resource_id, action);


