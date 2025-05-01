package com.group3.server.models.reports;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data // ❗ THÊM Setter để JPA có thể gán giá trị vào các field
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor // ❗ BẮT BUỘC phải có constructor không tham số
@AllArgsConstructor // ❗ Rất tiện nếu bạn muốn khởi tạo nhanh bằng 2 ID
public class SalesReportDetailId implements Serializable {
    @Column(name = "saving_type_id") // ❗ Ràng buộc tên cột khớp với DB (nếu khác tên mặc định)
    private Long savingTypeId;

    @Column(name = "report_id") // ❗ Tương tự
    private Long reportId;
    
}
