package list;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author krzykrucz
 */
@RunWith(Parameterized.class)
public class ConcurrentListIpmlTest {

    private final static String S_1 = "Some String";
    private final static String S_2 = "Some Other String";
    @Parameterized.Parameter
    public ConcurrentList<String> list;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new CoarseGrainedConcurrentList<>()},
                {new FineGrainedConcurrentList<>()}
        });
    }

    @Test
    public void testList() throws Exception {

        list.add(S_1);
        list.add(S_2);
        list.add(S_2);

        assertEquals(3, list.size());

        list.remove(S_2);

        assertEquals(2, list.size());

        boolean contains = list.contains(S_2);

        assertTrue(contains);

    }

}