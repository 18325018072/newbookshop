package po;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class Sell implements Serializable {
    private Integer sellId;
    private Integer bookId;
    private Integer sellNum;
    private Date sellDate;
    private Double realPrice;
    private Integer uerId;
    private Integer employeeId;
}
