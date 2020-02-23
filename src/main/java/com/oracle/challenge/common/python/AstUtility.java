package com.oracle.challenge.common.python;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

import com.oracle.challenge.common.Enums;

public class AstUtility {

    public String getStatements(RuleContext ctx) {
    	List<String> statements = new ArrayList<String>();
        explore(ctx, statements);
        return String.join("", statements);
    }

    private void explore(RuleContext ctx, List<String> statements) {
        boolean toBeIgnored = (ctx.getChildCount() == 1) && (ctx.getChild(0) instanceof ParserRuleContext);
        if (!toBeIgnored) {
            String ruleName = Python3Parser.ruleNames[ctx.getRuleIndex()];
            ParserRuleContext context = (ParserRuleContext)ctx;
            int a = context.start.getStartIndex();
            int b = context.stop.getStopIndex();
            Interval interval = new Interval(a,b);
            if(Enums.Statement.fromValue(ruleName) != null){
            	statements.add(context.start.getInputStream().getText(interval));
            	if(Enums.Statement.fromValue(ruleName).equals(Enums.Statement.FUNCTION_DEF) || 
            			Enums.Statement.fromValue(ruleName).equals(Enums.Statement.CLASS_DEF)) {
            		return;
            	}
            }
        }
        for (int i=0;i<ctx.getChildCount();i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                explore((RuleContext)element, statements);
            }
        }
        
    }

}