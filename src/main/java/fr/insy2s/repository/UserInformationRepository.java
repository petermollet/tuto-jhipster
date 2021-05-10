package fr.insy2s.repository;

import fr.insy2s.domain.UserInformation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {}
