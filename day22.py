from collections import deque

data = """
Player 1:
9
2
6
3
1

Player 2:
5
8
4
7
10
""".strip()

with open("./resources/day22.txt") as f:
    data = f.read().strip()

player1_data, player2_data = data.split("\n\n")


def parse_player_cards(data):
    player_cards = []
    for line in data.split("\n")[1:]:
        player_cards.append(int(line.strip()))

    return deque(player_cards)


def compute_score(winning_cards):
    m = 1
    score = 0
    for card in reversed(winning_cards):
        score += card * m
        m += 1

    return score


def part1():
    player1_cards = parse_player_cards(player1_data)
    player2_cards = parse_player_cards(player2_data)

    round = 0
    while player1_cards and player2_cards:
        round += 1
        card1 = player1_cards.popleft()
        card2 = player2_cards.popleft()
        if card1 > card2:
            player1_cards.append(card1)
            player1_cards.append(card2)
        else:
            player2_cards.append(card2)
            player2_cards.append(card1)

    score = compute_score(player1_cards + player2_cards)
    print(score)


def play(player1_cards, player2_cards):
    if not player1_cards:
        return 2
    if not player2_cards:
        return 1

    seen = set()
    round = 0
    while player1_cards and player2_cards:
        cards1 = tuple(player1_cards)
        cards2 = tuple(player2_cards)
        if (cards1, cards2) in seen:
            return 1

        seen.add((cards1, cards2))

        round += 1
        card1 = player1_cards.popleft()
        card2 = player2_cards.popleft()

        if card1 <= len(player1_cards) and card2 <= len(player2_cards):
            winner = play(deque(list(player1_cards)[0:card1]), deque(list(player2_cards)[0:card2]))
            if winner == 1:
                player1_cards.append(card1)
                player1_cards.append(card2)
            else:
                player2_cards.append(card2)
                player2_cards.append(card1)
        else:
            if card1 > card2:
                player1_cards.append(card1)
                player1_cards.append(card2)
            else:
                player2_cards.append(card2)
                player2_cards.append(card1)

    if player1_cards:
        return 1
    if player2_cards:
        return 2

    raise "ERROR!!!"


def part2():
    player1_cards = parse_player_cards(player1_data)
    player2_cards = parse_player_cards(player2_data)
    play(player1_cards, player2_cards)
    score = compute_score(player1_cards + player2_cards)
    print(score)


part1()
part2()
