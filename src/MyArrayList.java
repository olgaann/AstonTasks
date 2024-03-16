import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.StringJoiner;

public class MyArrayList<E> {


    private static final int DEFAULT_CAPACITY = 10;
    private static final int EXTENSION_KOFF = 2;
    private Object[] data;
    private int size = 0;
    private int capacity;

    public MyArrayList() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.capacity = data.length;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }


    public E get(int index) {
        ifIndexNotOkThrowException(index);
        return (E) data[index];
    }

    private void ifIndexNotOkThrowException(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(String.format("Элемента с индексом %d не существует", index));
        }
    }

    public boolean add(E e) {
        growIfNecessary();
        data[size] = e;
        size++;
        return true;
    }

    public boolean add(int index, E e) {
        growIfNecessary();
        System.arraycopy(data, index,
                data, index + 1,
                size - index);
        data[index] = e;
        size++;
        return true;
    }

    private void growIfNecessary() {
        if (size == data.length) {
            extension();
        }
    }

    private void extension() {
        int newCapacity = EXTENSION_KOFF * capacity;
        data = Arrays.copyOf(data, newCapacity);
        capacity = newCapacity;
    }

    public boolean addAll(Collection<? extends E> c) {
        int collSize = c.size();
        growIfNecessary(collSize);
        Object[] sourceArray = c.toArray();
        System.arraycopy(sourceArray, 0, data, size, collSize);
        size += collSize;
        return true;
    }

    private void growIfNecessary(int collSize) {
        if (size + collSize > data.length) {
            extension(collSize);
        }
    }

    private void extension(int collSize) {
        int newCapacity = capacity + collSize * EXTENSION_KOFF;
        data = Arrays.copyOf(data, newCapacity);
        capacity = newCapacity;
    }

    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E remove(int index) {
        ifIndexNotOkThrowException(index);
        E removing = (E) data[index];
        return removing;
    }

    public boolean remove(Object o) {
        if (o == null) {
            return seekNullForDeleting();
        } else {
            return seekObjectForDeleting(o);
        }
    }

    private boolean seekNullForDeleting() {
        for (int index = 0; index < size; index++) {
            if (data[index] == null) {
                deleteElemAndMoveOthers(index);
                return true;
            }
        }
        return false;
    }

    private boolean seekObjectForDeleting(Object o) {
        for (int index = 0; index < size; index++) {
            if (data[index].equals(o)) {
                deleteElemAndMoveOthers(index);
                return true;
            }
        }
        return false;
    }

    private void deleteElemAndMoveOthers(int index) {
        System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[size - 1] = null;
        size--;
    }

    public void sort(Comparator<? super E> c) {
        sort(data, 0, size - 1, c);
    }

    private static void sort(Object[] arr, int left, int right, Comparator c) {
        if (left < right) {
            int divideIndex = partition(arr, left, right, c);
            sort(arr, left, divideIndex - 1, c);
            sort(arr, divideIndex, right, c);
        }
    }

    private static int partition(Object[] data, int left, int right, Comparator c) {
        Object pivot = data[left + (right - left) / 2];
        while (left <= right) {

            while (c.compare(data[left], pivot) < 0) {
                left++;
            }

            while (c.compare(data[right], pivot) > 0) {
                right--;
            }

            if (left <= right) {
                swap(data, right, left);
                left++;
                right--;
            }
        }
        return left;
    }

    private static void swap(Object[] data, int index1, int index2) {
        Object temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" ", "[", "]");
        for (int i = 0; i < this.size; i++) {
            joiner.add(this.get(i) != null ? this.get(i).toString() : "null");
        }
        return joiner.toString();
    }
}
