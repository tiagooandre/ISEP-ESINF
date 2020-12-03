package model;

import java.util.List;
import java.util.Objects;

public class Relationship {
    private String user1;
    private String user2;
    List<User> users;

    public Relationship() {

    }

    public Relationship(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relationship that = (Relationship) o;
        return (user1.equals(that.user1) && user2.equals(that.user2)) || (user2.equals(that.user1) && user1.equals(that.user2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "user1='" + user1 + '\'' +
                ", user2='" + user2 + '\'' +
                '}';
    }
}
