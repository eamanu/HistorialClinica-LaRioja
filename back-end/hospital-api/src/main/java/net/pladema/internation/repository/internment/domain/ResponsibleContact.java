package net.pladema.internation.repository.internment.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "responsible_contact")
@Getter
@Setter
@ToString
public class ResponsibleContact implements Serializable {

    @Id
    @Column(name = "internment_episode_id")
    private Integer internmentEpisodeId;

    @Column(name = "full_name", length = 80)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "relationship", length = 100)
    private String relationship;
}