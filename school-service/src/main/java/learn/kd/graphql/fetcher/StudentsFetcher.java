package learn.kd.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import learn.kd.generated.schema.DgsConstants;
import learn.kd.generated.schema.types.School;
import learn.kd.generated.schema.types.Student;
import learn.kd.graphql.loader.SchoolLoader;
import learn.kd.graphql.service.StudentService;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
public class StudentsFetcher {

    private StudentService studentService;

    @Autowired
    public StudentsFetcher(StudentService studentService) {
        this.studentService = studentService;
    }

    @DgsQuery
    public List<Student> students(@InputArgument String nameFilter) {
        return studentService.listAllStudentsWithFilter(nameFilter);
    }

    // Child Data fetchers :: Useful for cases where some property can take time to return
    // We can return rest of the properties in parent query
    //
    // The problem with using only DataFetchers is that, we will run into N+1 problem, i.e.
    // for 1 parent query call, child will be called N times. Thus the underlying service to fetch the data will
    // be called N times. To overcome this we need to use DataLoaders which implements BatchLoaders/MappedBatchLoaders etc.
    // The dataloader will be called only once with list of keys
    //
    //    @DgsData(parentType = DgsConstants.STUDENT.TYPE_NAME)
    //    public Optional<School> school(DgsDataFetchingEnvironment dfe) {
    //        Student student = dfe.getSource();
    //        return schools.stream()
    //                .filter(school -> school.getId().equals(student.getSchool().getId()))
    //                .findAny();
    //    }
    @DgsData(parentType = DgsConstants.STUDENT.TYPE_NAME)
    public CompletableFuture<School> school(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, School> schoolDataLoader = dfe.getDataLoader(SchoolLoader.class);
        Student student = dfe.getSource();
        return schoolDataLoader.load(student.getSchool().getId());
    }
}
