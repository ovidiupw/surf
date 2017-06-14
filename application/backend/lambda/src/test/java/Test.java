import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Workflow;
import models.WorkflowMetadata;

public class Test {
    @org.junit.Test
    public void test() throws JsonProcessingException {
        WorkflowMetadata metadata = new WorkflowMetadata();
        metadata.setRootAddress("ftp://google.ro");

        Workflow workflow = new Workflow();
        workflow.setMetadata(metadata);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("workflow = " + objectMapper.writeValueAsString(workflow));
    }
}
