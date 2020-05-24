package org.claimapp.server.repository.misc;

public interface IdGenerator<T> {
    T next();
    void release(T id);
}
