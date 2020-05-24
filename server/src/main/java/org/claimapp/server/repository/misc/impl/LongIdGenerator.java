package org.claimapp.server.repository.misc.impl;

import org.claimapp.server.repository.misc.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class LongIdGenerator implements IdGenerator<Long> {
    private Long iterator;
    private final Queue<Long> releasedIds;

    public LongIdGenerator(Long seed) {
        this.iterator = seed;
        this.releasedIds = new LinkedList<>();
    }

    public LongIdGenerator() {
        this(1L);
    }

    @Override
    public Long next() {
        if (!releasedIds.isEmpty())
            return releasedIds.remove();

        ++iterator;
        return iterator;
    }

    @Override
    public void release(Long id) {
        releasedIds.add(id);
    }
}
