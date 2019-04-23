/* Scanner for C-Minus JFlex specification to work with BYACCJ. Spec taken from
 * Louden's "Compiler Construction..." Appendix A. 
 *
 * Regular expression for COMMENT below is not quite right. It expects a space
 * before and after the opening and closing comment symbols. See the JFlex manual
 * and other online resoures for a better regular expression for handling nested
 * C-style comments.
 * 
 * Authors: Vijay Gehlot & Tom Way

 To generate ParserTokens.java interface containing token constants:
   yacc -d -J cminus.y

 */
%%

%unicode
%line
%column
%byaccj

%{
/* store a reference to the parser object */
private Parser yyparser;

/* constructor taking an additional parser */
public Yylex (java.io.Reader r, Parser yyparser)
{
	this (r);	
	this.yyparser = yyparser;
}

/*	return the current line number. We need this
  	because yyline is made private and we don't have
	a mechanism like extern in C.
*/
public int getLine()
{
	// add 1 because lineno starts at 0
	return yyline + 1;
}

public int getCol() 
{
	return yycolumn;
}
	
public int value;

%}

letter = [A-Za-z]
digit  = [0-9]
eol    = \r|\n|\r\n
ws     = {eol} | [ \t\f]
//comment = "/*" [^*] ~"*/" | "/*" "*"+ "/"

comment = "/*"((("*"[^/])?)|[^*])*"*/"

%%
"else"			{return Parser.ELSE;}
"if"			{return Parser.IF;}
"int" 			{
					yyparser.yylval = new ParserVal(Parser.INT);
					return Parser.INT;
				}
"return"		{return Parser.RETURN;}
"void"			{
					yyparser.yylval = new ParserVal(Parser.VOID);
					return Parser.VOID;
				}
"while"			{return Parser.WHILE;}
"print"			{return Parser.PRINT;}
"input"			{return Parser.INPUT;}

{letter}({letter}|{digit})*	{
								yyparser.yylval = new ParserVal(yytext());
								return Parser.ID;
							}
{digit}{digit}*				{
								value = Integer.parseInt(yytext());
								yyparser.yylval = new ParserVal(value);
								return Parser.NUM;
							}
"+"				{yyparser.yylval = new ParserVal(Parser.PLUS); return Parser.PLUS;}
"-"				{return Parser.MINUS;}
"*"				{return Parser.MULT;}
"/"				{return Parser.DIVIDE;}
"="				{return Parser.ASSIGN;}

"("				{return Parser.LPAREN;}
")"				{return Parser.RPAREN;}
"{"				{return Parser.LCBRACKET;}
"}"				{return Parser.RCBRACKET;}
"["				{return Parser.LBRACKET;}
"]"				{return Parser.RBRACKET;}

","				{return Parser.COMMA;}
";"				{return Parser.SEMI;}

"=="			{return Parser.EQ;}
"!="			{return Parser.NOTEQ;}
"<="			{return Parser.LTE;}
">="			{return Parser.GTE;}
"<"				{return Parser.LT;}
">"				{return Parser.GT;}

{ws}			{/* ignore */}
{comment}       {/* ignore */}

.               {return Parser.ERROR;}
