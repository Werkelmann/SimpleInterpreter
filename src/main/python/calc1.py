
from collections import OrderedDict

ASSIGN = 'ASSIGN'
BEGIN = 'BEGIN'
COLON = 'COLON'
COMMA = 'COMMA'
DIV = 'DIV'
DOT = 'DOT'
END = 'END'
EOF = 'EOF'
FLOAT_DIV = 'FLOAT_DIV'
ID = 'ID'
INTEGER = 'INTEGER'
INTEGER_DIV = 'INTEGER_DIV'
INTEGER_CONST = 'INTEGER_CONST'
LEFT_PARENTHESIS = 'LEFT_PARENTHESIS'
OPERATOR = 'OPERATOR'
PROGRAM = 'PROGRAM'
REAL = 'REAL'
REAL_CONST = 'REAL_CONST'
RIGHT_PARENTHESIS = 'RIGHT_PARENTHESIS'
SEMICOLON = 'SEMICOLON'
VAR = 'VAR'

EXCEPTION_SCANNING = 'Error while scanning at {position}. Found: {found}'
EXCEPTION_PARSE = 'Error while parsing at {position}. Found: {found} Expected: {expected}'
EXCEPTION_UNKNOWN_OPERATOR = 'Unknown operator at {}. Found: {}'

###############################################################################
#                                                                             #
#  LEXER                                                                      #
#                                                                             #
###############################################################################


class Token(object):
    def __init__(self, token_type, value):
        self.type = token_type
        self.value = value

    def __str__(self):
        return 'Token({type}: {value})'.format(
            type=self.type,
            value=repr(self.value)
        )

    def __repr__(self):
        return self.__str__()


RESERVED_KEYWORDS = {
    DIV: Token(INTEGER_DIV, DIV),
    BEGIN: Token(BEGIN, BEGIN),
    END: Token(END, END),
    INTEGER: Token(INTEGER, INTEGER),
    REAL: Token(REAL, REAL),
    PROGRAM: Token(PROGRAM, PROGRAM),
    VAR: Token(VAR, VAR),
}


class Lexer(object):
    def __init__(self, text):
        self.text = text
        self.position = 0
        self.current_token = None
        self.current_char = self.text[self.position]

    def error(self, message):
        raise Exception(message.format(
            position=self.position,
            found=self.current_token.value))

    def advance(self):
        self.position += 1
        if self.position > len(self.text) - 1:
            self.current_char = None
        else:
            self.current_char = self.text[self.position]

    def peek(self):
        peek_position = self.position + 1
        if peek_position > len(self.text) - 1:
            return None
        return self.text[peek_position]

    def skip_whitespace(self):
        while self.current_char is not None and self.current_char.isspace():
            self.advance()

    def skip_comment(self):
        while self.current_char != '}':
            self.advance()
        self.advance()

    def number(self):
        result = ''
        while self.current_char is not None and self.current_char.isdigit():
            result += self.current_char
            self.advance()

        if self.current_char == '.':
            result += self.current_char
            self.advance()

            while self.current_char is not None and self.current_char.isdigit():
                result += self.current_char
                self.advance()

            return Token(REAL_CONST, float(result))

        return Token(INTEGER_CONST, int(result))

    def _id(self):
        result = ''
        while self.current_char is not None and self.current_char.isalnum():
            result += self.current_char
            self.advance()

        token = RESERVED_KEYWORDS.get(result.upper(), Token(ID, result))
        return token

    def get_next_token(self):
        while self.current_char is not None:

            if self.current_char == '{':
                self.advance()
                self.skip_comment()
                continue

            if self.current_char.isspace():
                self.skip_whitespace()
                continue

            if self.current_char.isalpha():
                return self._id()

            if self.current_char == ':' and self.peek() == '=':
                self.advance()
                self.advance()
                return Token(ASSIGN, ':=')

            if self.current_char == ':':
                token = Token(COLON, self.current_char)
                self.advance()
                return token

            if self.current_char == ';':
                token = Token(SEMICOLON, self.current_char)
                self.advance()
                return token

            if self.current_char == '.':
                token = Token(DOT, self.current_char)
                self.advance()
                return token

            if self.current_char == ',':
                token = Token(COMMA, self.current_char)
                self.advance()
                return token

            if self.current_char.isdigit():
                return self.number()

            if self.current_char in ('+', '-', '*'):
                token = Token(OPERATOR, self.current_char)
                self.advance()
                return token

            if self.current_char == '/':
                token = Token(FLOAT_DIV, self.current_char)
                self.advance()
                return token

            if self.current_char == '(':
                token = Token(LEFT_PARENTHESIS, self.current_char)
                self.advance()
                return token

            if self.current_char == ')':
                token = Token(RIGHT_PARENTHESIS, self.current_char)
                self.advance()
                return token

            self.error(EXCEPTION_SCANNING)

        return Token(EOF, None)

