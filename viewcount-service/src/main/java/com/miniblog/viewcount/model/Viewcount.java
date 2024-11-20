package com.miniblog.viewcount.model;

import com.miniblog.viewcount.listener.ViewcountListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(ViewcountListener.class)
@Table(name = "viewcounts")
public class Viewcount {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "post_uuid", length = 36)
    private UUID postUuid;

    @Column(name = "total_views", nullable = false)
    @Min(0)
    private Long totalViews;
}
