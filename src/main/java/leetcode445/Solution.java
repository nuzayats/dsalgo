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
class Solution {
    // time and space complexity: O(l1size + l2size)
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> s = new Stack<>();
        int l1size = size(l1), l2size = size(l2);

        if (l1size > l2size) {
            l1 = traverse(l1, l1size - l2size, s);
        } else if (l1size < l2size) {
            l2 = traverse(l2, l2size - l1size, s);
        }

        while (l1 != null && l2 != null) {
            s.push(l1.val);
            l1 = l1.next;
            s.push(l2.val);
            l2 = l2.next;
        }

        return buildListNode(l1size, l2size, s);
    }

    private ListNode buildListNode(int l1size, int l2size, Stack<Integer> s) {
        ListNode cur = null, prev = null;
        int carry = 0;
        while (!s.isEmpty() || carry > 0) {
            int sum;
            if (l1size > 0 && l2size > 0) {
                sum = s.pop() + s.pop() + carry;
                l1size--;
                l2size--;
            } else if (l1size > 0) {
                sum = s.pop() + carry;
                l1size--;
            } else if (l2size > 0) {
                sum = s.pop() + carry;
                l2size--;
            } else {
                // case where the stack is empty but carry>0
                sum = carry;
            }

            carry = 0;
            if (sum > 9) {
                carry += sum / 10;
                sum = sum % 10;
            }

            cur = new ListNode(sum);
            cur.next = prev;
            prev = cur;
        }
        return cur;
    }

    private ListNode traverse(ListNode node, int size, Stack<Integer> s) {
        for (int i = 0; i < size; i++) {
            s.push(node.val);
            node = node.next;
        }
        return node;
    }

    private int size(ListNode node) {
        int size = 0;
        while (node != null) {
            node = node.next;
            size++;
        }
        return size;
    }
}
