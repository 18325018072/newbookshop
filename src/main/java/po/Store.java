package po;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data@NoArgsConstructor@AllArgsConstructor
public class Store implements Serializable {
    private Integer bookNum;
    private Integer bookId;
    private Integer storeId;
}
