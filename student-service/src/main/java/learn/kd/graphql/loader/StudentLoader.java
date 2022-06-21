package learn.kd.graphql.loader;

import learn.kd.generated.schema.types.Student;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;

public class StudentLoader implements MappedBatchLoader<String, Student> {

    @Override
    public CompletionStage<Map<String, Student>> load(Set<String> keys) {
        return null;
    }
}
