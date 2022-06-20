package learn.kd.graphql.loader;

import com.netflix.graphql.dgs.DgsDataLoader;
import learn.kd.generated.schema.types.School;
import org.dataloader.MappedBatchLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@DgsDataLoader
public class SchoolLoader implements MappedBatchLoader<String, School> {

    private static final List<School> schools;

    static {
        schools = new ArrayList<>() {{
            add(School.newBuilder().id("Sch1").name("Primary School A").address("Address 1").build());
            add(School.newBuilder().id("Sch2").name("Primary School B").address("Address 2").build());
        }};
    }

    /**
     * Loads a batch of schools by collecting all the ids for which fetcher was called
     * in a single request
     *
     * @param ids
     * @return CompletionStage containing a map of id to school
     */
    @Override
    public CompletionStage<Map<String, School>> load(Set<String> ids) {
        return CompletableFuture.supplyAsync(() -> schools.stream()
                .filter(s -> ids.contains(s.getId()))
                .collect(Collectors.toMap(School::getId, school -> school)));
    }
}
