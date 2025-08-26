package fr.pilou.uhcapi.module.role;

import fr.pilou.uhcapi.module.camp.Camp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RoleRegistry {
    String name();

    String identifier();

    Class<? extends Camp> campClass();
}
