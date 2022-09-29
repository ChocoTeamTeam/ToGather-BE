package chocoteamteam.togather.entity;

import chocoteamteam.togather.type.MemberStatus;
import chocoteamteam.togather.type.ProviderType;
import chocoteamteam.togather.type.Role;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToMany(mappedBy = "member")
    private List<MemberTechStack> memberTechStacks = new ArrayList<>();


    public void changeStatus(MemberStatus status) {
        this.status = status;
    }

    public void modifyNicknameAndProfileImage(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

}
