package fr.insy2s.service;

import fr.insy2s.service.dto.UserInformationDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.insy2s.domain.UserInformation}.
 */
public interface UserInformationService {
    /**
     * Save a userInformation.
     *
     * @param userInformationDTO the entity to save.
     * @return the persisted entity.
     */
    UserInformationDTO save(UserInformationDTO userInformationDTO);

    /**
     * Partially updates a userInformation.
     *
     * @param userInformationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserInformationDTO> partialUpdate(UserInformationDTO userInformationDTO);

    /**
     * Get all the userInformations.
     *
     * @return the list of entities.
     */
    List<UserInformationDTO> findAll();

    /**
     * Get the "id" userInformation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserInformationDTO> findOne(Long id);

    /**
     * Delete the "id" userInformation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
