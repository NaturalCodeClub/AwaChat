package org.prawa.simpleutils.concurrent.forkjointasks;

import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class ParallelListTraverse<E> extends RecursiveAction {

    private final List<E> list;
    private final int start;
    private final int end;
    private final Consumer<E> action;
    private final int threshold;

    public ParallelListTraverse(List<E> list, Consumer<E> action, int taskPerThread) {
        this.action = action;
        this.list = list;
        this.threshold = taskPerThread;
        this.start = 0;
        this.end = list.size();
    }

    private ParallelListTraverse(List<E> list, Consumer<E> action, int start, int end, int taskPerThread) {
        this.action = action;
        this.list = list;
        this.threshold = taskPerThread;
        this.start = start;
        this.end = end;
    }

    public ParallelListTraverse(List<E> list, int threads,Consumer<E> action) {
        this.action = action;
        this.list = list;
        int taskPerThread = list.size() / threads;
        if (taskPerThread < 2) {
            taskPerThread = 2;
        }
        this.threshold = taskPerThread;
        this.start = 0;
        this.end = list.size();
    }

    @Override
    protected void compute() {
        try {
            if (end - start < this.threshold) {
                for (int i = start; i < end; i++) {
                    try {
                        this.action.accept(list.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                int middle = (start + end) / 2;
                invokeAll(new ParallelListTraverse<>(list, this.action, start, middle, this.threshold), new ParallelListTraverse<>(list, this.action, middle, end, this.threshold));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
