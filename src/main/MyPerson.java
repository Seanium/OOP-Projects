package main;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Person> acquaintance = new HashMap<>();
    private final HashMap<Integer, Integer> value = new HashMap<>();
    private int socialValue = 0;
    private final ArrayList<Message> messages = new ArrayList<>();

    private int bestAcquaintance = Integer.MAX_VALUE;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Person && ((Person) obj).getId() == id;
    }

    @Override
    public boolean isLinked(Person person) {
        return person.getId() == id || acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        return value.getOrDefault(person.getId(), 0);
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        return messages.size() < 5 ? messages : messages.subList(0, 5);
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public HashMap<Integer, Integer> getValue() {
        return value;
    }

    public int getBestAcquaintance() {
        return bestAcquaintance;
    }

    public void setBestAcquaintance(int bestAcquaintance) {
        this.bestAcquaintance = bestAcquaintance;
    }
}
