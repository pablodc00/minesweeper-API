import json
import requests


headers = {'Content-type': 'application/json'}



print("create game 1 with user pablo")
url = 'http://localhost:8080/minesweeper/v1/startgame'
payload = {'rows':6, 'columns':6, 'numberOfMines': 3, 'userName': 'pablo'}

r = requests.post(url, data=json.dumps(payload), headers=headers)
print(r.status_code)
#print(r.text)



print("create game 2 with user pablo")
url = 'http://localhost:8080/minesweeper/v1/startgame'
payload = {'rows':7, 'columns':7, 'numberOfMines': 4, 'userName': 'pablo'}

r = requests.post(url, data=json.dumps(payload), headers=headers)
print(r.status_code)
#print(r.text)




print("create game 3 with user john")
url = 'http://localhost:8080/minesweeper/v1/startgame'
payload = {'rows':8, 'columns':8, 'numberOfMines': 5, 'userName': 'john'}

r = requests.post(url, data=json.dumps(payload), headers=headers)
print(r.status_code)
#print(r.text)




print("get games for user pablo")
url = 'http://localhost:8080/minesweeper/v1/getgames?userName=pablo'
r = requests.get(url, headers=headers)
print(r.status_code)
#print(r.text)



print("play game 2")
data = r.json()
payload = data[1]


url = 'http://localhost:8080/minesweeper/v1/revealcell/1/1'
r = requests.put(url, data=json.dumps(payload), headers=headers)
print(r.status_code)
#print(r.text)



print("keep playing game 2")
url = 'http://localhost:8080/minesweeper/v1/getgames?userName=pablo'
r = requests.get(url, headers=headers)

data = r.json()
payload = data[1]


url = 'http://localhost:8080/minesweeper/v1/revealcell/1/4'
r = requests.put(url, data=json.dumps(payload), headers=headers)
print(r.status_code)
#print(r.text)




print("keep playing game 2")
url = 'http://localhost:8080/minesweeper/v1/getgames?userName=pablo'
r = requests.get(url, headers=headers)

data = r.json()
payload = data[1]

url = 'http://localhost:8080/minesweeper/v1/revealcell/1/0'
r = requests.put(url, data=json.dumps(payload), headers=headers)
print(r.status_code)
#print(r.text)




print("mark a cell in game 1 from user pablo")
url = 'http://localhost:8080/minesweeper/v1/getgames?userName=pablo'
r = requests.get(url, headers=headers)

data = r.json()
payload = data[0]


url = 'http://localhost:8080/minesweeper/v1/markcell/'+  str(payload.get('id')) +'/3/3/QUESTION_MARK'
r = requests.put(url, headers=headers)

print(r.status_code)
#print(r.text)