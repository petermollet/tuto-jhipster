package fr.insy2s.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.insy2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserInformationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInformation.class);
        UserInformation userInformation1 = new UserInformation();
        userInformation1.setId(1L);
        UserInformation userInformation2 = new UserInformation();
        userInformation2.setId(userInformation1.getId());
        assertThat(userInformation1).isEqualTo(userInformation2);
        userInformation2.setId(2L);
        assertThat(userInformation1).isNotEqualTo(userInformation2);
        userInformation1.setId(null);
        assertThat(userInformation1).isNotEqualTo(userInformation2);
    }
}
