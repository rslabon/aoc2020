from collections import defaultdict

data = """
sesenwnenenewseeswwswswwnenewsewsw
neeenesenwnwwswnenewnwwsewnenwseswesw
seswneswswsenwwnwse
nwnwneseeswswnenewneswwnewseswneseene
swweswneswnenwsewnwneneseenw
eesenwseswswnenwswnwnwsewwnwsene
sewnenenenesenwsewnenwwwse
wenwwweseeeweswwwnwwe
wsweesenenewnwwnwsenewsenwwsesesenwne
neeswseenwwswnwswswnw
nenwswwsewswnenenewsenwsenwnesesenew
enewnwewneswsewnwswenweswnenwsenwsw
sweneswneswneneenwnewenewwneswswnese
swwesenesewenwneswnwwneseswwne
enesenwswwswneneswsenwnewswseenwsese
wnwnesenesenenwwnenwsewesewsesesew
nenewswnwewswnenesenwnesewesw
eneswnwswnwsenenwnwnwwseeswneewsenese
neswnwewnwnwseenwseesewsenwsweewe
wseweeenwnesenwwwswnew
""".strip()

with open("./resources/day24.txt") as f:
    data = f.read().strip()


def find_color_of_tiles():
    # false -> white, true -> black
    colors = defaultdict(bool)
    tiles = []
    for line in data.split("\n"):
        tile = []
        buffer = ""
        for c in line:
            buffer += c
            if buffer == "se":
                tile.append(buffer)
                buffer = ""
            elif buffer == "ne":
                tile.append(buffer)
                buffer = ""
            elif buffer == "e":
                tile.append(buffer)
                buffer = ""
            elif buffer == "sw":
                tile.append(buffer)
                buffer = ""
            elif buffer == "nw":
                tile.append(buffer)
                buffer = ""
            elif buffer == "w":
                tile.append(buffer)
                buffer = ""

        tiles.append(tile)

    for tile in tiles:
        r, c = 0, 0
        for dir in tile:
            if dir == "se":
                r += 1
                c += 0
            elif dir == "sw":
                r += 1
                c += -1
            elif dir == "w":
                r += 0
                c += -1
            elif dir == "nw":
                r += -1
                c += 0
            elif dir == "ne":
                r += -1
                c += 1
            elif dir == "e":
                r += 0
                c += 1

        colors[(r, c)] = not colors[(r, c)]
    return colors


def part1():
    colors = find_color_of_tiles()
    black_count = len([c for c in colors.values() if c])
    print(black_count)


def next_day(colors):
    new_day = defaultdict(bool, colors)
    black_tiles = set([(r, c) for (r, c), color in colors.items() if color])
    white_tiles = set()
    for r, c in black_tiles:
        adj_black = 0
        adj_white = 0
        for dr, dc in [(-1, 1), (0, 1), (1, 0), (1, -1), (0, -1), (-1, 0)]:
            adj_r = r + dr
            adj_c = c + dc
            if colors[(adj_r, adj_c)]:
                adj_black += 1
            else:
                adj_white += 1
                white_tiles.add((adj_r, adj_c))

        # Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.
        if adj_black == 0 or adj_black > 2:
            new_day[(r, c)] = False

    for r, c in white_tiles:
        adj_black = 0
        adj_white = 0
        for dr, dc in [(-1, 1), (0, 1), (1, 0), (1, -1), (0, -1), (-1, 0)]:
            adj_r = r + dr
            adj_c = c + dc
            if colors[(adj_r, adj_c)]:
                adj_black += 1
            else:
                adj_white += 1

        # Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.
        if adj_black == 2:
            new_day[(r, c)] = True

    return new_day


def part2():
    colors = find_color_of_tiles()
    for _ in range(100):
        colors = next_day(colors)

    black_count = len([c for c in colors.values() if c])
    print(black_count)


part1()
part2()
