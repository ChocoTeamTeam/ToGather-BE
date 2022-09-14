package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.MemberStatus;
import chocoteamteam.togather.type.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class LoginMember {

	private Long id;
	private String nickname;
	private MemberStatus status;
	private Role role;

	public static LoginMember from(TokenMemberInfo info) {
		return LoginMember.builder()
			.id(info.getId())
			.nickname(info.getNickname())
			.status(MemberStatus.valueOf(info.getStatus()))
			.role(Role.valueOf(info.getRole()))
			.build();
	}

}
