package hello.jdbc.pureJava;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetTest {


    @Test
    void test1(){
        Who d = new Who("1", "대호");
        Who h = new Who("1", "홍모");

        Set<Who> set = new HashSet<>();

        set.add(d);
        set.add(h);

        boolean equals = d.equals(h);

        int hashCodeD = d.hashCode();
        int hashCodeH = h.hashCode();

        List<Who> list = Stream.of(d, h).collect(Collectors.toList());

        Set<Who> collect = list.stream().collect(Collectors.toSet());

        System.out.println("collect.getClass() = " + collect.getClass());

        System.out.println("SetTest.test1");
    }

    @AllArgsConstructor
    @EqualsAndHashCode(of = "id")
    static class Who{
        private String id;

        private String name;
    }
}
