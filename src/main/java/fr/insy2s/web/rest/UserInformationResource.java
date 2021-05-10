package fr.insy2s.web.rest;

import fr.insy2s.repository.UserInformationRepository;
import fr.insy2s.service.UserInformationService;
import fr.insy2s.service.dto.UserInformationDTO;
import fr.insy2s.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.insy2s.domain.UserInformation}.
 */
@RestController
@RequestMapping("/api")
public class UserInformationResource {

    private final Logger log = LoggerFactory.getLogger(UserInformationResource.class);

    private static final String ENTITY_NAME = "userInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserInformationService userInformationService;

    private final UserInformationRepository userInformationRepository;

    public UserInformationResource(UserInformationService userInformationService, UserInformationRepository userInformationRepository) {
        this.userInformationService = userInformationService;
        this.userInformationRepository = userInformationRepository;
    }

    /**
     * {@code POST  /user-informations} : Create a new userInformation.
     *
     * @param userInformationDTO the userInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userInformationDTO, or with status {@code 400 (Bad Request)} if the userInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-informations")
    public ResponseEntity<UserInformationDTO> createUserInformation(@Valid @RequestBody UserInformationDTO userInformationDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserInformation : {}", userInformationDTO);
        if (userInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new userInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserInformationDTO result = userInformationService.save(userInformationDTO);
        return ResponseEntity
            .created(new URI("/api/user-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-informations/:id} : Updates an existing userInformation.
     *
     * @param id the id of the userInformationDTO to save.
     * @param userInformationDTO the userInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userInformationDTO,
     * or with status {@code 400 (Bad Request)} if the userInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-informations/{id}")
    public ResponseEntity<UserInformationDTO> updateUserInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserInformationDTO userInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserInformation : {}, {}", id, userInformationDTO);
        if (userInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserInformationDTO result = userInformationService.save(userInformationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-informations/:id} : Partial updates given fields of an existing userInformation, field will ignore if it is null
     *
     * @param id the id of the userInformationDTO to save.
     * @param userInformationDTO the userInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userInformationDTO,
     * or with status {@code 400 (Bad Request)} if the userInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-informations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserInformationDTO> partialUpdateUserInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserInformationDTO userInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserInformation partially : {}, {}", id, userInformationDTO);
        if (userInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserInformationDTO> result = userInformationService.partialUpdate(userInformationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userInformationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-informations} : get all the userInformations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userInformations in body.
     */
    @GetMapping("/user-informations")
    public List<UserInformationDTO> getAllUserInformations() {
        log.debug("REST request to get all UserInformations");
        return userInformationService.findAll();
    }

    /**
     * {@code GET  /user-informations/:id} : get the "id" userInformation.
     *
     * @param id the id of the userInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-informations/{id}")
    public ResponseEntity<UserInformationDTO> getUserInformation(@PathVariable Long id) {
        log.debug("REST request to get UserInformation : {}", id);
        Optional<UserInformationDTO> userInformationDTO = userInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userInformationDTO);
    }

    /**
     * {@code DELETE  /user-informations/:id} : delete the "id" userInformation.
     *
     * @param id the id of the userInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-informations/{id}")
    public ResponseEntity<Void> deleteUserInformation(@PathVariable Long id) {
        log.debug("REST request to delete UserInformation : {}", id);
        userInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
