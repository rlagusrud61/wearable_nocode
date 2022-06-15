package main.codegen;


import main.*;
import processing.core.PApplet;

import javax.swing.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CodeGenerator {

    enum AnalogInput {
        A0,
        A1,
        A2,
    }

    public enum DigitalOutput {
        D9,
        D10,
        D11,
    }
    public enum SensorType{
        TOUCH, MICROPHONE, GSR, BENDING, DISTANCE, HR, ACCELEROMETER

    }

    public enum ActuatorType{
        BUZZER, VIBRATING_MOTOR, NEOPIXEL,SERVO
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

    public enum ComparisonOperator {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_EQUALS,
        SMALLER_THAN,
        SMALLER_THAN_EQUALS,
    }

    public record DoubleComparisonExpression(ComparisonOperator operator, DoubleExpression leftExpr,
                                             DoubleExpression rightExpr, DoubleExpression outValue) implements BoolExpression {
        @Override
        public void accept(ProgramVisitor visitor) {
            leftExpr.accept(visitor);
            outValue.accept(visitor);
            rightExpr.accept(visitor);
            visitor.visit(this);
        }
    }

    public enum BoolOperator {
        AND,
        OR,
        XOR,
    }

    public record BoolOperationExpression(BoolOperator operator, BoolExpression leftExpr,
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

    public interface Statement extends ProgramNode {

    }

    public record DigitalOutputStatement(DigitalOutput digitalOutput, ActuatorType type, BoolExpression expr) implements Statement {
        @Override
        public void accept(ProgramVisitor visitor) {
            expr.accept(visitor);
            visitor.visit(this);
        }
    }

//    public record ActuatorTypeExpression() implements  Statement{
//        @Override
//        public void accept(ProgramVisitor visitor){
//        }
//    }

    public static class Program implements ProgramNode {
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

//        void visit(ActuatorTypeExpression type);

        void visit(Program program);
    }

    public static class CodeGeneratorVisitor implements ProgramVisitor {
        private final Deque<String> exprStack = new ArrayDeque<>();
        private final List<String> generatedImports = new ArrayList<>();
        private final List<String> generatedStatements = new ArrayList<>();
        private String generatedCode;

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
            var rightExpr = exprStack.pop();
            var val = exprStack.pop();
            var leftExpr = exprStack.pop();
            var operator = switch (expression.operator) {
                case EQUALS -> "==";
                case NOT_EQUALS -> "!=";
                case GREATER_THAN -> ">";
                case GREATER_THAN_EQUALS -> ">=";
                case SMALLER_THAN -> "<";
                case SMALLER_THAN_EQUALS -> "<=";
            };
            var expr = String.format("(%s %s %s) ? %s : 0", leftExpr, operator, rightExpr, val);
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
            var type = assignment.type;
            var varName = String.format("DIGITAL_OUT_%s", assignment.digitalOutput);
            var statement = String.format("%s = %s;", varName, expr);
            generatedStatements.add(statement);
            // if assignment.digitalOutput is <actuator>
            // generate different needed code
            generatedImports.add(generateImportStatement(assignment.type));
        }

        @Override
        public void visit(Program program) {
            var varDefines = """
                    static const uint8_t PORT_A0 = A0;
                    static const uint8_t PORT_A1 = A1;
                    static const uint8_t PORT_A2 = A2;
                    static const uint8_t PORT_D9 = 9;
                    static const uint8_t PORT_D10 = 10;
                    static const uint8_t PORT_D11 = 11;""";

            var setupFuncDef = """            
                    void setup() {
                        Serial.begin(9600);
                        pinMode(PORT_A0, INPUT);
                        pinMode(PORT_A1, INPUT);
                        pinMode(PORT_A2, INPUT);
                        pinMode(PORT_D9, OUTPUT);
                        pinMode(PORT_D10, OUTPUT);
                        pinMode(PORT_D11, OUTPUT);
                    }
                    
                    """;

            var funcPrologue = """
                    double ANALOG_IN_A0 = analogRead(PORT_A0);
                    double ANALOG_IN_A1 = analogRead(PORT_A1);
                    double ANALOG_IN_A2 = analogRead(PORT_A2);
                    bool DIGITAL_OUT_D1 = false, DIGITAL_OUT_D2 = false, DIGITAL_OUT_D3 = false;
                    
                    """;
            var funcEpilogue = """
                    digitalWrite(PORT_D3, DIGITAL_OUT_D3);""";
            var sleep = String.format("delay(%d); // FIXME: imprecise", 1000 / program.updateFrequency);
            var statements = String.join("\n", generatedStatements);
            var imports = String.join("\n", generatedImports);


            var funcBody = String.format("%s\n%s\n%s\n%s", funcPrologue, statements, funcEpilogue, sleep);
            var loopFunDef = String.format("void loop() {\n%s}", indent(funcBody, 1));

            generatedCode = String.format("%s\n%s\n%s\n%s", imports, varDefines, setupFuncDef, loopFunDef);
        }

        public String getResult() {
            return generatedCode;
        }

        private static String indent(String str, int level) {
            return str.indent(level * 4);
        }
        private static String generateImportStatement(ActuatorType type){
            return switch (type) {
                case BUZZER -> "" ;
                case VIBRATING_MOTOR -> "";
                case NEOPIXEL -> String.format("%s\n\n%s\n", "#include <Adafruit_Neopixel.h>", "Adafruit_Neopixel pixels = Adafruit_Neopixel(16,11,NEO_GRB, NEO_KHZ800);");
                case SERVO -> String.format("%s\n\n%s\n", "#include <Servo.h>", "Servo servo");
            };
        }
    }

    public static DoubleComparisonExpression generateDoubleComparisonExpression(String operator, String analogInput, int inputValue, int outputValue) {

        DoubleLiteral iVal = new DoubleLiteral(inputValue);
        DoubleLiteral oVal = new DoubleLiteral(outputValue);
        AnalogInputExpression aiex = new AnalogInputExpression(AnalogInput.valueOf(analogInput));
        ComparisonOperator op = null;
        switch (operator) {
            case ">":
                op = ComparisonOperator.GREATER_THAN;
                break;
            case "<":
                op = ComparisonOperator.SMALLER_THAN;
                break;
            case "==":
                op = ComparisonOperator.EQUALS;
                break;

        }

        return new DoubleComparisonExpression(op, aiex, iVal, oVal);
    }

    public static void main(String[] args) {
        var program = new Program(
                10 /* Hz */,
                new DigitalOutputStatement(DigitalOutput.D9,
                        ActuatorType.BUZZER,
                        new BoolOperationExpression(
                                BoolOperator.XOR,
                                new DoubleComparisonExpression(
                                        ComparisonOperator.GREATER_THAN_EQUALS,
                                        new AnalogInputExpression(AnalogInput.A1),
                                        new DoubleLiteral(1.5),
                                        new DoubleLiteral(40)
                                ),
                                new NotExpression(
                                        new DoubleComparisonExpression(
                                                ComparisonOperator.SMALLER_THAN,
                                                new DoubleLiteral(2),
                                                new AnalogInputExpression(AnalogInput.A2),
                                                new DoubleLiteral(40)
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
