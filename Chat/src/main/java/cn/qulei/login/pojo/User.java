package cn.qulei.login.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * @author QuLei
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 8002068681110231556L;
    private String username;
    private String password;
}
