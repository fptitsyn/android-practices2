package ru.mirea.ptitsyn.domain.models;

public final class User {
    private final String id;
    private final String name;
    private final String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name == null ? "" : name;
        this.email = email == null ? "" : email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
