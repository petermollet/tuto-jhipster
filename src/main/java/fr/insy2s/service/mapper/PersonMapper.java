package fr.insy2s.service.mapper;

import fr.insy2s.domain.*;
import fr.insy2s.service.dto.PersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
    @Mapping(target = "address", source = "address", qualifiedByName = "id")
    PersonDTO toDto(Person s);
}
