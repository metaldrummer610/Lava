package org.icechamps.lava;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.exception.MultipleElementsFoundException;
import org.icechamps.lava.interfaces.Enumerable;
import org.icechamps.lava.util.Group;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * User: Robert.Diaz
 * Date: 3/8/13
 * Time: 10:39 AM
 */
public class LavaTest {
    private List<Person> people;

    private int peopleCount;

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

    private <T> void printList(Enumerable<T> list) {
        System.out.println("Printing List!!");

        for (T o : list) {
            System.out.println(o);
        }

        System.out.println();
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

        assertEquals(240, aggregatedAge);
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

    @Test
    public void testAverageByte() throws Exception {
        ArrayList<Byte> list = new ArrayList<Byte>();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);

        Byte average = Lava.average(list);

        assertTrue((byte) 2 == average);
    }

    @Test
    public void testAverageDouble() throws Exception {
        ArrayList<Double> list = new ArrayList<Double>();
        list.add((double) 0);
        list.add((double) 1);
        list.add((double) 2);
        list.add((double) 3);
        list.add((double) 4);

        Double average = Lava.average(list);

        assertTrue((double) 2 == average);
    }

    @Test
    public void testAverageFloat() throws Exception {
        ArrayList<Float> list = new ArrayList<Float>();
        list.add((float) 0);
        list.add((float) 1);
        list.add((float) 2);
        list.add((float) 3);
        list.add((float) 4);

        Float average = Lava.average(list);

        assertTrue((float) 2 == average);
    }

    @Test
    public void testAverageInteger() throws Exception {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        Integer average = Lava.average(list);

        assertTrue(2 == average);
    }

    @Test
    public void testAverageLong() throws Exception {
        ArrayList<Long> list = new ArrayList<Long>();
        list.add((long) 0);
        list.add((long) 1);
        list.add((long) 2);
        list.add((long) 3);
        list.add((long) 4);

        Long average = Lava.average(list);

        assertTrue((long) 2 == average);
    }

    @Test
    public void testAverageShort() throws Exception {
        ArrayList<Short> list = new ArrayList<Short>();
        list.add((short) 0);
        list.add((short) 1);
        list.add((short) 2);
        list.add((short) 3);
        list.add((short) 4);

        Short average = Lava.average(list);

        assertTrue((short) 2 == average);
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

        printList(list);

        assertTrue(list.count() == peopleCount - 1);
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
        Person person = Lava.elementAtOrDefault(people, 100);

        assertNull(person);
    }

