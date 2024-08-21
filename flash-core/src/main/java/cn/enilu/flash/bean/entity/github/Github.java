package cn.enilu.flash.bean.entity.github;

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

@Entity(name = "g_register_info")
@Table(appliesTo = "g_register_info", comment = "github注册")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Github extends BaseEntity {
    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT '邮箱账号'")
    @NotBlank(message = "邮箱不能为空")
    private String emailAddress;

    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT 'github用户名'")
    @NotBlank(message = "用户名不能为空")
    private String githubName;

    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT '仓库名称'")
    @NotBlank(message = "仓库名称不能为空")
    private String repositoryName;

    @Column(columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '仓库地址'")
    @NotBlank(message = "仓库地址不能为空")
    private String sshUrl;

    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT '用户账号'")
    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @Column(columnDefinition = "bigint NOT NULL COMMENT '关联的rsaid'")
    @NotNull(message = "关联的rsaid不能为空")
    private Long rsaId;

    @Column(columnDefinition = "text NOT NULL COMMENT 'rsa公钥'")
    @NotBlank(message = "rsa公钥不能为空")
    private String idRsaPublic;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    @NotNull(message = "创建时间不能为空")
    private Date timeSubmit;

    @Column(columnDefinition = "timestamp DEFAULT NULL COMMENT '验证时间'")
    private Date timeValidate;


}
