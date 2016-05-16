package benchmark;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.api.VmOptions;
import com.google.caliper.runner.CaliperMain;
import list.CoarseGrainedConcurrentList;
import list.ConcurrentList;
import list.FineGrainedConcurrentList;
/**
 * @author krzykrucz
 */

@VmOptions("-XX:-TieredCompilation")
public class GrainedLockListBenchmark {

    public static void main(String[] args) {
        CaliperMain.main(GrainedLockListBenchmark.class, args);
    }

    private enum Element {
        INSTANCE, NEW,
    }
    private enum ListImpl {
        Coarse {
            @Override
            ConcurrentList<Element> create() {
                return new CoarseGrainedConcurrentList<Element>();
            }
        },
        Fine {
            @Override ConcurrentList<Element> create() {
                return new FineGrainedConcurrentList<Element>();
            }
        };

        abstract ConcurrentList<Element> create();
    }

    @Param({"100",/* "1000",*/ "10000"})
    private int size;

    @Param({"Coarse", "Fine"})
    private ListImpl implementation;

    private ConcurrentList<Element> list;

    @BeforeExperiment void setUp() throws Exception {
        list = implementation.create();
        for (int i = 0; i < size - 1; i++) {
            list.add(Element.INSTANCE);
        }
        list.add(Element.NEW);
    }

    @Benchmark void populate(int reps) throws Exception {
        for (int rep = 0; rep < reps; rep++) {
            ConcurrentList<Element> list = implementation.create();
            for (int i = 0; i < size; i++) {
                list.add(Element.INSTANCE);
            }
        }
    }

    @Benchmark void tailAddRemove(int reps) {
        int index = size - 1;
        for (int rep = 0; rep < reps; rep++) {
            list.add(Element.NEW);
            list.remove(Element.NEW);
        }
    }

    @Benchmark void contains(int reps) {
        int index = size - 1;
        for (int rep = 0; rep < reps; rep++) {
            list.contains(Element.NEW);
        }
    }

}
