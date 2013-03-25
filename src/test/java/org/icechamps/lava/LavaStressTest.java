package org.icechamps.lava;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.exception.MultipleElementsFoundException;
import org.icechamps.lava.interfaces.Enumerable;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: Robert.Diaz
 * Date: 3/25/13
 * Time: 5:43 PM
 */
public class LavaStressTest {

    private ArrayList<Person> people;
    private static int peopleCount;

    @Before
    public void setUp() throws Exception {
        people = new ArrayList<Person>();

        for(int i = 0; i < 100000; i++) {
            people.add(createPerson(String.format("Person-%d", i), 2));
        }

        people.add(createPerson("Person-5000", 1));
        people.add(createPerson("Person-5001", 3));

        peopleCount = people.size();
    }

    private Person createPerson(String name, int age) {
        Person p = new Person();
        p.name = name;
        p.age = age;
        p.pets = new ArrayList<Pet>(age);

        for (int i = 0; i < age; i++) {
            Pet pet = new Pet();
            pet.name = String.format("%s-%d", name, i);
            pet.owner = p;
            p.pets.add(pet);
        }

        return p;
    }

    @Test
    public void testAggregate() throws Exception {
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

        assertEquals(peopleCount * 2, aggregatedAge);
    }

    @Test
    public void testAll() throws Exception {
        boolean all = Lava.all(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return !person.name.isEmpty();
            }
        });

        assertTrue(all);
    }

    @Test
    public void testAny() throws Exception {
        Enumerable<Person> list = Lava.where(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("foo");
            }
        });

        assertFalse(list.any());

        assertFalse(Lava.any(new ArrayList<Comparable>()));
    }

    @SuppressWarnings("RedundantTypeArguments")
    @Test
    public void testCast() throws Exception {
        List unTyped = people;
        // Dev Note: The <Person> is required on the Lava call because of some weird Java generics thing
        Enumerable<Person> list = Lava.<Person>cast(unTyped);

        assertNotNull(list);
        assertTrue(list.any());
        assertTrue(list.count() == unTyped.size());
    }

    @Test
    public void testConcat() throws Exception {
        Enumerable<Person> list = Lava.concat(people, people);

        assertNotNull(list);
        assertTrue(list.any());
        assertTrue(list.count() == (people.size() * 2));
    }

    @Test
    public void testCount() throws Exception {
        assertTrue(Lava.count(people) == people.size());
    }

    @Test
    public void testDistinct() throws Exception {
        Enumerable<Person> list = Lava.distinct(people);

        assertTrue(list.count() == peopleCount);
    }

    @Test
    public void testElementAt() throws Exception {
        Person person = Lava.elementAt(people, 0);

        assertNotNull(person);
        assertTrue(person == people.get(0));
    }

    @Test
    public void testElementAtOrDefault() throws Exception {
        Person person = Lava.elementAtOrDefault(people, 0);

        assertNotNull(person);
        assertTrue(person == people.get(0));
    }

    @Test
    public void testElementAtOrDefaultWithNull() throws Exception {
        Person person = Lava.elementAtOrDefault(people, peopleCount + 1);

        assertNull(person);
    }

    @Test
    public void testFirst() throws Exception {
        Person person = Lava.first(people);

        assertEquals(person, people.get(0));
    }

    @Test
    public void testFirstWithFunc() throws Exception {
        Person person = Lava.first(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Person-0");
            }
        });

        assertEquals(person, people.get(0));
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

        assertEquals(person, people.get(0));
    }

    @Test
    public void testFirstOrDefaultWithFunc() throws Exception {
        Person person = Lava.firstOrDefault(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Foo");
            }
        });

        assertNull(person);
    }

    @Test
    public void testJoin() throws Exception {
        // Grab all the pets from all the people
        Enumerable<Pet> pets = Lava.selectMany(people, new Func<Person, Collection<Pet>>() {
            @Override
            public Collection<Pet> callback(Person person) {
                return person.pets;
            }
        });

        assertNotNull(pets);
        assertTrue(pets.any());

        Enumerable<PetOwner> petOwners = Lava.join(people, pets.toList(), new Func<Person, Person>() {
                    @Override
                    public Person callback(Person person) {
                        return person;
                    }
                }, new Func<Pet, Person>() {
                    @Override
                    public Person callback(Pet pet) {
                        return pet.owner;
                    }
                }, new Func2<Person, Pet, PetOwner>() {
                    @Override
                    public PetOwner callback(Person person, Pet pet) {
                        return new PetOwner(person, pet);
                    }
                }
        );

        assertNotNull(petOwners);
        assertTrue(petOwners.any());
    }

    //@Test
    public void testJoinWithComparator() throws Exception {
        // Grab all the pets from all the people
        Enumerable<Pet> pets = Lava.selectMany(people, new Func<Person, Collection<Pet>>() {
            @Override
            public Collection<Pet> callback(Person person) {
                return person.pets;
            }
        });

        assertNotNull(pets);
        assertTrue(pets.any());

        Enumerable<PetOwner> petOwners = Lava.join(people, pets.toList(), new Func<Person, Person>() {
                    @Override
                    public Person callback(Person person) {
                        return person;
                    }
                }, new Func<Pet, Person>() {
                    @Override
                    public Person callback(Pet pet) {
                        return pet.owner;
                    }
                }, new Func2<Person, Pet, PetOwner>() {
                    @Override
                    public PetOwner callback(Person person, Pet pet) {
                        return new PetOwner(person, pet);
                    }
                }, new Comparator<Person>() {
                    @Override
                    public int compare(Person o1, Person o2) {
                        return o1.name.compareTo(o2.name);
                    }
                }
        );

        assertNotNull(petOwners);
        assertTrue(petOwners.any());
    }

    @Test
    public void testLast() throws Exception {
        Person person = Lava.last(people);

        assertEquals(person, people.get(peopleCount - 1));
    }

    @Test
    public void testLastWithFunc() throws Exception {
        Person person = Lava.last(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Person-1");
            }
        });

        assertEquals(person, people.get(1));
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

        assertEquals(person, people.get(peopleCount - 1));
    }

    @Test
    public void testLastOrDefaultWithFunc() throws Exception {
        Person person = Lava.lastOrDefault(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Foo");
            }
        });

        assertNull(person);
    }

    @Test
    public void testMax() throws Exception {
        Person person = Lava.max(people);
        assertTrue(person.age == 3);
    }

    @Test
    public void testMaxWithFunc() throws Exception {
        int age = Lava.max(people, new Func<Person, Integer>() {
            @Override
            public Integer callback(Person person) {
                return person.age;
            }
        });

        assertTrue(age == 3);
    }

    @Test
    public void testMin() throws Exception {
        Person person = Lava.min(people);
        assertTrue(person.age == 1);
    }

    @Test
    public void testMinWithFunc() throws Exception {
        int age = Lava.min(people, new Func<Person, Integer>() {
            @Override
            public Integer callback(Person person) {
                return person.age;
            }
        });

        assertTrue(age == 1);
    }

    @Test
    public void testOrderBy() throws Exception {
        Enumerable<Person> list = Lava.orderBy(people);
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
    }

    @Test
    public void testOrderByDescending() throws Exception {
        Enumerable<Person> list = Lava.orderByDescending(people);
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
    }

    @Test
    public void testSelect() throws Exception {
        Enumerable<String> names = Lava.select(people, new Func<Person, String>() {
            public String callback(Person person) {
                return person.name;
            }
        });

        assertTrue(names.count() == peopleCount);

        for (String name : names) {
            assertTrue(!name.isEmpty());
        }
    }

    @Test
    public void testSelectMany1() throws Exception {
        Enumerable<Pet> pets = Lava.selectMany(people, new Func<Person, Collection<Pet>>() {
            @Override
            public Collection<Pet> callback(Person person) {
                return person.pets;
            }
        });

        assertTrue(pets.any());
    }

    @Test
    public void testSelectMany2() throws Exception {
        Enumerable<Pet> pets = Lava.selectMany(people, new Func2<Person, Integer, Collection<Pet>>() {
            @Override
            public Collection<Pet> callback(Person person, Integer index) {
                return index % 2 == 0 ? person.pets : null;
            }
        });

        assertTrue(pets.any());
    }

    @Test
    public void testSelectMany3() throws Exception {
        Enumerable<String> petNames = Lava.selectMany(people, new Func<Person, Collection<Pet>>() {
                    @Override
                    public Collection<Pet> callback(Person person) {
                        return person.pets;
                    }
                }, new Func2<Person, Pet, String>() {
                    @Override
                    public String callback(Person person, Pet pet) {
                        return String.format("%s owns %s", person.name, pet.name);
                    }
                }
        );

        assertTrue(petNames.any());
    }

    @Test
    public void testSequenceEqual() throws Exception {
        assertTrue(Lava.sequenceEqual(people, people));
    }

    @Test
    public void testSingle() throws Exception {
        Person person = Lava.single(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Person-4999");
            }
        });

        assertNotNull(person);
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleNoSuchElement() throws Exception {
        Lava.single(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Foo");
            }
        });
    }

    @Test(expected = MultipleElementsFoundException.class)
    public void testSingleMultipleElementsFound() throws Exception {
        Lava.single(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Person-5001");
            }
        });
    }

    @Test
    public void testSingleOrDefault() throws Exception {
        Person person = Lava.singleOrDefault(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Person-4000");
            }
        });

        assertNotNull(person);
    }

    @Test
    public void testSingleOrDefault2() throws Exception {
        Person person = Lava.singleOrDefault(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Foo");
            }
        });

        assertNull(person);
    }

    @Test
    public void testSkip() throws Exception {
        Enumerable<Person> persons = Lava.skip(people, 3);

        assertEquals(peopleCount - 3, persons.count());
    }

    @Test
    public void testTake() throws Exception {
        Enumerable<Person> persons = Lava.take(people, 3);

        assertEquals(3, persons.count());
    }

    @Test
    public void testToList() throws Exception {
        List<Person> persons = Lava.toList(people);
        assertNotNull(persons);
    }

    @Test
    public void testToSet() throws Exception {
        Set<Person> persons = Lava.toSet(people);
        assertNotNull(persons);
    }
}
