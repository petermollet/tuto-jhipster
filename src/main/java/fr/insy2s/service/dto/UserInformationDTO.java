package fr.insy2s.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.insy2s.domain.UserInformation} entity.
 */
@ApiModel(description = "@author: PM")
public class UserInformationDTO implements Serializable {

    private Long id;

    private String phone;

    private UserDTO user;

    private AddressDTO address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInformationDTO)) {
            return false;
        }

        UserInformationDTO userInformationDTO = (UserInformationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userInformationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserInformationDTO{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", user=" + getUser() +
            ", address=" + getAddress() +
            "}";
    }
}
