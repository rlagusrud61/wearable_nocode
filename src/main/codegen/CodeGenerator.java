package main.codegen;


import main.DataStructure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CodeGenerator {

    enum AnalogInput {
        A1,
        A2,
        A3,
    }

    enum DigitalOutput {
        D1,
        D2,
        D3,
    }

    interface ProgramNode {
        void accept(ProgramVisitor visitor);
    }

    interface BoolExpression extends ProgramNode {
    }

    interface DoubleExpression extends ProgramNode {
    }

    record BoolLiteral(boolean value) implements BoolExpression {
        @Override
        public void accept(ProgramVisitor visitor) {
            visitor.visit(this);
        }
    }

    record DoubleLiteral(double value) implements DoubleExpression {
        @Override
        public void accept(ProgramVisitor visitor) {
            visitor.visit(this);
        }
    }

    record AnalogInputExpression(AnalogInput analogInput) implements DoubleExpression {
        @Override
        public void accept(ProgramVisitor visitor) {
            visitor.visit(this);
        }
    }

    enum ComparisonOperator {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_EQUALS,
        SMALLER_THAN,
        SMALLER_THAN_EQUALS,
    }

    record DoubleComparisonExpression(ComparisonOperator operator, DoubleExpression leftExpr,
                                      DoubleExpression rightExpr) implements BoolExpression {
        @Override
        public void accept(ProgramVisitor visitor) {
            leftExpr.accept(visitor);
            rightExpr.accept(visitor);
            visitor.visit(this);
        }
    }

    enum BoolOperator {
        AND,
        OR,
        XOR,
    }

    record BoolOperationExpression(BoolOperator operator, BoolExpression leftExpr,
                                   BoolExpression rightExpr) implements BoolExpression {
        @Override
        public void accept(ProgramVisitor visitor) {
            leftExpr.accept(visitor);
            rightExpr.accept(visitor);
            visitor.visit(this);
        }
    }

    record NotExpression(BoolExpression expr) implements BoolExpression {
        @Override
        public void accept(ProgramVisitor visitor) {
            expr.accept(visitor);
            visitor.visit(this);
        }
    }

    interface Statement extends ProgramNode {

    }

    record DigitalOutputStatement(DigitalOutput digitalOutput, BoolExpression expr) implements Statement {
        @Override
        public void accept(ProgramVisitor visitor) {
            expr.accept(visitor);
            visitor.visit(this);
        }
    }

    static class Program implements ProgramNode {
        public final int updateFrequency;
        public final List<Statement> statements;

        public Program(int updateFrequency, Statement... statements) {
            this.updateFrequency = updateFrequency;
            this.statements = List.of(statements);
        }

        @Override
        public void accept(ProgramVisitor visitor) {
            for (var statement : statements) {
                statement.accept(visitor);
            }

            visitor.visit(this);
        }
    }

    interface ProgramVisitor {
        void visit(AnalogInputExpression expression);

        void visit(BoolLiteral expression);

        void visit(DoubleLiteral expression);

        void visit(DoubleComparisonExpression expression);

        void visit(BoolOperationExpression expression);

        void visit(NotExpression notExpression);

        void visit(DigitalOutputStatement assignment);

        void visit(Program program);
    }

    static class CodeGeneratorVisitor implements ProgramVisitor {
        private final Deque<String> exprStack = new ArrayDeque<>();
        private final List<String> generatedStatements = new ArrayList<>();
        private String generatedCode;
        // boolean if neopixel

        @Override
        public void visit(AnalogInputExpression expression) {
            var varName = String.format("ANALOG_IN_%s", expression.analogInput);
            exprStack.push(varName);
        }

        @Override
        public void visit(BoolLiteral expression) {
            var value = Boolean.toString(expression.value);
            exprStack.push(value);
        }

        @Override
        public void visit(DoubleLiteral expression) {
            var value = Double.toString(expression.value);
            exprStack.push(value);
        }

        @Override
        public void visit(DoubleComparisonExpression expression) {
            var leftExpr = exprStack.pop();
            var rightExpr = exprStack.pop();
            var operator = switch (expression.operator) {
                case EQUALS -> "==";
                case NOT_EQUALS -> "!=";
                case GREATER_THAN -> ">";
                case GREATER_THAN_EQUALS -> ">=";
                case SMALLER_THAN -> "<";
                case SMALLER_THAN_EQUALS -> "<=";
            };
            var expr = String.format("(%s %s %s)", leftExpr, operator, rightExpr);
            exprStack.push(expr);
        }

        @Override
        public void visit(BoolOperationExpression expression) {
            var leftExpr = exprStack.pop();
            var rightExpr = exprStack.pop();
            var operator = switch (expression.operator) {
                case AND -> "&";
                case OR -> "|";
                case XOR -> "^";
            };
            var expr = String.format("(%s %s %s)", leftExpr, operator, rightExpr);
            exprStack.push(expr);
        }

        @Override
        public void visit(NotExpression notExpression) {
            var innerExpr = exprStack.pop();
            var expr = String.format("!%s", innerExpr);
            exprStack.push(expr);
        }

        @Override
        public void visit(DigitalOutputStatement assignment) {
            var expr = exprStack.pop();
            var varName = String.format("DIGITAL_OUT_%s", assignment.digitalOutput);
            var statement = String.format("%s = %s;", varName, expr);
            generatedStatements.add(statement);
        }

        @Override
        public void visit(Program program) {
            var varDefines = """
                    static const uint8_t PORT_A1 = 11;
                    static const uint8_t PORT_A2 = 12;
                    static const uint8_t PORT_A3 = 13;
                    static const uint8_t PORT_D1 = 14;
                    static const uint8_t PORT_D2 = 15;
                    static const uint8_t PORT_D3 = 16;""";

            var setupFuncDef = """            
                    void setup() {
                        pinMode(PORT_A1, INPUT);
                        pinMode(PORT_A2, INPUT);
                        pinMode(PORT_A3, INPUT);
                        pinMode(PORT_D1, OUTPUT);
                        pinMode(PORT_D2, OUTPUT);
                        pinMode(PORT_D3, OUTPUT);
                    }""";

            var funcPrologue = """
                    double ANALOG_IN_A1 = analogRead(PORT_A1);
                    double ANALOG_IN_A2 = analogRead(PORT_A2);
                    double ANALOG_IN_A3 = analogRead(PORT_A3);
                    bool DIGITAL_OUT_D1 = false, DIGITAL_OUT_D2 = false, DIGITAL_OUT_D3 = false;""";
            var funcEpilogue = """
                    digitalWrite(PORT_D1, DIGITAL_OUT_D1 ? HIGH : LOW);
                    digitalWrite(PORT_D2, DIGITAL_OUT_D2 ? HIGH : LOW);
                    digitalWrite(PORT_D3, DIGITAL_OUT_D3 ? HIGH : LOW);""";
            var sleep = String.format("delay(%d); // FIXME: imprecise", 1000 / program.updateFrequency);
            var statements = String.join("\n", generatedStatements);
            var funcBody = String.format("%s\n%s\n%s\n%s", funcPrologue, statements, funcEpilogue, sleep);
            var loopFunDef = String.format("void loop() {\n%s}", indent(funcBody, 1));

            generatedCode = String.format("%s\n%s\n%s", varDefines, setupFuncDef, loopFunDef);
        }

        public String getResult() {
            return generatedCode;
        }

        private static String indent(String str, int level) {
            return str.indent(level * 4);
        }
    }

    public static void generateCode(DataStructure dataStructure){


    }

    public static void main(String[] args) {

        var program = new Program(
                10 /* Hz */,
                new DigitalOutputStatement(DigitalOutput.D3,
                        new BoolOperationExpression(
                                BoolOperator.XOR,
                                new DoubleComparisonExpression(
                                        ComparisonOperator.GREATER_THAN_EQUALS,
                                        new AnalogInputExpression(AnalogInput.A1),
                                        new DoubleLiteral(1.5)
                                ),
                                new NotExpression(
                                        new DoubleComparisonExpression(
                                                ComparisonOperator.SMALLER_THAN,
                                                new DoubleLiteral(2),
                                                new AnalogInputExpression(AnalogInput.A2)
                                        )
                                )
                        )
                )
        );

        var visitor = new CodeGeneratorVisitor();
        program.accept(visitor);

        System.out.println(visitor.getResult());
    }
}
