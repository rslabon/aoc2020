data = """
abc

a
b
c

ab
ac

a
a
a
a

b
"""

with open('./resources/day6.txt') as f:
    data = f.read()

def part1():
    total = 0
    for group in data.split("\n\n"):
        answers = set()
        lines = [line for line in group.split("\n") if line != ""]
        for answer in lines:
            for letter in answer:
                answers.add(letter)
        total += len(answers)

    print(total)


def part2():
    total = 0
    for group in data.split("\n\n"):
        lines = [set(line) for line in group.split("\n") if line != ""]
        if lines:
            common = lines[0]
            for answer in lines:
                common = answer.intersection(common)
            total += len(common)

    print(total)


part1()
part2()
