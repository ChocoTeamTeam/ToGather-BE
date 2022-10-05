package chocoteamteam.togather.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Location {
    private String address;
    private Double latitude;
    private Double longitude;
}