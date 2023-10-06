package com.bashko.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Monster{
    String name;
    Integer health;
    Integer attack;
    Integer protection;
    Integer[] damage;
}
