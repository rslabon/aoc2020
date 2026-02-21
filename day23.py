class Cub:
    def __init__(self, value):
        self.value = value
        self.next = None

    def __repr__(self):
        return str(self.value)


def as_string(cubs):
    first = cubs
    current = first.next
    s = f"{first.value}"
    while current != first:
        s += str(current.value)
        current = current.next
    return s


def play(data, moves):
    max_value = max(data)
    cubs = {}
    c = Cub(data[0])
    cubs[c.value] = c
    first_cub = c
    for n in data[1:]:
        c.next = Cub(n)
        c = c.next
        cubs[c.value] = c

    c.next = first_cub

    current = first_cub
    for move in range(moves):
        tree_cubs = current.next
        current.next = current.next.next.next.next
        tree_cubs.next.next.next = None
        tree_cubs_values = [tree_cubs.value, tree_cubs.next.value, tree_cubs.next.next.value]

        value = current.value - 1
        if value == 0:
            value = max_value

        while value in tree_cubs_values:
            value -= 1
            if value == 0:
                value = max_value

        c = cubs[value]
        tree_cubs.next.next.next = c.next
        c.next = tree_cubs

        current = current.next

    return cubs[1]


def part1():
    data = [int(n) for n in "784235916"]
    one = play(data, 100)
    print(as_string(one.next)[:-1])


def part2():
    data = [int(n) for n in "784235916"]
    n = 10
    while len(data) < 1000_000:
        data.append(n)
        n += 1

    one = play(data, 10_000_000)
    print(one.next.value * one.next.next.value)


part1()
part2()
