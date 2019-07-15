package org.snake.zookeeper.zookeeper;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


/**
 * @author ynz
 * @version 创建时间：2018/7/2
 * @email ynz@myframe.cn
 */
@ConfigurationProperties(prefix="zookeeper")
@Data
@Validated
public class ZookeeperProperties {


    private String server;
    private String namespace;
    private String digest;
    private Integer sessionTimeoutMs;
    private Integer connectionTimeoutMs;
    private Integer maxRetries;
    private Integer baseSleepTimeMs;
}
