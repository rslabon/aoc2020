import math

data = """
Tile 2311:
..##.#..#.
##..#.....
#...##..#.
####.#...#
##.##.###.
##...#.###
.#.#.#..##
..#....#..
###...#.#.
..###..###

Tile 1951:
#.##...##.
#.####...#
.....#..##
#...######
.##.#....#
.###.#####
###.##.##.
.###....#.
..#.#..#.#
#...##.#..

Tile 1171:
####...##.
#..##.#..#
##.#..#.#.
.###.####.
..###.####
.##....##.
.#...####.
#.##.####.
####..#...
.....##...

Tile 1427:
###.##.#..
.#..#.##..
.#.##.#..#
#.#.#.##.#
....#...##
...##..##.
...#.#####
.#.####.#.
..#..###.#
..##.#..#.

Tile 1489:
##.#.#....
..##...#..
.##..##...
..#...#...
#####...#.
#..#.#.#.#
...#.#.#..
##.#...##.
..##.##.##
###.##.#..

Tile 2473:
#....####.
#..#.##...
#.##..#...
######.#.#
.#...#.#.#
.#########
.###.#..#.
########.#
##...##.#.
..###.#.#.

Tile 2971:
..#.#....#
#...###...
#.#.###...
##.##..#..
.#####..##
.#..####.#
#..#.#..#.
..####.###
..#.#.###.
...#.#.#.#

Tile 2729:
...#.#.#.#
####.#....
..#.#.....
....#..#.#
.##..##.#.
.#.####...
####.#.#..
##.####...
##..#.##..
#.##...##.

Tile 3079:
#.#.#####.
.#..######
..#.......
######....
####.#..#.
.#...#.##.
#.#####.##
..#.###...
..#.......
..#.###...
""".strip()

with open("./resources/day20.txt") as f:
    data = f.read().strip()


class Tile:
    def __init__(self, id, content):
        self.id = id
        self.content = content
        self.up = None
        self.down = None
        self.left = None
        self.right = None

    def __repr__(self):
        return str(self.id)


def flip(tile):
    ftile = []
    for row in tile:
        ftile.append(row[::-1])

    return ftile


def rotate(tile):
    rtile = []
    width = len(tile[0])
    height = len(tile)
    i = 0
    while i < width:
        row = []
        j = 0
        while j < height:
            row.append(tile[height - 1 - j][i])
            j += 1
        rtile.append(row)
        i += 1

    return rtile


def print_tile(tile):
    for row in tile:
        print("".join(row))


def freeze_tile(tile):
    ftile = []
    for row in tile:
        ftile.append(tuple(row))
    return tuple(ftile)


def transform(tile):
    result = []

    result.append(tile)
    result.append(flip(tile))

    r1tile = rotate(tile)
    result.append(r1tile)
    result.append(flip(r1tile))

    r2tile = rotate(r1tile)
    result.append(r2tile)
    result.append(flip(r2tile))

    r3tile = rotate(r2tile)
    result.append(r3tile)
    result.append(flip(r3tile))

    return set([freeze_tile(t) for t in result])


id_to_tiles = {}
for block in data.split("\n\n"):
    lines = block.split("\n")
    id = lines[0].replace("Tile ", "").replace(":", "").strip()
    id = int(id)
    tile = []
    for y, line in enumerate(lines[1:]):
        row = []
        for x, char in enumerate(line):
            row.append(char)
        tile.append(row)

    tiles = transform(tile)
    id_to_tiles[id] = [Tile(id, t) for t in tiles]


def match_right(tile1, tile2):
    border1 = []
    for row in tile1:
        border1.append(row[-1])

    border2 = []
    for row in tile2:
        border2.append(row[0])

    return tuple(border1) == tuple(border2)


def match_left(tile1, tile2):
    border1 = []
    for row in tile1:
        border1.append(row[0])

    border2 = []
    for row in tile2:
        border2.append(row[-1])

    return tuple(border1) == tuple(border2)


def match_down(tile1, tile2):
    return tuple(tile1[-1]) == tuple(tile2[0])


def match_up(tile1, tile2):
    return tuple(tile1[0]) == tuple(tile2[-1])


