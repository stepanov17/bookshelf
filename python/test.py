import requests, json


headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
admin = ('admin', 'a123')
user = ('user', 'u123')


def get(url):
    try:
        r = requests.get(url, headers=headers, auth=user)
        r.raise_for_status()
        parsed = json.loads(r.content.decode("utf-8"))
        print(json.dumps(parsed, indent=4, sort_keys=True, ensure_ascii=False))
    except requests.exceptions.HTTPError as err:
        raise SystemExit(err)


author1 = {"id": 101, "surname": "Петров",  "name": "Андрей",  "patronymic": "Иванович"}
author2 = {"id": 102, "surname": "Петров",  "name": "Сергей",  "patronymic": "Андреевич"}
author3 = {"id": 103, "surname": "Петрова", "name": "Анна",    "patronymic": "Семеновна"}
author4 = {"id": 300, "surname": "Попович", "name": "Евгений", "patronymic": "Юрьевич"}

title1 = "Сборник задач по физике, 7-9 классы"
title2 = "Сборник задач по физике, 10-11 классы"
title3 = "Инженерная графика"

book1 = {"id": 500, "title": title1, "authors": [author1, author3], "publisher": "просвещение", "publyear": 2001}
book2 = {"id": 510, "title": title2, "authors": [author2, author3, author4], "publisher": "просвещение", "publyear": 2010}
book3 = {"id": 550, "title": title3, "authors": [author1, author2], "publisher": "bhv", "publyear": 2020}

print("initialization...")

for a in [author1, author2, author3, author4]:
    try:
        r = requests.post('http://localhost:8080/author', headers=headers, json=a, auth=admin)
        r.raise_for_status()
    except requests.exceptions.HTTPError as err:
        raise SystemExit(err)

for b in [book1, book2, book3]:
    try:
        r = requests.post('http://localhost:8080/book', headers=headers, json=b, auth=admin)
        r.raise_for_status()
    except requests.exceptions.HTTPError as err:
        raise SystemExit(err)

print("done")


print("\n\n1) get all books")
get('http://localhost:8080/book/all')


print("\n\n2) get all books, filtered by author #300")
get('http://localhost:8080/book/all?authorID=300')


print("\n\n3) get book by id 510")
get('http://localhost:8080/book/510')


print("\n\n4) get all authors")
get('http://localhost:8080/author/all')


print("\n\n5) get all authors, filtered by surname 'Петров'")
get('http://localhost:8080/author/all?surname=Петров')


print("\n\n6) get author by id 101")
get('http://localhost:8080/author/101')


print("\n\n7) update author's info")
author4upd = {"id": 300, "surname": "Попович", "name": "Евгения", "patronymic": "Юрьевна"}
try:
    r = requests.put('http://localhost:8080/author/300', headers=headers, json=author4upd, auth=admin)
    r.raise_for_status()
    parsed = json.loads(r.content.decode("utf-8"))
    print(json.dumps(parsed, indent=4, sort_keys=True, ensure_ascii=False))
except requests.exceptions.HTTPError as err:
    raise SystemExit(err)


print("\n\n8) update book's info (remove author, update publ. year)")
book3upd = {"id": 550, "title": title3, "authors": [author2], "publisher": "bhv", "publyear": 2021}
try:
    r = requests.put('http://localhost:8080/book/550', headers=headers, json=book3upd, auth=admin)
    r.raise_for_status()
    parsed = json.loads(r.content.decode("utf-8"))
    print(json.dumps(parsed, indent=4, sort_keys=True, ensure_ascii=False))
except requests.exceptions.HTTPError as err:
    raise SystemExit(err)


print("\n\n9) custom update of book's info (add author)")
try:
    r = requests.put('http://localhost:8080/book/550/edit?addAuthor=300', headers=headers, auth=admin)
    r.raise_for_status()
    parsed = json.loads(r.content.decode("utf-8"))
    print(json.dumps(parsed, indent=4, sort_keys=True, ensure_ascii=False))
except requests.exceptions.HTTPError as err:
    raise SystemExit(err)


print("\n\n10) custom update of book's info (remove author)")
try:
    r = requests.put('http://localhost:8080/book/500/edit?rmAuthor=103', headers=headers, auth=admin)
    r.raise_for_status()
    parsed = json.loads(r.content.decode("utf-8"))
    print(json.dumps(parsed, indent=4, sort_keys=True, ensure_ascii=False))
except requests.exceptions.HTTPError as err:
    raise SystemExit(err)


print("\n\n11) custom update of book's info (title, publisher and publication year)")
try:
    url = 'http://localhost:8080/book/500/edit?title=Python%20для%20чайников&publisher=Наука&publyear=1999'
    r = requests.put(url, headers=headers, auth=admin)
    r.raise_for_status()
    parsed = json.loads(r.content.decode("utf-8"))
    print(json.dumps(parsed, indent=4, sort_keys=True, ensure_ascii=False))
except requests.exceptions.HTTPError as err:
    raise SystemExit(err)


print("\n\n12) check unauthorized status")
r = requests.put('http://localhost:8080/book/550', headers=headers, json=book3upd)
print("status code = " + str(r.status_code))
if r.status_code != 401:
    raise AssertionError('expected status code: 401')


print("\n\n13) use invalid credentials")
r = requests.put('http://localhost:8080/book/550', headers=headers, json=book3upd, auth=('dummy', 'd123'))
print("status code = " + str(r.status_code))
if r.status_code != 401:
    raise AssertionError('expected status code: 401')


print("\n\n14) try to put as user, 'Forbidden' status expected")
r = requests.put('http://localhost:8080/book/550', headers=headers, json=book3upd, auth=user)
print("status code = " + str(r.status_code))
if r.status_code != 403:
    raise AssertionError('expected status code: 403')
