package cs2030s.fp;

/**
 * This Memo class extends Lazy class.
 * Based on functionality of Lazy, this class
 * ensures that each value is only computed once,
 * make the idea of memoization possible.
 *
 * @version CS2030S Lab 6 AY22/23 Semester 1
 *
 * @author Shen Chenzi Tutorial group 08
 */
public class Memo<T> extends Lazy<T> {
  private Actually<T> value;

  /**
   * Protected constructor for Memo class.
   * The {@code Actually<T>} value is initialized to be a Failure case
   * of Actually, when it's not calculated.
   *
   * @param c  {@code super(c)} so that the init field in Lazy is initialized.
   */
  protected Memo(Constant<? extends T> c) {
    super(c);
    this.value = Actually.err(new Exception("notCalculated"));
  }

  /**
   * Protected constructor for Memo class.
   * The {@code Actually<T>} value is initialized to be a Success case
   * of Actually, when we already have its value v.
   *
   * @param v  {@code super(() -> v)} so that the init field in Lazy is initialized.
   */
  protected Memo(T v) {
    super(() -> v);
    this.value = Actually.ok(v);
  }

  /**
   * Factory method for Memo.
   *
   * @param <T> Type parameter, indicating the value take in is type T,
   *            and the return type value is {@code Memo<T>}.
   * @param v   The value take in to instantiate corresponding Memo Object.
   * @return Memo object using the construct method with single type T argument
   */
  public static <T> Memo<T> from(T v) {
    return new Memo<>(v);
  }

  /**
   * Factory method for Memo.
   *
   * @param <T> Type parameter, indicating the value take in is type T,
   *            and the return type value is {@code Memo<T>}.
   * @param c   The Constant object take in to instantiate corresponding Memo Object.
   * @return Memo object using the construct method with single type Constant argument
   */
  public static <T> Memo<T> from(Constant<? extends T> c) {
    return new Memo<>(c);
  }

  /**
   * Method to get value of Memo Objects
   * if the value is available, simply return it.
   * if not, compute the value, update Actually value
   * and return it.
   * Each value is only calculated once
   *
   * @return call except method from the class Actually
   *         to return type T value,
   */
  public T get() {
    this.value = Actually.ok(this.value.except(super::get));
    return this.value.except(super::get);
  }

  /**
   * Override toString method.
   *
   * @return returns "?" if the value is not yet available,
   *         returns the string representation of the value
   *         otherwise.
   */
  @Override
  public String toString() {
    return this.value.transform(String::valueOf).unless("?");
  }

  /**
   * method to operate on the value of Memo.
   *
   * @param <R> Type parameter to indicate the return type {@code Memo<R>},
   *            and the type parameters of argument immutator.
   * @param immutator   immutator take in to modify the value
   * @return new {@code Memo<R>}.
   */
  @Override
  public <R> Memo<R> transform(Immutator<? extends R, ? super T> immutator) {
    return from(() -> immutator.invoke(this.get()));
  }

  /**
   * Next method, returns a new Memo Object.
   *
   * @param <R> Type parameter to indicate return type and type parameter
   *            for argument immutator.
   * @param immutator with the value that is going to be the next value
   *        inside the next {@code Memo<R>} Object.
   * @return new {@code Memo<R>}.
   */
  @Override
  public <R> Memo<R> next(Immutator<? extends Lazy<? extends R>, ? super T> immutator) {
    return from(() -> immutator.invoke(this.get()).get());
  }

  /**
   * combine method to lazily combine the two Memo objects
   * which may contain values of different types
   * and return a new Memo object.
   *
   * @param <S> Type parameter to indicate
   *            the second input value for combine function.
   * @param <R> Type parameter to indicate the return type of combine
   *            method in combiner.
   * @param memo Memo needed to be combined.
   * @param combiner Combiner function.
   * @return Memo consists of the two memos.
   */
  public <R, S> Memo<R> combine(Memo<S> memo,
      Combiner<? extends R, ? super T, ? super S> combiner) {
    return from(() -> combiner.combine(this.get(), memo.get()));
  }
}
