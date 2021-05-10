package fr.insy2s.service.impl;

import fr.insy2s.domain.UserInformation;
import fr.insy2s.repository.UserInformationRepository;
import fr.insy2s.service.UserInformationService;
import fr.insy2s.service.dto.UserInformationDTO;
import fr.insy2s.service.mapper.UserInformationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserInformation}.
 */
@Service
@Transactional
public class UserInformationServiceImpl implements UserInformationService {

    private final Logger log = LoggerFactory.getLogger(UserInformationServiceImpl.class);

    private final UserInformationRepository userInformationRepository;

    private final UserInformationMapper userInformationMapper;

    public UserInformationServiceImpl(UserInformationRepository userInformationRepository, UserInformationMapper userInformationMapper) {
        this.userInformationRepository = userInformationRepository;
        this.userInformationMapper = userInformationMapper;
    }

    @Override
    public UserInformationDTO save(UserInformationDTO userInformationDTO) {
        log.debug("Request to save UserInformation : {}", userInformationDTO);
        UserInformation userInformation = userInformationMapper.toEntity(userInformationDTO);
        userInformation = userInformationRepository.save(userInformation);
        return userInformationMapper.toDto(userInformation);
    }

    @Override
    public Optional<UserInformationDTO> partialUpdate(UserInformationDTO userInformationDTO) {
        log.debug("Request to partially update UserInformation : {}", userInformationDTO);

        return userInformationRepository
            .findById(userInformationDTO.getId())
            .map(
                existingUserInformation -> {
                    userInformationMapper.partialUpdate(existingUserInformation, userInformationDTO);
                    return existingUserInformation;
                }
            )
            .map(userInformationRepository::save)
            .map(userInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInformationDTO> findAll() {
        log.debug("Request to get all UserInformations");
        return userInformationRepository
            .findAll()
            .stream()
            .map(userInformationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserInformationDTO> findOne(Long id) {
        log.debug("Request to get UserInformation : {}", id);
        return userInformationRepository.findById(id).map(userInformationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserInformation : {}", id);
        userInformationRepository.deleteById(id);
    }
}
