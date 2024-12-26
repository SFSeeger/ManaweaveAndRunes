import argparse
from json_from_nbt import parse_structure_file
import json

parser = argparse.ArgumentParser(description="Converts a structure file to a json file")
parser.add_argument("file", help="The file to convert")
parser.add_argument("-o", "--output", help="The output file", default="output.json")
parser.add_argument("-p", "--pretty", help="Pretty print the output", action="store_true")
parser.add_argument("-s", "--symmetric", help="Set the symmetric flag, for faster computation", action="store_true")
parser.add_argument("--origin", help="The origin of the structure", metavar=("X", "Y", "Z"), default=None, nargs=3, type=int)
args = parser.parse_args()

file = args.file
output = args.output
mapping, structure = parse_structure_file(file, args.origin)
with open(output, "w") as file:
    json.dump({"blockMapping": mapping, "structure": structure, "symmetric": args.symmetric}, file, indent= 4 if args.pretty else None)