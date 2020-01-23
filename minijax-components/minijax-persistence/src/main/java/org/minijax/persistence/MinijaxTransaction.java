package org.minijax.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinijaxTransaction implements javax.persistence.EntityTransaction {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxTransaction.class);
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
            LOG.error("Error commiting transaction: {}", ex.getMessage(), ex);
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (final SQLException ex) {
            LOG.error("Error rolling back transaction: {}", ex.getMessage(), ex);
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setRollbackOnly() {
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
