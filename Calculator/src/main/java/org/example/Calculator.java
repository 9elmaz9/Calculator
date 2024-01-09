package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //loop
        while (true) {
            System.out.println("Enter a mathematical expression");
            String expression = scanner.nextLine();
            //check
            if (expression.equalsIgnoreCase("exit")) {
                System.out.println("Program completed.");
                break;
            }

            try {
                double result = evaluateExpression(expression);
                System.out.println("Result:" + result);
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
    }

    //  expression evaluate method
    private static double evaluateExpression(String expression) {

        //converting an infix expression to a postfix expression
        Queue<String> postfixQueue = infixToPostfix(expression);
        //calculating the value of a postfix expression
        return evaluatePostfix(postfixQueue);
    }


    // converting an infix expression to a postfix expression
    private static Queue<String> infixToPostfix(String infix) {
        Queue<String> postfixQueue = new LinkedList<>(); //to store postfix
        Stack<String> operatorStack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder(); // construct complete num
                number.append(c);

                while (i + 1 < infix.length() && (Character.isDigit(infix.charAt(i + 1)) || infix.charAt(i + 1) == '.')) {
                    number.append(infix.charAt(++i));
                }
//add
                postfixQueue.offer(number.toString());
            } else if (isOperator(c)) {
                if (c == '(') {
                    operatorStack.push("(");
                } else if (c == ')') {
                    while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                        postfixQueue.offer(operatorStack.pop());
                    }
                    if (!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
                        operatorStack.pop(); // Remove '(' from the stack
                    } else {
                        throw new IllegalArgumentException("Mismatched parentheses");
                    }
                } else {
                    //for other operator
                    String currentOperator = String.valueOf(c);

                    while (!operatorStack.isEmpty() && getPrecedence(operatorStack.peek().charAt(0)) >= getPrecedence(c) && !operatorStack.peek().equals("(")) {
                        postfixQueue.offer(operatorStack.pop());
                    }

                    operatorStack.push(currentOperator);
                }
            }
        }

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek().equals("(") || operatorStack.peek().equals(")")) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            postfixQueue.offer(operatorStack.pop());
        }
        // return the final postfix expression
        return postfixQueue;
    }

    // () postfix expression evaluation
    private static double evaluatePostfix(Queue<String> postfixQueue) {
        Stack<Double> operandStack = new Stack<>();

        while (!postfixQueue.isEmpty()) {
            String token = postfixQueue.poll();
            // if (Character.isDigit(token.charAt(0)) || token.equals(".")) {
            if (isNumber(token)) {
                operandStack.push(Double.parseDouble(token));

            } else if (isOperator(token.charAt(0))) {
                double operand2 = operandStack.pop();
                double operand1 = operandStack.pop();

                operandStack.push(applyOperator(token.charAt(0), operand1, operand2));
            }
        }
        //result on the top of the stak
        return operandStack.pop();
    }


    //check char tp operat
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }

    //check ? if string num
    private static boolean isNumber(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //a priority
    private static int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            default:
                throw new IllegalArgumentException("Invalid operator" + operator);

        }
    }


    //applying an operator to two perands
    private static double applyOperator(char operator, double operand1, double operand2) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("/division by zero/");
                }
                return operand1 / operand2;
            case '%':
                return operand1 % operand2;
            default:
                throw new IllegalArgumentException("Invalid operator :" + operator);
        }
    }
}