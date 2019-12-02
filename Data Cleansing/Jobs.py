'''
Created on 01-Nov-2019

@author: Hp-pc
'''

import random, re, csv

fields = ['Company', 'Job Title', 'Department', 'Location', 'Salary',
          'Application Deadline', 'Job Posting Date', 'Desired Skills']

skills = {'management', 'supervisory', 'administrative', 'database',
        'accounting', 'finance', 'control', 'treasury', 'reserving',
        'reporting', 'leadership', 'analytical', 'communication',
        'teamwork', 'interpersonal', 'english', 'economical', 'economics'
        'ms excel', 'ms access', 'problem-solving', 'organizational',
        'interpersonal', 'team-orientation', 'e-mail', 'administration',
        'procurement', 'financial', 'reliable', 'responsible', 'goal-oriented'
        "valid driver's license", 'development', 'multi-task', 'maintenance',
        'computers', 'windows 2000 server', 'networking tcp/ ip technologies', 'database',
        'ms sql 2000 server', 'visual basic', 'database software development',
        'ms excel', 'ms access', 'ms windows', 'problem-solving', 'linux platform',
        'adobe photoshop', 'web development', 'animation', 'windows', 'word',
        'excel', 'visual studio', '.net', 'html', 'xml', 'asp', 'php', 'sql',
        'software life cycle', 'object oriented', 'java', 'python',
        'j2ee', 'xml', 'web services', 'soap', 'uml', 'corel draw',
        'adobe illustrator', 'adobe pagemaker', 'jsp', 'servlets',
        'c++', 'visual c++', 'vb', 'plsql', 'web based applications'}

input_fields = ['Company', 'Title', 'IT', 'Location', 'Salary', 'Deadline', 'date']

# c = 0
city = []

with open('city.csv', 'r') as cityFile:
    data = csv.reader(cityFile, delimiter=',')
    for i in data:
        city.append(i)

with open('output.csv', 'w', newline='') as outputFile:
    ofile = csv.writer(outputFile, delimiter=',', quoting=csv.QUOTE_MINIMAL)
    ofile.writerow(fields)
    with open('data job posts.csv', 'r') as jobPosts:
        infile_data = csv.DictReader(jobPosts)
        for data in infile_data:
#             if c == 10:
#                 break
#             c += 1
            jobskill = set()

            skil_data = data['RequiredQual'].lower()

            for i in skills:
                if i in skil_data:
                    jobskill.add(i)

            final_data = []

            if data['IT'] == 'TRUE':
                data['IT'] = 'Computer Science'
            else:
                data['IT'] = 'Others'

            deadline = data['Deadline'][:17]

            if bool(re.search(r'\d', deadline)):
                data['Deadline'] = deadline.strip(' ').rstrip(',')
            else:
                data['Deadline'] = None

            data['Salary'] = (random.randrange(70000, 250000) // 1000) * 1000

            data['Location'] = ''.join(random.choice(city))

            for i in input_fields:
                final_data.append(data[i])

            final_data.append(','.join(jobskill))
            ofile.writerow(final_data)
