package fr.insy2s.service.mapper;

import fr.insy2s.domain.*;
import fr.insy2s.service.dto.UserInformationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserInformation} and its DTO {@link UserInformationDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, AddressMapper.class })
public interface UserInformationMapper extends EntityMapper<UserInformationDTO, UserInformation> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "address", source = "address", qualifiedByName = "id")
    UserInformationDTO toDto(UserInformation s);
}
