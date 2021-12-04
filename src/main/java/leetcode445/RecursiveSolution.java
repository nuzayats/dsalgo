package leetcode445;

import java.util.Stack;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode() {}
 * ListNode(int val) { this.val = val; }
 * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class RecursiveSolution {

    public ListNode addTwoNumbers(ListNode a, ListNode b) {
        int asize = size(a), bsize = size(b);

        Stack<Integer> st = new Stack<>();

        if (asize > bsize) {
            a = traverse(a, asize - bsize, st);
        } else if (asize < bsize) {
            b = traverse(b, bsize - asize, st);
        }

        traverseUntilEnd(a, b, st);

        Stack<ListNode> nodeSt = new Stack<>();
        calculate(st, asize, bsize, 0, nodeSt);
        return createNode(nodeSt);
    }

    ListNode createNode(Stack<ListNode> nodeSt) {
        if (nodeSt.isEmpty()) return null;

        ListNode head = nodeSt.pop();
        head.next = createNode(nodeSt);
        return head;
    }

    void calculate(Stack<Integer> st, int asize, int bsize, int carry, Stack<ListNode> nodeSt) {
        if (st.isEmpty() && carry == 0) return;

        int sum;
        if (asize > 0 && bsize > 0) {
            int val1 = st.pop(), val2 = st.pop();
            sum = val1 + val2 + carry;
            asize--;
            bsize--;
        } else if (asize > 0) {
            sum = st.pop() + carry;
            asize--;
        } else if (bsize > 0) {
            sum = st.pop() + carry;
            bsize--;
        } else {
            sum = carry;
        }

        carry = 0;
        if (sum > 9) {
            carry = sum / 10;
            sum = sum % 10;
        }

        ListNode node = new ListNode(sum);
        nodeSt.push(node);
        calculate(st, asize, bsize, carry, nodeSt);
    }

    void traverseUntilEnd(ListNode n1, ListNode n2, Stack<Integer> st) {
        if (n1 == null) return;

        st.push(n1.val);
        st.push(n2.val);

        traverseUntilEnd(n1.next, n2.next, st);
    }

    ListNode traverse(ListNode node, int size, Stack<Integer> st) {
        if (size == 0) return node;
        st.push(node.val);
        return traverse(node.next, size - 1, st);
    }

    int size(ListNode node) {
        if (node == null) return 0;
        return 1 + size(node.next);
    }
}
