package fr.insy2s.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserInformationMapperTest {

    private UserInformationMapper userInformationMapper;

    @BeforeEach
    public void setUp() {
        userInformationMapper = new UserInformationMapperImpl();
    }
}
