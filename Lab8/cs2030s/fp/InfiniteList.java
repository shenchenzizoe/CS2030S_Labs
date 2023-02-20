package cs2030s.fp;

import java.util.ArrayList;
import java.util.List;

public class InfiniteList<T> {
  private Memo<Actually<T>> head;
  private Memo<InfiniteList<T>> tail;

  /**
   * Private constructor of InfiniteList.
   *
   * @param head The head of the list.
   * @param tail The tail of the list (another InfiniteList).
   */
  private InfiniteList(Memo<Actually<T>> head, Memo<InfiniteList<T>> tail) {
    this.head = head;
    this.tail = tail;
  }

  // You may add other private constructor but it's not necessary.
  /**
   * Another private constructor of InfiniteList.
   */
  private InfiniteList() {
  }

  /**
   * Produce an infiniteList with the given Constant prod.
   *
   * @param <T>  Type of list elements.
   * @param prod Constant to produce list elements.
   * @return InfiniteList of prod elements.
   */
  public static <T> InfiniteList<T> generate(Constant<T> prod) {
    return new InfiniteList<>(Memo.from(() -> Actually.ok(prod.init())),
        Memo.from(() -> InfiniteList.generate(prod)));
  }

  /**
   * Produce an infiniteList with the given immutator.
   * so that it returns a list that the successor element is
   * the result of applying the function to the predecessor
   * element.
   *
   * @param <T>  Type of list elements.
   * @param seed first element.
   * @param func function applied to the predecessor to
   *        obtain the predecessor.
   * @return InfiniteList of iteration of the element.
   */
  public static <T> InfiniteList<T> iterate(T seed, Immutator<T, T> func) {
    return new InfiniteList<>(Memo.from(Actually.ok(seed)),
        Memo.from(() -> InfiniteList.iterate(func.invoke(seed), func)));
  }

  /**
   * Get the head of list, jump over the empty elements.
   *
   * @return The first non-empty element.
   */
  public T head() {
    Actually<T> curHead = this.head.get();
    Constant<T> nextHead = (() -> this.tail.get().head());
    return curHead.except(nextHead);
  }

  /**
   * Get the tail of list, jump over the empty elements.
   *
   * @return The tail of the first non-empty element.
   */
  public InfiniteList<T> tail() {
    Actually<T> curHead = this.head.get();
    Constant<InfiniteList<T>> nextTail = (() -> this.tail.get().tail());
    return curHead
        .transform(x -> this.tail.get())
        .except(nextTail);
  }

  /**
   * Lazy version of applying immutator f to list elements.
   *
   * @param <R> element type of returned list.
   * @param f   immutator applied to change each element.
   * @return InfiniteList with modified elements.
   */
  public <R> InfiniteList<R> map(Immutator<? extends R, ? super T> f) {
    Memo<Actually<R>> curHead = this.head.transform(x -> x.transform(f));
    Memo<InfiniteList<R>> curTail = this.tail.transform(y -> y.map(f));
    return new InfiniteList<R>(curHead, curTail);
  }

  /**
   * Remove elements which do not meet th condition
   * of the Immutator pred.
   *
   * @param pred immutator checking the predicate condition.
   * @return InfiniteList of filtered elements.
   */
  public InfiniteList<T> filter(Immutator<Boolean, ? super T> pred) {
    Memo<Actually<T>> curHead = this.head.transform(x -> x.check(pred));
    Memo<InfiniteList<T>> curTail = this.tail.transform(y -> y.filter(pred));
    return new InfiniteList<>(curHead, curTail);
  }

  /**
   * Convert an infiniteList into a finiteList.
   *
   * @param n elements in the returned finiteList.
   * @return finiteList from the original infiniteList
   */
  public InfiniteList<T> limit(long n) {
    if (n > 0) {
      //immutator to produce the remaining
      //of finiteList.
      Immutator<InfiniteList<T>,
          ? super T> immutator =
              x -> this.tail.get().limit(n - 1);
      //provide the tail of the finiteList
      // when it has not reached the end.
      Constant<InfiniteList<T>> nextTail =
          () -> this.tail.get().limit(n);
      return new InfiniteList<>(this.head,
          Memo.from(() -> this.head.get()
              .transform(immutator)
              .except(nextTail)));
    } else {
      return end();
    }
  }

  /**
   * Convert infiniteList to a finiteList
   * until it reaches the first element
   * that fails the predicate.
   *
   * @param pred predicate determining whether
   *             to keep the element.
   * @return finiteList containing elements
   *         before the first one that fails
   *         the condition.
   */
  public InfiniteList<T> takeWhile(Immutator<Boolean, ? super T> pred) {
    Memo<Actually<T>> curHead =
        Memo.from(() -> Actually.ok(
            this.head()).check(pred));
    Immutator<InfiniteList<T>, ? super T> immutator =
        x -> this.tail().takeWhile(pred);
    return new InfiniteList<>(
        curHead,
        Memo.from(() -> curHead.get()
            // checks if its passes the condition
            .transform(immutator)
            // if fails the condition or reaches the end
            .except(End::end)));
  }

