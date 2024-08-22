package cn.enilu.flash.bean.entity.test;//package cn.enilu.flash.bean.entity.test;

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

@Entity(name = "g_rsa_info")
@Table(appliesTo = "g_rsa_info", comment = "rsa信息")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RsaInfo extends BaseEntity {
    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT '邮箱账号'")
    @NotBlank(message = "邮箱不能为空")
    private String emailAddress;

    @Column(columnDefinition = "varchar(32) NOT NULL DEFAULT '' COMMENT 'rsa用户别名'")
    @NotBlank(message = "邮箱类型不能为空")
    private String userRsaName;

    @Column(columnDefinition = "text COMMENT '公钥'")
    @NotBlank(message = "公钥不能为空")
    private String idRsaPublic;

    @Column(columnDefinition = "text COMMENT '私钥'")
    @NotBlank(message = "私钥不能为空")
    private String idRsa;

    @Column(columnDefinition = "varchar(4) NOT NULL DEFAULT '0' COMMENT '关联状态:0未关联,1已关联,2已失效'")
    @NotBlank(message = "私钥不能为空")
    private String status;

    @Column(columnDefinition = "timestamp NOT NULL COMMENT '生成时间'")
    @NotNull(message = "生成时间不能为空")
    private Date generalTime;

}
