package main;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.HashMap;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<>();
    private final DisjointSet disjointSet = new DisjointSet();
    private int blockSum = 0;
    private int tripleSum = 0;

    public MyNetwork() {

    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int id = person.getId();
        if (contains(id)) {
            throw new MyEqualPersonIdException(id);
        }
        people.put(id, person);
        disjointSet.add(id);
        blockSum++;
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);

        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (person1.isLinked(person2)) {
            throw new MyEqualRelationException(id1, id2);
        }
        //维护blockSum
        if (disjointSet.find(id1) != disjointSet.find(id2)) {
            blockSum--;
        }
        //加入边
        person1.getAcquaintance().put(id2, person2);
        person2.getAcquaintance().put(id1, person1);
        person1.getValue().put(id2, value);
        person2.getValue().put(id1, value);
        disjointSet.merge(id1, id2);
        //维护tripleSum
        for (Integer id : people.keySet()) {
            MyPerson person = (MyPerson) getPerson(id);
            HashMap<Integer, Person> acquaintance = person.getAcquaintance();
            if (acquaintance.containsKey(id1) && acquaintance.containsKey(id2)) {
                tripleSum++;
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return person1.queryValue(person2);
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return disjointSet.find(id1) == disjointSet.find(id2);
    }

    @Override
    public int queryBlockSum() {
        return blockSum;
    }

    @Override
    public int queryTripleSum() {
        return tripleSum;
    }

    @Override
    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        if (!beforeData.equals(afterData)) {
            return false;
        }
        int correctResult = 0;
        HashMap<Integer, Integer> idxIdMap = new HashMap<>();
        int idx = 0;
        for (Integer id : beforeData.keySet()) {
            idxIdMap.put(idx++, id);
        }
        int size = idxIdMap.size();
        for (int i = 0; i < size; i++) {
            int id1 = idxIdMap.get(i);
            for (int j = i + 1; j < size; j++) {
                int id2 = idxIdMap.get(j);
                for (int k = j + 1; k < size; k++) {
                    int id3 = idxIdMap.get(k);
                    if (beforeData.get(id1).containsKey(id2) && beforeData.get(id2).containsKey(id3)
                            && beforeData.get(id3).containsKey(id1)) {
                        correctResult++;
                    }
                }
            }
        }
        return correctResult == result;
    }
}