  /**
   * Convert infiniteList to a List.
   *
   * @return List containing all the elements in the InfiniteList.
   */
  public List<T> toList() {
    List<T> list = new ArrayList<>();
    Action<T> add = list::add;
    InfiniteList<T> curList = this;
    while (!curList.isEnd()) {
      Actually<T> curHead = curList.head.get();
      curHead.finish(add);
      curList = curList.tail.get();
    }
    return list;
  }

  /**
   * To accumulate elements in the InfiniteList
   * with given combiner.
   *
   * @param <U> return type.
   * @param id  initial value for accumulation.
   * @param acc combiner to accumulate elements.
   * @return finial accumulated value.
   */
  public <U> U reduce(U id, Combiner<U, U, ? super T> acc) {
    Actually<T> curHead = this.head.get();
    InfiniteList<T> curTail =
        this.tail.get();
    // immutator that calculate the result
    // of each accumulation
    Immutator<U, ? super T> immutator =
        x -> acc.combine(id, x);
    // update the initial value
    U pass = curHead.transform(immutator)
        .unless(id);
    return curTail.reduce(pass, acc);
  }

  /**
   * Count number of elements in infiniteList.
   *
   * @return Count of elements.
   */
  public long count() {
    return reduce((long) 0,
        (x, y) -> x + (long) 1);
  }

  /**
   * Override toString method of InfiniteList.
   *
   * @return String representation of the list.
   */
  @Override
  public String toString() {
    return "[" + this.head + " " + this.tail + "]";
  }

  /**
   * Check if it reaches the end of the List.
   *
   * @return return false when it has not
   *         reached the end of the list.
   */
  public boolean isEnd() {
    return false;
  }

  /**
   * Saves the end a list as a static final field.
   */
  private static final End END = new End();

  /**
   * Produce the end of InfiniteList.
   *
   * @param <T> Element type in the list.
   * @return an Empty InfiniteList.
   */
  public static <T> InfiniteList<T> end() {
    @SuppressWarnings("unchecked")
    InfiniteList<T> curEnd = (InfiniteList<T>) END;
    return curEnd;
  }

  // Add your End class here...
  static class End extends InfiniteList<Object> {
    /**
     * Constructor of End class.
     */
    End() {
      super();
    }

    /**
     * If reaches the end of the list
     * calling head throws NoSuchElementException.
     *
     * @return NoSuchElementException.
     */
    @Override
    public Object head() {
      throw new java.util.NoSuchElementException();
    }

    /**
     * If reaches the end of the list
     * calling tail throws NoSuchElementException.
     *
     * @return NoSuchElementException.
     */
    @Override
    public InfiniteList<Object> tail() {
      throw new java.util.NoSuchElementException();
    }

    /**
     * Lazy version of applying immutator f to list elements.
     *
     * @param <R> element type of returned list.
     * @param f   immutator applied to change each element.
     * @return InfiniteList with modified elements,
     *         in this case an empty list.
     */
    @Override
    public <R> InfiniteList<R> map(Immutator<? extends R, ? super Object> f) {
      return end();
    }

    /**
     * Remove elements which do not meet th condition
     * of the Immutator pred.
     *
     * @param pred immutator checking the predicate condition.
     * @return InfiniteList of filtered elements.
     *         in this case an empty list.
     */
    @Override
    public InfiniteList<Object> filter(Immutator<Boolean, ? super Object> pred) {
      return end();
    }

    /**
     * Convert an infiniteList into a finiteList.
     *
     * @param n elements in the returned finiteList.
     * @return empty infiniteList
     */
    @Override
    public InfiniteList<Object> limit(long n) {
      return end();
    }

    /**
     * Convert infiniteList to a finiteList
     * until it reaches the first element
     * that fails the predicate.
     *
     * @param pred predicate determining whether
     *             to keep the element.
     * @return emptyList.
     */
    @Override
    public InfiniteList<Object> takeWhile(Immutator<Boolean, ? super Object> pred) {
      return end();
    }

    /**
     * To accumulate elements in the InfiniteList
     * with given combiner.
     *
     * @param <U> return type.
     * @param id  initial value for accumulation.
     * @param acc combiner to accumulate elements.
     * @return initial value given.
     */
    @Override
    public <U> U reduce(U id, Combiner<U, U, ? super Object> acc) {
      return id;
    }

    /**
     * Number of elements in InfiniteList.
     *
     * @return zero.
     */
    @Override
    public long count() {
      return 0;
    }

    /**
     * Produces an empty list.
     *
     * @return Empty List.
     */
    @Override
    public List<Object> toList() {
      return List.of();
    }

    /**
     * To check if the InfiniteList reaches the end.
     *
     * @return true in case it is the end.
     */
    public boolean isEnd() {
      return true;
    }

    /**
     * An end returns "-".
     */
    @Override
    public String toString() {
      return "-";
    }
  }
}