###############################################################################
#                                                                             #
#  PARSER                                                                     #
#                                                                             #
###############################################################################


class AST(object):
    def __str__(self):
        return '{type}: {token}'.format(
            type=type(self).__name__,
            token=self.token.__repr__()
        )

    def __repr__(self):
        return self.__str__()


class Program(AST):
    def __init__(self, name, block):
        self.name = name
        self.block = block


class Block(AST):
    def __init__(self, declarations, compound_statement):
        self.declarations = declarations
        self.compound_statement = compound_statement


class VarDecl(AST):
    def __init__(self, var_node, type_node):
        self.var_node = var_node
        self.type_node = type_node


class Type(AST):
    def __init__(self, token):
        self.token = token
        self.value = token.value


class Compound(AST):
    def __init__(self):
        self.children = []


class Assign(AST):
    def __init__(self, left, op, right):
        self.left = left
        self.token = self.op = op
        self.right = right


class Var(AST):
    def __init__(self, token):
        self.token = token
        self.value = token.value


class NoOp(AST):
    pass


class BinOp(AST):
    def __init__(self, left, op, right):
        self.left = left
        self.token = self.op = op
        self.right = right


class UnaryOp(AST):
    def __init__(self, op, expr):
        self.token = self.op = op
        self.expr = expr


class Num(AST):
    def __init__(self, token):
        self.token = token
        self.value = token.value


