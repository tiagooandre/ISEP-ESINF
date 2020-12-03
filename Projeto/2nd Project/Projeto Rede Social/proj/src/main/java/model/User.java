package model;

import java.util.Objects;

public class User {
    private String user;
    private int age;
    private String city;
    private int numberOfFriends;

    public User() {
        user=null;
        age=0;
        city=null;
    }

    public User(String user, int age, String city) {
        this.user = user;
        this.age = age;
        this.city = city;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumberOfFriends() {
        return numberOfFriends;
    }

    public void setNumberOfFriends(int numberOfFriends) {
        this.numberOfFriends = numberOfFriends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User tempuser = (User) o;
        return user.equals(tempuser.user) &&
                age==tempuser.age && city.equals(tempuser.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, age, city);
    }

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", numberOfFriends=" + numberOfFriends +
                '}';
    }
}
