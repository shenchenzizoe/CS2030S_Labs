package cs2030s.fp;

import java.util.Objects;

public abstract class  Actually<T> implements Immutatorable<T>, Actionable<T> {
  public static <T> Actually<T> ok(T value) {
    return new Success<T>(value);
  }

  public static <T> Actually<T> err(Exception exception) {
    // safe to cast here as failure is a subtype of Actually<Object>
    @SuppressWarnings("unchecked")
    Actually<T> failure = (Actually<T>) new Failure(exception);
    return failure;
  }

  public abstract T unwrap() throws Exception;

  public abstract <S extends T> T except(Constant<S> constant);

  public abstract void finish(Action<? super T> action);

  public abstract <I extends T> T unless(I item);

  public abstract <R> Actually<R> next(Immutator<? extends Actually<? extends R>,
      ? super T> immutator);

  public abstract void act(Action<? super T> action);

  public abstract <R> Actually<R> transform(Immutator<? extends R, ? super T> immutator);

  static final class Success<T> extends Actually<T> {
    private final T value;

    private Success(T value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      } else if (obj instanceof Success<?>) {
        if (this.value == ((Success<?>) obj).value) {
          return true;
        } else if (this.value != null && this.value.equals(((Success<?>) obj).value)) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    @Override
    public T unwrap() {
      return this.value;
    }

    @Override
    public <S extends T> T except(Constant<S> constant) {
      return this.value;
    }

    @Override
    public void finish(Action<? super T> action) {
      action.call(this.value);
    }

    @Override
    public <I extends T> T unless(I item) {
      return this.value;
    }

    @Override
    public <R> Actually<R> transform(Immutator<? extends R, ? super T> immutator) {
      try {
        return Actually.ok(immutator.invoke(this.value));
      } catch (Exception exception) {
        return Actually.err(exception);
      }
    }

    @Override
    public void act(Action<? super T> action) {
      action.call(this.value);
    }

    @Override
    public <R> Actually<R> next(Immutator<? extends Actually<? extends R>, ? super T> immutator) {
      try {
        return (Actually<R>) immutator.invoke(this.value);
      } catch (Exception exception) {
        return Actually.err(exception);
      }
    }

    @Override
    public String toString() {
      if (this.value != null) {
        return "<" + value + ">";
      } else {
        return "<null>";
      }
    }
  }

  static final class Failure extends Actually<Object> {
    private final Exception exception;

    private Failure(Exception exception) {
      this.exception = exception;
    }

    @Override
    public String toString() {
      return "[" + exception.getClass().getName() + "] " + exception.getMessage();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      } else if (obj instanceof Failure) {
        if (this.exception.getMessage() == null ||
            ((Failure) obj).exception.getMessage() == null) {
          return false;
        } else if (this.exception.getMessage().equals(((Failure) obj).exception.getMessage())) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    @Override
    public Object unwrap() throws Exception {
      throw this.exception;
    }

    @Override
    public <S extends Object> S except(Constant<S> constant) {
      return constant.init();
    }

    @Override
    public void finish(Action<? super Object> action) {
      return;
    }

    @Override
    public <I extends Object> Object unless(I item) {
      return item;
    }

    @Override
    public <R> Actually<R> transform(Immutator<? extends R, ? super Object> immutator) {
      return Actually.err(this.exception);
    }

    @Override
    public void act(Action<? super Object> action) {
    }

    @Override
    public <R> Actually<R> next(Immutator<? extends Actually<? extends R>,
        ? super Object> immutator) {
      return Actually.err(this.exception);
    }
  }
}
