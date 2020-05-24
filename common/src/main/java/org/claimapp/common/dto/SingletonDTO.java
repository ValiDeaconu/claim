package org.claimapp.common.dto;

public class SingletonDTO<T> {

    private T content;

    public SingletonDTO() { }

    public SingletonDTO(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
