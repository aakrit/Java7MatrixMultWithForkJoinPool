#!/usr/bin/python
import sys
import os
import math
from random import randrange, uniform

# Prints a matrix to stdout
def print_matrix( A ):
    for i in A:
        print '[',
        for j in i:
            print '%3s' % (str(j)),
        print ' ]'

# Saves both matrices to the input text file
def save_matrices( A, B ):
    fp = open("input_file", 'w')
    for i in A:
        for j in i:
            fp.write(str(j)+' ')
        fp.write('\n')
    fp.write('\n')
    for i in B:
        for j in i:
            fp.write(str(j)+' ')
        fp.write('\n')
    fp.close

# Generates matrices / writes file / etc
if len(sys.argv) != 2:
    print 'Usage: '+sys.argv[0]+' <dimension>'
    exit()

n = int(sys.argv[1])

# Generates matrices
print 'Generating two '+str(n)+' x '+str(n)+' matrices...'
A = [n*[0] for x in range(n)]
B = [n*[0] for x in range(n)]
for i in range(0,n):
    for j in range(0,n):
        A[i][j] = randrange(0, 100)
        B[i][j] = randrange(0, 100)

if n <= 15:
    print 'Matrix A:'
    print_matrix( A )
    print 'Matrix B:'
    print_matrix( B )
else:
    print 'Matrices are too large for stdout... saving to file'

print 'Saving matrices to file \"input_file\"'

save_matrices( A, B )

# Calculates generated input file size
size = os.path.getsize('input_file')
kb = int(math.ceil(size / 1024.0))
print 'Generated input_file size: '+str(kb)+' Kb'


