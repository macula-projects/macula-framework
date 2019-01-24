package org.macula.boot.core.utils;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * <p>
 * <b>User</b> 测试POJO
 * </p>
 *
 * @author Rain
 * @since 2019-01-24
 */
@Data
public class User {
    private String username;
    private String password;
    private Instant instant;
    private LocalDateTime localDateTime;
    private OffsetDateTime offsetDateTime;
    private LocalDate localDate;
    private Date date;

}
