package org.claimapp.server.dto;

public class PairDTO<T, E> {
    private T first;
    private E second;

    public PairDTO() {
    }

    public PairDTO(T first, E second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public E getSecond() {
        return second;
    }

    public void setSecond(E second) {
        this.second = second;
    }
}
