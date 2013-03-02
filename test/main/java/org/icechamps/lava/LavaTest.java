package org.icechamps.lava;

import org.icechamps.lava.collection.LavaList;
import org.icechamps.lava.callback.MatchOneCallback;
import org.icechamps.lava.callback.SelectOneCallback;
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

		for(int i = 0; i < 10; i++) {
			Person p = new Person();
			p.name = "Test" + i;
			p.age = i * 5;
			p.birthdate = new Date();

			people.add(p);
		}
	}

	@Test
	public void wherePersonAgeGreaterThanTen() {
		LavaList<Person> ret = Lava.where(people, new MatchOneCallback<Person>() {
			public boolean matches(Person person) {
				return person.age > 10;
			}
		});

		LavaList<String> strings = ret.select(ret, new SelectOneCallback<Person, String>() {
			public String select(Person object) {
				return object.name;
			}
		});

		Assert.assertTrue(ret.size() > 0);
	}

	@Test
	public void test() {
		LavaList<String> list = Lava.where(people, new MatchOneCallback<Person>() {
			@Override
			public boolean matches(Person object) {
				return true;
			}
		}).distinct().select(new SelectOneCallback<Person, String>() {
			@Override
			public String select(Person object) {
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
