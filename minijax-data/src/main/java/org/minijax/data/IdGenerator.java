package org.minijax.data;

import java.util.Vector;

import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sequencing.Sequence;
import org.minijax.util.IdUtils;

public class IdGenerator extends Sequence {
    private static final long serialVersionUID = 1L;
    public static final String ID_GENERATOR_NAME = "org.minijax.data.IdGenerator";

    public IdGenerator() {
        super(ID_GENERATOR_NAME);
    }

    @Override
    public Object getGeneratedValue(final Accessor accessor, final AbstractSession writeSession, final String seqName) {
        return IdUtils.create();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector getGeneratedVector(final Accessor accessor, final AbstractSession writeSession, final String seqName, final int size) {
        throw new UnsupportedOperationException();
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
