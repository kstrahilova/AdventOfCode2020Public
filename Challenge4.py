def valid_byr(key, value):
    return key == "byr" and int(value) >= 1920 and int(value) <= 2002

def valid_iyr(key, value):
    return key == "iyr" and int(value) >= 2010 and int(value) <= 2020

def valid_eyr(key, value):
    return key == "eyr" and int(value) >= 2020 and int(value) <= 2030

def valid_hgt(key, value):
    return (key == "hgt" and len(value) == 5 and value.endswith("cm") and int(value[:-2]) >= 150 and int(value[:-2]) <= 193) or (key == "hgt" and len(value) == 4 and value.endswith("in") and int(value[:-2]) >= 59 and int(value[:2]) <= 76)

def valid_hcl(key, value):
    hcl_allowed_symbols = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']
    return key == "hcl" and value[:1] == "#" and len(value) == 7 and set(value[1:]).issubset(hcl_allowed_symbols)

def valid_ecl(key, value):
    ecl_allowed_values = ['amb', 'blu', 'brn', 'gry', 'grn', 'hzl', 'oth']
    return key == "ecl" and len(value) == 3 and value in ecl_allowed_values

def valid_pid(key, value):
    pid_allowed_values = [str(i) for i in range(0, 10)]
    return key == "pid" and len(value) == 9 and set(list(value)).issubset(pid_allowed_values)

def present_cid(key, value):
    return key == "cid"

def valid_values(key_value_pairs):
    key_value_pair_iter = iter(key_value_pairs.keys())
    for key in key_value_pair_iter:
        value = key_value_pairs[key]
        if not valid_byr(key, value) and not valid_iyr(key, value) and not valid_eyr(key, value) and not valid_hgt(key, value) and not valid_hcl(key, value) and not valid_ecl(key, value) and not valid_pid(key, value) and not present_cid(key, value):
            return False
    return True

def valid_passport(keys, encountered_key_value_pairs):
    for key in keys:
        if key != "cid" and key not in encountered_key_value_pairs:
            return False
    if not valid_values(encountered_key_value_pairs):
            return False
    return True

def process_line(line, encountered_key_value_pairs):
    key_value_pairs = line.split(' ')
    for key_value_pair in key_value_pairs:
        key = key_value_pair.split(':')[0]
        value = key_value_pair.split(':')[1]
        encountered_key_value_pairs[key] = value

def main():
    input = open("inputChallenge4.txt", "r");

    n_valid = 0
    keys = ["byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid"]
    encountered_key_value_pairs = {}

    for line in input:
        if line in ['\n', '\r\n']:
            if valid_passport(keys, encountered_key_value_pairs):
                n_valid = n_valid + 1
            encountered_key_value_pairs = {}
        else:
            line = line.strip('\n')
            process_line(line, encountered_key_value_pairs);
    print("number of valid passports: ", n_valid)
main()
