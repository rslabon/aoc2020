data = """
light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags.
""".strip()

data = """
shiny gold bags contain 2 dark red bags.
dark red bags contain 2 dark orange bags.
dark orange bags contain 2 dark yellow bags.
dark yellow bags contain 2 dark green bags.
dark green bags contain 2 dark blue bags.
dark blue bags contain 2 dark violet bags.
dark violet bags contain no other bags.
""".strip()

with open("./resources/day7.txt") as f:
    data = f.read().strip()


class Bag:
    def __init__(self, color):
        self.color = color
        self.inner_bags = []
        self.outer_bags = []

    def __repr__(self):
        return str(self.color)


graph = {}
for line in data.split("\n"):
    left, right = line.split("contain")
    outer_bag_color = left.replace("bags", "").strip()
    if outer_bag_color not in graph:
        graph[outer_bag_color] = Bag(outer_bag_color)

    if not "no other bags" in right:
        inner_bags = []
        for inner in right.split(","):
            inner = inner.strip()
            number_of_bags = int(inner[0])
            inner_color = inner[1:].replace("bags", "").replace("bag", "").replace(".", "").strip()
            if inner_color not in graph:
                graph[inner_color] = Bag(inner_color)

            edge = (graph[inner_color], number_of_bags)
            inner_bags.append(edge)

            graph[inner_color].outer_bags.append(graph[outer_bag_color])

        graph[outer_bag_color].inner_bags += inner_bags


def collect_outer_bags(graph, bag_color):
    result = set()
    for bag in graph[bag_color].outer_bags:
        result.add(bag.color)
        result |= collect_outer_bags(graph, bag.color)

    return result

def count_inner_bags(graph, bag_color):
    result = 0
    for bag, bag_count in graph[bag_color].inner_bags:
        result += bag_count + bag_count * count_inner_bags(graph, bag.color)

    return result


def part1():
    print(len(collect_outer_bags(graph, "shiny gold")))


def part2():
    print(count_inner_bags(graph, "shiny gold"))


part1()
part2()
