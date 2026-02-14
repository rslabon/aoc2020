from collections import Counter
from typing import Any

data = """
mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc fvjkl (contains soy)
sqjhc mxmxvkd sbzzf (contains fish)
""".strip()

with open("./resources/day21.txt") as f:
    data = f.read().strip()

total_ingredients = set()
total_allergens = set()
foods = []
for line in data.splitlines():
    left, right = line.split("(contains")
    ingredients = left.strip(" ").split(" ")
    allergens = right.replace(")", "").replace(",", "").strip(" ").split(" ")
    total_ingredients.update(ingredients)
    total_allergens.update(allergens)
    foods.append((ingredients, allergens))


def assign(allergen_to_possible_ingredients, allergen_to_ingredient):
    if not allergen_to_possible_ingredients:
        return
    for allergen, ingredients in allergen_to_possible_ingredients.items():
        if len(ingredients) == 1:
            allergen_to_ingredient[allergen] = list(ingredients)[0]

    new_allergen_to_possible_ingredients = {}
    for allergen, ingredients in allergen_to_possible_ingredients.items():
        ingredients = [ingredient for ingredient in ingredients if ingredient not in allergen_to_ingredient.values()]
        if ingredients:
            new_allergen_to_possible_ingredients[allergen] = ingredients

    assign(new_allergen_to_possible_ingredients, allergen_to_ingredient)


def find_allergen_to_ingredient():
    allergen_to_possible_ingredients = {}
    for allergen in total_allergens:
        possible_ingredients = set(total_ingredients)
        for ingredients, allergens in foods:
            if allergen in allergens:
                possible_ingredients = possible_ingredients.intersection(ingredients)
        allergen_to_possible_ingredients[allergen] = possible_ingredients

    allergen_to_ingredient = {}
    assign(allergen_to_possible_ingredients, allergen_to_ingredient)
    return allergen_to_ingredient


def part1():
    allergen_to_ingredient = find_allergen_to_ingredient()
    allergen_free_ingredients = total_ingredients - set(allergen_to_ingredient.values())

    total = 0
    for ingredient in allergen_free_ingredients:
        for ingredients, _ in foods:
            total += Counter(ingredients)[ingredient]

    print(total)

def part2():
    allergen_to_ingredient = find_allergen_to_ingredient()
    ingredient_to_allergen = {v: k for k, v in allergen_to_ingredient.items()}

    result = ",".join(sorted(ingredient_to_allergen.keys(), key=lambda x: ingredient_to_allergen[x]))
    print(result)


part1()
part2()
