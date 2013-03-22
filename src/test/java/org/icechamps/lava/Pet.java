package org.icechamps.lava;

/**
 * User: Robert.Diaz
 * Date: 3/9/13
 * Time: 9:51 AM
 */
public class Pet implements Comparable<Pet> {
    public String name;
    public Person owner;

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pet pet = (Pet) o;

        if (!name.equals(pet.name)) return false;
        if (!owner.equals(pet.owner)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Pet o) {
        return name.compareTo(o.name);
    }
}
