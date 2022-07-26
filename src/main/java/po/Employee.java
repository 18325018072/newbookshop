package po;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data@NoArgsConstructor@AllArgsConstructor
public class Employee implements Serializable {
    private Integer employeeId;
    private String employee;
    private String job;
}
