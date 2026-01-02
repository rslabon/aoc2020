import re

data = """
ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm

iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929

hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm

hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in
""".strip()

# data = """
# pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
# hcl:#623a2f
#
# eyr:2029 ecl:blu cid:129 byr:1989
# iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
#
# hcl:#888785
# hgt:164cm byr:2001 iyr:2015 cid:88
# pid:545766238 ecl:hzl
# eyr:2022
#
# iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
# """.strip()

# data = """
# eyr:1972 cid:100
# hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
#
# iyr:2019
# hcl:#602927 eyr:1967 hgt:170cm
# ecl:grn pid:012533040 byr:1946
#
# hcl:dab227 iyr:2012
# ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
#
# hgt:59cm ecl:zzz
# eyr:2038 hcl:74454a iyr:2023
# pid:3556412378 byr:2007
# """.strip()

with open("resources/day4.txt") as f:
    data = f.read().strip()

passports = []
for block in data.split("\n\n"):
    passport = {}
    for line in block.split("\n"):
        parts = line.split(" ")
        for part in parts:
            name, value = part.split(":")
            passport[name] = value

    passports.append(passport)


# byr (Birth Year)
# iyr (Issue Year)
# eyr (Expiration Year)
# hgt (Height)
# hcl (Hair Color)
# ecl (Eye Color)
# pid (Passport ID)
# cid (Country ID)

# byr (Birth Year) - four digits; at least 1920 and at most 2002.
# iyr (Issue Year) - four digits; at least 2010 and at most 2020.
# eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
# hgt (Height) - a number followed by either cm or in:
# If cm, the number must be at least 150 and at most 193.
# If in, the number must be at least 59 and at most 76.
# hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
# ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
# pid (Passport ID) - a nine-digit number, including leading zeroes.
# cid (Country ID) - ignored, missing or not.
def check_fields(passport):
    if not re.match(r"^[0-9]{4}$", passport["byr"]) or not 1920 <= int(passport["byr"]) <= 2002:
        return False
    if not re.match(r"^[0-9]{4}$", passport["iyr"]) or not 2010 <= int(passport["iyr"]) <= 2020:
        return False
    if not re.match(r"^[0-9]{4}$", passport["eyr"]) or not 2020 <= int(passport["eyr"]) <= 2030:
        return False

    if not re.match(r"^[0-9]+(cm|in)$", passport["hgt"]):
        return False
    if "cm" in passport["hgt"]:
        if not 150 <= int(passport["hgt"].replace("cm", "")) <= 193:
            return False
    if "in" in passport["hgt"]:
        if not 59 <= int(passport["hgt"].replace("in", "")) <= 76:
            return False

    if not re.match(r"^#[0-9a-f]{6}$", passport["hcl"]):
        return False

    if not re.match(r"^amb|blu|brn|gry|grn|hzl|oth$", passport["ecl"]):
        return False

    if not re.match(r"^[0-9]{9}$", passport["pid"]):
        return False

    return True


def is_valid(passport, validate_fields=False):
    return {"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"}.issubset(passport.keys()) \
        and ((not validate_fields) or check_fields(passport))


def part1():
    valid_count = 0
    for passport in passports:
        if is_valid(passport):
            valid_count += 1

    print(valid_count)


def part2():
    valid_count = 0
    for passport in passports:
        if is_valid(passport, True):
            valid_count += 1

    print(valid_count)


part1()
part2()
