package po;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data@NoArgsConstructor@AllArgsConstructor
public class Order implements Serializable {
    private Integer orderId;
    private Integer bookId;
    private Integer orderNum;
    private Date OrderDate;
    private Double orderPrice;
    private Integer employeeId;
    private String note;
}
