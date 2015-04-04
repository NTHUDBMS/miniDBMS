
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;

/**
 * 
 * 
 */
public class DBMS {
    public static void main(String[] args) throws Exception {
        // create a CharStream that reads from standard input
    	String inputFile = null;
    	System.out.println(args[0]);
    	
		if ( args.length>0 ){
			inputFile = args[0];
			
			InputStream is = System.in;
			if ( inputFile!=null ) {
				is = new FileInputStream(inputFile);
			}
			
			ANTLRInputStream input = new ANTLRInputStream(is);
			
	        // create a lexer that feeds off of input CharStream
	        SqlLexer lexer = new SqlLexer(input);
	
	        // create a buffer of tokens pulled from the lexer
	        CommonTokenStream tokens = new CommonTokenStream(lexer);
	
	        // create a parser that feeds off the tokens buffer
	        SqlParser parser = new SqlParser(tokens);
	
	        ParseTree tree = parser.start(); // begin parsing at start rule
	        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
		}
        
        
    }
}