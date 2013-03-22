package org.icechamps.lava;

/**
 * User: Robert.Diaz
 * Date: 3/22/13
 * Time: 11:12 AM
 */
public class PetOwner implements Comparable<PetOwner> {
    public Person person;
    public Pet pet;

    public PetOwner(Person person, Pet pet) {
        this.person = person;
        this.pet = pet;
    }

    @Override
    public int compareTo(PetOwner o) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == o) return EQUAL;

        int comparison = this.person.compareTo(o.person);
        if (comparison != EQUAL) return comparison;

        comparison = this.pet.compareTo(o.pet);
        if (comparison != EQUAL) return comparison;

        assert this.equals(o) : "compareTo inconsistent with equals.";

        return EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PetOwner petOwner = (PetOwner) o;

        if (!person.equals(petOwner.person)) return false;
        if (!pet.equals(petOwner.pet)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = person.hashCode();
        result = 31 * result + pet.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PetOwner{" +
                "person.name=" + person.name +
                ", pet.name=" + pet.name +
                '}';
    }
}