class Parser(object):
    def __init__(self, lexer):
        self.lexer = lexer
        self.current_token = self.lexer.get_next_token()

    def error(self, message, expected):
        raise Exception(message.format(
            position=self.lexer.position,
            found=self.current_token.type,
            expected=expected
        ))

    def eat(self, *token_type):
        if self.current_token.type in token_type:
            self.current_token = self.lexer.get_next_token()
        else:
            self.error(EXCEPTION_PARSE, token_type)

    def factor(self):
        token = self.current_token
        if token.type == OPERATOR and token.value in ['+', '-']:
            self.eat(OPERATOR)
            return UnaryOp(token, self.factor())

        if token.type in (INTEGER_CONST, REAL_CONST):
            self.eat(INTEGER_CONST, REAL_CONST)
            return Num(token)

        if token.type == LEFT_PARENTHESIS:
            self.eat(LEFT_PARENTHESIS)
            node = self.expr()
            self.eat(RIGHT_PARENTHESIS)
            return node

        return self.variable()

    def term(self):
        node = self.factor()
        while self.current_token.value == '*' or self.current_token.type in (INTEGER_DIV, FLOAT_DIV):
            op = self.current_token
            self.eat(OPERATOR, INTEGER_DIV, FLOAT_DIV)

            node = BinOp(left=node, op=op, right=self.factor())

        return node

    def expr(self):
        node = self.term()
        while self.current_token.value in ('+', '-'):
            op = self.current_token
            self.eat(OPERATOR)

            node = BinOp(left=node, op=op, right=self.term())

        return node

    def empty(self):
        return NoOp()

    def variable(self):
        node = Var(self.current_token)
        self.eat(ID)
        return node

    def assign_statement(self):
        left = self.variable()
        token = self.current_token
        self.eat(ASSIGN)
        right = self.expr()
        return Assign(left, token, right)

    def statement(self):
        if self.current_token.type == BEGIN:
            return self.compound_statement()
        if self.current_token.type == ID:
            return self.assign_statement()
        return self.empty()

    def statement_list(self):
        node = self.statement()
        results = [node]

        while self.current_token.type == SEMICOLON:
            self.eat(SEMICOLON)
            results.append(self.statement())

        if self.current_token.type == ID:
            self.error(EXCEPTION_PARSE, 'Every other thing')

        return results

    def compound_statement(self):
        self.eat(BEGIN)
        nodes = self.statement_list()
        self.eat(END)

        root = Compound()
        for node in nodes:
            root.children.append(node)

        return root

    def program(self):
        self.eat(PROGRAM)
        var_node = self.variable()
        prog_name = var_node.value
        self.eat(SEMICOLON)
        block_node = self.block()
        program_node = Program(prog_name, block_node)
        self.eat(DOT)
        return program_node

    def type_spec(self):
        token = self.current_token
        self.eat(INTEGER, REAL)
        return Type(token)

    def variable_declaration(self):
        var_nodes = [Var(self.current_token)]
        self.eat(ID)

        while self.current_token.type == COMMA:
            self.eat(COMMA)
            var_nodes.append(Var(self.current_token))
            self.eat(ID)

        self.eat(COLON)

        type_node = self.type_spec()
        var_declarations = [
            VarDecl(var_node, type_node)
            for var_node in var_nodes
        ]
        return var_declarations

    def declarations(self):
        declarations = []
        if self.current_token.type == VAR:
            self.eat(VAR)
            while self.current_token.type == ID:
                var_decl = self.variable_declaration()
                declarations.extend(var_decl)
                self.eat(SEMICOLON)

        return declarations

    def block(self):
        declarations_node = self.declarations()
        compound_statement_node = self.compound_statement()
        return Block(declarations_node, compound_statement_node)

    def parse(self):
        node = self.program()
        if self.current_token.type != EOF:
            self.error(EXCEPTION_PARSE, EOF)
        return node

###############################################################################
#                                                                             #
#  INTERPRETER                                                                #
#                                                                             #
###############################################################################


class NodeVisitor(object):
    def visit(self, node):
        method_name = 'visit_' + type(node).__name__
        visitor = getattr(self, method_name, self.generic_visit)
        return visitor(node)

    def generic_visit(self, node):
        raise Exception('No visit_{} method'.format(type(node).__name__))


class Interpreter(NodeVisitor):
    def __init__(self, parser):
        self.parser = parser
        self.GLOBAL_SCOPE = {}
        self.symbol_table = None

    def visit_BinOp(self, node):
        if node.op.value == '+':
            return self.visit(node.left) + self.visit(node.right)
        if node.op.value == '-':
            return self.visit(node.left) - self.visit(node.right)
        if node.op.value == '*':
            return self.visit(node.left) * self.visit(node.right)
        if node.op.type == INTEGER_DIV:
            return self.visit(node.left) // self.visit(node.right)
        if node.op.type == FLOAT_DIV:
            return self.visit(node.left) / self.visit(node.right)

    def visit_UnaryOp(self, node):
        if node.op.value == '+':
            return + self.visit(node.expr)
        if node.op.value == '-':
            return - self.visit(node.expr)

    def visit_Num(self, node):
        return node.value

    def visit_Compound(self, node):
        for child in node.children:
            self.visit(child)

    def visit_NoOp(self, node):
        pass

    def visit_Assign(self, node):
        var_name = node.left.value
        self.GLOBAL_SCOPE[var_name] = self.visit(node.right)

    def visit_Var(self, node):
        var_name = node.value
        val = self.GLOBAL_SCOPE.get(var_name)
        if val is None:
            raise NameError(repr(var_name))
        return val

    def visit_Program(self, node):
        self.visit(node.block)

    def visit_Block(self, node):
        for declaration in node.declarations:
            self.visit(declaration)
        self.visit(node.compound_statement)

    def visit_VarDecl(self, node):
        pass

    def visit_Type(self, node):
        pass

    def interpret(self):
        tree = self.parser.parse()
        sym_tab_builder = SymbolTableBuilder()
        sym_tab_builder.visit(tree)
        self.symbol_table = sym_tab_builder.symbol_table
        return self.visit(tree)

    def expr(self):
        tree = self.parser.expr()
        return self.visit(tree)


