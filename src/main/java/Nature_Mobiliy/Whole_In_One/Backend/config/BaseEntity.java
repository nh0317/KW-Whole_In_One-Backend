package Nature_Mobiliy.Whole_In_One.Backend.config;//package com.softsquared.template.config;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import javax.persistence.*;
//import java.util.Date;
//
//import static javax.persistence.TemporalType.TIMESTAMP;
//
//@Getter
//@Setter
//@MappedSuperclass
//public abstract class BaseEntity {
////    @Getter
////    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
////    @Temporal(TIMESTAMP)
//    @CreationTimestamp
//    @Column(name = "createdAt", nullable = false, updatable = false)
//    private Date createdAt;
//
////    @Getter
////    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
////    @Temporal(TIMESTAMP)
//    @UpdateTimestamp
//    @Column(name = "updatedAt", nullable = false)
//    private Date updatedAt;
//
////    @PrePersist
////    void prePersist() {
////        this.createdAt = this.updatedAt = new Date();
////    }
////
////    @PreUpdate
////    void preUpdate() {
////        this.updatedAt = new Date();
////    }
//}