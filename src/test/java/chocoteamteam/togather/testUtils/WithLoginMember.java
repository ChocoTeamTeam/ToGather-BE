package chocoteamteam.togather.testUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithLoginMemberSecurityContextFactory.class)
public @interface WithLoginMember {

	long id() default 1L;
	String nickname() default "test";
	String status() default "PERMITTED";
	String role() default "ROLE_USER";

}
