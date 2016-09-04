package com.javametrics;

import com.github.javaparser.Position;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;


class LocalVarsArgs{
    public Position[] boundaries;
    private Integer count;
    public LocalVarsArgs(){
        this.count = 0;
    }
    public void count(){
        this.count++;
    }
    public Integer getCount(){
        return this.count;
    }
}


/**
 * Visitor of methods and constructors
 */
class LocalVarsMethodVisitor extends GenericVisitorAdapter<Position[], Integer> {

    @Override
    public Position[] visit(ConstructorDeclaration n, Integer startingLine) {
        if( n.getBegin().line == startingLine ) {
            return new Position[]{n.getBegin(), n.getEnd()};
        }else{
            return super.visit(n, startingLine);
        }
    }

    @Override
    public Position[] visit(MethodDeclaration n, Integer startingLine) {
        if( n.getBegin().line == startingLine ) {
            return new Position[]{n.getBegin(), n.getEnd()};
        }else{
            return super.visit(n, startingLine);
        }
    }
}



/**
 * Visitor of variable declaration
 */
class LocalVarsVarVisitor extends GenericVisitorAdapter<Integer, LocalVarsArgs> {
    private Boolean belongsTo(Position[] element, Position parent[]){
        return (
                ( element[0].line > parent[0].line || (element[0].line == parent[0].line && element[0].column > parent[0].column))
                &&
                ( element[1].line < parent[1].line || (element[1].line == parent[1].line && element[1].column < parent[1].column))
        );
    }

    @Override
    public Integer visit(VariableDeclarator v, LocalVarsArgs args) {
        if( this.belongsTo( new Position[]{v.getBegin(), v.getEnd()}, args.boundaries ) )
        {
            args.count();
        }
        return super.visit(v, args);
    }
}