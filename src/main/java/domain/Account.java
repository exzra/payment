package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@AllArgsConstructor
public class Account {
    @Expose
    private UUID id;
    @Expose
    private long value;
    @JsonIgnore
    @Expose(serialize = false, deserialize = false)
    private final ReadWriteLock accountLock;

    public Account(UUID id, long value) {
        this.id = id;
        this.value = value;
        this.accountLock = new ReentrantReadWriteLock();
    }

    public long getValue() {
        return value;
    }

    public void setValue(long newValue) {
        value = newValue;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ReadWriteLock getAccountLock() {
        return accountLock;
    }
}
