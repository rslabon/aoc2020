def transform(subject_number, loop_size):
    value = 1
    for _ in range(loop_size):
        value *= subject_number
        value = value % 20201227

    return value


def find_loop_size(public_key):
    loop_size = 1
    value = 1
    while True:
        value *= 7
        value = value % 20201227
        if value == public_key:
            return loop_size

        loop_size += 1


card_loop_size = find_loop_size(10212254)
door_loop_size = find_loop_size(12577395)
card_public_key = transform(7, card_loop_size)
door_public_key = transform(7, door_loop_size)

encryption_key1 = transform(door_public_key, card_loop_size)
encryption_key2 = transform(card_public_key, door_loop_size)

print(encryption_key1 == encryption_key2, encryption_key1, encryption_key2)
