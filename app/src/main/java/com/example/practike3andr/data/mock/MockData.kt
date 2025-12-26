package com.example.practike3andr.data.mock

import com.example.practike3andr.data.model.Actor
import com.example.practike3andr.data.model.ActorsResponse
import com.example.practike3andr.data.model.Fact
import com.example.practike3andr.data.model.Movie
import com.example.practike3andr.data.model.Place
import com.example.practike3andr.data.model.Profession
import com.example.practike3andr.data.model.Spouse

object MockData {
    
    fun getMockActors(): ActorsResponse {
        return ActorsResponse(
            docs = listOf(
                Actor(
                    id = 1,
                    name = "Леонардо ДиКаприо",
                    enName = "Leonardo DiCaprio",
                    photo = null, // Убираем фото для тестирования
                    sex = "male",
                    growth = 183,
                    birthday = "1974-11-11",
                    death = null,
                    age = 49,
                    birthPlace = listOf(Place("Лос-Анджелес, Калифорния, США")),
                    deathPlace = null,
                    spouses = listOf(
                        Spouse(
                            id = 1,
                            name = "Камила Морроне",
                            divorced = false,
                            divorcedReason = null,
                            sex = "female",
                            children = 0,
                            relation = "жена"
                        )
                    ),
                    countAwards = 1,
                    profession = listOf(
                        Profession("Актер"),
                        Profession("Продюсер")
                    ),
                    facts = listOf(
                        Fact("Получил Оскар за роль в фильме 'Выживший'"),
                        Fact("Активный защитник окружающей среды")
                    ),
                    movies = listOf(
                        Movie(
                            id = 1,
                            name = "Титаник",
                            alternativeName = "Titanic",
                            rating = 8.1,
                            general = true,
                            description = "Романтическая драма о любви на фоне катастрофы",
                            enProfession = "actor"
                        ),
                        Movie(
                            id = 2,
                            name = "Выживший",
                            alternativeName = "The Revenant",
                            rating = 8.0,
                            general = true,
                            description = "История о выживании в дикой природе",
                            enProfession = "actor"
                        )
                    ),
                    updatedAt = "2024-01-01T00:00:00.000Z",
                    createdAt = "2024-01-01T00:00:00.000Z"
                ),
                Actor(
                    id = 2,
                    name = "Марго Робби",
                    enName = "Margot Robbie",
                    photo = null,
                    sex = "female",
                    growth = 168,
                    birthday = "1990-07-02",
                    death = null,
                    age = 33,
                    birthPlace = listOf(Place("Далби, Квинсленд, Австралия")),
                    deathPlace = null,
                    spouses = listOf(
                        Spouse(
                            id = 2,
                            name = "Том Акерли",
                            divorced = false,
                            divorcedReason = null,
                            sex = "male",
                            children = 0,
                            relation = "муж"
                        )
                    ),
                    countAwards = 0,
                    profession = listOf(
                        Profession("Актриса"),
                        Profession("Продюсер")
                    ),
                    facts = listOf(
                        Fact("Известна ролью Харли Квинн в фильмах о Бэтмене"),
                        Fact("Основала собственную продюсерскую компанию")
                    ),
                    movies = listOf(
                        Movie(
                            id = 3,
                            name = "Отряд самоубийц",
                            alternativeName = "Suicide Squad",
                            rating = 6.0,
                            general = true,
                            description = "Фильм о команде суперзлодеев",
                            enProfession = "actor"
                        ),
                        Movie(
                            id = 4,
                            name = "Я, Тоня",
                            alternativeName = "I, Tonya",
                            rating = 7.5,
                            general = true,
                            description = "Биографическая драма о фигуристке",
                            enProfession = "actor"
                        )
                    ),
                    updatedAt = "2024-01-01T00:00:00.000Z",
                    createdAt = "2024-01-01T00:00:00.000Z"
                ),
                Actor(
                    id = 3,
                    name = "Райан Гослинг",
                    enName = "Ryan Gosling",
                    photo = null,
                    sex = "male",
                    growth = 184,
                    birthday = "1980-11-12",
                    death = null,
                    age = 43,
                    birthPlace = listOf(Place("Лондон, Онтарио, Канада")),
                    deathPlace = null,
                    spouses = listOf(
                        Spouse(
                            id = 3,
                            name = "Ева Мендес",
                            divorced = false,
                            divorcedReason = null,
                            sex = "female",
                            children = 2,
                            relation = "жена"
                        )
                    ),
                    countAwards = 0,
                    profession = listOf(
                        Profession("Актер"),
                        Profession("Режиссер"),
                        Profession("Музыкант")
                    ),
                    facts = listOf(
                        Fact("Начинал карьеру в детском шоу 'Клуб Микки Мауса'"),
                        Fact("Играет в музыкальной группе Dead Man's Bones")
                    ),
                    movies = listOf(
                        Movie(
                            id = 5,
                            name = "Драйв",
                            alternativeName = "Drive",
                            rating = 7.8,
                            general = true,
                            description = "Неонуар о водителе-каскадере",
                            enProfession = "actor"
                        ),
                        Movie(
                            id = 6,
                            name = "Ла-Ла Ленд",
                            alternativeName = "La La Land",
                            rating = 8.0,
                            general = true,
                            description = "Мюзикл о любви в Голливуде",
                            enProfession = "actor"
                        )
                    ),
                    updatedAt = "2024-01-01T00:00:00.000Z",
                    createdAt = "2024-01-01T00:00:00.000Z"
                ),
                Actor(
                    id = 4,
                    name = "Эмма Стоун",
                    enName = "Emma Stone",
                    photo = null,
                    sex = "female",
                    growth = 168,
                    birthday = "1988-11-06",
                    death = null,
                    age = 35,
                    birthPlace = listOf(Place("Скоттсдейл, Аризона, США")),
                    deathPlace = null,
                    spouses = listOf(
                        Spouse(
                            id = 4,
                            name = "Дэйв Маккэри",
                            divorced = false,
                            divorcedReason = null,
                            sex = "male",
                            children = 1,
                            relation = "муж"
                        )
                    ),
                    countAwards = 1,
                    profession = listOf(
                        Profession("Актриса"),
                        Profession("Продюсер")
                    ),
                    facts = listOf(
                        Fact("Получила Оскар за роль в фильме 'Ла-Ла Ленд'"),
                        Fact("Страдает паническими атаками и открыто об этом говорит")
                    ),
                    movies = listOf(
                        Movie(
                            id = 7,
                            name = "Ла-Ла Ленд",
                            alternativeName = "La La Land",
                            rating = 8.0,
                            general = true,
                            description = "Мюзикл о любви в Голливуде",
                            enProfession = "actor"
                        ),
                        Movie(
                            id = 8,
                            name = "Человек-паук: Возвращение домой",
                            alternativeName = "Spider-Man: Homecoming",
                            rating = 7.4,
                            general = true,
                            description = "Супергеройский фильм о Человеке-пауке",
                            enProfession = "actor"
                        )
                    ),
                    updatedAt = "2024-01-01T00:00:00.000Z",
                    createdAt = "2024-01-01T00:00:00.000Z"
                ),
                Actor(
                    id = 5,
                    name = "Том Харди",
                    enName = "Tom Hardy",
                    photo = null,
                    sex = "male",
                    growth = 175,
                    birthday = "1977-09-15",
                    death = null,
                    age = 46,
                    birthPlace = listOf(Place("Лондон, Англия")),
                    deathPlace = null,
                    spouses = listOf(
                        Spouse(
                            id = 5,
                            name = "Шарлотта Райли",
                            divorced = false,
                            divorcedReason = null,
                            sex = "female",
                            children = 2,
                            relation = "жена"
                        )
                    ),
                    countAwards = 0,
                    profession = listOf(
                        Profession("Актер"),
                        Profession("Продюсер")
                    ),
                    facts = listOf(
                        Fact("Известен ролями в фильмах Кристофера Нолана"),
                        Fact("Бывший боксер и каскадер")
                    ),
                    movies = listOf(
                        Movie(
                            id = 9,
                            name = "Темный рыцарь: Возрождение легенды",
                            alternativeName = "The Dark Knight Rises",
                            rating = 8.4,
                            general = true,
                            description = "Фильм о Бэтмене",
                            enProfession = "actor"
                        ),
                        Movie(
                            id = 10,
                            name = "Безумный Макс: Дорога ярости",
                            alternativeName = "Mad Max: Fury Road",
                            rating = 8.1,
                            general = true,
                            description = "Постапокалиптический боевик",
                            enProfession = "actor"
                        )
                    ),
                    updatedAt = "2024-01-01T00:00:00.000Z",
                    createdAt = "2024-01-01T00:00:00.000Z"
                )
            ),
            total = 5,
            limit = 10,
            page = 1,
            pages = 1
        )
    }
}
