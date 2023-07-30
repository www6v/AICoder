
import pyhocon

# Load the HOCON file
config = pyhocon.ConfigFactory.parse_file('path_to_your_file.conf')

# Mutate the HOCON file
config.put('your.key', 'new_value')

# Save the mutated HOCON file
with open('path_to_your_file.conf', 'w') as f:
    f.write(pyhocon.HOCONConverter.convert(config, 'hocon'))


# 写一个函数，用于改写HOCON文件中的参数
# - config_file: HOCON 文件路径
# - key： HOCON 文件中的参数名


import pyhocon
def modify_hocon(config_file, key, new_value):
    # Load the HOCON file
    config = pyhocon.ConfigFactory.parse_file(config_file)

    # Mutate the HOCON file
    config.put(key, new_value)

    # Save the mutated HOCON file
    with open(config_file, 'w') as f:
        f.write(pyhocon.HOCONConverter.convert(config, 'hocon'))
