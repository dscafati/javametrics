package com.javametrics;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

import java.io.FileInputStream;

public class Main {

    public static void main(String[] args) {
        try {
            // Get operation type
            String opType = args[0];

            // Get file path and line where the method begins
            String filename = args[1];
            Integer startingLine = Integer.valueOf(args[2]);

            // Parse the file
            FileInputStream in = new FileInputStream(filename);
            CompilationUnit cu;
            try {
                cu = JavaParser.parse(in);
            } finally {
                in.close();
            }


            switch (opType){
                case "localvars":
                    LocalVarsArgs vArgs = new LocalVarsArgs();
                    vArgs.boundaries = new LocalVarsMethodVisitor().visit(cu, startingLine);
                    new LocalVarsVarVisitor().visit(cu, vArgs);
                    System.out.println( vArgs.getCount() );
                    break;
                case "maxnesting":
                    // Get and return the max nesting metric
                    Integer deep = new MaxNestingVisitor().visit(cu, startingLine);
                    System.out.println( (deep==null) ? -1 : deep );
                    break;
                default:
                    throw new Exception("Unknown operation");
            }

        } catch (Exception e) {
            // For any exception, return -1 instead of printing errors
            System.out.println(-1);
            System.exit(0);
        }
    }


}
