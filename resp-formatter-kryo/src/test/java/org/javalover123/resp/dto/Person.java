package org.javalover123.resp.dto;

/**
 * Demo class
 *
 * @author javalover123
 * @date 2022/4/29
 */
public class Person {

    private String name;

    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
