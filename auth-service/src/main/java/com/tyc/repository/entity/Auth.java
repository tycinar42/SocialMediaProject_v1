package com.tyc.repository.entity;


import com.tyc.repository.enums.Roles;
import com.tyc.repository.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
/**
 * Auth entitiy admin code ile status
 * status enum olacak
 * register dtoda admin code alacağız
 * controllermızda ki register metodunu get mappingden postmappinge çekelim
 * ve bir kullanıcı kaydedelim
 *
 *2-username uniq olsun
 * exception pakateini ekleyelim
 * admin code belirleyip register metodumuzu ona göre güncelleyelim
 * (admin code doğruysa rolemuz admin olsun)
 * Mapper paketimizi yaratalım ve IuserMapper oluşturalım
 * exceptionlarıda gerekli yerlerde kullanalım
 *3- Validsyon için gerekli bağımlılıkları ekleyelim ve register reguestdto ya
 * valıdasyon kosullaraı ekleyelım  pasword boyutunu belirleyeim username boyutu
 * swqggerı da ekleyelim
 *
 * 4- login metodu yazalım controller ve servicede
 * login için bir request dto oluşturalım
 * birde response dto olusturalım
 * bu oluşturdumuz dtolarıda metodumuzda kullanalım
 *
 */
public class Auth implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String username;
    private  String password;
    private String email;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Roles role=Roles.USER;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status=Status.PENDING;
    private String activatedCode;

}
