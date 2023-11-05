package main;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private final HashMap<Integer, Person> people = new HashMap<>();

    private int valueSum = 0;
    private int ageSum = 0;
    private int agePowSum = 0;

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Group && ((Group) obj).getId() == id;
    }

    @Override
    public void addPerson(Person person) {
        //维护valueSum
        int value = 0;
        for (Person p : people.values()) {
            if (person.isLinked(p)) {
                value += person.queryValue(p);
            }
        }
        valueSum += value * 2;

        people.put(person.getId(), person);

        //维护age
        int age = person.getAge();
        ageSum += age;
        agePowSum += age * age;
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {  //维护valueSum
        return valueSum;
    }

    @Override
    public int getAgeMean() {  //维护ageSum
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
    }

    @Override
    public int getAgeVar() {  //维护ageSum和agePowSum
        int size = people.size();
        if (size == 0) {
            return 0;
        }
        return (agePowSum - 2 * getAgeMean() * ageSum + size * getAgeMean() * getAgeMean()) / size;
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());

        //维护valueSum
        int value = 0;
        for (Person p : people.values()) {
            if (person.isLinked(p)) {
                value += person.queryValue(p);
            }
        }
        valueSum -= value * 2;

        //维护age
        int age = person.getAge();
        ageSum -= age;
        agePowSum -= age * age;
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public HashMap<Integer, Person> getPeople() {
        return people;
    }

    public void setValueSum(int valueSum) {
        this.valueSum = valueSum;
    }
}
