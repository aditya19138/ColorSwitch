package sample;

import java.util.ArrayList;
import java.util.List;

public class Iteration implements Container {

    public List<Integer> arr = new ArrayList<Integer>();

    public Iteration(List<Integer> arr) {
        this.arr = arr;
    }

    @Override
    public Iterator getIterator() {
        return new Iterator() {
            int index;
            @Override
            public boolean hasNext() {
                return index < arr.size();
            }

            @Override
            public Object next() {
                if(this.hasNext()){
                    return arr.get(index++);
                }
                return null;
            }
        };
    }
}
