package com.example.demo.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.example.demo.model.OrderLineItems;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@Builder
public class OrderLineItemsDto {
    private Long id;
    private String orderNumber;
    private Integer quantity;
    private BigDecimal price;
    private String skuCode;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItems> orderLineItems;
}
