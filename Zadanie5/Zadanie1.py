try:

    import numpy as np
    

except:
    print("DUPA")

artistName = input("Podaj nazwę artysty: ")
args = {'q': artistName, 'type': 'artist', 'token': 'yRgunkCAXhzRCYWybzTMKRQiHokIXYbXNDELZqpU'}
resp = requests.get('https://api.discogs.com/database/search', params=args)

artistsJSON = resp.json()
artist_list = artistsJSON['results']

if len(artist_list) > 1:
    print("Oto {0} najpopularniejszych wyników".format(len(artist_list)))
    for i in range(len(artist_list)):
        print(i, artist_list[i]['title'])
    choice = int(input("O którego artystę chodziło: "))

albumsResponse = requests.get('https://api.discogs.com/artists/' + str(artist_list[choice]['id']) + '/releases')
albumsJSON = albumsResponse.json()
albumsList = albumsJSON['releases']

print('\n\nAlbumy: \n')
for i in range(len(albumsList)):
    print(albumsList[i]['title'])

