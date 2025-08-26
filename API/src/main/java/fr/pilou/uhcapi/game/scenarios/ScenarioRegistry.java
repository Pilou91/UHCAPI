package fr.pilou.uhcapi.game.scenarios;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScenarioRegistry {
    String name();

    Material material();

    int data() default 0;

    String[] lore() default {""};
}