import cs2030s.fp.Combiner;
import cs2030s.fp.Immutator;
import cs2030s.fp.Memo;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper around a lazily evaluated and memoized
 * list that can be generated with a lambda expression.
 *
 * @author Adi Yoga S. Prabawa
 * @version CS2030S AY 22/23 Sem 1
 */
class MemoList<T> {
  /** The wrapped java.util.List object */
  private List<Memo<T>> list;

  /** 
   * A private constructor to initialize the list to the given one. 
   *
   * @param list The given java.util.List to wrap around.
   */
  private MemoList(List<Memo<T>> list) {
    this.list = list;
  }

  /** 
   * Generate the content of the list.  Given x and a lambda f, 
   * generate the list of n elements as [x, f(x), f(f(x)), f(f(f(x))), 
   * ... ]
   *
   * @param <T> The type of the elements in the list.
   * @param n The number of elements.
   * @param seed The first element.
   * @param f The immutator function on the elements.
   * @return The created list.
   */
  public static <T> MemoList<T> generate(int n, T seed,
      Immutator<? extends T, ? super T> f) {
    MemoList<T> memoList = new MemoList<>(new ArrayList<>());
    Memo<T> curr = Memo.from(seed);;
    for (int i = 0; i < n; i++) {
      memoList.list.add(curr);
      curr = curr.transform(f);
    }
    return memoList;
  }

  /**
   * Generate Lazy version of Fibonacci sequence,
   * each element is based on the result of operation on the previous two elements.
   *
   * @param <T>
   *          The type of the elements in the list.
   * @param n
   *          The number of elements.
   * @param fst
   *          The first element in the list.
   * @param snd
   *          The second element in the list.
   * @param f
   *          The combiner function operating on previous two elements.
   * @return The Fibonacci list created.
   */
  public static <T> MemoList<T> generate(int n, T fst, T snd,
      Combiner<? extends T, ? super T, ? super T> f) {
    MemoList<T> memoList = new MemoList<>(new ArrayList<>());
    Memo<T> memo1 = Memo.from(fst);
    Memo<T> memo2 = Memo.from(snd);
    memoList.list.add(memo1);
    memoList.list.add(memo2);
    for (int i = 2; i < n; i++) {
      if (i % 2 == 0) {
        memo1 = memo1.combine(memo2, f);
        memoList.list.add(memo1);
      } else {
        memo2 = memo2.combine(memo1, f);
        memoList.list.add(memo2);
      }
    }
    return memoList;
  }

  /**
   * Lazy version of map function so that immutator f is applied only
   * when necessary.
   *
   * @param <R>
   *          Type of the elements in the list.
   * @param f
   *          The immutator operation on elements.
   * @return The mapped list.
   */
  public <R> MemoList<R> map(Immutator<? extends R, ? super T> f) {
    MemoList<R> memoList = new MemoList<>(new ArrayList<>());
    for (Memo<T> memo : list) {
      memoList.list.add(memo.transform(f));
    }
    return memoList;
  }

  /**
   * FlatMap method that  the immutator returns an element of
   * type {@code MemoList<R>} corresponding to each element in the original list,
   * if the immutator is returning a MemoList, flapMap will flatten this nested list.
   *
   * @param <R>
   *          Type of the elements in the list.
   * @param f
   *          The immutator operation on elements.
   * @return The mapped list.
   */
  public <R> MemoList<R> flatMap(Immutator<? extends MemoList<R>, ? super T> f) {
    MemoList<R> memoList = new MemoList<>(new ArrayList<>());
    for (Memo<T> memo : list) {
      MemoList<R> tempList = f.invoke(memo.get());
      memoList.list.addAll(tempList.list);
    }
    return memoList;
  }

  /**
   * Return the element at index i of the list.
   *
   * @param i The index of the element to retrieved (0 for the 1st element).
   * @return The element at index i.
   */
  public T get(int i) {
    return this.list.get(i).get();
  }

  /**
   * Find the index of a given element.
   *
   * @param v The value of the element to look for.
   * @return The index of the element in the list.  -1 is element is not in the list.
   */
  public int indexOf(T v) {
    return this.list.indexOf(Memo.from(v));
  }

  /**
   * Return the string representation of the list.
   *
   * @return The string representation of the list.
   */
  @Override
  public String toString() {
    return this.list.toString();
  }
}
