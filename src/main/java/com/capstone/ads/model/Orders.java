package com.capstone.ads.model;

import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String address;                         //Địa chỉ giao hàng
    Long totalAmount;
    Long depositAmount;
    Long remainingAmount;
    String note;

    String draftImageUrl;                   //Hình ảnh sản phẩm bắt đầu thi công
    String productImageUrl;                 //Hình ảnh sản phẩm sau khi hoàn thành
    String deliveryImageUrl;                //Hình ảnh chuẩn bị giao hàng
    String installationImageUrl;            //Hình ảnh sau khi lắp đặt sau

    LocalDateTime estimatedDeliveryDate;    //Ngày giao dự tính
    LocalDateTime depositPaidDate;          //Ngày khách hàng đặt cọc
    LocalDateTime productionStartDate;      //Ngày bắt đầu thi công
    LocalDateTime productionEndDate;        //Ngày thi công xong
    LocalDateTime deliveryStartDate;        //Ngày băt đầu vận chuyển
    LocalDateTime actualDeliveryDate;       //Ngày giao hàng thực té
    LocalDateTime completionDate;           //Ngày khách hàng thanhtoán phần còn lại

    @CreationTimestamp
    LocalDateTime orderDate;                //Ngày đặt hàng
    @UpdateTimestamp
    LocalDateTime updateDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    CustomerChoiceHistories customerChoiceHistories;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @ManyToOne
    Users users;

    @OneToMany(mappedBy = "orders")
    List<Payments> payments;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    Contract contract;

    @OneToOne
    AIDesigns aiDesigns;


    @OneToOne
    CustomDesignRequests customDesignRequests;
}