def set_matched_tiles(id, id_to_tiles):
    for tile in id_to_tiles[id]:
        for other_id, other_tiles in id_to_tiles.items():
            if other_id == id:
                continue
            for other_tile in other_tiles:
                if match_up(tile.content, other_tile.content):
                    tile.up = other_tile
                    other_tile.down = tile
                if match_down(tile.content, other_tile.content):
                    tile.down = other_tile
                    other_tile.up = tile
                if match_left(tile.content, other_tile.content):
                    tile.left = other_tile
                    other_tile.right = tile
                if match_right(tile.content, other_tile.content):
                    tile.right = other_tile
                    other_tile.left = tile


def find_corners(id_to_tiles):
    for id in id_to_tiles.keys():
        set_matched_tiles(id, id_to_tiles)

    image_size = int(math.sqrt(len(id_to_tiles.keys())))
    for tiles in id_to_tiles.values():
        for tile in tiles:
            corners = []

            t = tile
            corners.append(t)

            p = None
            i = 0
            while t and i < image_size:
                p = t
                t = t.right
                i += 1

            if i < image_size:
                continue

            corners.append(p)

            i = 0
            t = p
            while t and i < image_size:
                p = t
                t = t.down
                i += 1

            if i < image_size:
                continue

            corners.append(p)

            i = 0
            t = p
            while t and i < image_size:
                p = t
                t = t.left
                i += 1

            if i < image_size:
                continue

            corners.append(p)

            i = 0
            t = p
            while t and i < image_size:
                p = t
                t = t.up
                i += 1

            if i < image_size:
                continue

            if p != tile:
                continue

            return corners


def get_image_grid(id_to_tiles):
    image_size = int(math.sqrt(len(id_to_tiles.keys())))
    corners = find_corners(id_to_tiles)
    top_left = corners[0]
    i = 0
    tr = top_left
    image_grid = []
    while i < image_size:
        j = 0
        tc = tr
        row = []
        while j < image_size:
            row.append(tc)
            tc = tc.right
            j += 1
        i += 1
        tr = tr.down
        image_grid.append(row)

    return image_grid


def remove_borders(image_grid):
    for row in image_grid:
        for tile in row:
            tile.content = tile.content[1:-1]

            new_content = []
            for r in tile.content:
                new_content.append(r[1:-1])
            tile.content = freeze_tile(new_content)


def merge_image(image_grid):
    image = []
    size = len(image_grid[0][0].content)

    for row in image_grid:
        i = 0
        while i < size:
            line = []
            for tile in row:
                line += tile.content[i]
            i += 1
            image.append(line)

    return image


def find_monsters(image, pattern_points):
    width = len(image[0])
    height = len(image)
    row = 0
    found_monster = False

    while row < height:
        col = 0
        while col < width:
            points = [(r + row, c + col) for (r, c) in pattern_points if (r + row) < height and (c + col) < width]
            cells = [image[r][c] for r, c in points]
            if cells == ["#"] * len(pattern_points):
                for r, c in points:
                    image[r][c] = "O"
                    found_monster = True

            col += 1
        row += 1

    if not found_monster:
        return None
    else:
        return image


def part1():
    corners = find_corners(id_to_tiles)
    result = 1
    for tile in corners:
        result *= tile.id

    print(result)


def part2():
    pattern = """******************#*
#****##****##****###
*#**#**#**#**#**#***"""
    pattern_points = []
    for row, line in enumerate(pattern.split("\n")):
        for col, c in enumerate(line):
            if c == "#":
                pattern_points.append((row, col))

    image_grid = get_image_grid(id_to_tiles)
    remove_borders(image_grid)
    image = merge_image(image_grid)

    image_with_monsters = None
    while not image_with_monsters:
        image_with_monsters = find_monsters(image, pattern_points)
        if image_with_monsters:
            break
        image = flip(image)
        image_with_monsters = find_monsters(image, pattern_points)
        if image_with_monsters:
            break

        image = flip(image)
        image = rotate(image)


    result = 0
    for row in image:
        for cell in row:
            if cell == "#":
                result += 1

    print(result)


part1()
part2()
