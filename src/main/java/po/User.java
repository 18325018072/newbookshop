package po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data@NoArgsConstructor@AllArgsConstructor
public class User implements Serializable {
    private String userId;
    private String userName;
    private String password;
    private Double consumption;
    private String note;
    private String vipGrade;
    private String loginState;
}
