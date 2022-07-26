package po;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data@NoArgsConstructor@AllArgsConstructor
public class Book implements Serializable {
    private Integer bookId;
    private String bookName;
    private String publisher;
    private String bookType;
    private String author;
    private Double nowPrice;
    private String note;
}
