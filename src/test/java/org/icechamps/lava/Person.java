package org.icechamps.lava;

import java.util.ArrayList;

/**
 * User: Robert.Diaz
 * Date: 3/8/13
 * Time: 10:40 AM
 */
public class Person implements Comparable<Person> {
    public String name;
    public int age;
    public ArrayList<Pet> pets;

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
                ", pets=" + pets +
                '}';
    }
}
