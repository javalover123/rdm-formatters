package org.javalover123.resp.kryo;

import org.javalover123.resp.dto.Person;
import org.junit.Test;

public class KryoSerializerTest {

    @Test
    public void serialize() {
        final Person person = new Person("jack", "jack@g.com");
        final KryoSerializer<Person> kryoSerializer = new KryoSerializer<>(person.getClass());
        final byte[] bytes = kryoSerializer.serialize(person);
        System.out.println(bytes.length);
    }

}