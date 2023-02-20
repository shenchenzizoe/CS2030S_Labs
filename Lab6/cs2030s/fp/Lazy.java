package cs2030s.fp;

/**
 * This Lazy class is used to support lazy values,
 * where the expression that produces a lazy value
 * is not evaluated until the value is needed.
 *
 * @version CS2030S Lab 6 AY22/23 Semester 1
 *
 * @author Shen Chenzi Tutorial group 08
 */
public class Lazy<T> implements Immutatorable<T> {
  private Constant<? extends T> init;

  /**
   * Protected constructor.
   *
   * @param c Constant of with type parameter wildcard T.
   */
  protected Lazy(Constant<? extends T> c) {
    this.init = c;
  }

  /**
   * Factory method for Lazy.
   *
   * @param <T> Type parameter, indicating the value take in is type T,
   *            and the return type value is {@code Lazy<T>}.
   * @param v   The value take in to instantiate corresponding Lazy Object.
   * @return Lazy object using the construct method with single type T argument
   */
  public static <T> Lazy<T> from(T v) {
    return new Lazy<>(() -> v);
  }

  /**
   * Factory method for Lazy.
   *
   * @param <T> Type parameter, indicating the value take in is type T,
   *            and the return type value is {@code Lazy<T>}.
   * @param c   The Constant object take in to instantiate corresponding Memo Object.
   * @return Memo object using the construct method with single type Constant argument
   */
  public static <T> Lazy<T> from(Constant<? extends T> c) {
    return new Lazy<>(c);
  }

  /**
   * calculates the value of Lazy.
   *
   * @return Lazy value computed by calling the {@code init()} method
   *         from Constant
   */
  public T get() {
    return this.init.init();
  }

  /**
   * Override toString method.
   *
   * @return the String representation of the value of the Constant.
   */
  @Override
  public String toString() {
    return this.init.init().toString();
  }

  /**
   * method to operate on the value of Lazy.
   *
   * @param <R> Type parameter to indicate the return type {@code Memo<R>},
   *            and the type parameters of argument immutator.
   * @param immutator   immutator take in to modify the value
   * @return new {@code Lazy<R>}.
   */
  @Override
  public <R> Lazy<R> transform(Immutator<? extends R, ? super T> immutator) {
    return from(() -> immutator.invoke(this.get()));
  }

  /**
   * Next method, returns a new Lazy Object.
   *
   * @param <R> Type parameter to indicate return type and type parameter
   *            for argument immutator.
   * @param immutator with the value that is going to be the next value
   *        inside the next {@code Lazy<R>} Object.
   * @return new {@code Lazy<R>}.
   */
  public <R> Lazy<R> next(Immutator<? extends Lazy<? extends R>, ? super T> immutator) {
    return from(() -> immutator.invoke(this.get()).get());
  }
}
