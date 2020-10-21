package org.minijax.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import jakarta.persistence.PersistenceException;

public class MinijaxTransaction implements jakarta.persistence.EntityTransaction {
    private final Connection connection;

    public MinijaxTransaction(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() {
        // TODO: Save points
        // For now, there is only one global "transaction" per connection
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (final SQLException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (final SQLException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setRollbackOnly() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getRollbackOnly() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isActive() {
        throw new UnsupportedOperationException();
    }
}
