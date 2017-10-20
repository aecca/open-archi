package com.araguacaima.gsa.persistence.am;

/**
 * Use {@link InteractionStyle}s on {@link Relationship}s to make the difference between synchronous and asynchronous communication
 * visible. You can pass either {@link Tag#SYNCHRONOUS} or {@link Tag#ASYNCHRONOUS}
 * to define different styles for synchronous and asynchronous communication.
 *
 * @see Tag#SYNCHRONOUS
 * @see Tag#ASYNCHRONOUS
 */
public enum InteractionStyle {

    /**
     * Denotes synchronous communication. The tag {@link Tag#SYNCHRONOUS} is automatically added to such {@link Relationship}s,
     * so you might use that tag to adapt the relationship style in the diagram
     *
     * @see Tag#SYNCHRONOUS
     */
    Synchronous,

    /**
     * Denotes asynchronous communication. The tag {@link Tag#ASYNCHRONOUS} is automatically added to such {@link Relationship}s,
     * so you might use that tag to adapt the relationship style in the diagram
     *
     * @see Tag#ASYNCHRONOUS
     */
    Asynchronous

}