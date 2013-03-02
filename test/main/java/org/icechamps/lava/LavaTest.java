package org.icechamps.lava;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.collection.LavaList;
import org.icechamps.lava.interfaces.LavaCollection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
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

        for (int i = 0; i < 10; i++) {
            Person p = new Person();
            p.name = "Test" + i;
            p.age = i * 5;
            p.birthdate = new Date();

            people.add(p);
        }
    }

    @Test
    public void wherePersonAgeGreaterThanTen() {
        LavaCollection<Person> ret = Lava.where(people, new Func<Person, Boolean>() {
            public Boolean callback(Person person) {
                return person.age > 10;
            }
        });

        LavaCollection<String> strings = ret.select(new Func<Person, String>() {
            public String callback(Person object) {
                return object.name;
            }
        });

        Assert.assertTrue(ret.size() > 0);
    }

    @Test
    public void test() {
        LavaCollection<String> list = Lava.where(people, new Func<Person, Boolean>() {
            public Boolean callback(Person object) {
                return true;
            }
        }).distinct().select(new Func<Person, String>() {
            public String callback(Person object) {
                return null;
            }
        });

        LavaList<Person> ret = new LavaList<Person>(people).orderBy(people);
    }
}

class Person implements Comparable<Person> {
    public String name;
    public int age;
    public Date birthdate;

    @Override
    public int compareTo(Person o) {
        return o.age;
    }
}