class RPNTranslator(NodeVisitor):
    def __init__(self, parser):
        self.parser = parser

    def visit_BinOp(self, node):
        return '{left} {right} {op}'.format(
            left=self.visit(node.left),
            right=self.visit(node.right),
            op=node.op.value
        )

    def visit_Num(self, node):
        return node.value

    def interpret(self):
        tree = self.parser.expr()
        return self.visit(tree)


class LispTranslator(NodeVisitor):
    def __init__(self, parser):
        self.parser = parser

    def visit_BinOp(self, node):
        return '({op} {left} {right})'.format(
            op=node.op.value,
            left=self.visit(node.left),
            right=self.visit(node.right)
        )

    def visit_Num(self, node):
        return node.value

    def interpret(self):
        tree = self.parser.expr()
        return self.visit(tree)

###############################################################################
#                                                                             #
#  Symbols                                                                    #
#                                                                             #
###############################################################################


class Symbol(object):
    def __init__(self, name, symbol_type=None):
        self.name = name
        self.type = symbol_type


class BuiltinTypeSymbol(Symbol):
    def __init__(self, name):
        super(BuiltinTypeSymbol, self).__init__(name)

    def __str__(self):
        return self.name

    __repr__ = __str__


class VarSymbol(Symbol):
    def __init__(self, name, symbol_type):
        super(VarSymbol, self).__init__(name, symbol_type)

    def __str__(self):
        return '<{name}:{type}>'.format(
            name=self.name,
            type=self.type,
        )

    __repr__ = __str__


class SymbolTable(object):
    def __init__(self):
        self.symbols = OrderedDict()
        self.init_builtins()

    def init_builtins(self):
        self.define(BuiltinTypeSymbol(INTEGER))
        self.define(BuiltinTypeSymbol(REAL))

    def __str__(self):
        return 'Symbols: {symbols}'.format(
            symbols=[value for value in self.symbols.values()]
        )

    __repr__ = __str__

    def define(self, symbol):
        print('Define: %s' % symbol)
        self.symbols[symbol.name] = symbol

    def lookup(self, name):
        print('Lookup: %s' % name)
        return self.symbols.get(name)


class SymbolTableBuilder(NodeVisitor):
    def __init__(self):
        self.symbol_table = SymbolTable()

    def visit_Block(self, node):
        for declaration in node.declarations:
            self.visit(declaration)
        self.visit(node.compound_statement)

    def visit_Program(self, node):
        self.visit(node.block)

    def visit_BinOp(self, node):
        self.visit(node.left)
        self.visit(node.right)

    def visit_Num(self, node):
        pass

    def visit_UnaryOp(self, node):
        self.visit(node.expr)

    def visit_Compound(self, node):
        for child in node.children:
            self.visit(child)

    def visit_NoOp(self, node):
        pass

    def visit_VarDecl(self, node):
        type_name = node.type_node.value
        type_symbol = self.symbol_table.lookup(type_name)
        var_name = node.var_node.value
        var_symbol = VarSymbol(var_name, type_symbol)
        self.symbol_table.define(var_symbol)

    def visit_Assign(self, node):
        var_name = node.left.value
        var_symbol = self.symbol_table.lookup(var_name)
        if var_symbol is None:
            raise NameError(repr(var_name))

        self.visit(node.right)

    def visit_Var(self, node):
        var_name = node.value
        var_symbol = self.symbol_table.lookup(var_name)
        if var_symbol is None:
            raise NameError(repr(var_name))



def main():
    while True:
        try:
            text = input('calc> ')
        except EOFError:
            break
        if not text:
            continue

        lexer = Lexer(text)
        parser = Parser(lexer)
        interpreter = Interpreter(parser)
        result = interpreter.interpret()
        print(result)


if __name__ == '__main__':
    main()
