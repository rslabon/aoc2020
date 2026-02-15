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


def part1():
    player1_cards = parse_player_cards(player1_data)
    player2_cards = parse_player_cards(player2_data)

    round = 0
    while player1_cards and player2_cards:
        round += 1
        print(f"-- Round {round} --")
        print(f"Player 1's deck: {player1_cards}")
        print(f"Player 2's deck: {player2_cards}")
        card1 = player1_cards.popleft()
        card2 = player2_cards.popleft()
        print(f"Player 1 plays: {card1}")
        print(f"Player 2 plays: {card2}")
        if card1 > card2:
            print("Player 1 wins the round!")
            player1_cards.append(card1)
            player1_cards.append(card2)
        else:
            print("Player 2 wins the round!")
            player2_cards.append(card2)
            player2_cards.append(card1)

    print("== Post-game results ==")
    print(f"Player 1's deck {player1_cards}")
    print(f"Player 2's deck {player2_cards}")

    winning_cards = player1_cards + player2_cards
    m = 1
    score = 0
    for card in reversed(winning_cards):
        score += card * m
        m += 1

    print(score)


part1()
