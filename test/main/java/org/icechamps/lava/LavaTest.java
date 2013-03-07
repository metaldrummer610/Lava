package org.icechamps.lava;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.interfaces.Enumerable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Robert.Diaz
 * Date: 2/27/13
 * Time: 4:25 PM
 */
public class LavaTest {

    private static List<Person> people;

    @BeforeClass
    public static void setup() {
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
    public void testWherePersonAgeGreaterThanTen() {
        Enumerable<Person> ret = Lava.where(people, new Func<Person, Boolean>() {
            public Boolean callback(Person person) {
                return person.age > 10;
            }
        });
        Assert.assertTrue(ret.count() == 8);

        printList(ret);
    }

    @Test
    public void testSelect() {
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
    public void testToList() {
        List<Person> persons = Lava.toList(people);
        Assert.assertNotNull(persons);
    }

    @Test
    public void testOrderBy() throws Exception {
        Enumerable<Person> list = Lava.orderBy(people);

        printList(list);
    }

    @Test
    public void testOrderByDescending() throws Exception {
        Enumerable<Person> list = Lava.orderByDescending(people);

        printList(list);
    }

    @Test
    public void testDistinct() throws Exception {
        Enumerable<Person> list = Lava.distinct(people);

        printList(list);

        Assert.assertTrue(list.count() == 8);
    }

    @Test
    public void testAny() throws Exception {
        Enumerable<Person> list = Lava.where(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name == "foo";
            }
        });

        Assert.assertFalse(list.any());
    }
}

class Person implements Comparable<Person> {
    public String name;
    public int age;

    @Override
    public int compareTo(Person o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        //this optimization is usually worthwhile, and can
        //always be added
        if (this == o) return EQUAL;

        //primitive numbers follow this form
        if (this.age < o.age) return BEFORE;
        if (this.age > o.age) return AFTER;

        int comparison = this.name.compareTo(o.name);
        if (comparison != EQUAL) return comparison;

        //all comparisons have yielded equality
        //verify that compareTo is consistent with equals (optional)
        assert this.equals(o) : "compareTo inconsistent with equals.";

        return EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        if (!name.equals(person.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
