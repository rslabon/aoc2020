with open("resources/day5.txt") as f:
    data = f.read().strip().split("\n")


def find_row(data, start=0, end=127):
    if len(data) == 1:
        if data[0] == "F":
            return start
        elif data[0] == "B":
            return end

    mid = start + int((end - start) / 2)
    if data[0] == "F":
        return find_row(data[1:], start=start, end=mid)

    return find_row(data[1:], start=mid + 1, end=end)


def find_column(data, start=0, end=7):
    if len(data) == 1:
        if data[0] == "L":
            return start
        elif data[0] == "R":
            return end

    mid = start + int((end - start) / 2)
    if data[0] == "L":
        return find_column(data[1:], start=start, end=mid)

    return find_column(data[1:], start=mid + 1, end=end)


def decode_seat_id(data):
    return find_row(data[0:7]) * 8 + find_column(data[7:])


def part1():
    max_seat_id = float("-inf")
    for line in data:
        max_seat_id = max(max_seat_id, decode_seat_id(line))
    print(max_seat_id)


def part2():
    seats = [decode_seat_id(line) for line in data]
    sorted_seats = sorted(seats)
    i = 1
    while i < len(sorted_seats):
        if sorted_seats[i] - sorted_seats[i - 1] != 1:
            print(sorted_seats[i - 1] + 1)

        i += 1


part1()
part2()
