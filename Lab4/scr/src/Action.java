/**
 * The Action interface that can be called
 * on an object of type T to act.
 *
 * Contains a single abstract method call.
 *
 * CS2030S Lab 4
 * AY22/23 Semester 1
 *
 * @author Shen Chenzi (Lab Group 08)
 */

public interface Action<T> {
    //abstract method
    public void call(T t);
}
