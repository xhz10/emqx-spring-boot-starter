package com.xhz.emqxspringbootstarter.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: xuhongzhuo
 * @Date: 2022/3/25 12:03 PM
 */
@Data
@Getter
@Setter
@Builder()
public class MessageModel {
    private String topic;

    private Object message;

    private String type;
}
