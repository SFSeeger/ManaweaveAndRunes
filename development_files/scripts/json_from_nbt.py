from nbt import nbt
import string
import numpy as np
import logging

LETTERS = string.ascii_uppercase
AIR = " "
ANY = "_"

LOGGER = logging.getLogger(__name__)

def _get_next_letter():
    for letter in LETTERS:
        yield letter

def _get_best_letter(block_id:str) -> str:
    return block_id.strip().split(":")[1][0].upper()

def _get_best_letters_for_blocks(blocks: list) -> dict:
    letter_generator = _get_next_letter()
    mapping = dict()
    blocks = list(set(blocks))
    blocks.sort()
    for block in blocks:
        if block == "minecraft:air":
            mapping[block] = AIR
            continue
        letter = _get_best_letter(block)
        if letter in mapping.values():
            new_letter = next(letter_generator)
            while new_letter in mapping.values():
                new_letter = next(letter_generator)
            mapping[block] = new_letter
        else:
            mapping[block] = letter
    return mapping

def parse_structure_file(file_name: str, origin: list = None) -> tuple:
    file = nbt.NBTFile(file_name, "rb")
    
    palette = dict()

    for idx, tag in enumerate(file["palette"]):
        name: str = tag["Name"].value
        palette[idx] = name
    
    mapping = _get_best_letters_for_blocks(palette.values())

    LOGGER.info("Found %s different blocks", len(mapping))

    x_size = file["size"][0].value
    y_size = file["size"][1].value
    z_size = file["size"][2].value

    LOGGER.info("Found Structure with size: %s, %s, %s", x_size, y_size, z_size)

    structure = np.full((y_size, x_size, z_size), ANY)

    for block in file["blocks"]:
            x = block["pos"][0].value
            y = block["pos"][1].value
            z = block["pos"][2].value
            state = block["state"].value
            block_id = palette[state]

            block_letter = mapping[block_id]
            try:
                structure[y][z][x] = block_letter
            except Exception as e:
                LOGGER.error(f"Error {e} at pos: ({x}, {y}, {z}) with block", block_id)

    block_mapping = {v: k for k, v in mapping.items()}
    block_mapping.pop(AIR)

    if origin:
        x, y, z = origin
        old_state = structure[y][z][x]
        structure[y][z][x] = "0"
        if not old_state in structure:
            block_mapping.pop(old_state)

    structure = structure[::-1]
    
    structure_list = structure.tolist()
    for i in range(len(structure_list)):
        for j in range(len(structure_list[i])):
            structure_list[i][j] = "".join(structure_list[i][j])

    return block_mapping, structure_list