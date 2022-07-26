package po;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data@NoArgsConstructor@AllArgsConstructor
public class Vip implements Serializable {
    private Integer vipGrade;
    private String vipName;
    private Double discount;
    private Integer minimumThreshold;
}
