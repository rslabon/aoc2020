data = """
..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#
""".strip()

with open("resources/day3.txt") as f:
    data = f.read().strip()

grid = []
for line in data.splitlines():
    row = []
    for val in line:
        row.append(val)
    grid.append(row)


def get_value(grid, row, col):
    col = col % len(grid[row])
    return grid[row][col]


def count_trees(grid, down, right):
    current_down = down
    current_right = right
    tree_count = 0
    while current_down < len(grid):
        if "#" == get_value(grid, current_down, current_right):
            tree_count += 1
        current_down += down
        current_right += right

    return tree_count


def part1():
    down = 1
    right = 3

    print(count_trees(grid, down, right))


def part2():
    result = 1
    for right, down in [(1, 1), (3, 1), (5, 1), (7, 1), (1, 2)]:
        result *= count_trees(grid, down, right)

    print(result)


part1()
part2()
