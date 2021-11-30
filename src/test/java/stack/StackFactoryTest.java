package stack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import stack.StackFactory.Stack;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StackFactoryTest {

    StackFactory sut = new StackFactory(5);

    @Test
    @DisplayName("toByte() and toInt() work")
    void byteAndInt() {
        assertThat(StackFactory.toInt(StackFactory.toByte(-1))).isEqualTo(-1);
        assertThat(StackFactory.toInt(StackFactory.toByte(Integer.MAX_VALUE))).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("push() and pop() work for one instance")
    void stack1() {
        Stack s = sut.createStack();

        s.push((byte) 1);
        s.push((byte) 2);

        assertThat(s.pop()).isEqualTo((byte) 2);
        assertThat(s.pop()).isEqualTo((byte) 1);
    }

    @Test
    @DisplayName("push() and pop() work for multiple instances")
    void stack2() {
        Stack s1 = sut.createStack(), s2 = sut.createStack();

        s1.push((byte) 1);
        s2.push((byte) 2);
        s1.push((byte) 3);
        s2.push((byte) 4);
        s1.push((byte) 5);

        assertThat(s1.pop()).isEqualTo((byte) 5);
        assertThat(s2.pop()).isEqualTo((byte) 4);
        assertThat(s1.pop()).isEqualTo((byte) 3);
        assertThat(s2.pop()).isEqualTo((byte) 2);
        assertThat(s1.pop()).isEqualTo((byte) 1);
    }

    @Test
    @DisplayName("pop() throws exception when empty")
    void pop() {
        Stack s1 = sut.createStack();
        assertThatThrownBy(s1::pop).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("pop() throws exception when exhausted")
    void pop2() {
        Stack s1 = sut.createStack();
        s1.push((byte) 1);

        s1.pop();
        assertThatThrownBy(s1::pop).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("it reuses space freed by pop()")
    void reuse() {
        Stack s1 = sut.createStack();
        s1.push((byte) 1);
        s1.push((byte) 2);
        s1.push((byte) 3);
        s1.push((byte) 4);
        s1.push((byte) 5);
        s1.pop();

        s1.push((byte) 6);

        assertThat(s1.pop()).isEqualTo((byte) 6);
    }

    @Test
    @DisplayName("push() expands underlying array")
    void expand() {
        Stack s1 = sut.createStack();
        s1.push((byte) 1);
        s1.push((byte) 2);
        s1.push((byte) 3);
        s1.push((byte) 4);
        s1.push((byte) 5);

        s1.push((byte) 6);

        assertThat(s1.pop()).isEqualTo((byte) 6);
        assertThat(s1.pop()).isEqualTo((byte) 5);
        assertThat(s1.pop()).isEqualTo((byte) 4);
        assertThat(s1.pop()).isEqualTo((byte) 3);
        assertThat(s1.pop()).isEqualTo((byte) 2);
        assertThat(s1.pop()).isEqualTo((byte) 1);
    }
}
