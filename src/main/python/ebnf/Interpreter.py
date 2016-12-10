from Parser import ID_TYPE_RULE_NAME, ID_TYPE_NON_TERMINAL


class NodeVisitor(object):
    def visit(self, node):
        method_name = 'visit_' + type(node).__name__.lower()
        visitor = getattr(self, method_name, self.generic_visit)
        return visitor(node)

    def generic_visit(self, node):
        raise Exception('No visit_{} method'.format(type(node).__name__))


class NonTerminalList(object):
    def __init__(self):
        self.left_sides = []
        self.right_sides = []

    def insert_left(self, symbol):
        if symbol not in self.left_sides:
            self.left_sides.append(symbol)
        if symbol in self.right_sides:
            self.right_sides.remove(symbol)

    def insert_right(self, symbol):
        if symbol not in self.left_sides:
            self.right_sides.append(symbol)

    def is_valid(self):
        if self.right_sides.__len__() != 0:
            raise Exception('Non-Terminal(s) {right} not defined'.format(
                right=self.right_sides.__repr__(),
            ))
        return True


class Validator(NodeVisitor):
    def __init__(self, tree):
        self.tree = tree
        self.non_terminal_list = NonTerminalList()

    def validate(self):
        self.visit(self.tree)
        return self.non_terminal_list.is_valid()

    def visit_program(self, node):
        for rule in node.rules:
            self.visit(rule)

    def visit_rule(self, node):
        self.visit(node.identifier)
        self.visit(node.right)

    def visit_rightside(self, node):
        for sequence in node.sequences:
            self.visit(sequence)

    def visit_sequence(self, node):
        for value in node.values:
            self.visit(value)

    def visit_option(self, node):
        self.visit(node.right)

    def visit_repetition(self, node):
        self.visit(node.right)

    def visit_identifier(self, node):
        id_type = node.type
        if id_type == ID_TYPE_RULE_NAME:
            self.non_terminal_list.insert_left(node.name)
        if id_type == ID_TYPE_NON_TERMINAL:
            self.non_terminal_list.insert_right(node.name)


class LexerGenerator(NodeVisitor):
    def __init__(self, parser):
        self.parser = parser

    def visit_program(self, node):
        pass

    def interpret(self):
        tree = self.parser.parse()
        return self.visit(tree)
