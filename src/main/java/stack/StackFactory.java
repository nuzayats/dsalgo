package stack;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A class which creates an arbitrary number of stacks
 * and maintains all the data the stacks keep in one dimension byte array.
 * <p>
 * In the array, one element uses 5 bytes. We call these 5 bytes a slot.
 * A slot consists of 2 parts (similar to a linked list).
 * The first part is the id of the next slot. The second part is the value.
 * <p>
 * The only data type which the stacks support is byte.
 */
public class StackFactory {

    /**
     * 1 byte (the value) + 4 bytes (the index of the next slot, in int)
     */
    private static final int SLOT_SIZE = 1 + 4;

    private byte[] data;
    private int size;

    private final Queue<Integer> freeSlots;

    /**
     * @param size An approximate total number of elements the stacks this instance creates will keep.
     *             If the number of the elements exceeds this parameter, the underlying array will be
     *             automatically doubled in order to accommodate additional data.
     */
    public StackFactory(int size) {
        this.size = size;
        this.data = new byte[SLOT_SIZE * size];

        freeSlots = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            freeSlots.offer(i);
        }
    }

    public Stack createStack() {
        return new Stack();
    }

    static byte[] toByte(int x) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) x;
            x = x >>> 8;
        }
        return bytes;
    }

    static int toInt(byte[] b) {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            int t = b[i] & 0xFF;
            t = t << (8 * i);
            x |= t;
        }
        return x;
    }

    private void ensureCapacity() {
        if (freeSlots.isEmpty()) {
            int newSize = size * 2;

            byte[] newData = new byte[newSize * SLOT_SIZE];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }

            for (int i = size; i < newSize; i++) {
                freeSlots.offer(i);
            }

            this.size = newSize;
            this.data = newData;
        }
    }

    public class Stack {

        private int headSlot = -1;

        public void push(byte val) {
            ensureCapacity();

            int slot = freeSlots.remove();
            int idxOfSlot = slot * SLOT_SIZE;
            byte[] next = toByte(headSlot);

            for (int i = 0; i < next.length; i++) {
                data[idxOfSlot + i] = next[i];
            }
            data[idxOfSlot + 4] = val;

            headSlot = slot;
        }

        public byte pop() {
            if (headSlot == -1) {
                throw new NoSuchElementException();
            }

            int idxOfSlot = headSlot * SLOT_SIZE;

            byte[] nextBytes = new byte[4];
            for (int i = 0; i < nextBytes.length; i++) {
                nextBytes[i] = data[idxOfSlot + i];
            }

            byte val = data[idxOfSlot + 4];

            freeSlots.offer(headSlot);
            headSlot = toInt(nextBytes);
            return val;
        }
    }
}
