import re

def printWithoutDuplicates(sp):
	t = {}
	for i in sp:
		t[i] = ""
	for c in t.keys():
		print c

s = []
while True:
	try:
		s.append(raw_input())
	except EOFError:
		break

s = ' '.join(s)

sp = re.findall(r'\w+', s)
printWithoutDuplicates(sp)
