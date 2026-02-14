import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TabooTest {
    @Test
    public void testNoFollow1() {
        List<Integer> list = Arrays.asList(1, 1, 5, 1, 2, 5, 2);
        Taboo<Integer> t = new Taboo<Integer>(list);
        Set<Integer> s = new HashSet<>();
        s.add(1);
        s.add(2);
        s.add(5);
        assertEquals(s, t.noFollow(1));

        s.clear();
        s.add(5);
        assertEquals(s, t.noFollow(2));

        s.clear();
        s.add(1);
        s.add(2);
        assertEquals(s, t.noFollow(5));
    }

    @Test
    public void testNoFollow2() {
        List<Integer> list = Arrays.asList(1, 2, null, 1, 3);
        Taboo<Integer> t = new Taboo<Integer>(list);
        Set<Integer> s = new HashSet<>();
        s.add(2);
        s.add(3);
        assertEquals(s, t.noFollow(1));

        s.clear();
        assertEquals(s, t.noFollow(2));
        assertEquals(s, t.noFollow(3));
    }

    @Test
    public void testNoFollow3() {
        List<String> list = Arrays.asList("ab", "ab", "cd", "gh");
        Taboo<String> t = new Taboo<String>(list);
        Set<String> s = new HashSet<>();
        s.add("ab");
        s.add("cd");
        assertEquals(s, t.noFollow("ab"));

        s.clear();
        s.add("gh");
        assertEquals(s, t.noFollow("cd"));

        s.clear();
        assertEquals(s, t.noFollow("gh"));
    }

    @Test
    public void testNoFollow4() {
        List<Character> list = Arrays.asList('a', 'c', null, 'a', 'b');
        Taboo<Character> t = new Taboo<Character>(list);
        Set<Character> s = new HashSet<>();
        s.add('b');
        s.add('c');
        assertEquals(s, t.noFollow('a'));

        s.clear();
        assertEquals(s, t.noFollow('b'));
        assertEquals(s, t.noFollow('c'));
    }

    @Test
    public void testNoFollow5() {
        List<Character> list = List.of();
        Taboo<Character> t = new Taboo<Character>(list);
        assertEquals(Collections.emptySet(), t.noFollow('a'));
        assertEquals(Collections.emptySet(), t.noFollow('b'));
        assertEquals(Collections.emptySet(), t.noFollow(' '));
    }

    @Test
    public void testReduce1() {
        List<Integer> list = Arrays.asList(1, 2, null, 1, 3);
        Taboo<Integer> t = new Taboo<Integer>(list);
        List<Integer> s = new ArrayList<>(Arrays.asList(2, 1, 3, 1, 2, 3, 1));
        t.reduce(s);
        assertEquals(Arrays.asList(2, 1, 1, 1), s);
    }

    @Test
    public void testReduce2() {
        List<Character> list = Arrays.asList('a', 'c', 'a', 'b');
        Taboo<Character> t = new Taboo<Character>(list);
        List<Character> s = new ArrayList<>(Arrays.asList('a', 'c', 'b', 'x', 'c', 'a'));
        t.reduce(s);
        assertEquals(Arrays.asList('a', 'x', 'c'), s);
    }

    @Test
    public void testReduce3() {
        // empty
        List<String> list = Arrays.asList("ab", "cd", "ef");
        Taboo<String> t = new Taboo<String>(list);
        List<String> s = new ArrayList<>();
        t.reduce(s);
        assertEquals(List.of(), s);
    }
}
