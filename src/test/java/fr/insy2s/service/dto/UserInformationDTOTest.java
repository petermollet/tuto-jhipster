package fr.insy2s.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.insy2s.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserInformationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInformationDTO.class);
        UserInformationDTO userInformationDTO1 = new UserInformationDTO();
        userInformationDTO1.setId(1L);
        UserInformationDTO userInformationDTO2 = new UserInformationDTO();
        assertThat(userInformationDTO1).isNotEqualTo(userInformationDTO2);
        userInformationDTO2.setId(userInformationDTO1.getId());
        assertThat(userInformationDTO1).isEqualTo(userInformationDTO2);
        userInformationDTO2.setId(2L);
        assertThat(userInformationDTO1).isNotEqualTo(userInformationDTO2);
        userInformationDTO1.setId(null);
        assertThat(userInformationDTO1).isNotEqualTo(userInformationDTO2);
    }
}