    @Test
    public void testExcept() throws Exception {
        ArrayList<Double> numbers1 = new ArrayList<Double>();
        numbers1.add(2.0);
        numbers1.add(2.1);
        numbers1.add(2.2);
        numbers1.add(2.3);
        numbers1.add(2.4);
        numbers1.add(2.5);

        ArrayList<Double> numbers2 = new ArrayList<Double>();
        numbers2.add(2.2);

        Enumerable<Double> list = Lava.except(numbers1, numbers2);

        assertNotNull(list);
        assertTrue(list.any());
        assertTrue(list.count() == numbers1.size() - 1);
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
                return person.name.equals("Robbie");
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
    public void testGroupBy1() throws Exception {
        Enumerable<Group<Integer, Person>> list = Lava.groupBy(people, new Func<Person, Integer>() {
            @Override
            public Integer callback(Person person) {
                return person.name.length();
            }
        });

        assertNotNull(list);
        assertTrue(list.count() == 5);

        for (Group<Integer, Person> group : list) {
            System.out.println(group.getKey());
            assertFalse(group.getValues().isEmpty());

            for (Person person : group.getValues())
                System.out.println("\t" + person.toString());
        }
    }

    @Test
    public void testGroupBy2() throws Exception {
        Enumerable<Group<Integer, String>> list = Lava.groupBy(people, new Func<Person, Integer>() {
                    @Override
                    public Integer callback(Person person) {
                        return person.name.length();
                    }
                }, new Func<Person, String>() {
                    @Override
                    public String callback(Person person) {
                        return person.name;
                    }
                }
        );

        assertNotNull(list);
        assertTrue(list.count() == 5);

        for (Group<Integer, String> group : list) {
            System.out.println(group.getKey());
            assertFalse(group.getValues().isEmpty());

            for (String name : group.getValues())
                System.out.println("\t" + name);
        }
    }

    @Test
    public void testGroupBy3() throws Exception {
        Enumerable<Integer> list = Lava.groupBy(people, new Func<Person, Integer>() {
                    @Override
                    public Integer callback(Person person) {
                        return person.name.length();
                    }
                }, new Func2<Integer, Collection<Person>, Integer>() {
                    @Override
                    public Integer callback(Integer integer, Collection<Person> persons) {
                        return integer;
                    }
                }
        );

        assertNotNull(list);
        assertTrue(list.count() == 5);

        for (Integer integer : list) {
            System.out.println(integer);
        }
    }

    @Test
    public void testGroupBy4() throws Exception {
        Enumerable<Integer> list = Lava.groupBy(people, new Func<Person, Integer>() {
                    @Override
                    public Integer callback(Person person) {
                        return person.name.length();
                    }
                }, new Func<Person, String>() {
                    @Override
                    public String callback(Person person) {
                        return person.name;
                    }
                }, new Func2<Integer, Collection<String>, Integer>() {
                    @Override
                    public Integer callback(Integer integer, Collection<String> strings) {
                        return integer + strings.size();
                    }
                }
        );

        assertNotNull(list);
        assertTrue(list.count() == 5);

        for (Integer integer : list) {
            System.out.println(integer);
        }
    }

    @Test
    public void testIntersect() throws Exception {
        // Populate the test lists
        ArrayList<String> first = new ArrayList<String>();
        ArrayList<String> second = new ArrayList<String>();

        first.add("a");
        first.add("b");
        first.add("c");
        first.add("d");

        second.add("a");
        second.add("b");
        second.add("c");

        Enumerable<String> strings = Lava.intersect(first, second);

        assertNotNull(strings);
        assertTrue(strings.any());
        assertTrue(strings.count() == 3);
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

    @Test
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

        printList(petOwners);
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
                return person.name.equals("Robbie");
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
        assertTrue(person.age == 44);
    }

    @Test
    public void testMaxWithFunc() throws Exception {
        int age = Lava.max(people, new Func<Person, Integer>() {
            @Override
            public Integer callback(Person person) {
                return person.age;
            }
        });

        assertTrue(age == 44);
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

        assertTrue(names.count() == peopleCount);

        printList(names);

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
                return person.name.equals("Alex");
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
                return person.name.equals("Robbie");
            }
        });
    }

    @Test
    public void testSingleOrDefault() throws Exception {
        Person person = Lava.singleOrDefault(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.name.equals("Alex");
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
    public void testSkipWhile() throws Exception {
        Enumerable<Person> persons = Lava.skipWhile(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.age > 20;
            }
        });

        assertEquals(peopleCount - 4, persons.count());
    }

    @Test
    public void testSumByte() throws Exception {
        ArrayList<Byte> list = new ArrayList<Byte>();
        list.add((byte) 0);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        list.add((byte) 4);

        Byte sum = Lava.sum(list);

        assertTrue((byte) 10 == sum);
    }

    @Test
    public void testSumDouble() throws Exception {
        ArrayList<Double> list = new ArrayList<Double>();
        list.add((double) 0);
        list.add((double) 1);
        list.add((double) 2);
        list.add((double) 3);
        list.add((double) 4);

        Double sum = Lava.sum(list);

        assertTrue((double) 10 == sum);
    }

    @Test
    public void testSumFloat() throws Exception {
        ArrayList<Float> list = new ArrayList<Float>();
        list.add((float) 0);
        list.add((float) 1);
        list.add((float) 2);
        list.add((float) 3);
        list.add((float) 4);

        Float sum = Lava.sum(list);

        assertTrue((float) 10 == sum);
    }

    @Test
    public void testSumInteger() throws Exception {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        Integer sum = Lava.sum(list);

        assertTrue(10 == sum);
    }

    @Test
    public void testSumLong() throws Exception {
        ArrayList<Long> list = new ArrayList<Long>();
        list.add((long) 0);
        list.add((long) 1);
        list.add((long) 2);
        list.add((long) 3);
        list.add((long) 4);

        Long sum = Lava.sum(list);

        assertTrue((long) 10 == sum);
    }

    @Test
    public void testSumShort() throws Exception {
        ArrayList<Short> list = new ArrayList<Short>();
        list.add((short) 0);
        list.add((short) 1);
        list.add((short) 2);
        list.add((short) 3);
        list.add((short) 4);

        Short sum = Lava.sum(list);

        assertTrue((short) 10 == sum);
    }

    @Test
    public void testTake() throws Exception {
        Enumerable<Person> persons = Lava.take(people, 3);

        assertEquals(3, persons.count());
    }

    @Test
    public void testTakeWhile() throws Exception {
        Enumerable<Person> persons = Lava.takeWhile(people, new Func<Person, Boolean>() {
            @Override
            public Boolean callback(Person person) {
                return person.age > 20;
            }
        });

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

    @Test
    public void testWhere() throws Exception {
        Enumerable<Person> ret = Lava.where(people, new Func<Person, Boolean>() {
            public Boolean callback(Person person) {
                return person.age > 10;
            }
        });
        assertTrue(ret.count() == 8);

        printList(ret);
    }

    @Test
    public void testUnion() throws Exception {
        ArrayList<Integer> ints1 = new ArrayList<Integer>();

        ints1.add(5);
        ints1.add(3);
        ints1.add(9);
        ints1.add(7);
        ints1.add(5);
        ints1.add(9);
        ints1.add(3);
        ints1.add(7);

        ArrayList<Integer> ints2 = new ArrayList<Integer>();

        ints2.add(8);
        ints2.add(3);
        ints2.add(6);
        ints2.add(4);
        ints2.add(4);
        ints2.add(9);
        ints2.add(1);
        ints2.add(0);

        Enumerable<Integer> ints = Lava.union(ints1, ints2);

        assertNotNull(ints);
        assertTrue(ints.any());
        assertTrue(ints.count() == 9);
    }

    @Test
    public void testZip() throws Exception {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("One");
        strings.add("Two");
        strings.add("Three");

        ArrayList<Integer> ints = new ArrayList<Integer>();
        ints.add(1);
        ints.add(2);
        ints.add(3);

        Enumerable<String> zipped = Lava.zip(strings, ints, new Func2<String, Integer, String>() {
            @Override
            public String callback(String s, Integer i) {
                return String.format("%s %d", s, i);
            }
        });

        assertNotNull(zipped);
        assertTrue(zipped.any());
        assertTrue(zipped.count() == 3);
    }
}
