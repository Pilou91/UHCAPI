package fr.pilou.uhcapi.module.camp;

import org.bukkit.ChatColor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CampRegistry {
    String name();

    ChatColor color();

    int rating();

    boolean alone() default false;
}
