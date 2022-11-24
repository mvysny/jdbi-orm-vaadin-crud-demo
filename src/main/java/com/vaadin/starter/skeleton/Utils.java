package com.vaadin.starter.skeleton;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * A bunch of utility functions.
 */
public class Utils {
    private static final Random RANDOM = new Random();

    /**
     * Returns a random enum constant.
     * @param clazz the enum class
     * @return random enum constant, not null.
     * @param <E> the enum type.
     */
    @NotNull
    public static <E extends Enum<E>> E random(@NotNull Class<E> clazz) {
        final E[] constants = clazz.getEnumConstants();
        return constants[RANDOM.nextInt(constants.length)];
    }
}
