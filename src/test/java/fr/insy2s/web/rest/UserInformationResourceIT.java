package fr.insy2s.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.insy2s.IntegrationTest;
import fr.insy2s.domain.Address;
import fr.insy2s.domain.User;
import fr.insy2s.domain.UserInformation;
import fr.insy2s.repository.UserInformationRepository;
import fr.insy2s.service.dto.UserInformationDTO;
import fr.insy2s.service.mapper.UserInformationMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserInformationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserInformationResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private UserInformationMapper userInformationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserInformationMockMvc;

    private UserInformation userInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInformation createEntity(EntityManager em) {
        UserInformation userInformation = new UserInformation().phone(DEFAULT_PHONE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userInformation.setUser(user);
        // Add required entity
        Address address;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            address = AddressResourceIT.createEntity(em);
            em.persist(address);
            em.flush();
        } else {
            address = TestUtil.findAll(em, Address.class).get(0);
        }
        userInformation.setAddress(address);
        return userInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInformation createUpdatedEntity(EntityManager em) {
        UserInformation userInformation = new UserInformation().phone(UPDATED_PHONE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userInformation.setUser(user);
        // Add required entity
        Address address;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            address = AddressResourceIT.createUpdatedEntity(em);
            em.persist(address);
            em.flush();
        } else {
            address = TestUtil.findAll(em, Address.class).get(0);
        }
        userInformation.setAddress(address);
        return userInformation;
    }

    @BeforeEach
    public void initTest() {
        userInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createUserInformation() throws Exception {
        int databaseSizeBeforeCreate = userInformationRepository.findAll().size();
        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);
        restUserInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeCreate + 1);
        UserInformation testUserInformation = userInformationList.get(userInformationList.size() - 1);
        assertThat(testUserInformation.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createUserInformationWithExistingId() throws Exception {
        // Create the UserInformation with an existing ID
        userInformation.setId(1L);
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        int databaseSizeBeforeCreate = userInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserInformations() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        // Get all the userInformationList
        restUserInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        // Get the userInformation
        restUserInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, userInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userInformation.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingUserInformation() throws Exception {
        // Get the userInformation
        restUserInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();

        // Update the userInformation
        UserInformation updatedUserInformation = userInformationRepository.findById(userInformation.getId()).get();
        // Disconnect from session so that the updates on updatedUserInformation are not directly saved in db
        em.detach(updatedUserInformation);
        updatedUserInformation.phone(UPDATED_PHONE);
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(updatedUserInformation);

        restUserInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
        UserInformation testUserInformation = userInformationList.get(userInformationList.size() - 1);
        assertThat(testUserInformation.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingUserInformation() throws Exception {
        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();
        userInformation.setId(count.incrementAndGet());

        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserInformation() throws Exception {
        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();
        userInformation.setId(count.incrementAndGet());

        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserInformation() throws Exception {
        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();
        userInformation.setId(count.incrementAndGet());

        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInformationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserInformationWithPatch() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();

        // Update the userInformation using partial update
        UserInformation partialUpdatedUserInformation = new UserInformation();
        partialUpdatedUserInformation.setId(userInformation.getId());

        partialUpdatedUserInformation.phone(UPDATED_PHONE);

        restUserInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserInformation))
            )
            .andExpect(status().isOk());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
        UserInformation testUserInformation = userInformationList.get(userInformationList.size() - 1);
        assertThat(testUserInformation.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateUserInformationWithPatch() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();

        // Update the userInformation using partial update
        UserInformation partialUpdatedUserInformation = new UserInformation();
        partialUpdatedUserInformation.setId(userInformation.getId());

        partialUpdatedUserInformation.phone(UPDATED_PHONE);

        restUserInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserInformation))
            )
            .andExpect(status().isOk());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
        UserInformation testUserInformation = userInformationList.get(userInformationList.size() - 1);
        assertThat(testUserInformation.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingUserInformation() throws Exception {
        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();
        userInformation.setId(count.incrementAndGet());

        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserInformation() throws Exception {
        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();
        userInformation.setId(count.incrementAndGet());

        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserInformation() throws Exception {
        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();
        userInformation.setId(count.incrementAndGet());

        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        int databaseSizeBeforeDelete = userInformationRepository.findAll().size();

        // Delete the userInformation
        restUserInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
