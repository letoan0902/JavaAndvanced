import re
import os

def decode_unicode(match):
    return chr(int(match.group(0)[2:], 16))

files = [
    r'src\main\java\org\example\util\DBConnection.java',
    r'src\main\java\org\example\util\ConsoleFormatter.java',
    r'src\main\java\org\example\presentation\StaffMenu.java',
    r'src\main\java\org\example\presentation\MainMenu.java',
    r'src\main\java\org\example\presentation\CustomerMenu.java'
]

for file in files:
    if os.path.exists(file):
        with open(file, 'r', encoding='utf-8') as f:
            content = f.read()
        
        new_content = re.sub(r'\\u[0-9a-fA-F]{4}', decode_unicode, content)
        
        if new_content != content:
            with open(file, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f'Updated {file}')
        else:
            print(f'No changes for {file}')
    else:
        print(f'File not found: {file}')
