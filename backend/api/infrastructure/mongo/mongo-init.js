db.createCollection("album")
db.album.createIndex({ "releaseDate": 1 })