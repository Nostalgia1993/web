package cn.enilu.flash.bean.entity.github;//package cn.enilu.flash.bean.entity.test;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "g_email")
@Table(appliesTo = "g_email", comment = "邮箱")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Email extends BaseEntity {
    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT '邮箱账号'")
    @NotBlank(message = "邮箱不能为空")
    private String emailAddress;

    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT '邮箱类型: 1-gmail,2-163,3-qq'")
    @NotBlank(message = "邮箱类型不能为空")
    private String emailType;

    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT '密码'")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Column(columnDefinition = "varchar(2) NOT NULL DEFAULT '' COMMENT '状态'")
    @NotBlank(message = "状态不能为空")
    private String status;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    @NotNull(message = "创建时间不能为空")
    private Date timeSubmit;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '验证时间'")
    @NotBlank(message = "验证时间不能为空")
    private Date timeValidate;


}
