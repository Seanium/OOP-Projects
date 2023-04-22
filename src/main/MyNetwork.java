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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<>();
    private final DisjointSet disjointSet = new DisjointSet();
    private int blockSum = 0;

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

        person1.getAcquaintance().put(id2, person2);
        person2.getAcquaintance().put(id1, person1);
        person1.getValue().put(id2, value);
        person2.getValue().put(id1, value);
        disjointSet.merge(id1, id2);
        blockSum--;
        person1.addDegree();
        person2.addDegree();
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
        //清除旧的有向图
        for (Integer id : people.keySet()) {
            MyPerson person = (MyPerson) people.get(id);
            person.clearDirectedEdge();
        }
        //建立新的有向图
        for (Integer id1 : people.keySet()) {
            MyPerson person1 = (MyPerson) people.get(id1);
            HashMap<Integer, Person> acquaintance = person1.getAcquaintance();
            for (Integer id2 : acquaintance.keySet()) {
                MyPerson person2 = (MyPerson) acquaintance.get(id2);
                int degree1 = person1.getDegree();
                int degree2 = person2.getDegree();
                if (degree1 < degree2 || (degree1 == degree2 && id1 < id2)) {
                    person1.addDirectedEdge(person2);
                } else {
                    person2.addDirectedEdge(person1);
                }
            }
        }
        //寻找三元环
        int res = 0;
        HashSet<Integer> visit = new HashSet<>();
        for (Integer id1 : people.keySet()) {
            MyPerson person1 = (MyPerson) people.get(id1);
            HashMap<Integer, Person> directedEdge1 = person1.getDirectedEdge();
            visit.addAll(directedEdge1.keySet());
            for (Integer id2 : directedEdge1.keySet()) {
                MyPerson person2 = (MyPerson) directedEdge1.get(id2);
                HashMap<Integer, Person> directedEdge2 = person2.getDirectedEdge();
                for (Integer id3 : directedEdge2.keySet()) {
                    if (visit.contains(id3)) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    @Override
    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        if (!beforeData.equals(afterData)) {
            return false;
        }
        ArrayList<Person> peopleMap = new ArrayList<>();
        int correctResult = 0;
        //插入节点
        for (Integer id : beforeData.keySet()) {
            peopleMap.add(new MyPerson(id, "", 0));
        }
        //插入边
        for (Integer id1 : beforeData.keySet()) {
            MyPerson person1 = null;
            for (Person p : peopleMap) {
                if (p.getId() == id1) {
                    person1 = (MyPerson) p;
                    break;
                }
            }
            HashMap<Integer, Integer> acquaintanceId = beforeData.get(id1);
            for (Integer id2 : acquaintanceId.keySet()) {
                MyPerson person2 = null;
                for (Person p : peopleMap) {
                    if (p.getId() == id2) {
                        person2 = (MyPerson) p;
                        break;
                    }
                }
                if (person1 != null) {
                    person1.getAcquaintance().put(id2, person2);
                }
            }
        }
        //三元环计数
        for (int i = 0; i < peopleMap.size(); i++) {
            MyPerson person1 = (MyPerson) peopleMap.get(i);
            int id1 = person1.getId();
            HashMap<Integer, Person> acquaintance1 = person1.getAcquaintance();
            for (int j = i + 1; j < peopleMap.size(); j++) {
                MyPerson person2 = (MyPerson) peopleMap.get(j);
                int id2 = person2.getId();
                HashMap<Integer, Person> acquaintance2 = person2.getAcquaintance();
                for (int k = j + 1; k < peopleMap.size(); k++) {
                    MyPerson person3 = (MyPerson) peopleMap.get(k);
                    int id3 = person3.getId();
                    HashMap<Integer, Person> acquaintance3 = person3.getAcquaintance();
                    if (acquaintance1.containsKey(id2) && acquaintance2.containsKey(id3) &&
                            acquaintance3.containsKey(id1)) {
                        correctResult++;
                    }
                }
            }
        }
        return correctResult == result;
    }
}
