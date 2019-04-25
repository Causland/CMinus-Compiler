/* 
 * A BYACCJ specification for the Cminus language.
 * Author: Vijay Gehlot
 */
%{
import java.io.*;
import java.util.*;
%}
  
%token ID NUM
%token PLUS MINUS MULT DIVIDE LT GT LTE GTE EQ NOTEQ
%token SEMI COMMA ASSIGN LPAREN RPAREN LBRACKET RBRACKET LCBRACKET RCBRACKET LCOMMENT RCOMMENT
%token PRINT INPUT WHILE VOID RETURN INT IF ELSE
%token ERROR

%%

program:		{ 
					symtab.enterScope();
					GenCode.genPrologue();
                } 
                declaration_list 
                {
                	if (usesRead) GenCode.genReadMethod();
                	GenCode.genClassConstructor();
                	GenCode.genEpilogue(symtab);
                	symtab.exitScope();
                	if (!seenMain) semerror("No main in file"); 
				}
			;

declaration_list:	declaration_list declaration
				|	declaration
			;

declaration:		var_declaration
				|	fun_declaration
			;

var_declaration:	type_specifier ID SEMI {

						int vartype = $1.ival;
						String name = $2.sval;
						int scope = symtab.getScope();

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of variable " + name + " in the current scope");
						}
						else if(vartype == VOID){
							semerror("Variable " + name + " cannot be of type void");
						}
						else
						{
						//Symbol table add
						SymTabRec rec = new VarRec(name, scope, vartype);
						symtab.insert(name, rec);

						if (scope == 0)
							GenCode.genStaticDecl(rec); 
						}
					}
				|	type_specifier ID LBRACKET NUM RBRACKET SEMI 
					{
						int vartype = $1.ival;
						String name = $2.sval;
						int scope = symtab.getScope();
						int arrayLength = $4.ival;

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of array " + name + " in the current scope");
						}
						else if(vartype == VOID){
							semerror("Array " + name + " cannot be of type void");
						}
						else
						{
						//Symbol table add
						SymTabRec rec = new ArrRec(name, scope, vartype, arrayLength);
						symtab.insert(name, rec); 
						}
					}
			;

type_specifier:		INT { $$ = $1; }
				|	VOID { $$ = $1; }
			;

fun_declaration: 	type_specifier ID  
					{
						

						int funtype = $1.ival;
						String name = $2.sval;
						int scope = symtab.getScope();

						//List<SymTabRec> params = $4.ival;

						// Create a symbol table record
						FunRec rec = new FunRec(name, scope, funtype, null);
						$$ = new ParserVal(rec);

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of function " + name + " in the current scope");
						}
						else if (!seenMain){
							SymTabRec.setJVMNum(0);
							// Insert record into symbol table
							symtab.insert(name,rec);

							if(name.equals("main")){
								seenMain = true;
								SymTabRec.setJVMNum(1);
							}
						}
						else{
							semerror("Function " + name + " cannot be declared after main");
						}
						symtab.enterScope();
					}
					LPAREN params RPAREN
					{
						symtab.enterScope();

						int funtype = $1.ival;
						String name = $2.sval;

						// Get params from $5
						List<SymTabRec> params = (List<SymTabRec>)$5.obj;
						FunRec rec = ((FunRec)($3.obj));
						rec.setParams(params);
						

						if(name.equals("main")){
							if(funtype != VOID){
								semerror("Return type of main function must be void");
							}
							if($5.obj != null){
								semerror("Parameters of main function must be void or empty");
							}
						}

						GenCode.genFunBegin(rec);

					}
					compound_stmt
					{
						firstTime = true;
						GenCode.genFunEnd();
					}
					//
			;

params:				param_list { $$ = $1; }
					| VOID { $$ = new ParserVal(null); }
					| { $$ = new ParserVal(null); }
			;

param_list:			param_list COMMA param
					{
						List<SymTabRec> params = (List<SymTabRec>)$1.obj;
						params.add((SymTabRec)$3.obj);
						$$ = new ParserVal(params);

					}
					| param
					{
						List<SymTabRec> params =  new ArrayList<SymTabRec>();
						params.add((SymTabRec)$1.obj);
						$$ = new ParserVal(params);
					}
			;

param:				type_specifier ID
					{
						int vartype = $1.ival;
						String name = $2.sval;
						int scope = symtab.getScope();
						VarRec rec = new VarRec(name,scope,vartype);
						$$ = new ParserVal(rec);
						if (symtab.lookup(name))
						{
							semerror("Redeclaration of param "+ name + " in the current scope");
						}
						else
						{
							symtab.insert(name,rec);
						}
					}
					| type_specifier ID LBRACKET RBRACKET
					{
						int vartype = $1.ival;
						String name = $2.sval;
						int scope = symtab.getScope();
						SymTabRec rec = new ArrRec(name,scope,vartype,-1);
						$$ = new ParserVal(rec);
						if (symtab.lookup(name))
						{
							semerror("Redeclaration of param array "+ name + " in the current scope");
						}
						else
						{
							symtab.insert(name,rec);
						}
					}
			;

