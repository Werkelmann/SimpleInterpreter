

class NodeVisitor(object):
    def visit(self, node):
        method_name = 'visit_' + type(node).__name__
        visitor = getattr(self, method_name, self.generic_visit)
        return visitor(node)

    def generic_visit(self, node):
        raise Exception('No visit_{} method'.format(type(node).__name__))


class LexerGenerator(NodeVisitor):
    def __init__(self, parser):
        self.parser = parser

    def visit_Program(self, node):
        pass

    def interpret(self):
        tree = self.parser.parse()
        return self.visit(tree)
