package com.example.demo.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class OrderRequest {
    private List<OrderLineItemsDto> orderLineItemListDtoList;
}