compound_stmt:		{
						if(firstTime){
							firstTime = false;
						}
						else{
							symtab.enterScope();
						}
					}
					LCBRACKET local_declarations statement_list RCBRACKET
					{
						symtab.exitScope();
					}

			;

local_declarations:	local_declarations var_declaration
					| 
			;

statement_list:		statement_list statement
					| 	
			;

statement:			assign_stmt
					| compound_stmt
					| selection_stmt
					| iteration_stmt
					| return_stmt
					| print_stmt
					| input_stmt
					| call_stmt
					| SEMI
			;

call_stmt:  		call SEMI
			;

assign_stmt:		ID ASSIGN expression SEMI
					{
						String name = $1.sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared variable" + name + "in the current scope");
						}
						else if(!rec.isVar())
						{
							semerror("Name " + name + " is not a variable in the current scope");
						}
						else if(rec.type != $3.ival){
							semerror("Type mismatch of variable " + name + ". Expected " + rec.type);
						}
						else
						{
							GenCode.genStore(rec);
						}

					}
					| ID LBRACKET expression RBRACKET ASSIGN expression SEMI
					{
						String name = $1.sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared array" + name + "in the current scope");
						}
						else if(!rec.isArr())
						{
							semerror("Name " + name + " is not an array in the current scope");
						}
					}
			;

selection_stmt:		IF LPAREN expression RPAREN statement ELSE statement
			;

iteration_stmt:		WHILE LPAREN expression RPAREN statement
			;

print_stmt:			PRINT LPAREN 
					{
						GenCode.genBeginPrint();
					}
					expression RPAREN SEMI
					{
						GenCode.genEndPrint();
					}
			;

input_stmt:			ID ASSIGN INPUT LPAREN RPAREN SEMI
					{
						String name = $1.sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared variable" + name + " in input statement in the current scope");
						}
						else if(!rec.isVar())
						{
							semerror("Name " + name + " is not a variable in the input statement in the current scope");
						}
						else
						{
							usesRead = true;
							GenCode.genRead(rec);
						}
					}
			;

return_stmt:		RETURN SEMI
					| RETURN expression SEMI
			;

expression:	  		additive_expression relop additive_expression
					| additive_expression
			;

relop:				LTE
					| LT
					| GT
					| GTE
					| EQ
					| NOTEQ
			;

additive_expression:	additive_expression addop term
						| term
			;

addop:				PLUS
					| MINUS
			;

term:				term mulop factor
					| factor
			;

mulop:				MULT
					| DIVIDE
			;

factor:				LPAREN expression RPAREN
					| ID
					{
						String name = $1.sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared factor variable " + name + "in the current scope");
						}
						else if(!rec.isVar() && !rec.isArr())
						{
							System.out.println(rec);
							semerror("Name " + name + " is not a factor variable in the current scope");
						}
						else
						{
							if (rec.isVar())
								GenCode.genLoadVar(rec);
							else
								GenCode.genLoadArrAddr(rec);
						}
					}
					| ID LBRACKET expression RBRACKET
					{
						String name = $1.sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared factor array" + name + "in the current scope");
						}
						else if(!rec.isArr())
						{
							semerror("Name " + name + " is not a factor array in the current scope");
						}

					}
					| call
					| NUM
					{
						int num = $1.ival;
						GenCode.genLoadConst(num);
					}
			;

call:				ID LPAREN args RPAREN
					{
						String name = $1.sval;
						SymTabRec rec = symtab.get(name);
						if (rec == null)
						{
							semerror("Undeclared function" + name + "in the current scope");
						}
						else if(!rec.isFun())
						{
							semerror("Name " + name + " is not a function in the current scope");
						}
					}
			;

args:				arg_list
					| 
			;

arg_list:			arg_list COMMA expression
					| expression
			;

%%

/* reference to the lexer object */
private static Yylex lexer;

/* The symbol table */
public final SymTab<SymTabRec> symtab = new SymTab<SymTabRec>();

/* To check if main has been encountered and is the last declaration */
private boolean seenMain = false;

/* To take care of nuance associated with params and decls in compound stsmt */
private boolean firstTime = false;

/* To gen boilerplate code for read only if input was encountered  */
private boolean usesRead = false;

/* Interface to the lexer */
private int yylex()
{
    int retVal = -1;
    try
	{
		retVal = lexer.yylex();
    }
	catch (IOException e)
	{
		System.err.println("IO Error:" + e);
    }
    return retVal;
}
	
/* error reporting */
public void yyerror (String error)
{
    System.err.println("Parse Error : " + error + " at line " + 
		lexer.getLine() + " column " + 
		lexer.getCol() + ". Got: " + lexer.yytext());
}

/* For semantic errors */
public void semerror (String error)
{
	if (ParseMain.SYMBOL_TABLE_OUTPUT){
    	System.err.println("Semantic Error : " + error + " at line " + 
			lexer.getLine() + " column " + 
			lexer.getCol());
		}
}

/* constructor taking in File Input */
public Parser (Reader r)
{
	lexer = new Yylex(r, this);
}

/* This is how to invoke the parser

public static void main (String [] args) throws IOException
{
	Parser yyparser = new Parser(new FileReader(args[0]));
	yyparser.yyparse();
}

*/

