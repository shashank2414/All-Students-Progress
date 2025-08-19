import java.util.Stack;

    public class PostfixEvaluation {

        // Function to evaluate a postfix expression
        public static int evaluatePostfix(String exp) {
            Stack<Integer> stack = new Stack<>();

            // Traverse the expression
            for (int i = 0; i < exp.length(); i++) {
                char c = exp.charAt(i);

                // Skip spaces
                if (c == ' ')
                    continue;

                // If the character is a number, push it into the stack
                if (Character.isDigit(c)) {
                    stack.push(c - '0'); // convert char to int
                }
                // Otherwise the character is an operator
                else {
                    int val2 = stack.pop();
                    int val1 = stack.pop();

                    switch (c) {
                        case '+':
                            stack.push(val1 + val2);
                            break;
                        case '-':
                            stack.push(val1 - val2);
                            break;
                        case '*':
                            stack.push(val1 * val2);
                            break;
                        case '/':
                            stack.push(val1 / val2);
                            break;
                    }
                }
            }

            // The final result will be on top of the stack
            return stack.pop();
        }

        public static void main(String[] args) {
            String expression = "2 3 + 4 * 5 +";   // Example from your image
            System.out.println("Postfix Evaluation: " + evaluatePostfix(expression));
        }
    }