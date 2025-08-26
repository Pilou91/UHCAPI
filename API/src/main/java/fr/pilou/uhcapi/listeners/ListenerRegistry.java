package fr.pilou.uhcapi.listeners;

import fr.pilou.uhcapi.game.State;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ListenerRegistry {
    State[] state() default {};
}
