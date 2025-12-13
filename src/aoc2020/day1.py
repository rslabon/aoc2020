with open("../../resources/day1.txt") as f:
    data = f.read().strip()

numbers = set([int(n) for n in data.split("\n")])


def part1():
    for n in numbers:
        if 2020 - n in numbers:
            print((2020 - n) * n)
            break


def part2():
    lnums = list(numbers)
    for i in range(len(lnums)):
        j = i + 1
        while j < len(lnums):
            first = lnums[i]
            second = lnums[j]
            third = 2020 - first - second
            if third in numbers:
                print(first * second * third)
                return
            j += 1


part1()
part2()
