package org.minijax.persistence.lazy;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.minijax.persistence.MinijaxBaseTypedQuery;

public class LazyList<E> implements List<E> {
    private final MinijaxBaseTypedQuery<E> query;
    private List<E> data;

    public LazyList(final MinijaxBaseTypedQuery<E> query) {
        this.query = query;
    }

    private List<E> getData() {
        if (data == null) {
            data = query.getResultList();
        }
        return data;
    }

    @Override
    public boolean isEmpty() {
        return getData().isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return getData().contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return getData().iterator();
    }

    @Override
    public boolean add(final E e) {
        return getData().add(e);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return getData().containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        return getData().addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        return getData().addAll(index, c);
    }

    @Override
    public void clear() {
        getData().clear();
    }

    @Override
    public boolean equals(final Object o) {
        return getData().equals(o);
    }

    @Override
    public int hashCode() {
        return getData().hashCode();
    }

    @Override
    public E get(final int index) {
        return getData().get(index);
    }

    @Override
    public void add(final int index, final E element) {
        getData().add(index, element);
    }

    @Override
    public void forEach(final Consumer<? super E> arg0) {
        getData().forEach(arg0);
    }

    @Override
    public int indexOf(final Object o) {
        return getData().indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return getData().lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return getData().listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return getData().listIterator(index);
    }

    @Override
    public Stream<E> parallelStream() {
        return getData().parallelStream();
    }

    @Override
    public int size() {
        return getData().size();
    }

    @Override
    public boolean remove(final Object o) {
        return getData().remove(o);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return getData().removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return getData().retainAll(c);
    }

    @Override
    public E remove(final int index) {
        return getData().remove(index);
    }

    @Override
    public boolean removeIf(final Predicate<? super E> arg0) {
        return getData().removeIf(arg0);
    }

    @Override
    public Object[] toArray() {
        return getData().toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return getData().toArray(a);
    }

    @Override
    public void replaceAll(final UnaryOperator<E> operator) {
        getData().replaceAll(operator);
    }

    @Override
    public void sort(final Comparator<? super E> c) {
        getData().sort(c);
    }

    @Override
    public E set(final int index, final E element) {
        return getData().set(index, element);
    }

    @Override
    public Spliterator<E> spliterator() {
        return getData().spliterator();
    }

    @Override
    public Stream<E> stream() {
        return getData().stream();
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        return getData().subList(fromIndex, toIndex);
    }

    @Override
    public <T> T[] toArray(final IntFunction<T[]> arg0) {
        return getData().toArray(arg0);
    }
}
