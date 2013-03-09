package org.icechamps.lava;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.interfaces.Enumerable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * User: Robert.Diaz
 * Date: 3/8/13
 * Time: 10:39 AM
 */
public class LavaTest {
    private static List<Person> people;

    @Before
    public void setUp() throws Exception {
        people = new ArrayList<Person>();

        people.add(createPerson("Robbie", 23));
        people.add(createPerson("Robbie", 23));
        people.add(createPerson("Stephanie", 24));
        people.add(createPerson("Alex", 1));
        people.add(createPerson("Todd", 34));
        people.add(createPerson("Mark", 21));
        people.add(createPerson("Dan", 28));
        people.add(createPerson("Justin", 42));
        people.add(createPerson("Brian", 44));
    }

    private static Person createPerson(String name, int age) {
        Person p = new Person();
        p.name = name;
        p.age = age;

        return p;
    }

    private static <T> void printList(Enumerable<T> list) {
        System.out.println("Printing List!!");

        for (T o : list) {
            System.out.println(o);
        }

        System.out.println();
    }

    @Test
    public void testAggregate() throws Exception {
        Assert.assertNotNull(people);
        int aggregatedAge = Lava.aggregate(people, new Func2<Person, Integer, Integer>() {
            @Override
            public Integer callback(Person person, Integer i) {
                if (i == null)
                    return person.age;

                if (person == null)
                    throw new NullPointerException("Person is null");

                return person.age + i;
            }
        });

        Assert.assertEquals("Aggregate", aggregatedAge, 240);
    }

    @Test
    public void testAll() throws Exception {
        boolean all = Lava.all(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return !person.name.isEmpty();
            }
        });

        Assert.assertTrue(all);
    }

    @Test
    public void testAny() throws Exception {
        Enumerable<Person> list = Lava.where(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("foo");
            }
        });

        Assert.assertFalse(list.any());
    }

    @Test
    public void testCount() throws Exception {
        Assert.assertTrue(Lava.count(people) == people.size());
    }

    @Test
    public void testDistinct() throws Exception {
        Enumerable<Person> list = Lava.distinct(people);

        printList(list);

        Assert.assertTrue(list.count() == 8);
    }

    @Test
    public void testFirst() throws Exception {
        Person person = Lava.first(people);

        Assert.assertEquals(person, people.get(0));
    }

    @Test
    public void testFirstWithFunc() throws Exception {
        Person person = Lava.first(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Robbie");
            }
        });

        Assert.assertEquals(person, people.get(0));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFirstWithFuncException() throws Exception {
        Lava.first(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Foo");
            }
        });
    }

    @Test
    public void testFirstOrDefault() throws Exception {
        Person person = Lava.firstOrDefault(people);

        Assert.assertEquals(person, people.get(0));
    }

    @Test
    public void testFirstOrDefaultWithFunc() throws Exception {
        Person person = Lava.firstOrDefault(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Foo");
            }
        });

        Assert.assertNull(person);
    }

    @Test
    public void testIntersect() throws Exception {

    }

    @Test
    public void testJoin() throws Exception {

    }

    @Test
    public void testLast() throws Exception {
        Person person = Lava.last(people);

        Assert.assertEquals(person, people.get(8));
    }

    @Test
    public void testLastWithFunc() throws Exception {
        Person person = Lava.last(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Robbie");
            }
        });

        Assert.assertEquals(person, people.get(1));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastWithFuncException() throws Exception {
        Lava.last(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Foo");
            }
        });
    }

    @Test
    public void testLastOrDefault() throws Exception {
        Person person = Lava.lastOrDefault(people);

        Assert.assertEquals(person, people.get(8));
    }

    @Test
    public void testLastOrDefaultWithFunc() throws Exception {
        Person person = Lava.lastOrDefault(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Foo");
            }
        });

        Assert.assertNull(person);
    }

    @Test
    public void testMax() throws Exception {
        Person person = Lava.max(people);
        Assert.assertTrue(person.age == 44);
    }

    @Test
    public void testMaxWithFunc() throws Exception {
        int age = Lava.max(people, new Func<Person, Integer>() {
            @Override
            public Integer callback(Person person) {
                return person.age;
            }
        });

        Assert.assertTrue(age == 44);
    }

    @Test
    public void testMin() throws Exception {
        Person person = Lava.min(people);
        Assert.assertTrue(person.age == 1);
    }

    @Test
    public void testMinWithFunc() throws Exception {
        int age = Lava.min(people, new Func<Person, Integer>() {
            @Override
            public Integer callback(Person person) {
                return person.age;
            }
        });

        Assert.assertTrue(age == 1);
    }

    @Test
    public void testOrderBy() throws Exception {
        Enumerable<Person> list = Lava.orderBy(people);

        printList(list);
    }

    @Test
    public void testOrderByWithComparator() throws Exception {
        Enumerable<Person> list = Lava.orderBy(people, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.age < o2.age) return -1;
                if (o1.age > o2.age) return 1;

                return 0;
            }
        });

        printList(list);
    }

    @Test
    public void testOrderByDescending() throws Exception {
        Enumerable<Person> list = Lava.orderByDescending(people);

        printList(list);
    }

    @Test
    public void testOrderByDescendingWithComparator() throws Exception {
        Enumerable<Person> list = Lava.orderByDescending(people, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.age < o2.age) return -1;
                if (o1.age > o2.age) return 1;

                return 0;
            }
        });

        printList(list);
    }

    @Test
    public void testSelect() throws Exception {
        Enumerable<String> names = Lava.select(people, new Func<Person, String>() {
            public String callback(Person person) {
                return person.name;
            }
        });

        Assert.assertTrue(names.count() == 9);

        printList(names);

        for (String name : names) {
            Assert.assertTrue(!name.isEmpty());
        }
    }

    @Test
    public void testSelectMany() throws Exception {

    }

    @Test
    public void testSequenceEqual() throws Exception {

    }

    @Test
    public void testSingle() throws Exception {

    }

    @Test
    public void testSingleOrDefault() throws Exception {

    }

    @Test
    public void testSkip() throws Exception {

    }

    @Test
    public void testSkipWhile() throws Exception {

    }

    @Test
    public void testSumByte() throws Exception {

    }

    @Test
    public void testSumDouble() throws Exception {

    }

    @Test
    public void testSumFloat() throws Exception {

    }

    @Test
    public void testSumInteger() throws Exception {

    }

    @Test
    public void testSumLong() throws Exception {

    }

    @Test
    public void testSumShort() throws Exception {

    }

    @Test
    public void testSumGeneric() throws Exception {

    }

    @Test
    public void testTake() throws Exception {

    }

    @Test
    public void testTakeWhile() throws Exception {

    }

    @Test
    public void testToList() throws Exception {
        List<Person> persons = Lava.toList(people);
        Assert.assertNotNull(persons);
    }

    @Test
    public void testToSet() throws Exception {
        Set<Person> persons = Lava.toSet(people);
        Assert.assertNotNull(persons);
    }

    @Test
    public void testWhere() throws Exception {
        Enumerable<Person> ret = Lava.where(people, new Func<Person, Boolean>() {
            public Boolean callback(Person person) {
                return person.age > 10;
            }
        });
        Assert.assertTrue(ret.count() == 8);

        printList(ret);
    }
}
