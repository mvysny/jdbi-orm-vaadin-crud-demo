package com.vaadin.starter.skeleton;

import com.gitlab.mvysny.jdbiorm.Dao;
import com.gitlab.mvysny.jdbiorm.Entity;
import com.gitlab.mvysny.jdbiorm.TableProperty;
import org.jdbi.v3.core.annotation.JdbiProperty;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jetbrains.annotations.Nullable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * An example entity.
 */
public class Person implements Entity<Long> {
    private Long id;
    @NotNull
    @Size(min = 1, max = 200)
    private String name;
    @NotNull
    @Min(15)
    @Max(100)
    private Integer age;
    private LocalDate dateOfBirth;
    @NotNull
    private Instant created;
    @NotNull
    private Instant modified;
    @NotNull
    @ColumnName("alive")
    private Boolean isAlive;
    @NotNull
    private MaritalStatus maritalStatus;

    @JdbiProperty(map = false)
    public static final TableProperty<Person, Long> ID = TableProperty.of(Person.class, "id");
    @JdbiProperty(map = false)
    public static final TableProperty<Person, String> NAME = TableProperty.of(Person.class, "name");
    @JdbiProperty(map = false)
    public static final TableProperty<Person, Integer> AGE = TableProperty.of(Person.class, "age");
    @JdbiProperty(map = false)
    public static final TableProperty<Person, LocalDate> DATEOFBIRTH = TableProperty.of(Person.class, "dateOfBirth");
    @JdbiProperty(map = false)
    public static final TableProperty<Person, Instant> CREATED = TableProperty.of(Person.class, "created");
    @JdbiProperty(map = false)
    public static final TableProperty<Person, Instant> MODIFIED = TableProperty.of(Person.class, "modified");
    @JdbiProperty(map = false)
    public static final TableProperty<Person, Boolean> ISALIVE = TableProperty.of(Person.class, "isAlive");
    @JdbiProperty(map = false)
    public static final TableProperty<Person, MaritalStatus> MARITALSTATUS = TableProperty.of(Person.class, "maritalStatus");

    public enum MaritalStatus {
        Single,
        Married,
        Divorced,
        Widowed
    }

    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Nullable
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", dateOfBirth=" + dateOfBirth +
                ", created=" + created +
                ", modified=" + modified +
                ", isAlive=" + isAlive +
                ", maritalStatus=" + maritalStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void save(boolean validate) {
        // always override the save(boolean) method, and not save().
        // If you only override save(), your code is not going to be called when
        // somebody calls save(boolean).
        if (id == null) {
            created = Instant.now();
        }
        modified = Instant.now();
        Entity.super.save(validate);
    }

    @org.jetbrains.annotations.NotNull
    public static final PersonDao dao = new PersonDao();

    public static class PersonDao extends Dao<Person, Long> {

        /**
         * protected so that PersonDao won't pop up in the IDE auto-completion
         * dialog when you type in {@code new Person}.
         */
        protected PersonDao() {
            super(Person.class);
        }

        @Nullable
        public Person findByName(@org.jetbrains.annotations.NotNull String name) {
            return findSingleBy("name=:name", q -> q.bind("name", name));
        }
    }

    /**
     * Creates a dummy person the database.
     * @param i the person number
     * @return the new person, already saved in the database
     */
    @org.jetbrains.annotations.NotNull
    public static Person createDummy(int i) {
        final Person person = new Person("Jon Lord" + i, 42);
        person.setDateOfBirth(LocalDate.of(1970, 1, 12).plusDays(i));
        person.setAlive(i % 2 == 0);
        person.setMaritalStatus(Person.MaritalStatus.values()[i % MaritalStatus.values().length]);
        person.save();
        return person;
    }
}
