package org.minijax.entity;

import java.util.Vector;

import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sequencing.Sequence;
import org.minijax.util.IdUtils;

public class UuidGenerator extends Sequence {
    private static final long serialVersionUID = 1L;
    public static final String ID_GENERATOR_NAME = "org.minijax.data.UuidGenerator";
    private final UuidConverter converter;

    public UuidGenerator() {
        super(ID_GENERATOR_NAME);
        converter = new UuidConverter();
    }

    @Override
    public byte[] getGeneratedValue(final Accessor accessor, final AbstractSession writeSession, final String seqName) {
        return converter.convertToDatabaseColumn(IdUtils.create());
    }

    @Override
    public Vector<byte[]> getGeneratedVector(final Accessor accessor, final AbstractSession writeSession, final String seqName, final int size) {
        final Vector<byte[]> result = new Vector<>(size);

        for (int i = 0; i < size; i++) {
            result.add(getGeneratedValue(accessor, writeSession, seqName));
        }

        return result;
    }

    @Override
    public void onConnect() {
        // no op
    }

    @Override
    public void onDisconnect() {
        // no op
    }

    @Override
    public boolean shouldAcquireValueAfterInsert() {
        return false;
    }

    @Override
    public boolean shouldUseTransaction() {
        return false;
    }

    @Override
    public boolean shouldUsePreallocation() {
        return false;
    }
}
