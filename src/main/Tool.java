package main;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import exceptions.MyEmojiIdNotFoundException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Tool {
    public static int deleteColdEmojiOkTest(
            int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
            ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        final HashSet<Integer> oldEmojiIdList = new HashSet<>(beforeData.get(0).keySet());
        final HashMap<Integer, Integer> oldEmojiHeatList = beforeData.get(0);
        final HashMap<Integer, Integer> oldMessages = beforeData.get(1);
        final HashSet<Integer> emojiIdList = new HashSet<>(afterData.get(0).keySet());
        final HashMap<Integer, Integer> emojiHeatList = afterData.get(0);
        final HashMap<Integer, Integer> messages = afterData.get(1);
        for (Integer id : oldEmojiIdList) {
            if (oldEmojiHeatList.get(id) >= limit && !emojiIdList.contains(id)) {
                return 1;
            }
        }
        for (Integer id : emojiIdList) {
            if (!oldEmojiIdList.contains(id) ||
                    !emojiHeatList.get(id).equals(oldEmojiHeatList.get(id))) {
                return 2;
            }
        }
        int num = 0;
        for (Integer id : oldEmojiIdList) {
            if (oldEmojiHeatList.get(id) >= limit) {
                num++;
            }
        }
        if (emojiIdList.size() != num) {
            return 3;
        }
        if (emojiIdList.size() != emojiHeatList.size()) {
            return 4;
        }
        for (Integer messageId : oldMessages.keySet()) {
            Integer emojiId = oldMessages.get(messageId);
            if (emojiId != null && emojiIdList.contains(emojiId)) {
                if (!messages.containsKey(messageId) || !messages.get(messageId).equals(emojiId)) {
                    return 5;
                }
            } else if (emojiId == null) {
                if (!messages.containsKey(messageId) || messages.get(messageId) != null) {
                    return 6;
                }
            }
        }
        num = 0;
        for (Integer messageId : oldMessages.keySet()) {
            Integer emojiId = oldMessages.get(messageId);
            if ((emojiId != null && emojiIdList.contains(emojiId))) {
                num++;
            } else if (emojiId == null) {
                num++;
            }
        }
        if (messages.size() != num) {
            return 7;
        }
        if (result != emojiIdList.size()) {
            return 8;
        }
        return 0;
    }

    public static boolean containsMessage(int id, HashMap<Integer, Message> messages) {
        return messages.containsKey(id);
    }

    public static void addMessage(Message message, HashMap<Integer, Message> messages,
                                  HashSet<Integer> emojiIdList) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        int id = message.getId();
        if (messages.containsKey(id)) {
            throw new MyEqualMessageIdException(id);
        }
        if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId(), emojiIdList)) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(id, message);
    }

    public static Message getMessage(int id, HashMap<Integer, Message> messages) {
        return messages.get(id);
    }

    public static void sendMessage(int id, HashMap<Integer, Message> messages,
                                   HashMap<Integer, Integer> emojiHeatList) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id, messages)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id, messages);
        if (message.getType() == 0 && !message.getPerson1().isLinked(message.getPerson2())) {
            throw new MyRelationNotFoundException(message.getPerson1().getId(),
                    message.getPerson2().getId());
        }
        if (message.getType() == 1 && !message.getGroup().hasPerson(message.getPerson1())) {
            throw new MyPersonIdNotFoundException(message.getPerson1().getId());
        }
        int messageValue = message.getSocialValue();
        Person person1 = message.getPerson1();
        if (message.getType() == 0) {
            Person person2 = message.getPerson2();
            person1.addSocialValue(messageValue);
            person2.addSocialValue(messageValue);
            person2.getMessages().add(0, message);
            if (message instanceof RedEnvelopeMessage) {
                person1.addMoney(-((RedEnvelopeMessage) message).getMoney());
                person2.addMoney(((RedEnvelopeMessage) message).getMoney());
            }
        } else {
            HashMap<Integer, Person> groupPeople = ((MyGroup) message.getGroup()).getPeople();
            for (Person p : groupPeople.values()) {
                p.addSocialValue(messageValue);
            }
            if (message instanceof RedEnvelopeMessage) {
                int perMoney = ((RedEnvelopeMessage) message).getMoney() / groupPeople.size();
                person1.addMoney(-groupPeople.size() * perMoney);
                for (Person p : groupPeople.values()) {
                    p.addMoney(perMoney);
                }
            }
        }
        if (message instanceof EmojiMessage) {
            int emojiId = ((EmojiMessage) message).getEmojiId();
            emojiHeatList.replace(emojiId, emojiHeatList.get(emojiId) + 1);
        }
        messages.remove(id);
    }

    public static boolean containsEmojiId(int id, HashSet<Integer> emojiIdList) {
        return emojiIdList.contains(id);
    }

    public static int deleteColdEmoji(int limit,
                               HashSet<Integer> emojiIdList,
                               HashMap<Integer, Integer> emojiHeatList,
                               HashMap<Integer, Message> messages) {
        Iterator<Integer> integerIterator = emojiIdList.iterator();
        //先删除emoji
        while (integerIterator.hasNext()) {
            Integer emojiId = integerIterator.next();
            if (emojiHeatList.get(emojiId) < limit) {
                emojiHeatList.remove(emojiId);
                integerIterator.remove();
            }
        }
        //再删除消息
        Iterator<Map.Entry<Integer, Message>> entryIterator = messages.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Message message = entryIterator.next().getValue();
            if (message instanceof EmojiMessage &&
                    !containsEmojiId(((EmojiMessage) message).getEmojiId(), emojiIdList)) {
                entryIterator.remove();
            }
        }
        return emojiIdList.size();
    }
}
