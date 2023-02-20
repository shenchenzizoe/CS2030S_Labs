public class Array <T extends Comparable<T>> { // TODO: Change to bounded type parameter
    private T[] array;

    Array(int size) {
        T[] temp = (T[]) new Comparable[size];
        this.array = temp;
    }

    public void set(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    public T min() {
        T min = array[0];
       for (int i = 1; i < array.length; i++){
           if(array[i].compareTo(min) < 0) {
               min = array[i];
           }
       }
       return min;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[ ");
        for (int i = 0; i < array.length; i++) {
            s.append(i + ":" + array[i]);
            if (i != array.length - 1) {
                s.append(", ");
            }
        }
        return s.append(" ]").toString();
    }
}

