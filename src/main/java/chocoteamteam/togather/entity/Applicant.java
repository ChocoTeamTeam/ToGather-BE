package chocoteamteam.togather.entity;

import chocoteamteam.togather.type.ApplicantStatus;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(indexes = @Index(name = "idx_member_project",columnList = "member_id,project_id"))
@Entity
public class Applicant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn
	private Project project;

	@Enumerated(EnumType.STRING)
	private ApplicantStatus status;

}
