package com.lmz.client.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: limingzhong
 * @create: 2023-06-24 10:33
 */
@Data
@AllArgsConstructor
public class Order {
    private Long orderId;

    private Integer count;

}
