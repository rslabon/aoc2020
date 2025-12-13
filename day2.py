import re
from collections import Counter

data = """
1-3 a: abcde
1-3 b: cdefg
2-9 c: ccccccccc
""".strip()

with open("resources/day2.txt") as f:
    data = f.read().strip()


def part1():
    valid_count = 0
    for line in data.split("\n"):
        part1, part2 = line.split(":")
        min_occurrences, max_occurrences, letter = re.findall(r"(\d+)-(\d+) (\w)", part1)[0]
        min_occurrences, max_occurrences = int(min_occurrences), int(max_occurrences)
        password = part2.strip()
        c = Counter(password)
        if min_occurrences <= c[letter] <= max_occurrences:
            valid_count += 1

    print(valid_count)


def part2():
    valid_count = 0
    for line in data.split("\n"):
        part1, part2 = line.split(":")
        first_position, second_position, letter = re.findall(r"(\d+)-(\d+) (\w)", part1)[0]
        first_position, second_position = int(first_position), int(second_position)
        password = part2.strip()
        if (password[first_position - 1] == letter) ^ (password[second_position - 1] == letter):
            valid_count += 1

    print(valid_count)


part1()
part2()
