package main;

import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import exceptions.MyAcquaintanceNotFoundException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<>();
    private final DisjointSet disjointSet = new DisjointSet();
    private int blockSum = 0;
    private int tripleSum = 0;
    private final HashMap<Integer, Group> groups = new HashMap<>();
    private final HashMap<Integer, Message> messages = new HashMap<>();
    private final HashSet<Integer> visit = new HashSet<>();

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
        person1.getValue().put(id2, value);
        person2.getAcquaintance().put(id1, person1);
        person2.getValue().put(id1, value);
        //维护并查集
        disjointSet.merge(id1, id2);
        //维护tripleSum
        for (Integer id : people.keySet()) {
            MyPerson person = (MyPerson) getPerson(id);
            HashMap<Integer, Person> acquaintance = person.getAcquaintance();
            if (acquaintance.containsKey(id1) && acquaintance.containsKey(id2)) {
                tripleSum++;
            }
        }
        //维护valueSum
        for (Group g : groups.values()) {
            if (g.hasPerson(person1) && g.hasPerson(person2)) {
                ((MyGroup) g).setValueSum(g.getValueSum() + value * 2);
            }
        }
        //维护person1的bestAcquaintance
        int oldBestId = person1.getBestAcquaintance();
        int oldBestValue = person1.getValue().getOrDefault(oldBestId, 0);
        if (value > oldBestValue || (value == oldBestValue && id2 < oldBestId)) {
            person1.setBestAcquaintance(id2);
        }
        //维护person2的bestAcquaintance
        oldBestId = person2.getBestAcquaintance();
        oldBestValue = person2.getValue().getOrDefault(oldBestId, 0);
        if (value > oldBestValue || (value == oldBestValue && id1 < oldBestId)) {
            person2.setBestAcquaintance(id1);
        }
    }

    @Override
    public void modifyRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        }
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        int oldValue = person1.queryValue(person2);
        int newValue = oldValue + value;
        if (newValue > 0) {
            modifyRelationBranch1(id1, id2, value, person1, person2, newValue);
        } else {
            modifyRelationBranch2(id1, id2, person1, person2, oldValue);
        }
    }

    private void modifyRelationBranch1(
            int id1, int id2, int value, MyPerson person1, MyPerson person2, int newValue) {
        final int oldBestId1 = person1.getBestAcquaintance();
        final int oldBestValue1 = person1.getValue().getOrDefault(oldBestId1, 0);
        final int oldBestId2 = person2.getBestAcquaintance();
        final int oldBestValue2 = person2.getValue().getOrDefault(oldBestId2, 0);
        //改边
        person1.getValue().replace(id2, newValue);
        person2.getValue().replace(id1, newValue);
        //维护valueSum
        for (Group g : groups.values()) {
            if (g.hasPerson(person1) && g.hasPerson(person2)) {
                ((MyGroup) g).setValueSum(g.getValueSum() + value * 2);
            }
        }
        //维护person1的bestAcquaintance
        maintainBestAcquaintance1(id2, person1, newValue, oldBestId1, oldBestValue1);
        //维护person2的bestAcquaintance
        maintainBestAcquaintance1(id1, person2, newValue, oldBestId2, oldBestValue2);
    }

    public void maintainBestAcquaintance1(
            int id, MyPerson person, int newValue, int oldBestId, int oldBestValue) {
        if (newValue > oldBestValue || (newValue == oldBestValue && id < oldBestId)) {
            person.setBestAcquaintance(id);
        } else if (newValue < oldBestValue && id == oldBestId) { //如果把原最佳边改短了，需重新搜索最佳边
            findBestId(person);
        }
    }

    private void modifyRelationBranch2(
            int id1, int id2, MyPerson person1, MyPerson person2, int oldValue) {
        final int oldBestId1 = person1.getBestAcquaintance();
        final int oldBestId2 = person2.getBestAcquaintance();
        //维护tripleSum
        for (Integer id : people.keySet()) {
            MyPerson person = (MyPerson) getPerson(id);
            HashMap<Integer, Person> acquaintance = person.getAcquaintance();
            if (acquaintance.containsKey(id1) && acquaintance.containsKey(id2)) {
                tripleSum--;
            }
        }
        //删边
        person1.getAcquaintance().remove(id2);
        person1.getValue().remove(id2);
        person2.getAcquaintance().remove(id1);
        person2.getValue().remove(id1);
        //维护并查集
        disjointSet.add(id1);
        disjointSet.add(id2);
        visit.clear();
        dfs(id1, id1);
        if (disjointSet.getParent(id2) != id1) {
            visit.clear();
            dfs(id2, id2);
        }
        //维护blockSum
        if (disjointSet.find(id1) != disjointSet.find(id2)) {
            blockSum++;
        }
        //维护valueSum
        for (Group g : groups.values()) {
            if (g.hasPerson(person1) && g.hasPerson(person2)) {
                ((MyGroup) g).setValueSum(g.getValueSum() - oldValue * 2);
            }
        }
        //维护person1的bestAcquaintance
        maintainBestAcquaintance2(id2, person1, oldBestId1);
        //维护person2的bestAcquaintance
        maintainBestAcquaintance2(id1, person2, oldBestId2);
    }

    private void maintainBestAcquaintance2(int id, MyPerson person, int oldBestId) {
        if (oldBestId == id) { //如果原最佳边被删除，则重新搜索新最佳边
            findBestId(person);
        }
    }

    private void findBestId(MyPerson person) {
        HashMap<Integer, Integer> valueMap = person.getValue();
        int maxValue = -1;
        int bestId = Integer.MAX_VALUE;
        for (Integer i : valueMap.keySet()) {
            if (valueMap.get(i) > maxValue) {
                maxValue = valueMap.get(i);
                bestId = i;
            } else if (valueMap.get(i) == maxValue && i < bestId) {
                bestId = i;
            }
        }
        person.setBestAcquaintance(bestId);
    }

    public void dfs(int id, int par) {
        visit.add(id);
        disjointSet.setParent(id, par);
        MyPerson person = (MyPerson) getPerson(id);
        HashMap<Integer, Person> acquaintance = person.getAcquaintance();
        for (Integer i : acquaintance.keySet()) {
            if (!visit.contains(i)) {
                dfs(i, par);
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
    public void addGroup(Group group) throws EqualGroupIdException {
        int id = group.getId();
        if (groups.containsKey(id)) {
            throw new MyEqualGroupIdException(id);
        }
        groups.put(id, group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        if (group.getSize() <= 1111) {
            group.addPerson(person);
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        group.delPerson(person);
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        int id = message.getId();
        if (messages.containsKey(id)) {
            throw new MyEqualMessageIdException(id);
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(id, message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        if (message.getType() == 0 && !message.getPerson1().isLinked(message.getPerson2())) {
            throw new MyRelationNotFoundException(message.getPerson1().getId(),
                    message.getPerson2().getId());
        }
        if (message.getType() == 1 && !message.getGroup().hasPerson(message.getPerson1())) {
            throw new MyPersonIdNotFoundException(message.getPerson1().getId());
        }
        int messageValue = message.getSocialValue();
        if (message.getType() == 0) {
            message.getPerson1().addSocialValue(messageValue);
            message.getPerson2().addSocialValue(messageValue);
            messages.remove(id);
            message.getPerson2().getMessages().add(0, message);
        } else {
            MyGroup group = (MyGroup) message.getGroup();
            HashMap<Integer, Person> groupPeople = group.getPeople();
            for (Integer i : groupPeople.keySet()) {
                groupPeople.get(i).addSocialValue(messageValue);
            }
            messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }

    @Override
    public int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        MyPerson person = (MyPerson) getPerson(id);
        HashMap<Integer, Person> acquaintance = person.getAcquaintance();
        if (acquaintance.size() == 0) {
            throw new MyAcquaintanceNotFoundException(id);
        }
        return person.getBestAcquaintance();
    }

    @Override
    public int queryCoupleSum() {
        int coupleSum = 0;
        for (Integer i : people.keySet()) {
            MyPerson person1 = (MyPerson) people.get(i);
            if (person1.getAcquaintance().size() == 0) {
                continue;
            }
            MyPerson person2 = (MyPerson) people.get(person1.getBestAcquaintance());
            if (person2.getAcquaintance().size() == 0) {
                continue;
            }
            if (person2.getBestAcquaintance() == i) {
                coupleSum++;
            }
        }
        coupleSum /= 2;
        return coupleSum;
    }

    @Override
    public int modifyRelationOKTest(
            int id1, int id2, int value,
            HashMap<Integer, HashMap<Integer, Integer>> beforeData,
            HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return OkTest.modifyRelationOkTest(id1, id2, value, beforeData, afterData);
    }
}
